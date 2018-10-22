package com.bettingapp.session;

import com.bettingapp.service.Service;
import com.bettingapp.session.model.Session;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.UUID;

/**
 * Created with IntelliJ IDEA.
 * User: shettiarachchi
 * Date: 20/10/18
 * Time: 10:57 PM
 * To change template use File | Settings | File Templates.
 */
public class SessionService implements Service{

    private static final int SESSION_TIMEOUT = 600;  // Timeout in seconds(10 Minutes)

    private HashMap<Integer, Session> sessionMap = new HashMap();

    private static SessionService instance;

    private SessionService(){

    }

    public static SessionService getInstance(){
        if(instance == null){
           instance = new SessionService();
        }
        return instance;
    }



    public Session getNewSession(int customerId) {
        Session session = new Session();
        session.setSessionId(UUID.randomUUID().toString()
                .replace("-", "")
                .toUpperCase());
        session.setCustomerId(customerId);
        session.setCreatedDateTime(LocalDateTime.now());
        sessionMap.put(customerId, session);
        return session;
    }

    private Session getSession(int customerId) {
        Session session = sessionMap.get(customerId);
        if (session != null) {
            if (isExpiredSession(session)) {
                sessionMap.remove(customerId);
                return null;
            } else {
                return session;
            }
        } else {
            return getNewSession(customerId);
        }
    }

    public boolean isExpiredSession(Session session) {
        long age = Duration.between(LocalDateTime.now(), session.getCreatedDateTime()).getSeconds();
        return age > SESSION_TIMEOUT ? true : false;
    }


    @Override
    public String serveRequest(int id, String action) {

        return getSession(id).getSessionId();
    }
}
