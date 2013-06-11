package org.halkneistiyor.social.connection;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Collection;
import java.util.List;

import org.halkneistiyor.datamodel.SocialConnection;
import org.halkneistiyor.datamodel.SocialConnectionManager;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionData;
import org.springframework.social.connect.ConnectionFactory;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.ConnectionKey;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.google.appengine.repackaged.com.google.common.collect.Lists;
import com.google.appengine.repackaged.com.google.common.collect.Sets;

public class GaeConnectionRepositoryTest {

	@Mock
	ConnectionFactoryLocator mockConnectionFactoryLocator;

	@Mock
	SocialConnectionManager mockSocialConnectionManager;

	@Mock
	TextEncryptor mockTextEncryptor;

	@Mock
	Connection<?> mockConnection;

	@Mock
	ConnectionData mockConnectionData;

	GaeConnectionRepository repository;
	String testUserId = "test.userId";
	String testProviderId = "test.providerId";
	String testProviderUserId = "test.providerUserId";
	ConnectionKey connectionKey;

	MockConnectionFactory mockConnectionFactory = new MockConnectionFactory();

	@Before
	@SuppressWarnings("unchecked")
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		repository = new GaeConnectionRepository(testUserId, mockConnectionFactoryLocator, mockTextEncryptor,
				mockSocialConnectionManager);
		connectionKey = new ConnectionKey(testProviderId, testProviderUserId);
		when(mockConnectionFactoryLocator.registeredProviderIds())
				.thenReturn(Sets.newHashSet("provider1", "provider2"));
		when(mockConnectionFactoryLocator.getConnectionFactory(anyString())).thenReturn(mockConnectionFactory);
		when(mockConnection.getKey()).thenReturn(connectionKey);
		when(mockConnection.createData()).thenReturn(mockConnectionData);
		when(mockConnectionData.getProviderId()).thenReturn(testProviderId);
		when(mockConnectionData.getProviderUserId()).thenReturn(testProviderUserId);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testFindAllConnections() {
		SocialConnection sc = new SocialConnection();
		sc.setProviderId(testProviderId);
		when(mockSocialConnectionManager.findConnectionsByUserId(testUserId)).thenReturn(Sets.newHashSet(sc));

		MultiValueMap<String, Connection<?>> allConnections = repository.findAllConnections();
		assertNotNull(allConnections);
	}

	@Test
	public void testFindConnectionsString() {
		SocialConnection sc = new SocialConnection();
		sc.setProviderId(testProviderId);
		when(mockSocialConnectionManager.findConnectionsByUserIdAndProviderId(testUserId, testProviderId)).thenReturn(
				Sets.newHashSet(sc));

		List<Connection<?>> connections = repository.findConnections(testProviderId);
		assertNotNull(connections);
		assertFalse(connections.isEmpty());
	}

	@Test
	@SuppressWarnings("unchecked")
	public void testFindConnectionsClassOfA() {
		SocialConnection sc = new SocialConnection();
		sc.setProviderId(testProviderId);
		when(mockSocialConnectionManager.findConnectionsByUserIdAndProviderId(testUserId, testProviderId)).thenReturn(
				Sets.newHashSet(sc));
		when(mockConnectionFactoryLocator.getConnectionFactory(any(Class.class))).thenReturn(mockConnectionFactory);

		List<Connection<Facebook>> connectionsFb = repository.findConnections(Facebook.class);
		assertNotNull(connectionsFb);
		assertFalse(connectionsFb.isEmpty());
	}

	@Test
	public void testFindConnectionsToUsers() {
		MultiValueMap<String, String> providerUserIds = new LinkedMultiValueMap<>();
		providerUserIds.put(testProviderId, Lists.newArrayList("test1", testProviderUserId));
		providerUserIds.put("test", Lists.newArrayList("test"));
		SocialConnection sc1 = new SocialConnection();
		sc1.setProviderId(testProviderId);
		sc1.setUserId(testUserId);
		sc1.setRank(1L);

		when(
				mockSocialConnectionManager.findConnectionsByUserIdAndProviderIdForProviderUserIds(testUserId,
						testProviderId, Sets.newHashSet("test1", testProviderUserId))).thenReturn(
				Sets.newHashSet(sc1));

		MultiValueMap<String, Connection<?>> connectionsToUsers = repository.findConnectionsToUsers(providerUserIds);
		assertNotNull(connectionsToUsers);
		assertFalse(connectionsToUsers.isEmpty());
	}

