package com.lifebrief_backend.app.controller;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lifebrief_backend.app.entity.Profile;
import com.lifebrief_backend.app.entity.Session;
import com.lifebrief_backend.app.repository.ProfileRepository;
import com.lifebrief_backend.app.repository.SessionRepository;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/api/auth")
public class LoginController {

    private final ProfileRepository profileRepository;
    private final SessionRepository sessionRepository;

    public LoginController(ProfileRepository profileRepository,
            SessionRepository sessionRepository) {
        this.profileRepository = profileRepository;
        this.sessionRepository = sessionRepository;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> payload) {
        String username = payload.get("username");
        String password = payload.get("password");

        Profile profile = profileRepository.findByUsernameIgnoreCase(username);

        if (profile == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "User not found"));
        }

        // ⚠️ Plain text comparison — replace with hashing later
        if (!profile.getPassword().equals(password)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Invalid password"));
        }

        // Generate token + expiry
        String token = UUID.randomUUID().toString();
        LocalDateTime expiry = LocalDateTime.now().plusHours(1);

        // Persist session in DB
        Session dbSession = new Session(
                profile.getProfileId(),
                profile.getUsername(),
                token,
                expiry);
        sessionRepository.save(dbSession);

        return ResponseEntity.ok(Map.of(
                "message", "Login successful",
                "sessionId", dbSession.getSessionId(),
                "username", profile.getUsername(),
                "email", profile.getEmail(),
                "token", token,
                "tokenExpiry", expiry.toString()));
    }
}