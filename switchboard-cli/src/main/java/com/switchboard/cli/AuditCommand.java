package com.switchboard.cli;

import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.ParentCommand;

@Command(name = "audit", description = "view audit log entries")
public class AuditCommand implements Runnable {

    @ParentCommand
    SwitchboardCli parent;

    @Option(names = "--flag", description = "filter by flag key")
    String flagKey;

    @Option(names = "--last", description = "number of entries", defaultValue = "20")
    int limit;

    @Option(names = "--project", description = "project key")
    String project;

    @Override
    public void run() {
        try {
            String p = project != null ? project : parent.resolveProject();
            String query = "?limit=" + limit;
            if (flagKey != null) {
                query += "&flagKey=" + flagKey;
            }
            String response = parent.http().get("/api/v1/projects/" + p + "/audit-log" + query);
            OutputFormatter.print(response, parent.resolveOutput(),
                "timestamp", "flagKey", "action", "changedBy");
        } catch (Exception e) {
            System.err.println("error: " + e.getMessage());
            System.exit(1);
        }
    }
}
