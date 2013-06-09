package org.halkneistiyor.datamodel.gae;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.Email;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.halkneistiyor.datamodel.User;
import org.halkneistiyor.datamodel.UserManager;
import org.springframework.beans.factory.annotation.Autowired;

public class GaeDatastoreUserManager implements UserManager
{
    @Autowired
    DatastoreService datastore;

    private final static Log log = LogFactory.getLog(GaeDatastoreUserManager.class);

    public User findUser(String userId)
    {
        if (log.isDebugEnabled())
        {
            log.debug("findUser <- " + userId);
        }

        Key key = KeyFactory.stringToKey(userId);

        try
        {
            Entity user = datastore.get(key);

            return UserEntityBuilder.getUser(user);
        }
        catch (EntityNotFoundException e)
        {
            log.debug(userId + " not found in datastore");
            return null;
        }
    }

    public String registerUser(User newUser)
    {
        if (log.isDebugEnabled())
        {
            log.debug("registerUser <- " + newUser);
        }

        Entity user = UserEntityBuilder.toEntity(newUser);
        String userId = KeyFactory.keyToString(datastore.put(user));

        newUser.setUserId(userId);
        return userId;
    }

    @Override
    public User findUserByEmail(String email)
    {
        Query query = new Query(User.KIND);
        query.setFilter(new Query.FilterPredicate(UserEntityBuilder.EMAIL, Query.FilterOperator.EQUAL, new Email(email)));

        PreparedQuery preparedQuery = datastore.prepare(query);
        Entity entity = preparedQuery.asSingleEntity();

        if (entity == null)
        {
            return null;
        }

        return UserEntityBuilder.getUser(entity);
    }

    public void removeUser(String userId)
    {
        Key key = KeyFactory.createKey(User.KIND, userId);

        datastore.delete(key);
    }
}
