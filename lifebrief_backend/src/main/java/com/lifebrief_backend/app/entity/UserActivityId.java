package com.lifebrief_backend.app.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

public class UserActivityId implements Serializable {
    private Long sessionId;
    private LocalDateTime timestamp;

    public UserActivityId() {}

    public UserActivityId(Long sessionId, LocalDateTime timestamp) {
        this.sessionId = sessionId;
        this.timestamp = timestamp;
    }

    public Long getSessionId() { return sessionId; }
    public void setSessionId(Long sessionId) { this.sessionId = sessionId; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserActivityId)) return false;
        UserActivityId that = (UserActivityId) o;
        return Objects.equals(sessionId, that.sessionId) &&
               Objects.equals(timestamp, that.timestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sessionId, timestamp);
    }
}