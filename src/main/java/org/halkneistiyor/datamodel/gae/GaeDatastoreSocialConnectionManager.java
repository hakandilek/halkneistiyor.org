package org.halkneistiyor.datamodel.gae;

import static org.halkneistiyor.datamodel.gae.SocialConnectionEntityBuilder.*;
import static org.halkneistiyor.datamodel.gae.SocialConnectionEntityBuilder.PROVIDER_USER_ID;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.halkneistiyor.datamodel.SocialConnection;
import org.halkneistiyor.datamodel.SocialConnectionManager;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.google.appengine.repackaged.com.google.common.collect.Lists;

public class GaeDatastoreSocialConnectionManager implements SocialConnectionManager {

	@Autowired
	DatastoreService datastore;

	private final static Log log = LogFactory.getLog(GaeDatastoreSocialConnectionManager.class);

	EntityBuilder<SocialConnection> entityBuilder = new SocialConnectionEntityBuilder();

	@Override
	public List<String> findUserIdsByProviderUserId(String providerId, String providerUserId) {
		if (log.isDebugEnabled())
			log.debug("findUserIdsByProviderUserId <-");

		QueryBuilder<SocialConnection> qb = new QueryBuilder<SocialConnection>(SocialConnection.KIND, entityBuilder)
				.eq(PROVIDER_ID, providerId).eq(PROVIDER_USER_ID, providerUserId);
		PreparedQuery pq = qb.prepare(datastore);

		List<String> localUserIds = new ArrayList<String>();
		for (Entity result : pq.asIterable()) {
			localUserIds.add((String) result.getProperty("userId"));
		}

		return localUserIds;
	}

	@Override
	public Set<String> findUserIdsConnectedTo(String providerId, Set<String> providerUserIds) {
		if (log.isDebugEnabled())
			log.debug("findUserIdsConnectedTo <-");

		QueryBuilder<SocialConnection> qb = new QueryBuilder<SocialConnection>(SocialConnection.KIND, entityBuilder)
				.eq(PROVIDER_ID, providerId).in(PROVIDER_USER_ID, providerUserIds);
		PreparedQuery pq = qb.prepare(datastore);

		Set<String> localUserIds = new HashSet<String>();
		for (final Entity entity : pq.asIterable()) {
			localUserIds.add((String) entity.getProperty("userId"));
		}

		return localUserIds;
	}

	@Override
	public Collection<SocialConnection> findConnectionsByUserId(String userId) {
		if (log.isDebugEnabled())
			log.debug("findConnectionsByUserId <-");
		QueryBuilder<SocialConnection> qb = new QueryBuilder<SocialConnection>(SocialConnection.KIND, entityBuilder)
				.eq(USER_ID, userId).sort(PROVIDER_ID, RANK);
		return qb.list(datastore);
	}

	@Override
	public Collection<SocialConnection> findConnectionsByUserIdAndProviderId(String userId, String providerId) {
		if (log.isDebugEnabled())
			log.debug("findConnectionsByUserIdAndProviderId <-");
		QueryBuilder<SocialConnection> qb = new QueryBuilder<SocialConnection>(SocialConnection.KIND, entityBuilder)
				.eq(USER_ID, userId).eq(PROVIDER_ID, providerId).sort(RANK);
		return qb.list(datastore);
	}

	@Override
	public Collection<SocialConnection> findConnectionsByUserIdAndProviderIdForProviderUserIds(String userId,
			String providerId, Set<String> providerUserIds) {
		if (log.isDebugEnabled())
			log.debug("findConnectionsByUserIdAndProviderIdForProviderUserIds <-");
		QueryBuilder<SocialConnection> qb = new QueryBuilder<SocialConnection>(SocialConnection.KIND, entityBuilder)
				.eq(USER_ID, userId).eq(PROVIDER_ID, providerId).in(PROVIDER_USER_ID, providerUserIds);
		return qb.list(datastore);
	}

	@Override
	public SocialConnection getConnectionByUserIdAndProviderId(String userId, String providerId, String providerUserId) {
		if (log.isDebugEnabled())
			log.debug("getConnectionByUserIdAndProviderId <-");
		QueryBuilder<SocialConnection> qb = new QueryBuilder<SocialConnection>(SocialConnection.KIND, entityBuilder)
				.eq(USER_ID, userId).eq(PROVIDER_ID, providerId).eq(PROVIDER_USER_ID, providerUserId);
		return qb.single(datastore);
	}

	@Override
	public SocialConnection getConnectionByUserIdProviderIdAndRank(String userId, String providerId, Long rank) {
		if (log.isDebugEnabled())
			log.debug("getConnectionByUserIdProviderIdAndRank <-");
		QueryBuilder<SocialConnection> qb = new QueryBuilder<SocialConnection>(SocialConnection.KIND, entityBuilder)
				.eq(USER_ID, userId).eq(PROVIDER_ID, providerId).eq(RANK, rank).sort(RANK, SortDirection.DESCENDING);
		return qb.single(datastore);
	}

	@Override
	public String create(SocialConnection sc) {
		if (log.isDebugEnabled())
			log.debug("create <-" + sc);
		Entity ent = entityBuilder.buildEntity(sc);
		String key = KeyFactory.keyToString(datastore.put(ent));
		sc.setKey(key);
		return key;
	}

	@Override
	public void update(SocialConnection sc) {
		if (log.isDebugEnabled())
			log.debug("update <-");
		Entity ent = entityBuilder.buildEntity(sc);
		datastore.put(ent);
	}

	@Override
	public void deleteAll(Collection<SocialConnection> connections) {
		if (log.isDebugEnabled())
			log.debug("deleteAll <-");
		List<Key> keys = Lists.newArrayList();
		for (SocialConnection sc : connections) {
			keys.add(KeyFactory.createKey(SocialConnection.KIND, sc.getKey()));
		}
		datastore.delete(keys);
	}

	@Override
	public void delete(SocialConnection sc) {
		if (log.isDebugEnabled())
			log.debug("delete <-");
		Key key = KeyFactory.createKey(SocialConnection.KIND, sc.getKey());
		datastore.delete(key);
	}

}
