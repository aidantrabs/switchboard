package com.switchboard.application.port.input;

import com.switchboard.domain.model.FeatureFlag;

import java.util.List;

public interface StaleFlagUseCase {

    List<FeatureFlag> findStaleFlags(String projectKey);
}
