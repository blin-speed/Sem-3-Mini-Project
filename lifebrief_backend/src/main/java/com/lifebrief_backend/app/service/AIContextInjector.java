// AIContextInjector.java
package com.lifebrief_backend.app.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.lifebrief_backend.app.dto.AIContextDTO;
import com.lifebrief_backend.app.dto.ProfileDTO;
import com.lifebrief_backend.app.dto.UserActivityDTO;
import com.lifebrief_backend.app.entity.Profile;
import com.lifebrief_backend.app.entity.Session;
import com.lifebrief_backend.app.mapper.ContextMapper;
import com.lifebrief_backend.app.repository.ProfileRepository;
import com.lifebrief_backend.app.repository.SessionRepository;
import com.lifebrief_backend.app.repository.UserActivityRepository;

@Service
public class AIContextInjector {

    private final SessionRepository sessionRepo;
    private final ProfileRepository profileRepo;
    private final UserActivityRepository activityRepo;
    private final ContextMapper mapper;

    public AIContextInjector(SessionRepository sessionRepo,
                             ProfileRepository profileRepo,
                             UserActivityRepository activityRepo,
                             ContextMapper mapper) {
        this.sessionRepo = sessionRepo;
        this.profileRepo = profileRepo;
        this.activityRepo = activityRepo;
        this.mapper = mapper;
    }

    public AIContextDTO buildContext(Long sessionId, String userPrompt) {
        Session session = sessionRepo.findById(sessionId)
            .orElseThrow(() -> new RuntimeException("Session not found"));

        Profile profile = profileRepo.findById(session.getProfileId())
            .orElseThrow(() -> new RuntimeException("Profile not found"));

        List<UserActivityDTO> activities = activityRepo
            .findBySessionIdOrderByTimestampDesc(sessionId)
            .stream()
            .limit(5) // last 5 activities
            .map(mapper::toActivityDTO)
            .toList();

        ProfileDTO profileDTO = mapper.toProfileDTO(profile);

        return new AIContextDTO(userPrompt, profileDTO, activities);
    }
}