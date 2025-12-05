package com.lifebrief_backend.app.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.lifebrief_backend.app.entity.UserActivity;
import com.lifebrief_backend.app.entity.UserActivityId;

public interface UserActivityRepository extends JpaRepository<UserActivity, UserActivityId> {

    // Fetch by session, ordered by time
    List<UserActivity> findBySessionIdOrderByTimestampAsc(Long sessionId);
    List<UserActivity> findBySessionIdOrderByTimestampDesc(Long sessionId);

    // Fetch by profile via join to session
    @Query("""
           SELECT ua
           FROM UserActivity ua
           JOIN Session s ON ua.sessionId = s.sessionId
           WHERE s.profileId = :profileId
           ORDER BY ua.timestamp DESC
           """)
    List<UserActivity> findByProfileId(@Param("profileId") String profileId);
}