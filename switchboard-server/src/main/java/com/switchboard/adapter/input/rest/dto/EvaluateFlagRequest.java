package com.switchboard.adapter.input.rest.dto;

import java.util.Map;

public record EvaluateFlagRequest(String flagKey, String userId, Map<String, String> attributes) {}
