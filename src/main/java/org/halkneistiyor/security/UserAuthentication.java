package org.halkneistiyor.security;

import java.util.Collection;

import org.halkneistiyor.datamodel.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

public class UserAuthentication extends PreAuthenticatedAuthenticationToken
		implements Authentication {

	private static final long serialVersionUID = 1L;

	public UserAuthentication(User principal, Object credentials) {
		super(principal, credentials);
	}

	public UserAuthentication(User principal, Object credentials,
			Collection<? extends GrantedAuthority> authorities) {
		super(principal, credentials, authorities);
	}

}
