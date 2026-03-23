package com.switchboard.cli;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class CliConfig {

    private String apiUrl;
    private String apiKey;
    private String defaultProject;
    private String defaultEnvironment;
    private String output = "table";

    private static final Path DEFAULT_PATH = Path.of(
        System.getProperty("user.home"), ".switchboard", "config.yml");

    public static CliConfig load() {
        return load(DEFAULT_PATH);
    }

    public static CliConfig load(Path path) {
        if (!Files.exists(path)) {
            return new CliConfig();
        }

        try {
            ObjectMapper mapper = new ObjectMapper(new YAMLFactory())
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            return mapper.readValue(path.toFile(), CliConfig.class);
        } catch (IOException e) {
            System.err.println("warning: failed to read config from " + path + ": " + e.getMessage());
            return new CliConfig();
        }
    }

    public String getApiUrl() { return apiUrl; }
    public void setApiUrl(String apiUrl) { this.apiUrl = apiUrl; }

    public String getApiKey() { return apiKey; }
    public void setApiKey(String apiKey) { this.apiKey = apiKey; }

    public String getDefaultProject() { return defaultProject; }
    public void setDefaultProject(String defaultProject) { this.defaultProject = defaultProject; }

    public String getDefaultEnvironment() { return defaultEnvironment; }
    public void setDefaultEnvironment(String defaultEnvironment) { this.defaultEnvironment = defaultEnvironment; }

    public String getOutput() { return output; }
    public void setOutput(String output) { this.output = output; }
}
