// ContextMapper.java
package com.lifebrief_backend.app.mapper;

import org.springframework.stereotype.Component;

import com.lifebrief_backend.app.dto.ProfileDTO;
import com.lifebrief_backend.app.dto.UserActivityDTO;
import com.lifebrief_backend.app.entity.Profile;
import com.lifebrief_backend.app.entity.UserActivity;

@Component
public class ContextMapper {

    public ProfileDTO toProfileDTO(Profile p) {
        return new ProfileDTO(
                p.getProfileId(),
                p.getUsername(),
                p.getCity(),
                p.getState(),
                p.getCountry(),
                p.getAge(),
                p.getWeight(),
                p.getFoodPreference() != null ? p.getFoodPreference().name() : null,
                p.getHealthProblems(),
                p.getFoodAllergies(),
                p.getDaysNonVeg());
    }

    public UserActivityDTO toActivityDTO(UserActivity ua) {
        return new UserActivityDTO(
                ua.getSessionId(),
                ua.getTimestamp(),
                ua.getActivityDetail());
    }
}