package com.switchboard.cli;

import picocli.CommandLine.Command;
import picocli.CommandLine.ParentCommand;

@Command(name = "projects", description = "manage projects",
    subcommands = { ProjectsCommand.List.class })
public class ProjectsCommand implements Runnable {

    @ParentCommand
    SwitchboardCli parent;

    @Override
    public void run() {
        new picocli.CommandLine(this).usage(System.out);
    }

    @Command(name = "list", description = "list all projects")
    static class List implements Runnable {
        @ParentCommand ProjectsCommand projects;

        @Override
        public void run() {
            try {
                var cli = projects.parent;
                String response = cli.http().get("/api/v1/projects");
                OutputFormatter.print(response, cli.resolveOutput(), "key", "name");
            } catch (Exception e) {
                System.err.println("error: " + e.getMessage());
                System.exit(1);
            }
        }
    }
}
