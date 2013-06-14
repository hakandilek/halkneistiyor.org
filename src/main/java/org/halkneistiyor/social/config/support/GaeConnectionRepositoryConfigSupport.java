/*
 * Copyright 2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.halkneistiyor.social.config.support;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.halkneistiyor.social.connect.GaeUsersConnectionRepository;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.social.config.support.AbstractConnectionRepositoryConfigSupport;
import org.springframework.social.connect.jdbc.JdbcUsersConnectionRepository;

/**
 * Support class providing methods for configuring a singleton {@link JdbcUsersConnectionRepository} bean and a request-scoped JdbcConnectionRepository bean.
 * Used by JdbcConnectionRepositoryRegistrar (for EnableJdbcConnectionRepository) and JdbcConnectionRepositoryBeanDefinitionParser for XML configuration.
 * @author Craig Walls
 */
public abstract class GaeConnectionRepositoryConfigSupport extends AbstractConnectionRepositoryConfigSupport {

	private final static Log logger = LogFactory.getLog(GaeConnectionRepositoryConfigSupport.class);

	public BeanDefinition registerGaeConnectionRepositoryBeans(BeanDefinitionRegistry registry, String connectionRepositoryId, String usersConnectionRepositoryId, 
			String connectionFactoryLocatorRef, String socialConnectionManagerRef, String encryptorRef, String userIdSourceRef) {
		registerUsersConnectionRepositoryBeanDefinition(registry, usersConnectionRepositoryId, connectionFactoryLocatorRef, socialConnectionManagerRef, encryptorRef);
		return registerConnectionRepository(registry, usersConnectionRepositoryId, connectionRepositoryId, userIdSourceRef);		
	}
	
	
	private BeanDefinition registerUsersConnectionRepositoryBeanDefinition(BeanDefinitionRegistry registry, String usersConnectionRepositoryId, 
			String connectionFactoryLocatorRef, String socialConnectionManagerRef, String encryptorRef) {
		if (logger.isDebugEnabled()) {
			logger.debug("Registering JdbcUsersConnectionRepository bean");
		}				
		BeanDefinitionBuilder usersConnectionRepositoryBeanBuilder = BeanDefinitionBuilder.genericBeanDefinition(GaeUsersConnectionRepository.class)
				.addConstructorArgReference(connectionFactoryLocatorRef)
				.addConstructorArgReference(encryptorRef).addConstructorArgReference(socialConnectionManagerRef);
		BeanDefinition usersConnectionRepositoryBD = usersConnectionRepositoryBeanBuilder.getBeanDefinition();
		BeanDefinition scopedProxyBean = decorateWithScopedProxy(usersConnectionRepositoryId, usersConnectionRepositoryBD, registry);
		registry.registerBeanDefinition(usersConnectionRepositoryId, scopedProxyBean);
		return scopedProxyBean;
	}

}
