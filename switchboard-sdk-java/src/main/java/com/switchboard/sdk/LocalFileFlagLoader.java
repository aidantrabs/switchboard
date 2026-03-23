package com.switchboard.sdk;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.switchboard.sdk.model.FlagConfig;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class LocalFileFlagLoader {

    private static final Logger logger = Logger.getLogger(LocalFileFlagLoader.class.getName());
    private static final ObjectMapper objectMapper = new ObjectMapper()
        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    public static List<FlagConfig> loadFromFile(String path) throws IOException {
        InputStream is;

        if (path.startsWith("classpath:")) {
            String resource = path.substring("classpath:".length());
            is = LocalFileFlagLoader.class.getClassLoader().getResourceAsStream(resource);
            if (is == null) {
                throw new IOException("classpath resource not found: " + resource);
            }
        } else {
            is = Files.newInputStream(Path.of(path));
        }

        try (is) {
            return parse(is);
        }
    }

    static List<FlagConfig> parse(InputStream is) throws IOException {
        JsonNode root = objectMapper.readTree(is);
        JsonNode flagsNode = root.get("flags");

        if (flagsNode == null || !flagsNode.isObject()) {
            return List.of();
        }

        List<FlagConfig> configs = new ArrayList<>();
        var fields = flagsNode.fields();

        while (fields.hasNext()) {
            var entry = fields.next();
            String key = entry.getKey();
            JsonNode value = entry.getValue();

            boolean enabled = value.has("enabled") && value.get("enabled").asBoolean();
            String variant = value.has("variant") ? value.get("variant").asText() : "off";

            FlagConfig config = new FlagConfig(key, enabled, enabled ? "off" : variant,
                List.of(
                    new com.switchboard.sdk.model.Variant("on", "true"),
                    new com.switchboard.sdk.model.Variant("off", "false")
                ),
                enabled ? 100 : 0, List.of());

            configs.add(config);
        }

        logger.info("loaded " + configs.size() + " flags from local file");
        return configs;
    }
}
