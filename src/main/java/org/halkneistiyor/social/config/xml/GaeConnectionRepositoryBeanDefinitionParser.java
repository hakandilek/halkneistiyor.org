package org.halkneistiyor.social.config.xml;

import org.halkneistiyor.social.config.support.GaeConnectionRepositoryConfigSupport;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

public class GaeConnectionRepositoryBeanDefinitionParser extends GaeConnectionRepositoryConfigSupport implements
		BeanDefinitionParser {

	public BeanDefinition parse(Element element, ParserContext parserContext) {
		String connectionRepositoryId = element.getAttribute("connection-repository-id");
		String usersConnectionRepositoryId = element.getAttribute("users-connection-repository-id");
		String connectionFactoryLocatorRef = element.getAttribute("connection-factory-locator-ref");
		String socialConnectionManagerRef = element.getAttribute("social-connection-manager-ref");
		String encryptorRef = element.getAttribute("encryptor-ref");
		String userIdSourceRef = element.getAttribute("user-id-source-ref");
		return registerGaeConnectionRepositoryBeans(parserContext.getRegistry(), connectionRepositoryId,
				usersConnectionRepositoryId, connectionFactoryLocatorRef, socialConnectionManagerRef, encryptorRef,
				userIdSourceRef);
	}

}
