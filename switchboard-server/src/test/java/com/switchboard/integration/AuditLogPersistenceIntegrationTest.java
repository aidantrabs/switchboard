package com.switchboard.integration;

import com.switchboard.adapter.output.persistence.entity.OrganizationJpaEntity;
import com.switchboard.adapter.output.persistence.entity.ProjectJpaEntity;
import com.switchboard.adapter.output.persistence.repository.ProjectJpaRepository;
import com.switchboard.application.port.output.AuditLogPersistencePort;
import com.switchboard.domain.model.AuditLog;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
class AuditLogPersistenceIntegrationTest extends IntegrationTestBase {

    @Autowired
    AuditLogPersistencePort auditLogPersistence;

    @Autowired
    ProjectJpaRepository projectRepository;

    @Autowired
    EntityManager entityManager;

    private UUID projectId;

    @BeforeEach
    void setUp() {
        UUID orgId = UUID.randomUUID();
        entityManager.persist(new OrganizationJpaEntity(orgId, "Test Org", "test-org-" + orgId, Instant.now()));

        projectId = UUID.randomUUID();
        projectRepository.save(new ProjectJpaEntity(projectId, orgId, "Test Project", "test-project-" + projectId, Instant.now()));
        entityManager.flush();
    }

    @Test
    void saveAndQueryByProject() {
        auditLogPersistence.save(AuditLog.create(projectId, "flag-1", "dev",
            "FLAG_CREATED", "user-1", null, "{\"key\":\"flag-1\"}"));
        auditLogPersistence.save(AuditLog.create(projectId, "flag-2", "dev",
            "FLAG_CREATED", "user-1", null, "{\"key\":\"flag-2\"}"));

        Instant from = Instant.now().minus(1, ChronoUnit.HOURS);
        Instant to = Instant.now().plus(1, ChronoUnit.HOURS);

        List<AuditLog> logs = auditLogPersistence.findByProjectId(projectId, from, to);
        assertEquals(2, logs.size());
    }

    @Test
    void queryByFlagKey() {
        auditLogPersistence.save(AuditLog.create(projectId, "target-flag", "dev",
            "FLAG_CREATED", "user-1", null, null));
        auditLogPersistence.save(AuditLog.create(projectId, "target-flag", "dev",
            "FLAG_TOGGLED", "user-1", null, null));
        auditLogPersistence.save(AuditLog.create(projectId, "other-flag", "dev",
            "FLAG_CREATED", "user-1", null, null));

        List<AuditLog> logs = auditLogPersistence.findByProjectIdAndFlagKey(
            projectId, "target-flag", 10);
        assertEquals(2, logs.size());
        assertTrue(logs.stream().allMatch(l -> "target-flag".equals(l.getFlagKey())));
    }
}
