package com.lifebrief_backend.app.controller;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lifebrief_backend.app.repository.SessionRepository;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/api/auth")
public class ValidationController {

    private final SessionRepository sessionRepository;

    public ValidationController(SessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
    }

    @PostMapping("/validate")
    public ResponseEntity<Map<String, Object>> validate(@RequestBody Map<String, String> payload) {
        String token = payload.get("token");
        if (token == null || token.isBlank()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Collections.singletonMap("error", "Missing token"));
        }

        return sessionRepository.findByToken(token)
                .filter(session -> session.getTokenExpiry().isAfter(LocalDateTime.now()))
                .<ResponseEntity<Map<String, Object>>>map(session ->
                        ResponseEntity.ok(Collections.singletonMap("valid", true)))
                .orElseGet(() ->
                        ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                                .body(Collections.singletonMap("error", "Invalid or expired token")));
    }
}