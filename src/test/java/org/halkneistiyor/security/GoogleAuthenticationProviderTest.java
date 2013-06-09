package org.halkneistiyor.security;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.google.appengine.tools.development.testing.LocalUserServiceTestConfig;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

public class GoogleAuthenticationProviderTest {
	GoogleAuthenticationProvider provider = new GoogleAuthenticationProvider();
	private final LocalServiceTestHelper helper = new LocalServiceTestHelper(
			new LocalUserServiceTestConfig()).setEnvIsLoggedIn(true)
			.setEnvAuthDomain("localhost").setEnvEmail("test@localhost");

	@Before
	public void setUp() throws Exception {
		helper.setUp();
		MockitoAnnotations.initMocks(this);

	}

	@After
	public void tearDown() throws Exception {
		helper.tearDown();

	}

	@Test
	public void testAuthenticate() {
		//TODO:provider.authenticate(authentication);
	}

	@Test
	public void testSupports() {
		assertTrue(provider.supports(UserAuthentication.class));
	}

}
