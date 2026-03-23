package com.switchboard.openfeature;

import com.switchboard.sdk.SwitchboardClient;
import dev.openfeature.sdk.*;

public class SwitchboardProvider implements FeatureProvider {

    private static final String NAME = "switchboard";

    private final SwitchboardClient client;

    public SwitchboardProvider(SwitchboardClient client) {
        this.client = client;
    }

    @Override
    public Metadata getMetadata() {
        return () -> NAME;
    }

    @Override
    public ProviderEvaluation<Boolean> getBooleanEvaluation(String key, Boolean defaultValue,
                                                            EvaluationContext ctx) {
        com.switchboard.sdk.EvaluationContext context = mapContext(ctx);
        boolean result = client.isEnabled(key, context);

        return ProviderEvaluation.<Boolean>builder()
            .value(result)
            .reason(Reason.TARGETING_MATCH.toString())
            .build();
    }

    @Override
    public ProviderEvaluation<String> getStringEvaluation(String key, String defaultValue,
                                                          EvaluationContext ctx) {
        com.switchboard.sdk.EvaluationContext context = mapContext(ctx);
        String result = client.getVariant(key, context).orElse(defaultValue);

        return ProviderEvaluation.<String>builder()
            .value(result)
            .reason(Reason.TARGETING_MATCH.toString())
            .build();
    }

    @Override
    public ProviderEvaluation<Integer> getIntegerEvaluation(String key, Integer defaultValue,
                                                            EvaluationContext ctx) {
        com.switchboard.sdk.EvaluationContext context = mapContext(ctx);
        String value = client.getVariantValue(key, context).orElse(null);

        if (value == null) {
            return ProviderEvaluation.<Integer>builder()
                .value(defaultValue)
                .reason(Reason.DEFAULT.toString())
                .build();
        }

        try {
            return ProviderEvaluation.<Integer>builder()
                .value(Integer.parseInt(value))
                .reason(Reason.TARGETING_MATCH.toString())
                .build();
        } catch (NumberFormatException e) {
            return ProviderEvaluation.<Integer>builder()
                .value(defaultValue)
                .reason(Reason.ERROR.toString())
                .build();
        }
    }

    @Override
    public ProviderEvaluation<Double> getDoubleEvaluation(String key, Double defaultValue,
                                                          EvaluationContext ctx) {
        com.switchboard.sdk.EvaluationContext context = mapContext(ctx);
        String value = client.getVariantValue(key, context).orElse(null);

        if (value == null) {
            return ProviderEvaluation.<Double>builder()
                .value(defaultValue)
                .reason(Reason.DEFAULT.toString())
                .build();
        }

        try {
            return ProviderEvaluation.<Double>builder()
                .value(Double.parseDouble(value))
                .reason(Reason.TARGETING_MATCH.toString())
                .build();
        } catch (NumberFormatException e) {
            return ProviderEvaluation.<Double>builder()
                .value(defaultValue)
                .reason(Reason.ERROR.toString())
                .build();
        }
    }

    @Override
    public ProviderEvaluation<Value> getObjectEvaluation(String key, Value defaultValue,
                                                         EvaluationContext ctx) {
        com.switchboard.sdk.EvaluationContext context = mapContext(ctx);
        String value = client.getVariantValue(key, context).orElse(null);

        if (value == null) {
            return ProviderEvaluation.<Value>builder()
                .value(defaultValue)
                .reason(Reason.DEFAULT.toString())
                .build();
        }

        return ProviderEvaluation.<Value>builder()
            .value(new Value(value))
            .reason(Reason.TARGETING_MATCH.toString())
            .build();
    }

    private com.switchboard.sdk.EvaluationContext mapContext(EvaluationContext ctx) {
        if (ctx == null) {
            return com.switchboard.sdk.EvaluationContext.builder().build();
        }

        com.switchboard.sdk.EvaluationContext.Builder builder =
            com.switchboard.sdk.EvaluationContext.builder();

        if (ctx.getTargetingKey() != null) {
            builder.userId(ctx.getTargetingKey());
        }

        ctx.asObjectMap().forEach((k, v) -> {
            if (v != null) {
                builder.attribute(k, v.toString());
            }
        });

        return builder.build();
    }
}
