package com.switchboard.demo;

import com.switchboard.sdk.EvaluationContext;
import com.switchboard.sdk.SwitchboardClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;

@RestController
public class DemoController {

    private static final Logger log = LoggerFactory.getLogger(DemoController.class);
    private static final String[] FLAG_KEYS = {"new-checkout", "dark-mode", "premium-dashboard"};

    private final SwitchboardClient switchboard;

    public DemoController(SwitchboardClient switchboard) {
        this.switchboard = switchboard;
    }

    @GetMapping("/demo")
    public Map<String, Object> demo(
            @RequestParam(defaultValue = "anonymous") String userId,
            @RequestParam(required = false) String plan,
            @RequestParam(required = false) String country) {

        EvaluationContext.Builder builder = EvaluationContext.builder().userId(userId);
        if (plan != null) builder.attribute("user.plan", plan);
        if (country != null) builder.attribute("user.country", country);
        EvaluationContext context = builder.build();

        Map<String, Object> flags = new LinkedHashMap<>();
        for (String flagKey : FLAG_KEYS) {
            boolean enabled = switchboard.isEnabled(flagKey, context);
            String variant = switchboard.getVariant(flagKey, context).orElse("unknown");
            flags.put(flagKey, Map.of("enabled", enabled, "variant", variant));
            log.info("flag={} user={} enabled={} variant={}", flagKey, userId, enabled, variant);
        }

        return Map.of(
            "userId", userId,
            "flags", flags,
            "cachedFlags", switchboard.getCache().size(),
            "evaluatedAt", Instant.now().toString()
        );
    }

    @GetMapping("/health")
    public Map<String, Object> health() {
        return Map.of(
            "status", "up",
            "cachedFlags", switchboard.getCache().size()
        );
    }
}
