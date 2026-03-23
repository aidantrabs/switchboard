package com.switchboard.adapter.output.cache;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Optional;

@Component
public class RedisFlagCacheAdapter {

    private static final String KEY_PREFIX = "switchboard:flags:";
    private static final Duration TTL = Duration.ofMinutes(5);

    private final StringRedisTemplate redisTemplate;
    private final ObjectMapper objectMapper;

    public RedisFlagCacheAdapter(StringRedisTemplate redisTemplate, ObjectMapper objectMapper) {
        this.redisTemplate = redisTemplate;
        this.objectMapper = objectMapper;
    }

    public void put(String projectKey, String environmentKey, Object flagConfigs) {
        String key = buildKey(projectKey, environmentKey);
        try {
            String json = objectMapper.writeValueAsString(flagConfigs);
            redisTemplate.opsForValue().set(key, json, TTL);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("failed to serialize flag configs for cache", e);
        }
    }

    public Optional<String> get(String projectKey, String environmentKey) {
        String key = buildKey(projectKey, environmentKey);
        return Optional.ofNullable(redisTemplate.opsForValue().get(key));
    }

    public void evict(String projectKey, String environmentKey) {
        String key = buildKey(projectKey, environmentKey);
        redisTemplate.delete(key);
    }

    public void evictProject(String projectKey) {
        var keys = redisTemplate.keys(KEY_PREFIX + projectKey + ":*");
        if (keys != null && !keys.isEmpty()) {
            redisTemplate.delete(keys);
        }
    }

    private String buildKey(String projectKey, String environmentKey) {
        return KEY_PREFIX + projectKey + ":" + environmentKey;
    }
}
