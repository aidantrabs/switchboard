package com.switchboard.integration;

import com.switchboard.adapter.output.persistence.entity.EnvironmentJpaEntity;
import com.switchboard.adapter.output.persistence.entity.OrganizationJpaEntity;
import com.switchboard.adapter.output.persistence.entity.ProjectJpaEntity;
import com.switchboard.adapter.output.persistence.repository.EnvironmentJpaRepository;
import com.switchboard.adapter.output.persistence.repository.ProjectJpaRepository;
import com.switchboard.application.port.output.ProjectPersistencePort;
import com.switchboard.domain.model.Environment;
import com.switchboard.domain.model.Project;
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
class ProjectPersistenceIntegrationTest extends IntegrationTestBase {

    @Autowired
    ProjectPersistencePort projectPersistence;

    @Autowired
    ProjectJpaRepository projectRepository;

    @Autowired
    EnvironmentJpaRepository environmentRepository;

    @Autowired
    EntityManager entityManager;

    private UUID orgId;

    @BeforeEach
    void setUp() {
        orgId = UUID.randomUUID();
        entityManager.persist(new OrganizationJpaEntity(orgId, "Test Org", "test-org-" + orgId, Instant.now()));
        entityManager.flush();
    }

    @Test
    void saveAndFindByKey() {
        Project project = Project.create(orgId, "My Project", "my-project");
        projectPersistence.save(project);

        var found = projectPersistence.findByKey("my-project");
        assertTrue(found.isPresent());
        assertEquals("My Project", found.get().getName());
    }

    @Test
    void findAllByOrganizationId() {
        projectPersistence.save(Project.create(orgId, "Project A", "proj-a"));
        projectPersistence.save(Project.create(orgId, "Project B", "proj-b"));

        List<Project> projects = projectPersistence.findAllByOrganizationId(orgId);
        assertEquals(2, projects.size());
    }

    @Test
    void findEnvironmentsByProjectId() {
        UUID projectId = UUID.randomUUID();
        projectRepository.save(new ProjectJpaEntity(projectId, orgId, "Env Test", "env-test", Instant.now()));
        environmentRepository.save(new EnvironmentJpaEntity(UUID.randomUUID(), projectId, "Dev", "dev", 0));
        environmentRepository.save(new EnvironmentJpaEntity(UUID.randomUUID(), projectId, "Prod", "production", 1));
        entityManager.flush();

        List<Environment> envs = projectPersistence.findEnvironmentsByProjectId(projectId);
        assertEquals(2, envs.size());
        assertEquals("dev", envs.get(0).getKey());
        assertEquals("production", envs.get(1).getKey());
    }

    @Test
    void findEnvironmentByProjectIdAndKey() {
        UUID projectId = UUID.randomUUID();
        projectRepository.save(new ProjectJpaEntity(projectId, orgId, "Env Test 2", "env-test-2", Instant.now()));
        environmentRepository.save(new EnvironmentJpaEntity(UUID.randomUUID(), projectId, "Staging", "staging", 1));
        entityManager.flush();

        var found = projectPersistence.findEnvironmentByProjectIdAndKey(projectId, "staging");
        assertTrue(found.isPresent());
        assertEquals("Staging", found.get().getName());

        assertTrue(projectPersistence.findEnvironmentByProjectIdAndKey(projectId, "nope").isEmpty());
    }
}
