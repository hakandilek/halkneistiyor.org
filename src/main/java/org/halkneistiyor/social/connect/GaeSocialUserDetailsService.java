package org.halkneistiyor.social.connect;

import org.halkneistiyor.datamodel.SocialUser;
import org.halkneistiyor.datamodel.SocialUserManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.social.security.SocialUserDetails;
import org.springframework.social.security.SocialUserDetailsService;

public class GaeSocialUserDetailsService implements SocialUserDetailsService {

    @Autowired
	SocialUserManager socialUserManager;
	
	public GaeSocialUserDetailsService() {
	}

	@Override
	public SocialUserDetails loadUserByUserId(String userId) throws UsernameNotFoundException, DataAccessException {
		SocialUser user = null;
		try {
			user = socialUserManager.findUser(userId);
		} catch (Exception e) {
			throw new DataRetrievalFailureException("error occured finging user: " + userId, e);
		}
		if (user == null)
			throw new UsernameNotFoundException(userId + " not found");
		return user;
	}

}
