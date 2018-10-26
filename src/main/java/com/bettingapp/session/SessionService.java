package com.bettingapp.session;

import com.bettingapp.common.model.RequestDetails;
import com.bettingapp.common.model.ResponseDetails;
import com.bettingapp.service.Service;
import com.bettingapp.session.model.Session;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created with IntelliJ IDEA.
 * User: shettiarachchi
 * Date: 20/10/18
 * Time: 10:57 PM
 * To class is responsible for session management of the system. Every session will be timed out after 10 minutes.
 */
public class SessionService implements Service {

    private static final int SESSION_TIMEOUT = 600;  // Timeout in seconds(10 Minutes)

    private ConcurrentHashMap<Integer, Session> customerToSessionMap = new ConcurrentHashMap<>();

    private ConcurrentHashMap<String, Integer> sessionToCustomerMap = new ConcurrentHashMap<>();

    private static SessionService instance;

    private SessionService() {
    }

    public static SessionService getInstance() {
        if (instance == null) {
            instance = new SessionService();
        }
        return instance;
    }

    /**
     * This method will always return a new session object.
     * @param customerId
     * @return  New session object
     */
    public Session getNewSession(int customerId) {

        Session session = new Session();
        session.setSessionId(UUID.randomUUID().toString()
                .replace("-", "")
                .toUpperCase());
        session.setCustomerId(customerId);
        session.setCreatedDateTime(LocalDateTime.now());
        customerToSessionMap.put(customerId, session);
        return session;
    }

    /**
     * This method will return the associated session object of a customer.
     * If the current session has been expired, it would return null otherwise return the current session.
     * @param customerId
     * @return Attached session object.
     */
    public Session getSession(int customerId) {

        Session session = customerToSessionMap.get(customerId);
        if (session != null) {
            if (isExpiredSession(session)) {
                customerToSessionMap.remove(customerId);
                sessionToCustomerMap.remove(session.getSessionId());
                return null;
            }
        } else {
            session = getNewSession(customerId);
            customerToSessionMap.put(customerId, session);
            sessionToCustomerMap.put(session.getSessionId(), customerId);
        }
        System.out.println(session);
        return session;
    }

    /**
     * This method will return the corresponding session object for a session Id.
     * @param sessionId
     * @return
     */
    public Session getSession(String sessionId) {
        if (!sessionToCustomerMap.isEmpty()) {
            Integer customerId = sessionToCustomerMap.get(sessionId);
            if (customerId != null) {
                return getSession(customerId);
            }
        }
        return null;
    }


    public boolean isExpiredSession(Session session) {
        long age = Duration.between(session.getCreatedDateTime(), LocalDateTime.now()).getSeconds();
        return age > SESSION_TIMEOUT;
    }

    /**
     * This method would handle the session based on the situation.
     * @param requestDetails Request object that contains the necessary information.
     * @return
     */
    @Override
    public ResponseDetails serveRequest(RequestDetails requestDetails) {

        ResponseDetails response;
        try {
            Session session = getSession(requestDetails.getId());
            if (session == null) {
                response = new ResponseDetails(ResponseDetails.INTERNAL_ERROR, "Session has been expired");
                return response;
            }
            response = new ResponseDetails(ResponseDetails.OK, session.getSessionId());
        } catch (Exception e) {
            response = new ResponseDetails(ResponseDetails.INTERNAL_ERROR, "An error occurred while retrieving a session");
            e.printStackTrace();
        }
        return response;
    }

    public ConcurrentHashMap<Integer, Session> getCustomerToSessionMap() {
        return customerToSessionMap;
    }

    public ConcurrentHashMap<String, Integer> getSessionToCustomerMap() {
        return sessionToCustomerMap;
    }
}
