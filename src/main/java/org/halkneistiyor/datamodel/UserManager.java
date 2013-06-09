package org.halkneistiyor.datamodel;

public interface UserManager
{
	SocialUser findUser(String userId);

	String registerUser(SocialUser newUser);

    SocialUser findUserByEmail(String email);

	void removeUser(String userId);
}
