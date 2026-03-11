package com.switchboard.domain.exception;

public class DuplicateFlagKeyException extends RuntimeException {

    public DuplicateFlagKeyException(String flagKey) {
        super("flag key already exists: " + flagKey);
    }
}
