package com.switchboard.integration;

import com.switchboard.adapter.output.persistence.entity.EnvironmentJpaEntity;
import com.switchboard.adapter.output.persistence.entity.OrganizationJpaEntity;
import com.switchboard.adapter.output.persistence.entity.ProjectJpaEntity;
import com.switchboard.adapter.output.persistence.repository.EnvironmentJpaRepository;
import com.switchboard.adapter.output.persistence.repository.ProjectJpaRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class FlagManagementApiIntegrationTest extends IntegrationTestBase {

    @Autowired
    TestRestTemplate restTemplate;

    @Autowired
    ProjectJpaRepository projectRepository;

    @Autowired
    EnvironmentJpaRepository environmentRepository;

    @Autowired
    EntityManager entityManager;

    private final String projectKey = "api-test-project";

    @BeforeEach
    void setUp() {
        UUID orgId = UUID.randomUUID();
        entityManager.persist(new OrganizationJpaEntity(orgId, "API Test Org", "api-test-org-" + orgId, Instant.now()));

        UUID projectId = UUID.randomUUID();
        projectRepository.save(new ProjectJpaEntity(projectId, orgId, "API Test Project", projectKey, Instant.now()));

        environmentRepository.save(new EnvironmentJpaEntity(UUID.randomUUID(), projectId, "Dev", "dev", 0));
        entityManager.flush();
    }

    @Test
    void createAndGetFlag() {
        Map<String, Object> createBody = Map.of(
            "key", "integration-flag",
            "name", "Integration Flag",
            "description", "test",
            "flagType", "RELEASE",
            "defaultVariant", "off",
            "variants", List.of(
                Map.of("key", "on", "value", "true"),
                Map.of("key", "off", "value", "false")
            )
        );

        ResponseEntity<Map> createResponse = restTemplate.postForEntity(
            "/api/v1/projects/" + projectKey + "/flags",
            createBody, Map.class);

        assertEquals(HttpStatus.CREATED, createResponse.getStatusCode());
        assertNotNull(createResponse.getBody());
        assertEquals("integration-flag", createResponse.getBody().get("key"));
    }

    @Test
    void createDuplicateFlagReturnsConflict() {
        Map<String, Object> body = Map.of(
            "key", "dup-flag",
            "name", "Dup",
            "description", "",
            "flagType", "RELEASE",
            "defaultVariant", "off",
            "variants", List.of()
        );

        restTemplate.postForEntity("/api/v1/projects/" + projectKey + "/flags", body, Map.class);
        ResponseEntity<Map> second = restTemplate.postForEntity(
            "/api/v1/projects/" + projectKey + "/flags", body, Map.class);

        assertEquals(HttpStatus.CONFLICT, second.getStatusCode());
    }
}
