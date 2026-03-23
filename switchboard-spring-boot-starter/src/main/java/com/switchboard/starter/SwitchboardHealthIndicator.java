package com.switchboard.starter;

import com.switchboard.sdk.SwitchboardClient;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;

public class SwitchboardHealthIndicator implements HealthIndicator {

    private final SwitchboardClient client;

    public SwitchboardHealthIndicator(SwitchboardClient client) {
        this.client = client;
    }

    @Override
    public Health health() {
        int cacheSize = client.getCache().size();

        if (cacheSize > 0) {
            return Health.up()
                .withDetail("cachedFlags", cacheSize)
                .build();
        }

        return Health.down()
            .withDetail("cachedFlags", 0)
            .withDetail("reason", "no flags in cache")
            .build();
    }
}
