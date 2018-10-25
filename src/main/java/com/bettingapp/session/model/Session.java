package com.bettingapp.session.model;

import java.time.LocalDateTime;

/**
 * Created with IntelliJ IDEA.
 * User: shettiarachchi
 * Date: 20/10/18
 * Time: 11:03 PM
 * To change this template use File | Settings | File Templates.
 */
public class Session {

    private String sessionId;

    private int customerId;

    private LocalDateTime createdDateTime;

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public LocalDateTime getCreatedDateTime() {
        return createdDateTime;
    }

    public void setCreatedDateTime(LocalDateTime createdDateTime) {
        this.createdDateTime = createdDateTime;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Session{");
        sb.append("sessionId='").append(sessionId).append('\'');
        sb.append(", customerId=").append(customerId);
        sb.append(", createdDateTime=").append(createdDateTime);
        sb.append('}');
        return sb.toString();
    }
}
