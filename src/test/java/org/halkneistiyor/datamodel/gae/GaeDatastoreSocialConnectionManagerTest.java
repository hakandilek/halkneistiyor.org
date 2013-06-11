package org.halkneistiyor.datamodel.gae;

import static org.junit.Assert.*;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.halkneistiyor.datamodel.SocialConnection;
import org.halkneistiyor.datamodel.SocialConnectionManager;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.repackaged.com.google.common.collect.Sets;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import static org.halkneistiyor.datamodel.gae.SocialConnectionEntityBuilder.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/spring/app-context.xml" })
public class GaeDatastoreSocialConnectionManagerTest {

	private final LocalServiceTestHelper helper = new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());

	@Autowired
	SocialConnectionManager connectionManager;

	@Autowired
	DatastoreService datastore;

	@Before
	public void setUp() {
		helper.setUp();
	}

	@After
	public void tearDown() {
		helper.tearDown();
	}

	@Test
	public void testCreate() throws Exception {
		SocialConnection sc = new SocialConnection();
		sc.setUserId("userId");
		sc.setProviderId("providerId");
		sc.setProviderUserId("providerUserId");
		sc.setAccessToken("accessToken");
		sc.setDisplayName("displayName");
		sc.setExpireTime(42L);
		sc.setImageUrl("imageUrl");
		sc.setProfileUrl("profileUrl");
		sc.setRank(42L);
		sc.setRefreshToken("refreshToken");
		sc.setSecret("secret");

		String scKey = connectionManager.create(sc);

		assertNotNull(scKey);
		Key key = KeyFactory.stringToKey(scKey);
		assertEquals(SocialConnection.KIND, key.getKind());

		Entity entity = datastore.get(key);
		assertNotNull(entity);

		assertEquals("userId", entity.getProperty(USER_ID));
		assertEquals("providerId", entity.getProperty(PROVIDER_ID));
		assertEquals("providerUserId", entity.getProperty(PROVIDER_USER_ID));
		assertEquals("accessToken", entity.getProperty(ACCESS_TOKEN));
		assertEquals("displayName", entity.getProperty(DISPLAY_NAME));
		assertEquals(42L, entity.getProperty(EXPIRE_TIME));
		assertEquals("imageUrl", entity.getProperty(IMAGE_URL));
		assertEquals("profileUrl", entity.getProperty(PROFILE_URL));
		assertEquals(42L, entity.getProperty(RANK));
		assertEquals("refreshToken", entity.getProperty(REFRESH_TOKEN));
		assertEquals("secret", entity.getProperty(SECRET));

		SocialConnection modelFromEntity = new SocialConnectionEntityBuilder().buildModel(entity);

		assertEquals(sc.getUserId(), modelFromEntity.getUserId());
		assertEquals(sc.getProviderId(), modelFromEntity.getProviderId());
		assertEquals(sc.getProviderUserId(), modelFromEntity.getProviderUserId());
		assertEquals(sc.getAccessToken(), modelFromEntity.getAccessToken());
		assertEquals(sc.getDisplayName(), modelFromEntity.getDisplayName());
		assertEquals(sc.getExpireTime(), modelFromEntity.getExpireTime());
		assertEquals(sc.getImageUrl(), modelFromEntity.getImageUrl());
		assertEquals(sc.getProfileUrl(), modelFromEntity.getProfileUrl());
		assertEquals(sc.getRank(), modelFromEntity.getRank());
		assertEquals(sc.getRefreshToken(), modelFromEntity.getRefreshToken());
		assertEquals(sc.getSecret(), modelFromEntity.getSecret());

		SocialConnection modelFromManager = connectionManager.getConnectionByUserIdAndProviderId("userId",
				"providerId", "providerUserId");

		assertNotNull(modelFromManager);

		assertEquals(sc.getUserId(), modelFromManager.getUserId());
		assertEquals(sc.getProviderId(), modelFromManager.getProviderId());
		assertEquals(sc.getProviderUserId(), modelFromManager.getProviderUserId());
		assertEquals(sc.getAccessToken(), modelFromManager.getAccessToken());
		assertEquals(sc.getDisplayName(), modelFromManager.getDisplayName());
		assertEquals(sc.getExpireTime(), modelFromManager.getExpireTime());
		assertEquals(sc.getImageUrl(), modelFromManager.getImageUrl());
		assertEquals(sc.getProfileUrl(), modelFromManager.getProfileUrl());
		assertEquals(sc.getRank(), modelFromManager.getRank());
		assertEquals(sc.getRefreshToken(), modelFromManager.getRefreshToken());
		assertEquals(sc.getSecret(), modelFromManager.getSecret());

		assertNotNull(connectionManager.getConnectionByUserIdProviderIdAndRank("userId", "providerId", 42L));

		Collection<SocialConnection> scs = connectionManager.findConnectionsByUserId("userId");
		assertNotNull(scs);
		assertEquals(1, scs.size());

		scs = connectionManager.findConnectionsByUserIdAndProviderId("userId", "providerId");
		assertNotNull(scs);
		assertEquals(1, scs.size());

		scs = connectionManager.findConnectionsByUserIdAndProviderIdForProviderUserIds("userId", "providerId",
				Sets.newHashSet("providerUserId"));
		assertNotNull(scs);
		assertEquals(1, scs.size());
		
		List<String> idlist = connectionManager.findUserIdsByProviderUserId("providerId", "providerUserId");
		assertNotNull(idlist);
		assertEquals(1, idlist.size());
		
		Set<String> idset = connectionManager.findUserIdsConnectedTo("providerId", Sets.newHashSet("providerUserId"));
		assertNotNull(idset);
		assertEquals(1, idset.size());
	}

}
