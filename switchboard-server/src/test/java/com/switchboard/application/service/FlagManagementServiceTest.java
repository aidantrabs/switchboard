package com.switchboard.application.service;

import com.switchboard.application.port.output.AuditLogPersistencePort;
import com.switchboard.application.port.output.FlagChangeEventPort;
import com.switchboard.application.port.output.FlagPersistencePort;
import com.switchboard.application.port.output.ProjectPersistencePort;
import com.switchboard.domain.exception.DuplicateFlagKeyException;
import com.switchboard.domain.exception.FlagNotFoundException;
import com.switchboard.domain.exception.ProjectNotFoundException;
import com.switchboard.domain.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class FlagManagementServiceTest {

    private FlagPersistencePort flagPersistence;
    private ProjectPersistencePort projectPersistence;
    private AuditLogPersistencePort auditLogPersistence;
    private FlagChangeEventPort flagChangeEvent;
    private FlagManagementService service;

    private final UUID orgId = UUID.randomUUID();
    private final Project project = new Project(UUID.randomUUID(), orgId, "Test", "test-project", Instant.now());
    private final Environment env = new Environment(UUID.randomUUID(), project.getId(), "Dev", "dev", 0);

    @BeforeEach
    void setUp() {
        flagPersistence = mock(FlagPersistencePort.class);
        projectPersistence = mock(ProjectPersistencePort.class);
        auditLogPersistence = mock(AuditLogPersistencePort.class);
        flagChangeEvent = mock(FlagChangeEventPort.class);
        service = new FlagManagementService(flagPersistence, projectPersistence,
            auditLogPersistence, flagChangeEvent);

        when(auditLogPersistence.save(any())).thenAnswer(i -> i.getArgument(0));
    }

    @Test
    void createFlagSucceeds() {
        when(projectPersistence.findByKey("test-project")).thenReturn(Optional.of(project));
        when(flagPersistence.existsByProjectIdAndKey(project.getId(), "new-flag")).thenReturn(false);
        when(flagPersistence.save(any())).thenAnswer(i -> i.getArgument(0));

        List<Variant> variants = List.of(new Variant("on", "true"), new Variant("off", "false"));
        FeatureFlag result = service.createFlag("test-project", "new-flag", "New Flag",
            "desc", FlagType.RELEASE, "off", variants);

        assertEquals("new-flag", result.getKey());
        verify(flagPersistence).save(any());
        verify(auditLogPersistence).save(any());
        verify(flagChangeEvent).publishFlagCreated("test-project", null, "new-flag", "system");
    }

    @Test
    void createFlagThrowsOnDuplicateKey() {
        when(projectPersistence.findByKey("test-project")).thenReturn(Optional.of(project));
        when(flagPersistence.existsByProjectIdAndKey(project.getId(), "existing-flag")).thenReturn(true);

        assertThrows(DuplicateFlagKeyException.class, () ->
            service.createFlag("test-project", "existing-flag", "Flag", "desc",
                FlagType.RELEASE, "off", List.of()));
    }

    @Test
    void createFlagThrowsOnMissingProject() {
        when(projectPersistence.findByKey("missing")).thenReturn(Optional.empty());

        assertThrows(ProjectNotFoundException.class, () ->
            service.createFlag("missing", "flag", "Flag", "desc",
                FlagType.RELEASE, "off", List.of()));
    }

    @Test
    void deleteFlagSucceeds() {
        FeatureFlag flag = FeatureFlag.create(project.getId(), "flag-1", "Flag",
            "desc", FlagType.RELEASE, "off", List.of());
        when(projectPersistence.findByKey("test-project")).thenReturn(Optional.of(project));
        when(flagPersistence.findByProjectIdAndKey(project.getId(), "flag-1")).thenReturn(Optional.of(flag));

        service.deleteFlag("test-project", "flag-1");

        verify(flagPersistence).delete(flag.getId());
        verify(flagChangeEvent).publishFlagDeleted("test-project", null, "flag-1", "system");
    }

    @Test
    void deleteFlagThrowsOnMissingFlag() {
        when(projectPersistence.findByKey("test-project")).thenReturn(Optional.of(project));
        when(flagPersistence.findByProjectIdAndKey(project.getId(), "missing")).thenReturn(Optional.empty());

        assertThrows(FlagNotFoundException.class, () ->
            service.deleteFlag("test-project", "missing"));
    }

    @Test
    void toggleFlagSucceeds() {
        FeatureFlag flag = FeatureFlag.create(project.getId(), "flag-1", "Flag",
            "desc", FlagType.RELEASE, "off", List.of());
        FlagEnvironmentConfig config = FlagEnvironmentConfig.create(flag.getId(), env.getId());

        when(projectPersistence.findByKey("test-project")).thenReturn(Optional.of(project));
        when(flagPersistence.findByProjectIdAndKey(project.getId(), "flag-1")).thenReturn(Optional.of(flag));
        when(projectPersistence.findEnvironmentByProjectIdAndKey(project.getId(), "dev"))
            .thenReturn(Optional.of(env));
        when(flagPersistence.findConfig(flag.getId(), env.getId())).thenReturn(Optional.of(config));
        when(flagPersistence.saveConfig(any())).thenAnswer(i -> i.getArgument(0));

        service.toggleFlag("test-project", "flag-1", "dev");

        assertTrue(config.isEnabled());
        verify(flagPersistence).saveConfig(config);
        verify(flagChangeEvent).publishFlagToggled("test-project", "dev", "flag-1", true, "system");
    }

    @Test
    void updateRolloutSucceeds() {
        FeatureFlag flag = FeatureFlag.create(project.getId(), "flag-1", "Flag",
            "desc", FlagType.RELEASE, "off", List.of());
        FlagEnvironmentConfig config = FlagEnvironmentConfig.create(flag.getId(), env.getId());

        when(projectPersistence.findByKey("test-project")).thenReturn(Optional.of(project));
        when(flagPersistence.findByProjectIdAndKey(project.getId(), "flag-1")).thenReturn(Optional.of(flag));
        when(projectPersistence.findEnvironmentByProjectIdAndKey(project.getId(), "dev"))
            .thenReturn(Optional.of(env));
        when(flagPersistence.findConfig(flag.getId(), env.getId())).thenReturn(Optional.of(config));
        when(flagPersistence.saveConfig(any())).thenAnswer(i -> i.getArgument(0));

        service.updateRollout("test-project", "flag-1", "dev", 50);

        assertEquals(50, config.getRolloutPercentage());
        verify(flagChangeEvent).publishRolloutUpdated("test-project", "dev", "flag-1", 50, "system");
    }
}
