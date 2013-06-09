package org.halkneistiyor.datamodel.gae;

import com.google.appengine.api.datastore.Email;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import org.halkneistiyor.datamodel.User;
import org.halkneistiyor.security.model.UserRole;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.EnumSet;
import java.util.Set;


/**
 * @author Erdinc Yilmazel (eyilmazel@tripadvisor.com)
 * @since 6/8/13
 */
public class UserEntityBuilder
{
    public static final String FIRST_NAME = "firstName";
    public static final String LAST_NAME = "lastName";
    public static final String NICK_NAME = "nickName";
    public static final String EMAIL = "email";
    public static final String ENABLED = "enabled";
    public static final String PERMISSIONS = "permissions";

    public static User getUser(Entity entity)
    {
        User user = new User();
        Key key = entity.getKey();

        if (key != null)
        {
            user.setUserId(KeyFactory.keyToString(key));
        }

        user.setFirstName((String) entity.getProperty(FIRST_NAME));
        user.setLastName((String) entity.getProperty(LAST_NAME));
        user.setNickname((String) entity.getProperty(NICK_NAME));

        Email email = (Email) entity.getProperty(EMAIL);
        if (email != null)
        {
            user.setEmail(email.getEmail());
        }

        user.setEnabled((Boolean) entity.getProperty(ENABLED));

        Long binaryPermissions = (Long) entity.getProperty(PERMISSIONS);
        Set<UserRole> roles = EnumSet.noneOf(UserRole.class);

        if (binaryPermissions != null)
        {
            for (UserRole r : UserRole.values()) {
                if ((binaryPermissions & (1 << r.getBit())) != 0) {
                    roles.add(r);
                }
            }
        }

        user.setRoles(roles);

        return user;
    }

    public static Entity toEntity(User user)
    {
        Entity entity;

        if (user.getUserId() != null)
        {
            entity = new Entity(KeyFactory.stringToKey(user.getUserId()));
        }
        else
        {
            entity = new Entity(User.KIND);
        }

        entity.setProperty(FIRST_NAME, user.getFirstName());
        entity.setProperty(LAST_NAME, user.getLastName());
        entity.setProperty(NICK_NAME, user.getNickname());
        entity.setProperty(EMAIL, new Email(user.getEmail()));
        entity.setProperty(ENABLED, user.isEnabled());

        Collection<? extends GrantedAuthority> roles = user.getRoles();

        long binaryPermissions = 0;

        for (GrantedAuthority r : roles)
        {
            binaryPermissions |= 1 << ((UserRole) r).getBit();
        }

        entity.setUnindexedProperty(PERMISSIONS, binaryPermissions);

        return entity;
    }
}
