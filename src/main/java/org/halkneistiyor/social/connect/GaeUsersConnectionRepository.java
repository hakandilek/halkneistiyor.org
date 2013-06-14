package org.halkneistiyor.social.connect;

import java.util.List;
import java.util.Set;

import org.halkneistiyor.datamodel.SocialConnectionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.ConnectionKey;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.connect.UsersConnectionRepository;

import com.google.appengine.repackaged.com.google.common.base.Strings;

public class GaeUsersConnectionRepository implements UsersConnectionRepository {

	ConnectionFactoryLocator connectionFactoryLocator;

	TextEncryptor textEncryptor;

	SocialConnectionManager socialConnectionManager;

	@Autowired
	public GaeUsersConnectionRepository(
			ConnectionFactoryLocator connectionFactoryLocator,
			TextEncryptor textEncryptor,
			SocialConnectionManager socialConnectionManager) {
		super();
		this.connectionFactoryLocator = connectionFactoryLocator;
		this.textEncryptor = textEncryptor;
		this.socialConnectionManager = socialConnectionManager;
	}

	@Override
	public List<String> findUserIdsWithConnection(Connection<?> connection) {
		ConnectionKey key = connection.getKey();
		String providerId = key.getProviderId();
		String providerUserId = key.getProviderUserId();

		List<String> userIds = socialConnectionManager
				.findUserIdsByProviderUserId(providerId, providerUserId);
		return userIds;
	}

	@Override
	public Set<String> findUserIdsConnectedTo(String providerId,
			Set<String> providerUserIds) {
		Set<String> userIds = socialConnectionManager.findUserIdsConnectedTo(
				providerId, providerUserIds);
		return userIds;
	}

	@Override
	public ConnectionRepository createConnectionRepository(String userId) {
		if (Strings.isNullOrEmpty(userId)) {
			return null;
		}

		return new GaeConnectionRepository(userId, connectionFactoryLocator,
				textEncryptor, socialConnectionManager);
	}

}
