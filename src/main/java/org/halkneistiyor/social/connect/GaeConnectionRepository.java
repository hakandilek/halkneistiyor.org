package org.halkneistiyor.social.connect;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import org.halkneistiyor.datamodel.SocialConnection;
import org.halkneistiyor.datamodel.SocialConnectionManager;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionData;
import org.springframework.social.connect.ConnectionFactory;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.ConnectionKey;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.connect.DuplicateConnectionException;
import org.springframework.social.connect.NoSuchConnectionException;
import org.springframework.social.connect.NotConnectedException;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

public class GaeConnectionRepository implements ConnectionRepository {

	private String userId;
	private ConnectionFactoryLocator connectionFactoryLocator;
	private TextEncryptor textEncryptor;
	private SocialConnectionManager socialConnectionManager;

	public GaeConnectionRepository(String userId,
			ConnectionFactoryLocator connectionFactoryLocator,
			TextEncryptor textEncryptor,
			SocialConnectionManager socialConnectionManager) {
		this.userId = userId;
		this.connectionFactoryLocator = connectionFactoryLocator;
		this.textEncryptor = textEncryptor;
		this.socialConnectionManager = socialConnectionManager;
	}

	public String getUserId() {
		return userId;
	}

	@Override
	public MultiValueMap<String, Connection<?>> findAllConnections() {
		final MultiValueMap<String, Connection<?>> connections = new LinkedMultiValueMap<String, Connection<?>>();
		final Set<String> registeredProviderIds = connectionFactoryLocator
				.registeredProviderIds();
		for (String registeredProviderId : registeredProviderIds) {
			connections.put(registeredProviderId,
					Collections.<Connection<?>> emptyList());
		}
		Collection<SocialConnection> entities = socialConnectionManager
				.findConnectionsByUserId(userId);
		for (SocialConnection sc : entities) {
			final Connection<?> connection = mapEntityToConnection(sc);
			final String providerId = connection.getKey().getProviderId();
			List<Connection<?>> connectionsForProvider = connections.get(providerId);
			if (connectionsForProvider == null || connectionsForProvider.size() == 0) {
				connections.put(providerId, new LinkedList<Connection<?>>());
			}
			connections.add(providerId, connection);
		}
		return connections;
	}

	@Override
	public List<Connection<?>> findConnections(String providerId) {
		final List<Connection<?>> connections = new LinkedList<Connection<?>>();

		Collection<SocialConnection> entities = socialConnectionManager
				.findConnectionsByUserIdAndProviderId(userId, providerId);
		for (SocialConnection sc : entities) {
			final Connection<?> connection = mapEntityToConnection(sc);
			connections.add(connection);
		}

		return connections;
	}

	@Override
	@SuppressWarnings("unchecked")
	public <A> List<Connection<A>> findConnections(Class<A> apiType) {
		String providerId = getProviderId(apiType);
		final List<?> connections = findConnections(providerId);
		return (List<Connection<A>>) connections;
	}

	@Override
	public MultiValueMap<String, Connection<?>> findConnectionsToUsers(
			MultiValueMap<String, String> providerUserIds) {

		if (providerUserIds.isEmpty()) {
			throw new IllegalArgumentException(
					"Unable to execute find: no providerUsers provided");
		}
		final SortedMap<String, SortedMap<Number, Connection<?>>> indexedConnections = new TreeMap<String, SortedMap<Number, Connection<?>>>();

		Set<String> providerIds = providerUserIds.keySet();
		for (String providerId : providerIds) {
			Set<String> providerUserId = Sets.newHashSet(providerUserIds.get(providerId));
			Collection<SocialConnection> connections = socialConnectionManager
					.findConnectionsByUserIdAndProviderIdForProviderUserIds(
							userId, providerId, providerUserId);
			for (SocialConnection sc : connections) {
				SortedMap<Number, Connection<?>> rankMap = indexedConnections
						.get(providerId);
				if (rankMap == null) {
					rankMap = new TreeMap<Number, Connection<?>>();
					indexedConnections.put(providerId, rankMap);
				}
				Connection<?> connection = mapEntityToConnection(sc);
				rankMap.put(sc.getRank(), connection);
			}
		}

		MultiValueMap<String, Connection<?>> connectionsForUsers = new LinkedMultiValueMap<String, Connection<?>>();
		Collection<SortedMap<Number, Connection<?>>> connectionRanks = indexedConnections
				.values();
		for (SortedMap<Number, Connection<?>> providerIdMap : connectionRanks) {
			for (Connection<?> connection : providerIdMap.values()) {
				String providerId = connection.getKey().getProviderId();
				List<String> userIds = providerUserIds.get(providerId);
				List<Connection<?>> connections = connectionsForUsers
						.get(providerId);
				if (connections == null) {
					//create a null array of connections
					Connection<?>[] elements = new Connection<?>[userIds.size()];
					connections = Lists.newArrayList(elements);
					connectionsForUsers.put(providerId, connections);
				}
				String providerUserId = connection.getKey().getProviderUserId();
				int connectionIndex = userIds.indexOf(providerUserId);
				connections.set(connectionIndex, connection);
			}
		}
		return connectionsForUsers;
	}

	@Override
	public Connection<?> getConnection(ConnectionKey connectionKey) {
		SocialConnection sc = getUniqueConnection(
				connectionKey.getProviderId(),
				connectionKey.getProviderUserId());
		if (sc == null)
			throw new NoSuchConnectionException(connectionKey);
		return mapEntityToConnection(sc);
	}

