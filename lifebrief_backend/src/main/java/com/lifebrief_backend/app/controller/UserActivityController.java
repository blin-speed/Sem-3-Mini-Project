package com.lifebrief_backend.app.controller;

import java.time.LocalDateTime;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lifebrief_backend.app.dto.UserActivityDTO;
import com.lifebrief_backend.app.entity.UserActivity;
import com.lifebrief_backend.app.repository.SessionRepository;
import com.lifebrief_backend.app.repository.UserActivityRepository;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/api/activity")
public class UserActivityController {

    private final UserActivityRepository userActivityRepository;
    private final SessionRepository sessionRepository;

    public UserActivityController(UserActivityRepository userActivityRepository,
                                  SessionRepository sessionRepository) {
        this.userActivityRepository = userActivityRepository;
        this.sessionRepository = sessionRepository;
    }

    @PostMapping("/log")
    public ResponseEntity<?> logActivity(@RequestBody Map<String, String> payload) {
        String token = payload.get("token");
        String detail = payload.get("detail");

        return sessionRepository.findByToken(token)
                .filter(s -> s.getTokenExpiry().isAfter(LocalDateTime.now()))
                .map(session -> {
                    UserActivity activity = new UserActivity(session.getSessionId(), detail);
                    userActivityRepository.save(activity);
                    return ResponseEntity.ok(Map.of("message", "Activity logged"));
                })
                .orElseGet(() -> ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("error", "Invalid or expired session")));
    }

    @GetMapping("/session/{sessionId}")
    public ResponseEntity<?> getActivitiesBySession(@PathVariable Long sessionId) {
        var activities = userActivityRepository.findBySessionIdOrderByTimestampAsc(sessionId)
                .stream()
                .map(ua -> new UserActivityDTO(ua.getSessionId(), ua.getTimestamp(), ua.getActivityDetail()))
                .toList();
        return ResponseEntity.ok(activities);
    }

    @GetMapping("/profile/{profileId}")
    public ResponseEntity<?> getActivitiesByProfile(@PathVariable String profileId) {
        var activities = userActivityRepository.findByProfileId(profileId)
                .stream()
                .map(ua -> new UserActivityDTO(ua.getSessionId(), ua.getTimestamp(), ua.getActivityDetail()))
                .toList();
        return ResponseEntity.ok(activities);
    }
}