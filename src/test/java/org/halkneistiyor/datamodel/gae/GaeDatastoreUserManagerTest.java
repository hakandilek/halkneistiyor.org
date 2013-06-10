package org.halkneistiyor.datamodel.gae;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.Email;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import org.halkneistiyor.datamodel.SocialUser;
import org.halkneistiyor.datamodel.SocialUserManager;
import org.halkneistiyor.security.model.UserRole;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.EnumSet;
import java.util.Set;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

/**
 * @author Erdinc Yilmazel (eyilmazel@tripadvisor.com)
 * @since 6/8/13
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/spring/app-context.xml"})
public class GaeDatastoreUserManagerTest
{
    private final LocalServiceTestHelper helper =
        new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());

    @Autowired
    SocialUserManager userManager;
    
    @Autowired
    DatastoreService datastore;

    @Before
    public void setUp()
    {
        helper.setUp();
    }

    @After
    public void tearDown()
    {
        helper.tearDown();
    }

    @Test
    public void testRegisterUser() throws Exception
    {
        SocialUser user = new SocialUser();
        user.setEmail("test@halkneistiyor.org");
        user.setEnabled(true);
        user.setFirstName("TestName");
        user.setLastName("TestLastName");
        user.setNickname("test");
        Set<UserRole> roles = EnumSet.of(UserRole.ADMIN, UserRole.USER);
        user.setRoles(roles);

        String userId = userManager.registerUser(user);

        assertNotNull(userId);
        Key key = KeyFactory.stringToKey(userId);
        assertEquals(SocialUser.KIND, key.getKind());

        Entity entity = datastore.get(key);
        assertNotNull(entity);
        
        assertEquals(new Email("test@halkneistiyor.org"), entity.getProperty(UserEntityBuilder.EMAIL));
        assertEquals(true, entity.getProperty(UserEntityBuilder.ENABLED));
        assertEquals("TestName", entity.getProperty(UserEntityBuilder.FIRST_NAME));
        assertEquals("TestLastName", entity.getProperty(UserEntityBuilder.LAST_NAME));
        assertEquals("test", entity.getProperty(UserEntityBuilder.NICK_NAME));
        
        SocialUser userFromEntity = UserEntityBuilder.getUser(entity);
        
        assertEquals(user.getEmail(), userFromEntity.getEmail());
        assertEquals(user.isEnabled(), userFromEntity.isEnabled());
        assertEquals(user.getFirstName(), userFromEntity.getFirstName());
        assertEquals(user.getLastName(), userFromEntity.getLastName());
        assertEquals(user.getNickname(), userFromEntity.getNickname());
        assertEquals(user.getRoles(), userFromEntity.getRoles());
        
        SocialUser userFromManager = userManager.findUser(userId);
        
        assertNotNull(userFromManager);

        assertEquals(user.getEmail(), userFromManager.getEmail());
        assertEquals(user.isEnabled(), userFromManager.isEnabled());
        assertEquals(user.getFirstName(), userFromManager.getFirstName());
        assertEquals(user.getLastName(), userFromManager.getLastName());
        assertEquals(user.getNickname(), userFromManager.getNickname());
        assertEquals(user.getRoles(), userFromManager.getRoles());
    }

    @Test
    public void testGetUserByEmail()
    {
        SocialUser user = new SocialUser();
        user.setEmail("test@halkneistiyor.org");
        user.setEnabled(true);
        user.setFirstName("TestName");
        user.setLastName("TestLastName");
        user.setNickname("test");
        Set<UserRole> roles = EnumSet.of(UserRole.ADMIN, UserRole.USER);
        user.setRoles(roles);

        String userId = userManager.registerUser(user);

        assertNotNull(userId);

        SocialUser userByEmail = userManager.findUserByEmail("test@halkneistiyor.org");
        assertEquals(user.getEmail(), userByEmail.getEmail());
        assertEquals(user.isEnabled(), userByEmail.isEnabled());
        assertEquals(user.getFirstName(), userByEmail.getFirstName());
        assertEquals(user.getLastName(), userByEmail.getLastName());
        assertEquals(user.getNickname(), userByEmail.getNickname());
        assertEquals(user.getRoles(), userByEmail.getRoles());
    }
}
