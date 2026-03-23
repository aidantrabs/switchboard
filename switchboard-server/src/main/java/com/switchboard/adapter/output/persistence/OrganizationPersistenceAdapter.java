package com.switchboard.adapter.output.persistence;

import com.switchboard.adapter.output.persistence.entity.OrganizationJpaEntity;
import com.switchboard.adapter.output.persistence.repository.OrganizationJpaRepository;
import com.switchboard.application.port.output.OrganizationPersistencePort;
import com.switchboard.domain.model.Organization;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OrganizationPersistenceAdapter implements OrganizationPersistencePort {

    private final OrganizationJpaRepository repository;

    public OrganizationPersistenceAdapter(OrganizationJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    public Organization save(Organization org) {
        var entity = new OrganizationJpaEntity(
            org.getId(), org.getName(), org.getSlug(), org.getCreatedAt());
        repository.save(entity);
        return org;
    }

    @Override
    public List<Organization> findAll() {
        return repository.findAll().stream()
            .map(e -> new Organization(e.getId(), e.getName(), e.getSlug(), e.getCreatedAt()))
            .toList();
    }
}
