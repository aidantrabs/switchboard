package com.switchboard.openfeature;

import com.switchboard.sdk.SwitchboardClient;
import com.switchboard.sdk.model.FlagConfig;
import com.switchboard.sdk.model.Variant;
import dev.openfeature.sdk.ImmutableContext;
import dev.openfeature.sdk.ProviderEvaluation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class SwitchboardProviderTest {

    private SwitchboardProvider provider;

    @BeforeEach
    void setUp() {
        SwitchboardClient client = SwitchboardClient.builder().build();

        client.getCache().putAll(List.of(
            new FlagConfig("bool-flag", true, "off",
                List.of(new Variant("on", "true"), new Variant("off", "false")),
                100, List.of()),
            new FlagConfig("string-flag", true, "off",
                List.of(new Variant("on", "enabled"), new Variant("off", "disabled")),
                100, List.of()),
            new FlagConfig("disabled-flag", false, "off",
                List.of(new Variant("on", "true"), new Variant("off", "false")),
                0, List.of())
        ));

        provider = new SwitchboardProvider(client);
    }

    @Test
    void booleanEvaluationReturnsTrue() {
        var ctx = new ImmutableContext("user-1", Map.of());
        ProviderEvaluation<Boolean> result = provider.getBooleanEvaluation("bool-flag", false, ctx);

        assertTrue(result.getValue());
    }

    @Test
    void booleanEvaluationReturnsFalseForDisabled() {
        var ctx = new ImmutableContext("user-1", Map.of());
        ProviderEvaluation<Boolean> result = provider.getBooleanEvaluation("disabled-flag", false, ctx);

        assertFalse(result.getValue());
    }

    @Test
    void booleanEvaluationReturnsFalseForMissing() {
        var ctx = new ImmutableContext("user-1", Map.of());
        ProviderEvaluation<Boolean> result = provider.getBooleanEvaluation("nonexistent", false, ctx);

        assertFalse(result.getValue());
    }

    @Test
    void stringEvaluationReturnsVariant() {
        var ctx = new ImmutableContext("user-1", Map.of());
        ProviderEvaluation<String> result = provider.getStringEvaluation("string-flag", "default", ctx);

        assertEquals("on", result.getValue());
    }

    @Test
    void stringEvaluationReturnsDefaultForMissing() {
        var ctx = new ImmutableContext("user-1", Map.of());
        ProviderEvaluation<String> result = provider.getStringEvaluation("nonexistent", "fallback", ctx);

        assertEquals("fallback", result.getValue());
    }

    @Test
    void metadataReturnsName() {
        assertEquals("switchboard", provider.getMetadata().getName());
    }
}
