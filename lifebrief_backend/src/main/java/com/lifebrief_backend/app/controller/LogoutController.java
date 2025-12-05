package com.lifebrief_backend.app.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lifebrief_backend.app.entity.Session;
import com.lifebrief_backend.app.repository.SessionRepository;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/api/auth")
public class LogoutController {

    private final SessionRepository sessionRepository;

    public LogoutController(SessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestBody Map<String, String> payload) {
        String token = payload.get("token");
        if (token == null || token.isBlank()) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Token is required for logout"));
        }

        // Look up session by token
        Session session = sessionRepository.findByToken(token).orElse(null);
        if (session == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "Invalid or expired session"));
        }

        session.setEndedAt(java.time.LocalDateTime.now());
        sessionRepository.save(session);

        return ResponseEntity.ok(Map.of("message", "Logout successful"));
    }
}