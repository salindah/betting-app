package com.bettingapp.session;

import com.bettingapp.session.model.Session;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Created with IntelliJ IDEA.
 * User: shettiarachchi
 * Date: 21/10/18
 * Time: 12:22 AM
 * To change this template use File | Settings | File Templates.
 */
public class SessionServiceTest {

    private SessionService sessionService;

    private int customerId = 1234;

    @Before
    public void setUp() {
        sessionService = SessionService.getInstance();
    }

    @Test
    public void getNewSessionTest() {

        Session session = sessionService.getSession(customerId);

        Assert.assertNotNull(session);
        Assert.assertNotNull(session.getSessionId());
        Assert.assertEquals(customerId, session.getCustomerId());
        Assert.assertEquals(sessionService.getCustomerToSessionMap().size(), 1);
    }


    @Test
    public void getSessionTest() {

        Session session = sessionService.getSession(customerId);

        Assert.assertNotNull(session);
        Assert.assertNotNull(session.getSessionId());
        Assert.assertEquals(sessionService.getCustomerToSessionMap().size(), 1);
    }

    @Test
    public void getSessionNotExpiredTest() {

        Session session = sessionService.getNewSession(customerId);
        Assert.assertNotNull(session);
        Assert.assertNotNull(session.getSessionId());

        try {
            Thread.sleep(200 * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Session sessionAgain = sessionService.getSession(customerId);
        Assert.assertNotNull(sessionAgain);
        Assert.assertNotNull(sessionAgain.getSessionId());
        Assert.assertEquals(customerId, sessionAgain.getCustomerId());
    }


    @Test
    public void getSessionExpiredTest() {

        Session session = sessionService.getNewSession(customerId);
        Assert.assertNotNull(session);
        Assert.assertNotNull(session.getSessionId());

        try {
            Thread.sleep(700 * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Session sessionAgain = sessionService.getSession(customerId);
        Assert.assertNull(sessionAgain);
    }

    @Test
    public void getSessionBySessionIdTest() {

        Session session = sessionService.getSession(customerId);

        Assert.assertNotNull(session);
        Assert.assertNotNull(session.getSessionId());

        Session sessionAgain = sessionService.getSession(session.getSessionId());
        Assert.assertNotNull(sessionAgain);
        Assert.assertNotNull(sessionAgain.getSessionId());
        Assert.assertEquals(customerId, sessionAgain.getCustomerId());
    }

}
