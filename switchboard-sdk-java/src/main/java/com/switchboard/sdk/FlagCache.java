package com.switchboard.sdk;

import com.switchboard.sdk.model.FlagConfig;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class FlagCache {

    private final Map<String, FlagConfig> flags = new ConcurrentHashMap<>();

    public void putAll(List<FlagConfig> configs) {
        configs.forEach(c -> flags.put(c.getKey(), c));
    }

    public void put(FlagConfig config) {
        flags.put(config.getKey(), config);
    }

    public Optional<FlagConfig> get(String flagKey) {
        return Optional.ofNullable(flags.get(flagKey));
    }

    public void remove(String flagKey) {
        flags.remove(flagKey);
    }

    public void clear() {
        flags.clear();
    }

    public int size() {
        return flags.size();
    }
}
