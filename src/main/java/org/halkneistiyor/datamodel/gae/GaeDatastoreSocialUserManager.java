package org.halkneistiyor.datamodel.gae;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.halkneistiyor.datamodel.SocialUser;
import org.halkneistiyor.datamodel.SocialUserManager;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.Email;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.common.base.Strings;

public class GaeDatastoreSocialUserManager implements SocialUserManager
{
    @Autowired
    DatastoreService datastore;

    private final static Log log = LogFactory.getLog(GaeDatastoreSocialUserManager.class);

    public SocialUser findUser(String userId)
    {
        if (log.isDebugEnabled())
        {
            log.debug("findUser <- " + userId);
        }

        Key key = KeyFactory.stringToKey(userId);

        try
        {
            Entity user = datastore.get(key);

            return SocialUserEntityBuilder.getUser(user);
        }
        catch (EntityNotFoundException e)
        {
            log.debug(userId + " not found in datastore");
            return null;
        }
    }

    public String registerUser(SocialUser newUser)
    {
        if (log.isDebugEnabled())
        {
            log.debug("registerUser <- " + newUser);
        }

        Entity user = SocialUserEntityBuilder.toEntity(newUser);
        String userId = KeyFactory.keyToString(datastore.put(user));

        newUser.setUserId(userId);
        return userId;
    }

    @Override
    public SocialUser findUserByEmail(String email)
    {
        if (Strings.isNullOrEmpty(email))
        {
            return null;
        }

        Query query = new Query(SocialUser.KIND);
        query.setFilter(new Query.FilterPredicate(SocialUserEntityBuilder.EMAIL, Query.FilterOperator.EQUAL, new Email(email)));

        PreparedQuery preparedQuery = datastore.prepare(query);
        Entity entity = preparedQuery.asSingleEntity();

        if (entity == null)
        {
            return null;
        }

        return SocialUserEntityBuilder.getUser(entity);
    }

    public void removeUser(String userId)
    {
        Key key = KeyFactory.createKey(SocialUser.KIND, userId);

        datastore.delete(key);
    }
}
