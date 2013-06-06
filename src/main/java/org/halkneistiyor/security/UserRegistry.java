package org.halkneistiyor.security;

import org.halkneistiyor.security.model.User;

public interface UserRegistry {
	User findUser(String userId);

	void registerUser(User newUser);

	void removeUser(String userId);
}
