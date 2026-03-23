package com.switchboard.sdk;

import com.switchboard.sdk.model.FlagConfig;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class LocalFileFlagLoaderTest {

    @Test
    void parsesSimpleFlagsJson() throws Exception {
        String json = """
            {
              "flags": {
                "feature-a": { "enabled": true, "variant": "on" },
                "feature-b": { "enabled": false, "variant": "off" }
              }
            }
            """;

        var is = new ByteArrayInputStream(json.getBytes(StandardCharsets.UTF_8));
        List<FlagConfig> flags = LocalFileFlagLoader.parse(is);

        assertEquals(2, flags.size());

        FlagConfig featureA = flags.stream()
            .filter(f -> f.getKey().equals("feature-a")).findFirst().orElseThrow();
        assertTrue(featureA.isEnabled());
        assertEquals(100, featureA.getRolloutPercentage());

        FlagConfig featureB = flags.stream()
            .filter(f -> f.getKey().equals("feature-b")).findFirst().orElseThrow();
        assertFalse(featureB.isEnabled());
        assertEquals(0, featureB.getRolloutPercentage());
    }

    @Test
    void emptyFlagsReturnsEmptyList() throws Exception {
        String json = "{ \"flags\": {} }";
        var is = new ByteArrayInputStream(json.getBytes(StandardCharsets.UTF_8));

        List<FlagConfig> flags = LocalFileFlagLoader.parse(is);

        assertTrue(flags.isEmpty());
    }

    @Test
    void missingFlagsKeyReturnsEmptyList() throws Exception {
        String json = "{}";
        var is = new ByteArrayInputStream(json.getBytes(StandardCharsets.UTF_8));

        List<FlagConfig> flags = LocalFileFlagLoader.parse(is);

        assertTrue(flags.isEmpty());
    }

    @Test
    void loadFromClasspathResource() throws Exception {
        List<FlagConfig> flags = LocalFileFlagLoader.loadFromFile("classpath:test-flags.json");

        assertEquals(2, flags.size());
    }

    @Test
    void localModeClientEvaluatesFlags() {
        SwitchboardClient client = SwitchboardClient.builder()
            .localFlagsFile("classpath:test-flags.json")
            .build();

        var ctx = EvaluationContext.builder().userId("user-1").build();

        assertTrue(client.isEnabled("feature-a", ctx));
        assertFalse(client.isEnabled("feature-b", ctx));
        assertFalse(client.isEnabled("nonexistent", ctx));

        client.close();
    }
}
