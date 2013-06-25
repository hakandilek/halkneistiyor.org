package org.halkneistiyor.datamodel.gae;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Transaction;
import com.google.appengine.api.memcache.AsyncMemcacheService;
import com.google.appengine.api.memcache.Expiration;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.halkneistiyor.datamodel.RequestEntry;
import org.halkneistiyor.datamodel.RequestManager;
import org.halkneistiyor.datamodel.Vote;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ConcurrentModificationException;
import java.util.Date;
import java.util.concurrent.Future;

/**
 * @author Erdinc Yilmazel (eyilmazel@tripadvisor.com)
 * @since 6/6/13
 */
public class GaeDatastoreRequestManager implements RequestManager
{
    @Autowired
    DatastoreService dataStore;

    @Autowired
    AsyncMemcacheService memcacheService;

    private final static Log log = LogFactory.getLog(GaeDatastoreSocialUserManager.class);

    private EntityBuilder<RequestEntry> requestEntityBuilder = new RequestEntityBuilder();
    private EntityBuilder<Vote> voteEntityBuilder = new VoteEntityBuilder();

    @Override
    public String addRequest(RequestEntry entry) throws Exception
    {
        int retries = 3;
        while (true)
        {
            try
            {
                Key key = dataStore.put(requestEntityBuilder.buildEntity(entry));
                String requestId = KeyFactory.keyToString(key);
                entry.setRequestId(requestId);
                memcacheService.put(entry.getUrlId(), entry, Expiration.byDeltaSeconds(30 * 60));
                return requestId;
            }
            catch (ConcurrentModificationException e)
            {
                log.info("Error trying to store a request", e);
                if (retries == 0)
                {
                    log.warn("All attempts failed trying to store a request", e);
                    throw e;
                }
                // Allow retry to occur
                --retries;
            }
        }
    }

    @Override
    public RequestEntry getRequest(String requestId) throws Exception
    {
        Key key = KeyFactory.stringToKey(requestId);
        try
        {
            Entity entity = dataStore.get(key);
            return requestEntityBuilder.buildModel(entity);
        }
        catch (EntityNotFoundException e)
        {
            log.warn("Request by id " + requestId + " not found", e);
            return null;
        }
    }

    @Override
    public RequestEntry getRequestByUrlId(String urlId) throws Exception
    {
        Future<Object> future = memcacheService.get(urlId);

        Object request = future.get();

        if (request != null)
        {
            return (RequestEntry) request;
        }

        Query findRequestByUrlId = new Query(RequestEntry.KIND);
        Query.Filter urlIdFilter = new Query.FilterPredicate(RequestEntityBuilder.URL_ID, Query.FilterOperator.EQUAL, urlId);
        findRequestByUrlId.setFilter(urlIdFilter);

        PreparedQuery preparedQuery = dataStore.prepare(findRequestByUrlId);
        Entity requestEntity = preparedQuery.asSingleEntity();

        if (requestEntity == null)
        {
            return null;
        }

        RequestEntry requestEntry = requestEntityBuilder.buildModel(requestEntity);

        memcacheService.put(urlId, requestEntity);

        return requestEntry;
    }

    /**
     * Stores the vote record in the data store and returns the data store id of the stored vote.
     * This method retries the same transaction 3 times.
     *
     * @param userId    Id (data store key) of the user
     * @param requestId Id (data store key) of the request
     * @param accept    Yes or no
     * @return The data store id for the registered vote
     * @throws Exception
     */
    @Override
    public String registerVote(String userId, String requestId, boolean accept) throws Exception
    {
        int retries = 3;
        while (true)
        {
            try
            {
                return registerVoteTX(userId, requestId, accept);
            }
            catch (ConcurrentModificationException e)
            {
                if (retries == 0)
                {
                    throw e;
                }
                // Allow retry to occur
                --retries;
            }

        }
    }

    private String registerVoteTX(String userId, String requestId, boolean accept) throws EntityNotFoundException
    {
        Transaction tx = dataStore.beginTransaction();
        try
        {
            Key requestKey = KeyFactory.stringToKey(requestId);

            Query findExisting = new Query(Vote.KIND, requestKey);
            Query.Filter userIdFilter = new Query.FilterPredicate(VoteEntityBuilder.USER_ID, Query.FilterOperator.EQUAL, KeyFactory.stringToKey(userId));

            findExisting.setFilter(userIdFilter);

            PreparedQuery preparedQuery = dataStore.prepare(tx, findExisting);
            Entity existingVote = preparedQuery.asSingleEntity();

            if (existingVote != null && Boolean.valueOf(accept).equals(existingVote.getProperty(VoteEntityBuilder.ACCEPT)))
            {
                // Existing vote same as the one we are trying to store. Nothing to update.
                tx.rollback();
                return KeyFactory.keyToString(existingVote.getKey());
            }

            Vote vote = new Vote();
            vote.setUserId(userId);
            vote.setRequestId(requestId);
            vote.setAccepted(accept);
            vote.setVoteDate(new Date());

            Key voteKey;

            if (existingVote != null)
            {
                existingVote.setProperty(VoteEntityBuilder.ACCEPT, accept);
                dataStore.put(tx, existingVote);

                voteKey = existingVote.getKey();
            }
            else
            {
                Entity voteEntity = VoteEntityBuilder.getEntity(vote, requestKey);
                voteKey = dataStore.put(tx, voteEntity);
            }

            Entity requestEntity = dataStore.get(requestKey);

            Long yesCount = (Long) requestEntity.getProperty(RequestEntityBuilder.YES_COUNT);
            Long noCount = (Long) requestEntity.getProperty(RequestEntityBuilder.NO_COUNT);

            if (accept)
            {
                yesCount = yesCount == null ? 1 : yesCount + 1;
                if (existingVote != null)
                {
                    noCount = noCount == null ? 0 : noCount - 1;
                    if (noCount < 0)
                    {
                        noCount = 0l;
                    }
                }
            }
            else
            {
                noCount = noCount == null ? 1 : noCount + 1;
                if (existingVote != null)
                {
                    yesCount = yesCount == null ? 0 : yesCount - 1;
                    if (yesCount < 0)
                    {
                        yesCount = 0l;
                    }
                }
            }

            requestEntity.setProperty(RequestEntityBuilder.YES_COUNT, yesCount);
            requestEntity.setProperty(RequestEntityBuilder.NO_COUNT, noCount);

            dataStore.put(tx, requestEntity);

            tx.commit();
            return KeyFactory.keyToString(voteKey);
        }
        finally
        {
            if (tx.isActive())
            {
                tx.rollback();
            }
        }
    }

    @Override
    public Vote getVote(String userId, String requestId) throws Exception
    {
        Key requestKey = KeyFactory.stringToKey(requestId);

        Query query = new Query(Vote.KIND, requestKey);
        Query.Filter userIdFilter = new Query.FilterPredicate(VoteEntityBuilder.USER_ID, Query.FilterOperator.EQUAL, KeyFactory.stringToKey(userId));

        query.setFilter(userIdFilter);

        PreparedQuery preparedQuery = dataStore.prepare(query);
        Entity voteEntity = preparedQuery.asSingleEntity();

        if (voteEntity == null)
        {
            return null;
        }

        return voteEntityBuilder.buildModel(voteEntity);
    }
}
