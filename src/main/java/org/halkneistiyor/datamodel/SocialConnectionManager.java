package org.halkneistiyor.datamodel;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public interface SocialConnectionManager {

	List<String> findUserIdsByProviderUserId(String providerId,
			String providerUserId);

	Set<String> findUserIdsConnectedTo(String providerId,
			Set<String> providerUserIds);

	Collection<SocialConnection> findConnectionsByUserId(String userId);

	Collection<SocialConnection> findConnectionsByUserIdAndProviderId(String userId,
			String providerId);

	Collection<SocialConnection> findConnectionsByUserIdAndProviderIdForProviderUserIds(String userId,
			String providerId, Set<String> providerUserIds);

	SocialConnection getConnectionByUserIdAndProviderId(String userId,
			String providerId, String providerUserId);

	SocialConnection getConnectionByUserIdProviderIdAndRank(String userId,
			String providerId, Long rank);

	String create(SocialConnection sc);

	void update(SocialConnection sc);

	void deleteAll(Collection<SocialConnection> connections);

	void delete(SocialConnection sc);

}
