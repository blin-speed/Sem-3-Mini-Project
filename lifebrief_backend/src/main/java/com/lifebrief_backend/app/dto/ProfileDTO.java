// ProfileDTO.java
package com.lifebrief_backend.app.dto;

import java.util.Set;

public record ProfileDTO(
    String profileId,
    String username,
    String city,
    String state,
    String country,
    Integer age,
    Double weight,
    String foodPreference,
    String healthProblems,
    String foodAllergies,
    Set<String> daysNonVeg
) {}