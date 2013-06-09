package org.halkneistiyor.security;

import org.halkneistiyor.datamodel.User;

public class UserFactory {

	public User fromGoogleUser(com.google.appengine.api.users.User googleUser) {
		User u = new User();
		u.setEnabled(true);
		u.setEmail(googleUser.getEmail());
		u.setUserId(googleUser.getUserId());
		u.setNickname(googleUser.getNickname());
		return u;
	}

}
