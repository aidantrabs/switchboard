package com.switchboard.application.service;

import com.switchboard.application.port.output.FlagPersistencePort;
import com.switchboard.application.port.output.ProjectPersistencePort;
import com.switchboard.domain.exception.FlagNotFoundException;
import com.switchboard.domain.exception.ProjectNotFoundException;
import com.switchboard.domain.model.*;
import com.switchboard.domain.model.EvaluationResult.Reason;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class FlagEvaluationServiceTest {

    private FlagPersistencePort flagPersistence;
    private ProjectPersistencePort projectPersistence;
    private FlagEvaluationService service;

    private final UUID orgId = UUID.randomUUID();
    private final Project project = new Project(UUID.randomUUID(), orgId, "Test", "test-project", Instant.now());
    private final Environment env = new Environment(UUID.randomUUID(), project.getId(), "Dev", "dev", 0);

    @BeforeEach
    void setUp() {
        flagPersistence = mock(FlagPersistencePort.class);
        projectPersistence = mock(ProjectPersistencePort.class);
        service = new FlagEvaluationService(flagPersistence, projectPersistence);
    }

    @Test
    void evaluateDisabledFlagReturnsDefault() {
        List<Variant> variants = List.of(new Variant("on", "true"), new Variant("off", "false"));
        FeatureFlag flag = FeatureFlag.create(project.getId(), "flag-1", "Flag",
            "desc", FlagType.RELEASE, "off", variants);
        FlagEnvironmentConfig config = FlagEnvironmentConfig.create(flag.getId(), env.getId());

        when(projectPersistence.findByKey("test-project")).thenReturn(Optional.of(project));
        when(projectPersistence.findEnvironmentByProjectIdAndKey(project.getId(), "dev"))
            .thenReturn(Optional.of(env));
        when(flagPersistence.findByProjectIdAndKey(project.getId(), "flag-1"))
            .thenReturn(Optional.of(flag));
        when(flagPersistence.findConfig(flag.getId(), env.getId()))
            .thenReturn(Optional.of(config));

        EvaluationResult result = service.evaluate("test-project", "dev", "flag-1",
            new EvaluationContext("user-1", Map.of()));

        assertEquals("off", result.variantKey());
        assertEquals(Reason.FLAG_DISABLED, result.reason());
    }

    @Test
    void evaluateEnabledFlagWithTargeting() {
        List<Variant> variants = List.of(new Variant("on", "true"), new Variant("off", "false"));
        FeatureFlag flag = FeatureFlag.create(project.getId(), "flag-1", "Flag",
            "desc", FlagType.RELEASE, "off", variants);
        FlagEnvironmentConfig config = FlagEnvironmentConfig.create(flag.getId(), env.getId());
        config.toggle();
        config.updateTargetingRules(List.of(
            TargetingRule.create(1,
                List.of(new Condition("user.country", Operator.EQUALS, "us")),
                "on")
        ));

        when(projectPersistence.findByKey("test-project")).thenReturn(Optional.of(project));
        when(projectPersistence.findEnvironmentByProjectIdAndKey(project.getId(), "dev"))
            .thenReturn(Optional.of(env));
        when(flagPersistence.findByProjectIdAndKey(project.getId(), "flag-1"))
            .thenReturn(Optional.of(flag));
        when(flagPersistence.findConfig(flag.getId(), env.getId()))
            .thenReturn(Optional.of(config));

        EvaluationResult result = service.evaluate("test-project", "dev", "flag-1",
            new EvaluationContext("user-1", Map.of("user.country", "us")));

        assertEquals("on", result.variantKey());
        assertEquals(Reason.TARGETING_MATCH, result.reason());
    }

    @Test
    void evaluateThrowsOnMissingProject() {
        when(projectPersistence.findByKey("missing")).thenReturn(Optional.empty());

        assertThrows(ProjectNotFoundException.class, () ->
            service.evaluate("missing", "dev", "flag-1",
                new EvaluationContext("user-1", Map.of())));
    }

    @Test
    void evaluateThrowsOnMissingFlag() {
        when(projectPersistence.findByKey("test-project")).thenReturn(Optional.of(project));
        when(projectPersistence.findEnvironmentByProjectIdAndKey(project.getId(), "dev"))
            .thenReturn(Optional.of(env));
        when(flagPersistence.findByProjectIdAndKey(project.getId(), "missing"))
            .thenReturn(Optional.empty());

        assertThrows(FlagNotFoundException.class, () ->
            service.evaluate("test-project", "dev", "missing",
                new EvaluationContext("user-1", Map.of())));
    }
}
