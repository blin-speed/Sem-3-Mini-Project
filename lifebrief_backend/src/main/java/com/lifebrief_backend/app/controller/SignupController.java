package com.lifebrief_backend.app.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lifebrief_backend.app.entity.Profile;
import com.lifebrief_backend.app.service.DuplicateUserException;
import com.lifebrief_backend.app.service.ProfileService;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/api/auth")
public class SignupController {

    private final ProfileService profileService;

    public SignupController(ProfileService profileService) {
        this.profileService = profileService;
    }

    @PostMapping("/signup")   // 👈 Explicitly map to /api/auth/signup
    public ResponseEntity<?> signup(@RequestBody Profile profile) {
        // Basic required field checks
        if (profile.getUsername() == null || profile.getUsername().isBlank()) {
            return ResponseEntity.badRequest().body("Username is required");
        }
        if (profile.getEmail() == null || profile.getEmail().isBlank()) {
            return ResponseEntity.badRequest().body("Email is required");
        }
        if (profile.getPassword() == null || profile.getPassword().isBlank()) {
            return ResponseEntity.badRequest().body("Password is required");
        }

        try {
            Profile created = profileService.create(profile);
            // Return safe response (no password)
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(Map.of(
                            "id", created.getProfileId(),
                            "username", created.getUsername(),
                            "email", created.getEmail()
                    ));
        } catch (DuplicateUserException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }
}