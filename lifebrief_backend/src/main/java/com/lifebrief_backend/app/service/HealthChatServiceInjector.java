package com.lifebrief_backend.app.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.lifebrief_backend.app.dto.AIContextDTO;
import com.lifebrief_backend.app.dto.UserActivityDTO;

@Service
public class HealthChatServiceInjector {

    private final AIContextInjector injector;

    public HealthChatServiceInjector(AIContextInjector injector) {
        this.injector = injector;
    }

    /**
     * Build a natural-language system prompt for Ollama
     * that includes profile details, recent activities, and the user prompt.
     * No disclaimers — let the model handle refusals itself.
     */
    public String buildPrompt(Long sessionId, String userPrompt) {
        AIContextDTO ctx = injector.buildContext(sessionId, userPrompt);

        StringBuilder sb = new StringBuilder();

        // System instructions
        sb.append("System: You are a helpful assistant focused on health, wellness, ");
        sb.append("diet, exercise, symptoms, treatments, and medical guidance.");
        sb.append("Absolutely do not give warning if the prompt is unrelated to health, but instead reframe it to fit the list of your actions. I AGAIN REPEAT, DO NOT GIVE WARNING \n\n");

        // Profile context
        sb.append("User profile:\n");
        sb.append("- Username: ").append(ctx.profile().username()).append("\n");
        if (ctx.profile().age() != null) sb.append("- Age: ").append(ctx.profile().age()).append("\n");
        if (ctx.profile().city() != null) sb.append("- City: ").append(ctx.profile().city()).append("\n");
        if (ctx.profile().foodPreference() != null) sb.append("- Food preference: ").append(ctx.profile().foodPreference()).append("\n");
        if (ctx.profile().healthProblems() != null) sb.append("- Health problems: ").append(ctx.profile().healthProblems()).append("\n");
        if (ctx.profile().foodAllergies() != null) sb.append("- Allergies: ").append(ctx.profile().foodAllergies()).append("\n");

        // Activities context
        sb.append("\nRecent activities:\n");
        List<UserActivityDTO> acts = ctx.recentActivities();
        if (acts.isEmpty()) {
            sb.append("- No recent activities logged.\n");
        } else {
            for (UserActivityDTO act : acts) {
                sb.append("- ").append(act.timestamp()).append(": ")
                  .append(act.activityDetail()).append("\n");
            }
        }

        // User input
        sb.append("\nNow the user asks: ").append(ctx.userPrompt()).append("\n");
        sb.append("Assistant:");

        return sb.toString();
    }
}