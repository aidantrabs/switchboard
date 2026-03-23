package com.switchboard.cli;

import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;
import picocli.CommandLine.ParentCommand;

import java.util.HashMap;
import java.util.Map;

@Command(name = "evaluate", description = "evaluate a flag for a given context")
public class EvaluateCommand implements Runnable {

    @ParentCommand
    SwitchboardCli parent;

    @Parameters(index = "0", description = "flag key")
    String flagKey;

    @Option(names = "--user", required = true, description = "user id")
    String userId;

    @Option(names = "--attr", description = "attribute key=value")
    Map<String, String> attributes;

    @Option(names = "--project", description = "project key")
    String project;

    @Option(names = "--env", required = true, description = "environment key")
    String env;

    @Override
    public void run() {
        try {
            String p = project != null ? project : parent.resolveProject();
            Map<String, Object> body = new HashMap<>();
            body.put("flagKey", flagKey);
            body.put("userId", userId);
            body.put("attributes", attributes != null ? attributes : Map.of());

            String response = parent.http().post(
                "/api/v1/client/" + p + "/" + env + "/evaluate", body);
            OutputFormatter.print(response, parent.resolveOutput());
        } catch (Exception e) {
            System.err.println("error: " + e.getMessage());
            System.exit(1);
        }
    }
}
