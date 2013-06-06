package org.halkneistiyor.security;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.halkneistiyor.security.model.User;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

public class GoogleAuthenticationProvider implements AuthenticationProvider {
	private static Log log = LogFactory.getLog(GoogleAuthenticationProvider.class);
	
	UserRegistry userRegistry;

	UserFactory userFactory = new UserFactory();

	public Authentication authenticate(Authentication authentication)
			throws AuthenticationException {
		
		if (log.isDebugEnabled())
			log.debug("authenticate <-");

		com.google.appengine.api.users.User googleUser = null;
		String userKey = null;
		Object principal = authentication.getPrincipal();
		if (log.isDebugEnabled())
			log.debug("principal : " + principal);

		if (principal instanceof com.google.appengine.api.users.User) {
			googleUser = (com.google.appengine.api.users.User) principal;
			userKey = googleUser.getUserId();
		}

		if (principal instanceof User) {
			User appUser = (User) principal;
			userKey = appUser.getKey();
		}

		if (log.isDebugEnabled())
			log.debug("userKey : " + userKey);

		User user = userRegistry.findUser(userKey);

		if (user == null) {
			// User not in registry. Needs to register
			if (googleUser != null)
				user = userFactory.fromGoogleUser(googleUser);
			else
				user = (User) principal;
		}

		if (!user.isEnabled()) {
			if (log.isDebugEnabled())
				log.debug("account is disabled:" + user);
			throw new DisabledException("Account is disabled");
		}

		return new UserAuthentication(user, authentication.getDetails());
	}

	public final boolean supports(Class<?> authentication) {
		return PreAuthenticatedAuthenticationToken.class
				.isAssignableFrom(authentication);
	}

	public void setUserRegistry(UserRegistry userRegistry) {
		this.userRegistry = userRegistry;
	}

	public void setUserFactory(UserFactory userFactory) {
		this.userFactory = userFactory;
	}

}
