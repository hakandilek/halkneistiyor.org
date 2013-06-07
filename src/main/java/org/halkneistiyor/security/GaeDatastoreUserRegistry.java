package org.halkneistiyor.security;

import java.util.Collection;
import java.util.EnumSet;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.halkneistiyor.security.model.SocialUser;
import org.halkneistiyor.security.model.UserRole;
import org.springframework.security.core.GrantedAuthority;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

public class GaeDatastoreUserRegistry implements UserRegistry {
	
	private final static Log log = LogFactory.getLog(GaeDatastoreUserRegistry.class);
	
	// TODO: use a decent Data Access Layer
	
	private static final String USER_TYPE = SocialUser.class.getSimpleName();
	private static final String USER_FIRSTNAME = "firstname";
	private static final String USER_LASTNAME = "lastname";
	private static final String USER_NICKNAME = "nickname";
	private static final String USER_EMAIL = "email";
	private static final String USER_ENABLED = "enabled";
	private static final String USER_AUTHORITIES = "authorities";

	public SocialUser findUser(String userId) {
		if (log.isDebugEnabled())
			log.debug("findUser <- " + userId);
		
		Key key = KeyFactory.createKey(USER_TYPE, userId);
		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();

		try {
			Entity user = datastore.get(key);

			long binaryAuthorities = (Long) user.getProperty(USER_AUTHORITIES);
			Set<UserRole> roles = EnumSet.noneOf(UserRole.class);

			for (UserRole r : UserRole.values()) {
				if ((binaryAuthorities & (1 << r.getBit())) != 0) {
					roles.add(r);
				}
			}

			String keyName = user.getKey().getName();
			String nickname = (String) user.getProperty(USER_NICKNAME);
			String email = (String) user.getProperty(USER_EMAIL);
			String firstname = (String) user.getProperty(USER_FIRSTNAME);
			String lastname = (String) user.getProperty(USER_LASTNAME);
			Boolean enabled = (Boolean) user.getProperty(USER_ENABLED);
			SocialUser u = new SocialUser();
			u.setKey(keyName);
			u.setEmail(email);
			u.setFirstname(firstname);
			u.setLastname(lastname);
			u.setNickname(nickname);
			u.setEnabled(enabled);

			return u;
			
		} catch (EntityNotFoundException e) {
			log.debug(userId + " not found in datastore");
			return null;
		}
	}

	public void registerUser(SocialUser newUser) {
		if (log.isDebugEnabled())
			log.debug("registerUser <- " + newUser);
		
		Key key = KeyFactory.createKey(USER_TYPE, newUser.getKey());
		Entity user = new Entity(key);
		user.setProperty(USER_EMAIL, newUser.getEmail());
		user.setProperty(USER_NICKNAME, newUser.getNickname());
		user.setProperty(USER_FIRSTNAME, newUser.getFirstname());
		user.setProperty(USER_LASTNAME, newUser.getLastname());
		user.setUnindexedProperty(USER_ENABLED, newUser.isEnabled());

		Collection<? extends GrantedAuthority> roles = newUser.getAuthorities();

		long binaryAuthorities = 0;

		for (GrantedAuthority r : roles) {
			binaryAuthorities |= 1 << ((UserRole) r).getBit();
		}

		user.setUnindexedProperty(USER_AUTHORITIES, binaryAuthorities);

		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();
		datastore.put(user);
	}

	public void removeUser(String userId) {
		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();
		Key key = KeyFactory.createKey(USER_TYPE, userId);

		datastore.delete(key);
	}

}
