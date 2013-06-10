package org.halkneistiyor.datamodel.gae;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.halkneistiyor.datamodel.SocialConnection;
import org.halkneistiyor.datamodel.SocialConnectionManager;

public class GaeSocialConnectionManager implements SocialConnectionManager {

	@Override
	public List<String> findUserIdsByProviderUserId(String providerId,
			String providerUserId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<String> findUserIdsConnectedTo(String providerId,
			Set<String> providerUserIds) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<SocialConnection> findConnectionsByUserId(String userId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<SocialConnection> findConnectionsByUserIdAndProviderId(
			String userId, String providerId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SocialConnection getConnectionByUserIdAndProviderId(String userId,
			String providerId, String providerUserId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SocialConnection getConnectionByUserIdProviderIdAndRank(
			String userId, String providerId, Integer rank) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void create(SocialConnection sc) {
		// TODO Auto-generated method stub

	}

	@Override
	public void update(SocialConnection sc) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteAll(Collection<SocialConnection> connections) {
		// TODO Auto-generated method stub

	}

	@Override
	public void delete(SocialConnection sc) {
		// TODO Auto-generated method stub

	}

	@Override
	public Collection<SocialConnection> findConnectionsByUserIdAndProviderIdForProviderUserIds(
			String userId, String providerId, Collection<String> providerUserIds) {
		// TODO Auto-generated method stub
		return null;
	}

}
