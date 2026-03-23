package com.switchboard.adapter.input.rest;

import com.switchboard.adapter.input.rest.dto.CreateOrganizationRequest;
import com.switchboard.adapter.input.rest.dto.OrganizationResponse;
import com.switchboard.adapter.output.persistence.entity.OrganizationJpaEntity;
import com.switchboard.adapter.output.persistence.repository.OrganizationJpaRepository;
import com.switchboard.domain.model.Organization;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/organizations")
public class OrganizationController {

    private final OrganizationJpaRepository repository;

    public OrganizationController(OrganizationJpaRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<OrganizationResponse> list() {
        return repository.findAll().stream()
            .map(e -> new Organization(e.getId(), e.getName(), e.getSlug(), e.getCreatedAt()))
            .map(OrganizationResponse::from)
            .toList();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrganizationResponse create(@RequestBody CreateOrganizationRequest request) {
        Organization org = Organization.create(request.name(), request.slug());
        var entity = new OrganizationJpaEntity(
            org.getId(), org.getName(), org.getSlug(), org.getCreatedAt());
        repository.save(entity);
        return OrganizationResponse.from(org);
    }
}
