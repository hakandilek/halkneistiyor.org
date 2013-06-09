package org.halkneistiyor.security;

import java.util.Collection;

import org.halkneistiyor.datamodel.SocialUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

public class UserAuthentication extends PreAuthenticatedAuthenticationToken
		implements Authentication {

	private static final long serialVersionUID = 1L;

	public UserAuthentication(SocialUser principal, Object credentials) {
		super(principal, credentials);
	}

	public UserAuthentication(SocialUser principal, Object credentials,
			Collection<? extends GrantedAuthority> authorities) {
		super(principal, credentials, authorities);
	}

}
