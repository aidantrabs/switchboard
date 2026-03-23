package com.switchboard.cli;

import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.ParentCommand;

@Command(name = "envs", description = "manage environments",
    subcommands = { EnvsCommand.List.class })
public class EnvsCommand implements Runnable {

    @ParentCommand
    SwitchboardCli parent;

    @Override
    public void run() {
        new picocli.CommandLine(this).usage(System.out);
    }

    @Command(name = "list", description = "list environments")
    static class List implements Runnable {
        @ParentCommand EnvsCommand envs;
        @Option(names = "--project", description = "project key") String project;

        @Override
        public void run() {
            try {
                var cli = envs.parent;
                String p = project != null ? project : cli.resolveProject();
                String response = cli.http().get("/api/v1/projects/" + p + "/environments");
                OutputFormatter.print(response, cli.resolveOutput(), "key", "name", "sortOrder");
            } catch (Exception e) {
                System.err.println("error: " + e.getMessage());
                System.exit(1);
            }
        }
    }
}
