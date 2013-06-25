package org.halkneistiyor.datamodel.gae;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.CompositeFilter;
import com.google.appengine.api.datastore.Query.CompositeFilterOperator;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.google.common.collect.Lists;

public class QueryBuilder<M> {

	private Query query;
	private Filter filter;
	private EntityBuilder<M> entityBuilder;

    public QueryBuilder(String kind, EntityBuilder<M> entityBuilder) {
		this.entityBuilder = entityBuilder;
		query = new Query(kind);
	}

	public <V> QueryBuilder<M> eq(String property, V value) {
		Filter nf = new Query.FilterPredicate(property, Query.FilterOperator.EQUAL, value);
		addFilter(nf);
		return this;
	}

	public <V> QueryBuilder<M> in(String property, Set<V> values) {
		Filter nf = new Query.FilterPredicate(property, Query.FilterOperator.IN, values);
		addFilter(nf);
		return this;
	}

	public QueryBuilder<M> sort(String property) {
		query.addSort(property);
		return this;
	}

	public QueryBuilder<M> sort(String property, SortDirection direction) {
		query.addSort(property, direction);
		return this;
	}

	public QueryBuilder<M> sort(String... properties) {
		for (String property : properties) {
			query.addSort(property);
		}
		return this;
	}

	public PreparedQuery prepare(DatastoreService datastore) {
		return datastore.prepare(query);
	}

	public Collection<M> list(DatastoreService datastore) {
		PreparedQuery pq = prepare(datastore);
		ArrayList<M> l = Lists.newArrayList();
		Iterable<Entity> it = pq.asIterable();
		if (it == null) return null;
		for (Entity e : it) {
			l.add(entityBuilder.buildModel(e));
		}
		return l;
	}

	public M single(DatastoreService datastore) {
		PreparedQuery pq = prepare(datastore);
		Entity e = pq.asSingleEntity();
		if (e == null) return null;
        return entityBuilder.buildModel(e);
	}

	private void addFilter(Filter nf) {
		if (filter == null) {
			filter = nf;
			query.setFilter(filter);
		} else {
            List<Filter> filterList;
            if (filter instanceof CompositeFilter) {
				CompositeFilter cf = (CompositeFilter) filter;
				filterList = Lists.newArrayList(cf.getSubFilters());
				filterList.add(nf);
			} else {
				filterList = Lists.newArrayList(filter, nf);
			}
			CompositeFilter cf = new Query.CompositeFilter(CompositeFilterOperator.AND, filterList);
			filter = cf;
			query.setFilter(cf);
		}
	}

}
