package com.switchboard.domain.exception;

public class FlagNotFoundException extends RuntimeException {

    public FlagNotFoundException(String flagKey) {
        super("feature flag not found: " + flagKey);
    }
}
