package com.switchboard.cli;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class CliHttpClient {

    private final String apiUrl;
    private final String apiKey;
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    public CliHttpClient(String apiUrl, String apiKey) {
        this.apiUrl = apiUrl;
        this.apiKey = apiKey;
        this.httpClient = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(5))
            .build();
        this.objectMapper = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public String get(String path) throws IOException, InterruptedException {
        HttpRequest request = buildRequest(path).GET().build();
        return send(request);
    }

    public String post(String path, Object body) throws IOException, InterruptedException {
        String json = objectMapper.writeValueAsString(body);
        HttpRequest request = buildRequest(path)
            .POST(HttpRequest.BodyPublishers.ofString(json))
            .header("Content-Type", "application/json")
            .build();
        return send(request);
    }

    public String put(String path, Object body) throws IOException, InterruptedException {
        String json = objectMapper.writeValueAsString(body);
        HttpRequest request = buildRequest(path)
            .PUT(HttpRequest.BodyPublishers.ofString(json))
            .header("Content-Type", "application/json")
            .build();
        return send(request);
    }

    public String patch(String path) throws IOException, InterruptedException {
        HttpRequest request = buildRequest(path)
            .method("PATCH", HttpRequest.BodyPublishers.noBody())
            .build();
        return send(request);
    }

    public String delete(String path) throws IOException, InterruptedException {
        HttpRequest request = buildRequest(path).DELETE().build();
        return send(request);
    }

    public ObjectMapper getObjectMapper() {
        return objectMapper;
    }

    private HttpRequest.Builder buildRequest(String path) {
        HttpRequest.Builder builder = HttpRequest.newBuilder()
            .uri(URI.create(apiUrl + path))
            .header("Accept", "application/json")
            .timeout(Duration.ofSeconds(10));

        if (apiKey != null) {
            builder.header("Authorization", "Bearer " + apiKey);
        }

        return builder;
    }

    private String send(HttpRequest request) throws IOException, InterruptedException {
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() >= 400) {
            throw new IOException("HTTP " + response.statusCode() + ": " + response.body());
        }

        return response.body();
    }
}
