// AIContextDTO.java
package com.lifebrief_backend.app.dto;

import java.util.List;

public record AIContextDTO(
    String userPrompt,              // the immediate user input
    ProfileDTO profile,             // safe profile details (no password)
    List<UserActivityDTO> recentActivities // last N activities for context
) {}