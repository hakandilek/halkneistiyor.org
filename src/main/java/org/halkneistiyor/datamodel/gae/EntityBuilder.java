package org.halkneistiyor.datamodel.gae;

import com.google.appengine.api.datastore.Entity;

public interface EntityBuilder<M> {

	M buildModel(Entity entity);

	Entity buildEntity(M sc);

}