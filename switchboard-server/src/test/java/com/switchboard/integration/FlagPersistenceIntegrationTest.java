package com.switchboard.integration;

import com.switchboard.adapter.output.persistence.entity.EnvironmentJpaEntity;
import com.switchboard.adapter.output.persistence.entity.OrganizationJpaEntity;
import com.switchboard.adapter.output.persistence.entity.ProjectJpaEntity;
import com.switchboard.adapter.output.persistence.repository.EnvironmentJpaRepository;
import com.switchboard.adapter.output.persistence.repository.ProjectJpaRepository;
import com.switchboard.application.port.output.FlagPersistencePort;
import com.switchboard.domain.model.FeatureFlag;
import com.switchboard.domain.model.FlagType;
import com.switchboard.domain.model.Variant;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
class FlagPersistenceIntegrationTest extends IntegrationTestBase {

    @Autowired
    FlagPersistencePort flagPersistence;

    @Autowired
    ProjectJpaRepository projectRepository;

    @Autowired
    EnvironmentJpaRepository environmentRepository;

    @Autowired
    EntityManager entityManager;

    private UUID projectId;
    private UUID envId;

    @BeforeEach
    void setUp() {
        UUID orgId = UUID.randomUUID();
        entityManager.persist(new OrganizationJpaEntity(orgId, "Test Org", "test-org", Instant.now()));

        projectId = UUID.randomUUID();
        projectRepository.save(new ProjectJpaEntity(projectId, orgId, "Test Project", "test-project", Instant.now()));

        envId = UUID.randomUUID();
        environmentRepository.save(new EnvironmentJpaEntity(envId, projectId, "Dev", "dev", 0));

        entityManager.flush();
    }

    @Test
    void saveAndFindFlag() {
        List<Variant> variants = List.of(new Variant("on", "true"), new Variant("off", "false"));
        FeatureFlag flag = FeatureFlag.create(projectId, "test-flag", "Test Flag",
            "description", FlagType.RELEASE, "off", variants);

        FeatureFlag saved = flagPersistence.save(flag);

        assertNotNull(saved.getId());
        assertEquals("test-flag", saved.getKey());

        var found = flagPersistence.findByProjectIdAndKey(projectId, "test-flag");
        assertTrue(found.isPresent());
        assertEquals("Test Flag", found.get().getName());
        assertEquals(2, found.get().getVariants().size());
    }

    @Test
    void existsByProjectIdAndKey() {
        FeatureFlag flag = FeatureFlag.create(projectId, "exists-flag", "Exists",
            "desc", FlagType.RELEASE, "off", List.of());
        flagPersistence.save(flag);

        assertTrue(flagPersistence.existsByProjectIdAndKey(projectId, "exists-flag"));
        assertFalse(flagPersistence.existsByProjectIdAndKey(projectId, "nope"));
    }

    @Test
    void findAllByProjectId() {
        flagPersistence.save(FeatureFlag.create(projectId, "flag-1", "Flag 1",
            "desc", FlagType.RELEASE, "off", List.of()));
        flagPersistence.save(FeatureFlag.create(projectId, "flag-2", "Flag 2",
            "desc", FlagType.EXPERIMENT, "control", List.of()));

        List<FeatureFlag> flags = flagPersistence.findAllByProjectId(projectId);
        assertEquals(2, flags.size());
    }

    @Test
    void deleteFlag() {
        FeatureFlag flag = FeatureFlag.create(projectId, "delete-me", "Delete Me",
            "desc", FlagType.RELEASE, "off", List.of());
        FeatureFlag saved = flagPersistence.save(flag);

        flagPersistence.delete(saved.getId());

        assertTrue(flagPersistence.findByProjectIdAndKey(projectId, "delete-me").isEmpty());
    }
}
