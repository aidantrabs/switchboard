package com.switchboard.adapter.input.rest.dto;

import java.util.List;

public record UpdateTargetingRequest(List<TargetingRuleDto> rules) {}
