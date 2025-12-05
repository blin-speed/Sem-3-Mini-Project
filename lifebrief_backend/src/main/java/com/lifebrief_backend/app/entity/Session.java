package com.lifebrief_backend.app.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

@Entity
@Table(name = "session")
public class Session {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "session_id")
    private Long sessionId;   // auto-increment PK

    // FK to profile.profile_id (UUID)
    @Column(name = "profile_id", nullable = false, length = 36)
    private String profileId;

    @Column(nullable = false, length = 100)
    private String username;

    @Column(nullable = false, length = 255)
    private String token;

    @Column(name = "token_expiry", nullable = false)
    private LocalDateTime tokenExpiry;

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "last_activity", insertable = false, updatable = false)
    private LocalDateTime lastActivity;

    @Column(name = "ended_at")
    private LocalDateTime endedAt;

    public LocalDateTime getEndedAt() { return endedAt; }
    public void setEndedAt(LocalDateTime endedAt) { this.endedAt = endedAt; }

    public Session() {}

    public Session(String profileId, String username, String token, LocalDateTime tokenExpiry) {
        this.profileId = profileId;
        this.username = username;
        this.token = token;
        this.tokenExpiry = tokenExpiry;
    }

    @PreUpdate
    public void onUpdate() {
        this.lastActivity = LocalDateTime.now();
    }

    // --- Getters and Setters ---
    public Long getSessionId() { return sessionId; }

    public String getProfileId() { return profileId; }
    public void setProfileId(String profileId) { this.profileId = profileId; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    public LocalDateTime getTokenExpiry() { return tokenExpiry; }
    public void setTokenExpiry(LocalDateTime tokenExpiry) { this.tokenExpiry = tokenExpiry; }

    public LocalDateTime getCreatedAt() { return createdAt; }

    public LocalDateTime getLastActivity() { return lastActivity; }
}