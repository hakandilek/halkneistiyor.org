package org.halkneistiyor.social.connection;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.halkneistiyor.datamodel.SocialConnectionManager;
import org.halkneistiyor.social.connect.GaeUsersConnectionRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.ConnectionKey;
import org.springframework.social.connect.ConnectionRepository;

import com.google.appengine.repackaged.com.google.common.collect.Sets;
import com.google.common.collect.Lists;

public class GaeUsersConnectionRepositoryTest {
	GaeUsersConnectionRepository repository;

	@Mock
	ConnectionFactoryLocator mockConnectionFactoryLocator;
	
	@Mock
	SocialConnectionManager mockSocialConnectionManager;
	
	@Mock
	TextEncryptor mockTextEncryptor;

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		repository = new GaeUsersConnectionRepository(mockConnectionFactoryLocator, mockTextEncryptor, mockSocialConnectionManager);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testFindUserIdsWithConnection() {
		Connection<?> connection = mock(Connection.class);
		when(connection.getKey()).thenReturn(new ConnectionKey("test.providerId", "test.providerUserId"));
		List<String> userIdList = Lists.newArrayList("test1", "test2");
		when(mockSocialConnectionManager.findUserIdsByProviderUserId("test.providerId", "test.providerUserId")).thenReturn(userIdList);
		
		List<String> userIds = repository.findUserIdsWithConnection(connection);
		assertNotNull(userIds);
		assertEquals(userIdList, userIds);
	}

	@Test
	public void testFindUserIdsConnectedTo() {
		Set<String> userIdsExpected = Sets.newHashSet("test1", "test2");
		HashSet<String> providerUserIds = Sets.newHashSet("test.providerUserId");
		when(mockSocialConnectionManager.findUserIdsConnectedTo("test.providerId", providerUserIds)).thenReturn(userIdsExpected);
		
		Set<String> userIds = repository.findUserIdsConnectedTo("test.providerId", providerUserIds);
		assertNotNull(userIds);
		assertEquals(userIdsExpected, userIds);
	}

	@Test
	public void testCreateConnectionRepository() {
		assertNull(repository.createConnectionRepository(null));
		
		ConnectionRepository cr = repository.createConnectionRepository("test.userId");
		assertNotNull(cr);
	}

}