	@Override
	@SuppressWarnings("unchecked")
	public <A> Connection<A> getConnection(Class<A> apiType,
			String providerUserId) {
		final String providerId = getProviderId(apiType);
		return (Connection<A>) getConnection(new ConnectionKey(providerId,
				providerUserId));
	}

	@SuppressWarnings("unchecked")
	public <A> Connection<A> getPrimaryConnection(final Class<A> apiType) {
		final String providerId = getProviderId(apiType);
		final Connection<A> connection = (Connection<A>) getPrimaryConnection(providerId);
		if (connection == null) {
			throw new NotConnectedException(providerId);
		}
		return connection;
	}

	@SuppressWarnings("unchecked")
	public <A> Connection<A> findPrimaryConnection(final Class<A> apiType) {
		final String providerId = getProviderId(apiType);
		return (Connection<A>) getPrimaryConnection(providerId);
	}

	@Override
	public void addConnection(Connection<?> connection) {
		ConnectionData data = connection.createData();
		String providerId = data.getProviderId();
		String providerUserId = data.getProviderUserId();

		// check for a connection for the current provider
		if (getUniqueConnection(providerId, providerUserId, true) != null) {
			throw new DuplicateConnectionException(connection.getKey());
		}

		// find the rank
		SocialConnection sc = socialConnectionManager
				.getConnectionByUserIdProviderIdAndRank(userId, providerId, 1L);
		Long rank = sc == null ? 1 : sc.getRank() + 1;

		// check if a connection for the user and provider exists
		sc = socialConnectionManager.getConnectionByUserIdProviderIdAndRank(
				userId, providerId, rank);
		if (sc != null) {
			throw new DuplicateConnectionException(connection.getKey());
		}

		// create a new connection entity from connection data
		sc = new SocialConnection();
		sc.setUserId(userId);
		sc.setProviderId(data.getProviderId());
		sc.setProviderUserId(data.getProviderUserId());
		sc.setRank(rank);
		sc.setDisplayName(data.getDisplayName());
		sc.setProfileUrl(data.getProfileUrl());
		sc.setImageUrl(data.getImageUrl());
		sc.setAccessToken(encrypt(data.getAccessToken()));
		sc.setSecret(encrypt(data.getSecret()));
		sc.setRefreshToken(encrypt(data.getRefreshToken()));
		sc.setExpireTime(data.getExpireTime());

		// and store it
		socialConnectionManager.create(sc);
	}

	@Override
	public void updateConnection(Connection<?> connection) {
		final ConnectionData data = connection.createData();
		final SocialConnection sc = getUniqueConnection(data.getProviderId(),
				data.getProviderUserId());
		if (sc == null)
			return;

		sc.setDisplayName(data.getDisplayName());
		sc.setProfileUrl(data.getProfileUrl());
		sc.setImageUrl(data.getImageUrl());
		sc.setAccessToken(encrypt(data.getAccessToken()));
		sc.setSecret(encrypt(data.getSecret()));
		sc.setRefreshToken(encrypt(data.getRefreshToken()));
		sc.setExpireTime(data.getExpireTime());

		socialConnectionManager.update(sc);
	}

	@Override
	public void removeConnections(String providerId) {
		Collection<SocialConnection> connections = socialConnectionManager
				.findConnectionsByUserIdAndProviderId(userId, providerId);
		socialConnectionManager.deleteAll(connections);
	}

	@Override
	public void removeConnection(ConnectionKey connectionKey) {
		SocialConnection sc = getUniqueConnection(
				connectionKey.getProviderId(),
				connectionKey.getProviderUserId(), true);
		if (sc == null)
			return;
		socialConnectionManager.delete(sc);
	}

	// internal helpers

	private SocialConnection getUniqueConnection(final String providerId,
			final String providerUserId) {

		return getUniqueConnection(providerId, providerUserId, false);
	}

	private SocialConnection getUniqueConnection(String providerId,
			String providerUserId, boolean keysOnly) {

		return socialConnectionManager.getConnectionByUserIdAndProviderId(
				userId, providerId, providerUserId);
	}

	private Connection<?> getPrimaryConnection(String providerId) {
		SocialConnection sc = socialConnectionManager
				.getConnectionByUserIdProviderIdAndRank(userId, providerId, 1L);
		if (sc != null) {
			return mapEntityToConnection(sc);
		} else {
			return null;
		}
	}

	private Connection<?> mapEntityToConnection(SocialConnection sc) {
		final Long expireTime = sc.getExpireTime();
		final ConnectionData connectionData = new ConnectionData(
				sc.getProviderId(), sc.getProviderUserId(),
				sc.getDisplayName(), sc.getProfileUrl(), sc.getImageUrl(),
				decrypt(sc.getAccessToken()), decrypt(sc.getSecret()),
				decrypt(sc.getRefreshToken()),
				expireTime != null ? expireTime(expireTime) : null);
		final ConnectionFactory<?> connectionFactory = connectionFactoryLocator
				.getConnectionFactory(connectionData.getProviderId());
		return connectionFactory.createConnection(connectionData);
	}

	private String decrypt(final String encryptedText) {
		return encryptedText != null ? textEncryptor.decrypt(encryptedText)
				: encryptedText;
	}

	private static Long expireTime(final long expireTime) {
		return expireTime == 0 ? null : expireTime;
	}

	private <A> String getProviderId(final Class<A> apiType) {
		ConnectionFactory<A> connectionFactory = connectionFactoryLocator.getConnectionFactory(apiType);
		return connectionFactory.getProviderId();
	}

	private String encrypt(final String text) {
		return text != null ? textEncryptor.encrypt(text) : text;
	}

}
