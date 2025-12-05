package com.lifebrief_backend.app.controller;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.lifebrief_backend.app.entity.Session;
import com.lifebrief_backend.app.repository.SessionRepository;

@RestController
@RequestMapping("/api/healthchat")
public class SessionController {

    private final SessionRepository sessionRepo;

    public SessionController(SessionRepository sessionRepo) {
        this.sessionRepo = sessionRepo;
    }

    /**
     * Start a new chat session for a given profile.
     * Generates a token and expiry automatically.
     */
    @PostMapping("/start")
    @ResponseStatus(HttpStatus.CREATED)
    public Session startSession(@RequestParam String profileId,
                                @RequestParam String username) {
        String token = UUID.randomUUID().toString();
        LocalDateTime expiry = LocalDateTime.now().plusHours(1);

        Session session = new Session(profileId, username, token, expiry);
        return sessionRepo.save(session);
    }

    /**
     * Get an existing session by ID.
     */
    @GetMapping("/{sessionId}")
    public Session getSession(@PathVariable Long sessionId) {
        return sessionRepo.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Session not found"));
    }

    /**
     * End a session (mark endedAt).
     */
    @PostMapping("/{sessionId}/end")
    public Session endSession(@PathVariable Long sessionId) {
        Session session = sessionRepo.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Session not found"));
        session.setEndedAt(LocalDateTime.now());
        return sessionRepo.save(session);
    }

    /**
     * Delete a session completely (e.g. logout).
     */
    @DeleteMapping("/{sessionId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteSession(@PathVariable Long sessionId) {
        if (!sessionRepo.existsById(sessionId)) {
            throw new RuntimeException("Session not found");
        }
        sessionRepo.deleteById(sessionId);
    }

    /**
     * Optionally: find session by token (useful for auth).
     */
    @GetMapping("/by-token/{token}")
    public Session getByToken(@PathVariable String token) {
        return sessionRepo.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Session not found"));
    }
}