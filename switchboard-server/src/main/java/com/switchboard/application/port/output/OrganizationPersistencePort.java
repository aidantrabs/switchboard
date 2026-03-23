package com.switchboard.application.port.output;

import com.switchboard.domain.model.Organization;

import java.util.List;

public interface OrganizationPersistencePort {

    Organization save(Organization organization);

    List<Organization> findAll();
}
