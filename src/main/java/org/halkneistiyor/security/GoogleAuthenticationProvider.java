package org.halkneistiyor.security;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.halkneistiyor.security.model.SocialUser;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

import com.google.appengine.api.users.User;

public class GoogleAuthenticationProvider implements AuthenticationProvider {
	private static Log log = LogFactory.getLog(GoogleAuthenticationProvider.class);
	
	UserRegistry userRegistry;

	UserFactory userFactory = new UserFactory();

	public Authentication authenticate(Authentication authentication)
			throws AuthenticationException {
		
		if (log.isDebugEnabled())
			log.debug("authenticate <-");

		User googleUser = null;
		String userKey = null;
		Object principal = authentication.getPrincipal();
		if (log.isDebugEnabled())
			log.debug("principal : " + principal);

		if (principal instanceof User) {
			googleUser = (User) principal;
			userKey = googleUser.getUserId();
		}

		if (principal instanceof SocialUser) {
			SocialUser appUser = (SocialUser) principal;
			userKey = appUser.getKey();
		}

		if (log.isDebugEnabled())
			log.debug("userKey : " + userKey);

		SocialUser socialUser = userRegistry.findUser(userKey);

		if (socialUser == null) {
			// SocialUser not in registry. Needs to register
			if (googleUser != null)
				socialUser = userFactory.fromGoogleUser(googleUser);
			else
				socialUser = (SocialUser) principal;
		}

		if (!socialUser.isEnabled()) {
			if (log.isDebugEnabled())
				log.debug("account is disabled:" + socialUser);
			throw new DisabledException("Account is disabled");
		}

		return new UserAuthentication(socialUser, authentication.getDetails());
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
