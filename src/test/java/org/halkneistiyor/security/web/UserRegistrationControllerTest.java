package org.halkneistiyor.security.web;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;


import org.halkneistiyor.datamodel.SocialUser;
import org.halkneistiyor.datamodel.SocialUserManager;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.validation.BindingResult;

import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.google.appengine.tools.development.testing.LocalUserServiceTestConfig;

public class UserRegistrationControllerTest {
	
	private final LocalServiceTestHelper helper = new LocalServiceTestHelper(
			new LocalUserServiceTestConfig()).setEnvIsLoggedIn(true)
			.setEnvAuthDomain("localhost").setEnvEmail("test@localhost");

	SecurityContext context;

	UserRegistrationController controller;

	@Mock
	SocialUserManager mockRegistry;

	@Mock
	BindingResult mockResult;

	@Mock
	Authentication mockAuthentication;

	@Before
	public void setUp() throws Exception {
		helper.setUp();
		MockitoAnnotations.initMocks(this);

		controller = new UserRegistrationController();
		controller.registry = mockRegistry;
		context = new SecurityContextImpl();
		context.setAuthentication(mockAuthentication);
		SecurityContextHolder.setContext(context);
	}

	@After
	public void tearDown() throws Exception {
		helper.tearDown();
	}

	@Test
	public void register() {
		SocialUser socialUser = new SocialUser();
		when(mockAuthentication.getPrincipal()).thenReturn(socialUser);

		UserRegistrationForm form = new UserRegistrationForm();
		String result = controller.register(form, mockResult);
		Authentication authentication = context.getAuthentication();
		assertNotNull(result);
		assertNotNull(authentication);
		assertEquals(socialUser, authentication.getPrincipal());
	}

}
