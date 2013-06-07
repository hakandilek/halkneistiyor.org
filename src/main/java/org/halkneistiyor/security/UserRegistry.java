package org.halkneistiyor.security;

import org.halkneistiyor.security.model.SocialUser;

public interface UserRegistry {
	SocialUser findUser(String userId);

	void registerUser(SocialUser newUser);

	void removeUser(String userId);
}