	@Test
	public void testGetConnectionConnectionKey() {
		SocialConnection sc = new SocialConnection();
		sc.setProviderId(testProviderId);
		sc.setUserId(testUserId);
		sc.setProviderUserId(testProviderUserId);
		when(
				mockSocialConnectionManager.getConnectionByUserIdAndProviderId(testUserId, testProviderId,
						testProviderUserId)).thenReturn(sc);

		Connection<?> connection = repository.getConnection(connectionKey);
		assertNotNull(connection);
	}

	@Test
	@SuppressWarnings("unchecked")
	public void testGetConnectionClassOfAString() {
		SocialConnection sc = new SocialConnection();
		sc.setProviderId(testProviderId);
		sc.setUserId(testUserId);
		sc.setProviderUserId(testProviderUserId);
		when(
				mockSocialConnectionManager.getConnectionByUserIdAndProviderId(testUserId, testProviderId,
						testProviderUserId)).thenReturn(sc);
		when(mockConnectionFactoryLocator.getConnectionFactory(any(Class.class))).thenReturn(mockConnectionFactory);

		Connection<?> connection = repository.getConnection(Facebook.class, testProviderUserId);
		assertNotNull(connection);
	}

	@Test
	@SuppressWarnings("unchecked")
	public void testGetPrimaryConnection() {
		SocialConnection sc = new SocialConnection();
		sc.setProviderId(testProviderId);
		sc.setUserId(testUserId);
		sc.setProviderUserId(testProviderUserId);
		when(mockSocialConnectionManager.getConnectionByUserIdProviderIdAndRank(testUserId, testProviderId, 1l))
				.thenReturn(sc);
		when(mockConnectionFactoryLocator.getConnectionFactory(any(Class.class))).thenReturn(mockConnectionFactory);

		Connection<?> connection = repository.getPrimaryConnection(Facebook.class);
		assertNotNull(connection);
	}

	@Test
	@SuppressWarnings("unchecked")
	public void testFindPrimaryConnection() {
		SocialConnection sc = new SocialConnection();
		sc.setProviderId(testProviderId);
		sc.setUserId(testUserId);
		sc.setProviderUserId(testProviderUserId);
		when(mockSocialConnectionManager.getConnectionByUserIdProviderIdAndRank(testUserId, testProviderId, 1l))
				.thenReturn(sc);
		when(mockConnectionFactoryLocator.getConnectionFactory(any(Class.class))).thenReturn(mockConnectionFactory);

		Connection<?> connection = repository.findPrimaryConnection(Facebook.class);
		assertNotNull(connection);
	}

	@Test
	public void testAddConnection() {
		repository.addConnection(mockConnection);
		verify(mockSocialConnectionManager).create(any(SocialConnection.class));
	}

	@Test
	public void testUpdateConnection() {
		SocialConnection sc = new SocialConnection();
		sc.setProviderId(testProviderId);
		sc.setUserId(testUserId);
		sc.setProviderUserId(testProviderUserId);
		when(
				mockSocialConnectionManager.getConnectionByUserIdAndProviderId(testUserId, testProviderId,
						testProviderUserId)).thenReturn(sc);

		repository.updateConnection(mockConnection);
		verify(mockSocialConnectionManager).update(any(SocialConnection.class));
	}

	@Test
	public void testRemoveConnections() {
		Collection<SocialConnection> connections = Sets.newHashSet(new SocialConnection(), new SocialConnection());
		when(mockSocialConnectionManager.findConnectionsByUserIdAndProviderId(testUserId, testProviderId)).thenReturn(
				connections);

		repository.removeConnections(testProviderId);

		verify(mockSocialConnectionManager).deleteAll(connections);
	}

	@Test
	public void testRemoveConnection() {
		SocialConnection sc = new SocialConnection();
		sc.setProviderId(testProviderId);
		sc.setUserId(testUserId);
		sc.setProviderUserId(testProviderUserId);
		when(
				mockSocialConnectionManager.getConnectionByUserIdAndProviderId(testUserId, testProviderId,
						testProviderUserId)).thenReturn(sc);

		repository.removeConnection(connectionKey);
		verify(mockSocialConnectionManager).delete(sc);
	}

	@SuppressWarnings("rawtypes")
	class MockConnectionFactory extends ConnectionFactory {

		@SuppressWarnings("unchecked")
		public MockConnectionFactory() {
			super(testProviderId, null, null);
		}

		@Override
		public Connection createConnection(ConnectionData data) {
			return mockConnection;
		}

	}
}
