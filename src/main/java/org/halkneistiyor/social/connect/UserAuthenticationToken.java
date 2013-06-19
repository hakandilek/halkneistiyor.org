package org.halkneistiyor.social.connect;

import java.io.Serializable;

import org.halkneistiyor.datamodel.SocialUser;
import org.springframework.security.authentication.AbstractAuthenticationToken;

public class UserAuthenticationToken extends AbstractAuthenticationToken implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private SocialUser user;

	public UserAuthenticationToken(SocialUser user) {
		super(user.getAuthorities());
		this.user = user;
		setAuthenticated(user.isEnabled());
	}

	@Override
	public Object getPrincipal() {
		return user;
	}

	@Override
	public Object getCredentials() {
		return null;
	}

	public SocialUser getUser() {
		return user;
	}

	@Override
	public String toString() {
		return "UserAuthenticationToken [user=" + user + "]";
	}
	
}
