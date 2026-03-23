package com.switchboard.sdk;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.switchboard.sdk.model.FlagConfig;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.List;

public class HttpFlagFetcher {

    private final String apiUrl;
    private final String apiKey;
    private final String projectKey;
    private final String environmentKey;
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    public HttpFlagFetcher(String apiUrl, String apiKey, String projectKey, String environmentKey) {
        this.apiUrl = apiUrl;
        this.apiKey = apiKey;
        this.projectKey = projectKey;
        this.environmentKey = environmentKey;
        this.httpClient = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(5))
            .build();
        this.objectMapper = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public List<FlagConfig> fetchAll() throws IOException, InterruptedException {
        String url = apiUrl + "/api/v1/client/" + projectKey + "/" + environmentKey + "/flags";

        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(url))
            .header("Authorization", "Bearer " + apiKey)
            .header("Accept", "application/json")
            .timeout(Duration.ofSeconds(10))
            .GET()
            .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new IOException("failed to fetch flags: HTTP " + response.statusCode());
        }

        return objectMapper.readValue(response.body(), new TypeReference<>() {});
    }
}
