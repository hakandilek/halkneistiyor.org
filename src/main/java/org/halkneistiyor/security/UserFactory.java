package org.halkneistiyor.security;

import org.halkneistiyor.security.model.User;

public class UserFactory {

	public User fromGoogleUser(com.google.appengine.api.users.User googleUser) {
		User u = new User();
		u.setEnabled(true);
		u.setEmail(googleUser.getEmail());
		u.setKey(googleUser.getUserId());
		u.setNickname(googleUser.getNickname());
		return u;
	}

}
