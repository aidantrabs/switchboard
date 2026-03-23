package com.switchboard.demo;

import com.switchboard.sdk.EvaluationContext;
import com.switchboard.sdk.SwitchboardClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.Map;

@RestController
public class DemoController {

    private static final Logger log = LoggerFactory.getLogger(DemoController.class);

    private final SwitchboardClient switchboard;

    public DemoController(SwitchboardClient switchboard) {
        this.switchboard = switchboard;
    }

    @GetMapping("/demo")
    public Map<String, Object> demo(@RequestParam(defaultValue = "anonymous") String userId) {
        EvaluationContext context = EvaluationContext.builder()
            .userId(userId)
            .build();

        Map<String, Object> flags = new LinkedHashMap<>();

        for (String flagKey : new String[]{"new-checkout", "dark-mode", "premium-dashboard"}) {
            boolean enabled = switchboard.isEnabled(flagKey, context);
            String variant = switchboard.getVariant(flagKey, context).orElse("unknown");
            flags.put(flagKey, Map.of("enabled", enabled, "variant", variant));
            log.info("flag={} user={} enabled={} variant={}", flagKey, userId, enabled, variant);
        }

        return Map.of("userId", userId, "flags", flags);
    }
}
