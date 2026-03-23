package com.switchboard.sdk;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PollingFlagSyncer {

    private static final Logger logger = Logger.getLogger(PollingFlagSyncer.class.getName());

    private final HttpFlagFetcher fetcher;
    private final FlagCache cache;
    private final long intervalSeconds;
    private ScheduledExecutorService executor;

    public PollingFlagSyncer(HttpFlagFetcher fetcher, FlagCache cache, long intervalSeconds) {
        this.fetcher = fetcher;
        this.cache = cache;
        this.intervalSeconds = intervalSeconds;
    }

    public void start() {
        executor = Executors.newSingleThreadScheduledExecutor(r -> {
            Thread t = new Thread(r, "switchboard-poller");
            t.setDaemon(true);
            return t;
        });

        executor.scheduleAtFixedRate(this::sync, intervalSeconds, intervalSeconds, TimeUnit.SECONDS);
        logger.info("polling syncer started with interval " + intervalSeconds + "s");
    }

    public void stop() {
        if (executor != null) {
            executor.shutdown();
            logger.info("polling syncer stopped");
        }
    }

    private void sync() {
        try {
            var flags = fetcher.fetchAll();
            cache.putAll(flags);
        } catch (Exception e) {
            logger.log(Level.WARNING, "polling sync failed, using cached flags", e);
        }
    }
}
