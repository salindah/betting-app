package com.bettingapp.session;

import com.bettingapp.session.model.Session;

/**
 * Created with IntelliJ IDEA.
 * User: shettiarachchi
 * Date: 21/10/18
 * Time: 12:22 AM
 * To change this template use File | Settings | File Templates.
 */
public class SessionManagerTest {

    public static void main(String[] args) {
        SessionService sessionService = SessionService.getInstance();

        Session session = sessionService.getNewSession(12123);
        System.out.println(session);
    }

}
