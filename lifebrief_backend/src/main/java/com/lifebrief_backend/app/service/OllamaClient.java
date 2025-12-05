package com.lifebrief_backend.app.service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class OllamaClient {

    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final ObjectMapper mapper = new ObjectMapper();

    /**
     * Sends a prompt to the Ollama API and returns only the model's reply,
     * filtering out any warning/disclaimer chunks.
     */
    public String generate(String model, String prompt) {
        try {
            Map<String, String> payload = Map.of(
                "model", model,
                "prompt", prompt
            );

            String requestBody = mapper.writeValueAsString(payload);

            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:11434/api/generate"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            StringBuilder finalAnswer = new StringBuilder();
            String[] lines = response.body().split("\n");

            for (String line : lines) {
                if (line.isBlank()) continue;
                JsonNode node = mapper.readTree(line);
                if (node.has("response")) {
                    String chunk = node.get("response").asText();
                    // Skip warning/disclaimer chunks
                    if (chunk.startsWith("⚠️") 
                        || chunk.toLowerCase().contains("inappropriate prompt") 
                        || chunk.toLowerCase().contains("only answer")) {
                        continue;
                    }
                    finalAnswer.append(chunk);
                }
            }

            return finalAnswer.toString().trim();

        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Error calling Ollama API", e);
        }
    }
}