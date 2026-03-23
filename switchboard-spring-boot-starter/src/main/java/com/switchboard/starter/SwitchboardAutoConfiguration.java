package com.switchboard.starter;

import com.switchboard.sdk.SwitchboardClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(SwitchboardProperties.class)
public class SwitchboardAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public SwitchboardClient switchboardClient(SwitchboardProperties properties) {
        SwitchboardClient.Builder builder = SwitchboardClient.builder();

        if (properties.isLocalMode()) {
            return builder
                .localFlagsFile(properties.getLocalFlagsFile())
                .build();
        }

        return builder
            .apiUrl(properties.getApiUrl())
            .apiKey(properties.getApiKey())
            .project(properties.getProject())
            .environment(properties.getEnvironment())
            .pollInterval(properties.getPollIntervalSeconds())
            .pollingEnabled(properties.isPollingEnabled())
            .build();
    }

    @Bean
    @ConditionalOnMissingBean
    public SwitchboardHealthIndicator switchboardHealthIndicator(SwitchboardClient client) {
        return new SwitchboardHealthIndicator(client);
    }
}
