package com.switchboard.cli;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.util.ArrayList;
import java.util.List;

public class OutputFormatter {

    private static final ObjectMapper JSON_MAPPER = new ObjectMapper()
        .enable(SerializationFeature.INDENT_OUTPUT);

    public static void print(String json, String format, String... columns) {
        if ("json".equalsIgnoreCase(format)) {
            try {
                Object parsed = JSON_MAPPER.readValue(json, Object.class);
                System.out.println(JSON_MAPPER.writeValueAsString(parsed));
            } catch (Exception e) {
                System.out.println(json);
            }
            return;
        }

        try {
            JsonNode node = JSON_MAPPER.readTree(json);
            if (node.isArray()) {
                printTable(node, columns);
            } else {
                printObject(node);
            }
        } catch (Exception e) {
            System.out.println(json);
        }
    }

    private static void printTable(JsonNode array, String[] columns) {
        if (array.isEmpty()) {
            System.out.println("(no results)");
            return;
        }

        if (columns.length == 0) {
            var first = array.get(0);
            List<String> cols = new ArrayList<>();
            first.fieldNames().forEachRemaining(cols::add);
            columns = cols.toArray(new String[0]);
        }

        int[] widths = new int[columns.length];
        for (int i = 0; i < columns.length; i++) {
            widths[i] = columns[i].length();
        }

        for (JsonNode row : array) {
            for (int i = 0; i < columns.length; i++) {
                String val = getField(row, columns[i]);
                widths[i] = Math.max(widths[i], val.length());
            }
        }

        StringBuilder header = new StringBuilder();
        StringBuilder separator = new StringBuilder();
        for (int i = 0; i < columns.length; i++) {
            header.append(String.format("%-" + widths[i] + "s", columns[i].toUpperCase()));
            separator.append("-".repeat(widths[i]));
            if (i < columns.length - 1) {
                header.append("  ");
                separator.append("  ");
            }
        }
        System.out.println(header);
        System.out.println(separator);

        for (JsonNode row : array) {
            StringBuilder line = new StringBuilder();
            for (int i = 0; i < columns.length; i++) {
                line.append(String.format("%-" + widths[i] + "s", getField(row, columns[i])));
                if (i < columns.length - 1) {
                    line.append("  ");
                }
            }
            System.out.println(line);
        }
    }

    private static void printObject(JsonNode node) {
        node.fields().forEachRemaining(entry ->
            System.out.println(entry.getKey() + ": " + entry.getValue().asText()));
    }

    private static String getField(JsonNode node, String field) {
        JsonNode value = node.get(field);
        if (value == null) return "";
        if (value.isTextual()) return value.asText();
        return value.toString();
    }
}
