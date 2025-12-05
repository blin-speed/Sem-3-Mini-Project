package com.lifebrief_backend.app.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;

@Entity
@Table(name = "user_activity")
@IdClass(UserActivityId.class)
public class UserActivity {

    @Id
    @Column(name = "session_id", nullable = false)
    private Long sessionId;

    @Id
    @Column(name = "timestamp", nullable = false, columnDefinition = "DATETIME(6)")
    private LocalDateTime timestamp;

    @Column(name = "activity_detail", length = 1000, nullable = false)
    private String activityDetail;

    public UserActivity() {}

    public UserActivity(Long sessionId, String activityDetail) {
        this.sessionId = sessionId;
        this.activityDetail = activityDetail;
        this.timestamp = LocalDateTime.now();
    }

    public Long getSessionId() { return sessionId; }
    public void setSessionId(Long sessionId) { this.sessionId = sessionId; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }

    public String getActivityDetail() { return activityDetail; }
    public void setActivityDetail(String activityDetail) { this.activityDetail = activityDetail; }
}