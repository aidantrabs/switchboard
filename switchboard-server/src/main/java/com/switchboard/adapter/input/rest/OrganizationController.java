package com.switchboard.adapter.input.rest;

import com.switchboard.adapter.input.rest.dto.CreateOrganizationRequest;
import com.switchboard.adapter.input.rest.dto.OrganizationResponse;
import com.switchboard.application.port.output.OrganizationPersistencePort;
import com.switchboard.domain.model.Organization;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/organizations")
public class OrganizationController {

    private final OrganizationPersistencePort organizationPersistence;

    public OrganizationController(OrganizationPersistencePort organizationPersistence) {
        this.organizationPersistence = organizationPersistence;
    }

    @GetMapping
    public List<OrganizationResponse> list() {
        return organizationPersistence.findAll().stream()
            .map(OrganizationResponse::from)
            .toList();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrganizationResponse create(@RequestBody CreateOrganizationRequest request) {
        Organization org = Organization.create(request.name(), request.slug());
        return OrganizationResponse.from(organizationPersistence.save(org));
    }
}
