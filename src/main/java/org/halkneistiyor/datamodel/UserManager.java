package org.halkneistiyor.datamodel;

public interface UserManager
{
	User findUser(String userId);

	String registerUser(User newUser);

    User findUserByEmail(String email);

	void removeUser(String userId);
}
