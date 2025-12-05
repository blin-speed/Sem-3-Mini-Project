package com.lifebrief_backend.app.dto;

import java.time.LocalDateTime;

public record UserActivityDTO(Long sessionId, LocalDateTime timestamp, String activityDetail) {}