package org.halkneistiyor.security;

import com.google.appengine.api.users.User;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.halkneistiyor.datamodel.SocialUser;
import org.halkneistiyor.datamodel.SocialUserManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

public class GoogleAuthenticationProvider implements AuthenticationProvider {
	
	private static Log log = LogFactory.getLog(GoogleAuthenticationProvider.class);

	@Autowired
    SocialUserManager userManager;

    UserFactory userFactory = new UserFactory();

    public Authentication authenticate(Authentication authentication)
        throws AuthenticationException
    {

        if (log.isDebugEnabled())
        {
            log.debug("authenticate <-");
        }

        User googleUser = null;
        String userEmail = null;
        Object principal = authentication.getPrincipal();
        if (log.isDebugEnabled())
        {
            log.debug("principal : " + principal);
        }

        if (principal instanceof User)
        {
            googleUser = (User) principal;
            userEmail = googleUser.getEmail();
        }

        if (principal instanceof SocialUser)
        {
            SocialUser appUser = (SocialUser) principal;
            userEmail = appUser.getEmail();
        }

        if (log.isDebugEnabled())
        {
            log.debug("userEmail : " + userEmail);
        }

        SocialUser user = userManager.findUserByEmail(userEmail);

        if (user == null)
        {
            // User not in registry. Needs to register
            if (googleUser != null)
            {
                user = userFactory.fromGoogleUser(googleUser);
            }
            else
            {
                user = (SocialUser) principal;
            }
        }

        if (!user.isEnabled())
        {
            if (log.isDebugEnabled())
            {
                log.debug("account is disabled:" + user);
            }
            throw new DisabledException("Account is disabled");
        }

        return new UserAuthentication(user, authentication.getDetails());
    }

    public final boolean supports(Class<?> authentication)
    {
        return PreAuthenticatedAuthenticationToken.class
            .isAssignableFrom(authentication);
    }

    public void setUserManager(SocialUserManager userManager)
    {
        this.userManager = userManager;
    }

    public void setUserFactory(UserFactory userFactory)
    {
        this.userFactory = userFactory;
    }

}
