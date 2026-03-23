package com.switchboard.sdk;

import com.switchboard.sdk.model.FlagConfig;
import com.switchboard.sdk.model.Variant;

import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SwitchboardClient {

    private static final Logger logger = Logger.getLogger(SwitchboardClient.class.getName());

    private final FlagCache cache;
    private final FlagEvaluator evaluator;
    private final HttpFlagFetcher fetcher;

    private SwitchboardClient(FlagCache cache, FlagEvaluator evaluator, HttpFlagFetcher fetcher) {
        this.cache = cache;
        this.evaluator = evaluator;
        this.fetcher = fetcher;
    }

    public static Builder builder() {
        return new Builder();
    }

    public boolean isEnabled(String flagKey, EvaluationContext context) {
        return cache.get(flagKey)
            .map(flag -> {
                String variant = evaluator.evaluate(flag, context);
                return !variant.equals(flag.getDefaultVariant());
            })
            .orElse(false);
    }

    public Optional<String> getVariant(String flagKey, EvaluationContext context) {
        return cache.get(flagKey)
            .map(flag -> evaluator.evaluate(flag, context));
    }

    public Optional<String> getVariantValue(String flagKey, EvaluationContext context) {
        return cache.get(flagKey)
            .map(flag -> {
                String variantKey = evaluator.evaluate(flag, context);
                if (flag.getVariants() == null) return null;
                return flag.getVariants().stream()
                    .filter(v -> v.getKey().equals(variantKey))
                    .map(Variant::getValue)
                    .findFirst()
                    .orElse(null);
            });
    }

    public FlagCache getCache() {
        return cache;
    }

    void bootstrap() {
        if (fetcher == null) return;
        try {
            var flags = fetcher.fetchAll();
            cache.putAll(flags);
            logger.info("bootstrapped " + flags.size() + " flags");
        } catch (Exception e) {
            logger.log(Level.WARNING, "failed to bootstrap flags from server", e);
        }
    }

    public static class Builder {
        private String apiUrl;
        private String apiKey;
        private String projectKey;
        private String environmentKey;

        public Builder apiUrl(String apiUrl) {
            this.apiUrl = apiUrl;
            return this;
        }

        public Builder apiKey(String apiKey) {
            this.apiKey = apiKey;
            return this;
        }

        public Builder project(String projectKey) {
            this.projectKey = projectKey;
            return this;
        }

        public Builder environment(String environmentKey) {
            this.environmentKey = environmentKey;
            return this;
        }

        public SwitchboardClient build() {
            FlagCache cache = new FlagCache();
            FlagEvaluator evaluator = new FlagEvaluator();
            HttpFlagFetcher fetcher = null;

            if (apiUrl != null) {
                fetcher = new HttpFlagFetcher(apiUrl, apiKey, projectKey, environmentKey);
            }

            SwitchboardClient client = new SwitchboardClient(cache, evaluator, fetcher);
            client.bootstrap();
            return client;
        }
    }
}
