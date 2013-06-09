package org.halkneistiyor.datamodel.gae;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import org.halkneistiyor.datamodel.Vote;

import java.util.Date;


/**
 * @author Erdinc Yilmazel (eyilmazel@tripadvisor.com)
 * @since 6/7/13
 */
public class VoteEntityBuilder
{
    public static final String VOTE_ID = "voteId";
    public static final String REQUEST_ID = "requestId";
    public static final String USER_ID = "userId";
    public static final String VOTE_DATE = "voteDate";
    public static final String ACCEPT = "accept";

    public static Entity getEntity(Vote vote, Key requestKey)
    {
        Entity entity;
        if (vote.getVoteId() != null)
        {
            entity = new Entity(vote.getVoteId());
        }
        else
        {
            entity = new Entity(Vote.KIND, requestKey);
        }

        entity.setProperty(REQUEST_ID, KeyFactory.stringToKey(vote.getRequestId()));
        entity.setProperty(USER_ID, KeyFactory.stringToKey(vote.getUserId()));
        entity.setProperty(VOTE_DATE, vote.getVoteDate());
        entity.setProperty(ACCEPT, vote.isAccepted());

        return entity;
    }

    public static Vote getVote(Entity entity)
    {
        Vote vote = new Vote();
        Key key = entity.getKey();
        if (key != null)
        {
            vote.setVoteId(KeyFactory.keyToString(key));
        }

        vote.setAccepted((Boolean) entity.getProperty(ACCEPT));
        vote.setUserId(KeyFactory.keyToString((Key) entity.getProperty(USER_ID)));
        vote.setRequestId(KeyFactory.keyToString((Key) entity.getProperty(REQUEST_ID)));
        vote.setVoteDate((Date) entity.getProperty(VOTE_DATE));

        return vote;
    }
}
