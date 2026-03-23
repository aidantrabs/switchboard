package com.switchboard.sdk;

import com.switchboard.sdk.model.FlagConfig;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FlagCacheTest {

    @Test
    void putAndGet() {
        var cache = new FlagCache();
        var flag = new FlagConfig("flag-1", true, "off", List.of(), 0, List.of());

        cache.put(flag);

        assertTrue(cache.get("flag-1").isPresent());
        assertEquals("flag-1", cache.get("flag-1").get().getKey());
    }

    @Test
    void getMissingReturnsEmpty() {
        var cache = new FlagCache();

        assertTrue(cache.get("nonexistent").isEmpty());
    }

    @Test
    void putAllPopulatesCache() {
        var cache = new FlagCache();
        var flags = List.of(
            new FlagConfig("flag-1", true, "off", List.of(), 0, List.of()),
            new FlagConfig("flag-2", false, "off", List.of(), 0, List.of())
        );

        cache.putAll(flags);

        assertEquals(2, cache.size());
        assertTrue(cache.get("flag-1").isPresent());
        assertTrue(cache.get("flag-2").isPresent());
    }

    @Test
    void removeDeletesEntry() {
        var cache = new FlagCache();
        cache.put(new FlagConfig("flag-1", true, "off", List.of(), 0, List.of()));

        cache.remove("flag-1");

        assertTrue(cache.get("flag-1").isEmpty());
    }

    @Test
    void clearRemovesAll() {
        var cache = new FlagCache();
        cache.putAll(List.of(
            new FlagConfig("flag-1", true, "off", List.of(), 0, List.of()),
            new FlagConfig("flag-2", false, "off", List.of(), 0, List.of())
        ));

        cache.clear();

        assertEquals(0, cache.size());
    }

    @Test
    void putOverwritesExisting() {
        var cache = new FlagCache();
        cache.put(new FlagConfig("flag-1", false, "off", List.of(), 0, List.of()));
        cache.put(new FlagConfig("flag-1", true, "on", List.of(), 0, List.of()));

        var flag = cache.get("flag-1").get();
        assertTrue(flag.isEnabled());
        assertEquals("on", flag.getDefaultVariant());
    }
}
