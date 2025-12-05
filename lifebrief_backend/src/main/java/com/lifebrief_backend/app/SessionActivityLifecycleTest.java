package com.lifebrief_backend.app;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class SessionActivityLifecycleTest {

    private static final String AUTH_URL = "http://localhost:8080/api/auth";
    private static final String ACTIVITY_URL = "http://localhost:8080/api/activity";
    private static final String CONTEXT_URL = "http://localhost:8080/api/context";

    private static String token = null;
    private static Long sessionId = null;

    public static void main(String[] args) throws Exception {
        // Step 1: Login
        login("Ea Nasir", "123456");

        // Step 2: Log a single test activity with a unique marker
        String detail = "🔥 Test activity at " + System.currentTimeMillis();
        logActivity(detail);

        // Step 3: Fetch activities for this session
        if (sessionId != null) {
            getActivitiesBySession(sessionId);
        }

        // Step 4: Fetch AIContextDTO (Profile + Activities + Prompt)
        if (sessionId != null) {
            getAIContext(sessionId, "What’s a good breakfast for energy?");
        }

        // Step 5: Logout
        logout();
    }

    private static void login(String username, String password) throws Exception {
        URL url = URI.create(AUTH_URL + "/login").toURL();
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setDoOutput(true);
        conn.setRequestProperty("Content-Type", "application/json");

        String json = String.format("{\"username\":\"%s\",\"password\":\"%s\"}", username, password);
        try (OutputStream os = conn.getOutputStream()) {
            os.write(json.getBytes(StandardCharsets.UTF_8));
        }

        int status = conn.getResponseCode();
        String response = readResponse(conn);

        if (status == 200) {
            System.out.println("✅ Login successful. Response: " + response);

            ObjectMapper mapper = new ObjectMapper();
            Map<String, Object> map = mapper.readValue(response, new TypeReference<>() {});
            token = (String) map.get("token");
            sessionId = ((Number) map.get("sessionId")).longValue();

            System.out.println("Captured token: " + token);
            System.out.println("Captured sessionId: " + sessionId);
        } else {
            System.out.println("❌ Login failed. Status: " + status);
            System.out.println("Response: " + response);
        }
        conn.disconnect();
    }

    private static void logActivity(String detail) throws Exception {
        if (token == null) {
            System.out.println("⚠️ No token available. Cannot log activity.");
            return;
        }

        URL url = URI.create(ACTIVITY_URL + "/log").toURL();
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setDoOutput(true);
        conn.setRequestProperty("Content-Type", "application/json");

        String json = String.format("{\"token\":\"%s\",\"detail\":\"%s\"}", token, detail);

        try (OutputStream os = conn.getOutputStream()) {
            os.write(json.getBytes(StandardCharsets.UTF_8));
        }

        int status = conn.getResponseCode();
        System.out.println("POST /activity/log => " + status);
        System.out.println("Response: " + readResponse(conn));

        conn.disconnect();
    }

    private static void getActivitiesBySession(Long sessionId) throws Exception {
        URL url = URI.create(ACTIVITY_URL + "/session/" + sessionId).toURL();
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        int status = conn.getResponseCode();
        System.out.println("GET /activity/session/" + sessionId + " => " + status);
        System.out.println("Response: " + readResponse(conn));

        conn.disconnect();
    }

    private static void getAIContext(Long sessionId, String userPrompt) throws Exception {
        String fullUrl = CONTEXT_URL + "/" + sessionId + "?userPrompt=" + 
                         java.net.URLEncoder.encode(userPrompt, StandardCharsets.UTF_8);
        URL url = URI.create(fullUrl).toURL();
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        int status = conn.getResponseCode();
        String response = readResponse(conn);

        System.out.println("GET /context/" + sessionId + " => " + status);

        // Pretty print JSON
        ObjectMapper mapper = new ObjectMapper();
        Object json = mapper.readValue(response, Object.class);
        String pretty = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(json);

        System.out.println("AIContextDTO Response:\n" + pretty);

        conn.disconnect();
    }

    private static void logout() throws Exception {
        if (token == null) {
            System.out.println("⚠️ No token found. Cannot logout.");
            return;
        }

        URL url = URI.create(AUTH_URL + "/logout").toURL();
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setDoOutput(true);
        conn.setRequestProperty("Content-Type", "application/json");

        String json = String.format("{\"token\":\"%s\"}", token);
        try (OutputStream os = conn.getOutputStream()) {
            os.write(json.getBytes(StandardCharsets.UTF_8));
        }

        int status = conn.getResponseCode();
        System.out.println("Logout status: " + status);
        System.out.println("Response: " + readResponse(conn));
        conn.disconnect();
    }

    private static String readResponse(HttpURLConnection conn) throws IOException {
        InputStream is = (conn.getResponseCode() < 400) ? conn.getInputStream() : conn.getErrorStream();
        BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line);
        }
        return sb.toString();
    }
}