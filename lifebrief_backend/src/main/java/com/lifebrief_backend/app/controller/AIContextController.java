// AIContextController.java
package com.lifebrief_backend.app.controller;

import org.springframework.web.bind.annotation.*;

import com.lifebrief_backend.app.dto.AIContextDTO;
import com.lifebrief_backend.app.service.AIContextInjector;

@RestController
@RequestMapping("/api/context")
public class AIContextController {

    private final AIContextInjector injector;

    public AIContextController(AIContextInjector injector) {
        this.injector = injector;
    }

    @GetMapping("/{sessionId}")
    public AIContextDTO getContext(@PathVariable Long sessionId,
                                   @RequestParam String userPrompt) {
        return injector.buildContext(sessionId, userPrompt);
    }
}