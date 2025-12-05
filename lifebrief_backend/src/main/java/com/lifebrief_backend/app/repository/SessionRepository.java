package com.lifebrief_backend.app.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lifebrief_backend.app.entity.Session;

public interface SessionRepository extends JpaRepository<Session, Long> {

    // Find by the servlet session ID (stored as string in DB)
    Optional<Session> findByToken(String token);

    // Find active session(s) for a given profile
    Optional<Session> findByProfileId(String profileId);

    // If you want to support multiple sessions per user, you could also return a list:
    // List<Session> findAllByProfileId(String profileId);

    // Find by username if needed
    Optional<Session> findByUsername(String username);

    // Delete by profileId (used in logout)
    void deleteByProfileId(String profileId);
}