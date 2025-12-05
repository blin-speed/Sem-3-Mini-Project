package com.lifebrief_backend.app.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lifebrief_backend.app.service.HealthChatServiceInjector;
import com.lifebrief_backend.app.service.OllamaClient;

@RestController
@RequestMapping("/api/healthchat")
public class HealthChatController {

    private final HealthChatServiceInjector injector;
    private final OllamaClient ollama;

    public HealthChatController(HealthChatServiceInjector injector, OllamaClient ollama) {
        this.injector = injector;
        this.ollama = ollama;
    }

    @GetMapping("/{sessionId}/ask")
    public String ask(@PathVariable Long sessionId,
                      @RequestParam String userPrompt) {
        String prompt = injector.buildPrompt(sessionId, userPrompt);
        // Always use your custom health-only model
        return ollama.generate("healthmistral", prompt);
    }
}