package org.halkneistiyor.datamodel.gae;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import org.halkneistiyor.datamodel.RequestEntry;
import org.halkneistiyor.datamodel.RequestManager;
import org.halkneistiyor.datamodel.SocialUser;
import org.halkneistiyor.datamodel.SocialUserManager;
import org.halkneistiyor.datamodel.Vote;
import org.halkneistiyor.security.model.UserRole;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;
import java.util.EnumSet;
import java.util.Set;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

/**
 * @author Erdinc Yilmazel (eyilmazel@tripadvisor.com)
 * @since 6/8/13
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/spring/app-context.xml"})
public class GaeDatastoreRequestManagerTest
{
    private final LocalServiceTestHelper helper =
        new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());

    @Autowired
    RequestManager requestManager;

    @Autowired
    SocialUserManager userManager;

    private static SocialUser user, user2, user3;

    @BeforeClass
    public static void setUpTest()
    {
        user = new SocialUser();
        user.setEmail("test@halkneistiyor.org");
        user.setEnabled(true);
        user.setFirstName("TestName");
        user.setLastName("TestLastName");
        user.setNickname("test");
        Set<UserRole> roles = EnumSet.of(UserRole.ADMIN, UserRole.USER);
        user.setRoles(roles);

        user2 = new SocialUser();
        user2.setEmail("test@halkneistiyor.org");
        user2.setEnabled(true);
        user2.setFirstName("TestName");
        user2.setLastName("TestLastName");
        user2.setNickname("test");
        Set<UserRole> roles2 = EnumSet.of(UserRole.ADMIN, UserRole.USER);
        user2.setRoles(roles2);

        user3 = new SocialUser();
        user3.setEmail("test@halkneistiyor.org");
        user3.setEnabled(true);
        user3.setFirstName("TestName");
        user3.setLastName("TestLastName");
        user3.setNickname("test");
        Set<UserRole> roles3 = EnumSet.of(UserRole.ADMIN, UserRole.USER);
        user3.setRoles(roles3);
    }

    @Before
    public void setUp()
    {
        helper.setUp();

        userManager.registerUser(user);
        userManager.registerUser(user2);
        userManager.registerUser(user3);
    }

    @After
    public void tearDown()
    {
        helper.tearDown();
    }

    @Test
    public void testSpringInjections()
    {
        assertNotNull("Testing request manager injection", requestManager);
    }

    @Test
    public void testAddRequest() throws Exception
    {
        RequestEntry entry = new RequestEntry();
        entry.setEntryDate(new Date());
        entry.setRequest("Gezi Parki kisla olmasin!");
        entry.setUrlId("gezi-parki-kisla-olmasin");
        entry.setYesCount(100);
        entry.setNoCount(20);
        entry.setRequesterId(user.getUserId());

        String requestId = requestManager.addRequest(entry);

        assertNotNull(requestId);

        RequestEntry requestFromDs = requestManager.getRequest(requestId);

        assertNotNull(requestFromDs);
        assertEquals(entry.getEntryDate(), requestFromDs.getEntryDate());
        assertEquals(entry.getRequest(), requestFromDs.getRequest());
        assertEquals(entry.getUrlId(), requestFromDs.getUrlId());
        assertEquals(entry.getYesCount(), requestFromDs.getYesCount());
        assertEquals(entry.getNoCount(), requestFromDs.getNoCount());
        assertEquals(entry.getRequesterId(), requestFromDs.getRequesterId());
    }

    @Test
    public void testVote() throws Exception
    {
        RequestEntry entry = new RequestEntry();
        entry.setEntryDate(new Date());
        entry.setRequest("Gezi Parki kisla olmasin!");
        entry.setUrlId("gezi-parki-kisla-olmasin");
        entry.setYesCount(100);
        entry.setNoCount(20);
        entry.setRequesterId(user.getUserId());

        String requestId = requestManager.addRequest(entry);
        assertNotNull(requestId);

        String vote1Id = requestManager.registerVote(user.getUserId(), requestId, true);
        String vote2Id = requestManager.registerVote(user2.getUserId(), requestId, true);
        String vote3Id = requestManager.registerVote(user3.getUserId(), requestId, false);

        RequestEntry updatedRequest = requestManager.getRequest(requestId);

        assertEquals(102, updatedRequest.getYesCount());
        assertEquals(21, updatedRequest.getNoCount());

        Vote vote1 = requestManager.getVote(user.getUserId(), requestId);
        Vote vote2 = requestManager.getVote(user2.getUserId(), requestId);
        Vote vote3 = requestManager.getVote(user3.getUserId(), requestId);

        assertEquals("Testing vote 1 id", vote1Id, vote1.getVoteId());
        assertEquals("Testing vote 2 id", vote2Id, vote2.getVoteId());
        assertEquals("Testing vote 3 id", vote3Id, vote3.getVoteId());
        assertEquals(requestId, vote1.getRequestId());
        assertEquals(requestId, vote2.getRequestId());
        assertEquals(requestId, vote3.getRequestId());

        assertTrue(vote1.isAccepted());
        assertTrue(vote2.isAccepted());
        assertFalse(vote3.isAccepted());

        assertEquals(vote1.getUserId(), user.getUserId());
        assertEquals(vote2.getUserId(), user2.getUserId());
        assertEquals(vote3.getUserId(), user3.getUserId());
    }

    @Test
    public void testChangeVote() throws Exception
    {
        RequestEntry entry = new RequestEntry();
        entry.setEntryDate(new Date());
        entry.setRequest("Gezi Parki kisla olmasin!");
        entry.setUrlId("gezi-parki-kisla-olmasin");
        entry.setYesCount(100);
        entry.setNoCount(20);
        entry.setRequesterId(user.getUserId());

        String requestId = requestManager.addRequest(entry);
        assertNotNull(requestId);

        String voteId = requestManager.registerVote(user2.getUserId(), requestId, true);

        RequestEntry updatedRequest = requestManager.getRequest(requestId);

        assertEquals(101, updatedRequest.getYesCount());
        assertEquals(20, updatedRequest.getNoCount());

        Vote vote = requestManager.getVote(user2.getUserId(), requestId);

        assertTrue(vote.isAccepted());

        String voteId2 = requestManager.registerVote(user2.getUserId(), requestId, false);

        Key vote1Key = KeyFactory.stringToKey(voteId);
        Key vote2Key = KeyFactory.stringToKey(voteId2);

        assertEquals(vote1Key, vote2Key);

        RequestEntry updatedRequest2 = requestManager.getRequest(requestId);

        assertEquals(100, updatedRequest2.getYesCount());
        assertEquals(21, updatedRequest2.getNoCount());

        Vote vote2 = requestManager.getVote(user2.getUserId(), requestId);

        assertFalse(vote2.isAccepted());
    }
}
