package com.switchboard.starter;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "switchboard")
public class SwitchboardProperties {

    private String apiUrl;
    private String apiKey;
    private String project;
    private String environment;
    private String mode = "remote";
    private String localFlagsFile;
    private long pollIntervalSeconds = 30;
    private boolean pollingEnabled = true;

    public String getApiUrl() { return apiUrl; }
    public void setApiUrl(String apiUrl) { this.apiUrl = apiUrl; }

    public String getApiKey() { return apiKey; }
    public void setApiKey(String apiKey) { this.apiKey = apiKey; }

    public String getProject() { return project; }
    public void setProject(String project) { this.project = project; }

    public String getEnvironment() { return environment; }
    public void setEnvironment(String environment) { this.environment = environment; }

    public String getMode() { return mode; }
    public void setMode(String mode) { this.mode = mode; }

    public String getLocalFlagsFile() { return localFlagsFile; }
    public void setLocalFlagsFile(String localFlagsFile) { this.localFlagsFile = localFlagsFile; }

    public long getPollIntervalSeconds() { return pollIntervalSeconds; }
    public void setPollIntervalSeconds(long pollIntervalSeconds) { this.pollIntervalSeconds = pollIntervalSeconds; }

    public boolean isPollingEnabled() { return pollingEnabled; }
    public void setPollingEnabled(boolean pollingEnabled) { this.pollingEnabled = pollingEnabled; }

    public boolean isLocalMode() {
        return "local".equalsIgnoreCase(mode);
    }
}
