package org.halkneistiyor.datamodel.gae;

import org.halkneistiyor.datamodel.SocialConnection;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

public class SocialConnectionEntityBuilder implements EntityBuilder<SocialConnection> {
	public static final String KEY = "key";
	public static final String USER_ID = "userId";
	public static final String PROVIDER_ID = "providerId";
	public static final String PROVIDER_USER_ID = "providerUserId";
	public static final String RANK = "rank";
	public static final String DISPLAY_NAME = "displayName";
	public static final String PROFILE_URL = "profileUrl";
	public static final String IMAGE_URL = "imageUrl";
	public static final String ACCESS_TOKEN = "accessToken";
	public static final String SECRET = "secret";
	public static final String REFRESH_TOKEN = "refreshToken";
	public static final String EXPIRE_TIME = "expireTime";

	@Override
	public SocialConnection buildModel(Entity entity) {
		SocialConnection sc = new SocialConnection();
		Key key = entity.getKey();

		if (key != null) {
			sc.setKey(KeyFactory.keyToString(key));
		}

		sc.setUserId((String) entity.getProperty(USER_ID));
		sc.setProviderId((String) entity.getProperty(PROVIDER_ID));
		sc.setProviderUserId((String) entity.getProperty(PROVIDER_USER_ID));
		sc.setRank((Long) entity.getProperty(RANK));
		sc.setDisplayName((String) entity.getProperty(DISPLAY_NAME));
		sc.setProfileUrl((String) entity.getProperty(PROFILE_URL));
		sc.setImageUrl((String) entity.getProperty(IMAGE_URL));
		sc.setAccessToken((String) entity.getProperty(ACCESS_TOKEN));
		sc.setSecret((String) entity.getProperty(SECRET));
		sc.setRefreshToken((String) entity.getProperty(REFRESH_TOKEN));
		sc.setExpireTime((Long) entity.getProperty(EXPIRE_TIME));

		return sc;
	}

	@Override
	public Entity buildEntity(SocialConnection sc) {
		Entity entity;

		if (sc.getKey() != null) {
			entity = new Entity(KeyFactory.stringToKey(sc.getKey()));
		} else {
			entity = new Entity(SocialConnection.KIND);
		}

		entity.setProperty(USER_ID, sc.getUserId());
		entity.setProperty(PROVIDER_ID, sc.getProviderId());
		entity.setProperty(PROVIDER_USER_ID, sc.getProviderUserId());
		entity.setProperty(RANK, sc.getRank());
		entity.setProperty(DISPLAY_NAME, sc.getDisplayName());
		entity.setProperty(PROFILE_URL, sc.getProfileUrl());
		entity.setProperty(IMAGE_URL, sc.getImageUrl());
		entity.setProperty(ACCESS_TOKEN, sc.getAccessToken());
		entity.setProperty(SECRET, sc.getSecret());
		entity.setProperty(REFRESH_TOKEN, sc.getRefreshToken());
		entity.setProperty(EXPIRE_TIME, sc.getExpireTime());

		return entity;
	}
}
