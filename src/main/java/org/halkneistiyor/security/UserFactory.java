package org.halkneistiyor.security;

import org.halkneistiyor.datamodel.SocialUser;

public class UserFactory {

	public SocialUser fromGoogleUser(com.google.appengine.api.users.User googleUser) {
		SocialUser u = new SocialUser();
		u.setEnabled(true);
		u.setEmail(googleUser.getEmail());
		u.setUserId(googleUser.getUserId());
		u.setNickname(googleUser.getNickname());
		return u;
	}

}
