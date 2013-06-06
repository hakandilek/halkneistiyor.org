package org.halkneistiyor.security.web;

import java.io.IOException;
import java.util.Collection;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.halkneistiyor.security.model.UserRole;
import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.web.filter.GenericFilterBean;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

public class GaeAuthenticationFilter extends GenericFilterBean {

	private static Log log = LogFactory.getLog(GaeAuthenticationFilter.class);
	
	AuthenticationDetailsSource<HttpServletRequest, WebAuthenticationDetails> ads = new WebAuthenticationDetailsSource();
	AuthenticationManager authenticationManager;
	AuthenticationFailureHandler failureHandler = new SimpleUrlAuthenticationFailureHandler();
	String registrationUrl = "/register";

	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		SecurityContext securityCtx = SecurityContextHolder.getContext();
		Authentication authentication = securityCtx.getAuthentication();

		if (authentication == null) {
			HttpServletResponse httpResponse = (HttpServletResponse) response;
			HttpServletRequest httpRequest = (HttpServletRequest) request;

			// User isn't authenticated. Check if there is a Google Accounts
			// user
			UserService userService = UserServiceFactory.getUserService();
			User user = userService.getCurrentUser();

			if (user != null) {
				if (log.isDebugEnabled())
					log.debug("user : " + user);

				// User has returned after authenticating through GAE. Need to
				// authenticate to Spring Security.
				PreAuthenticatedAuthenticationToken token = new PreAuthenticatedAuthenticationToken(
						user, null);
				WebAuthenticationDetails details = ads
						.buildDetails(httpRequest);
				token.setDetails(details);

				try {
					authentication = authenticationManager.authenticate(token);
					if (log.isDebugEnabled())
						log.debug("authentication : " + authentication);

					// Setup the security context
					securityCtx.setAuthentication(authentication);
					// Send new users to the registration page.
					Collection<? extends GrantedAuthority> authorities = authentication
							.getAuthorities();
					if (log.isDebugEnabled())
						log.debug("authorities : " + authorities);

					if (authorities.contains(UserRole.NEW_USER)) {
						httpResponse.sendRedirect(registrationUrl );
						return;
					}
				} catch (AuthenticationException e) {
					// Authentication information was rejected by the
					// authentication manager
					failureHandler.onAuthenticationFailure(httpRequest,
							httpResponse, e);
					return;
				}
			}
		}

		chain.doFilter(request, response);
	}

	public void setAuthenticationManager(
			AuthenticationManager authenticationManager) {
		this.authenticationManager = authenticationManager;
	}

	public void setFailureHandler(AuthenticationFailureHandler failureHandler) {
		this.failureHandler = failureHandler;
	}

	public void setRegistrationUrl(String registrationUrl) {
		this.registrationUrl = registrationUrl;
	}
	
}
