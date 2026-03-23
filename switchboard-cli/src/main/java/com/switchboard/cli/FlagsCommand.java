package com.switchboard.cli;

import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;
import picocli.CommandLine.ParentCommand;

import java.util.Map;

@Command(name = "flags", description = "manage feature flags",
    subcommands = {
        FlagsCommand.List.class,
        FlagsCommand.Get.class,
        FlagsCommand.Create.class,
        FlagsCommand.Toggle.class,
        FlagsCommand.Rollout.class,
        FlagsCommand.Delete.class
    })
public class FlagsCommand implements Runnable {

    @ParentCommand
    SwitchboardCli parent;

    @Override
    public void run() {
        new picocli.CommandLine(this).usage(System.out);
    }

    @Command(name = "list", description = "list all flags")
    static class List implements Runnable {
        @ParentCommand FlagsCommand flags;
        @Option(names = "--project", description = "project key") String project;
        @Option(names = "--env", description = "environment key") String env;

        @Override
        public void run() {
            try {
                var cli = flags.parent;
                String p = project != null ? project : cli.resolveProject();
                String response = cli.http().get("/api/v1/projects/" + p + "/flags");
                OutputFormatter.print(response, cli.resolveOutput(), "key", "name", "flagType");
            } catch (Exception e) {
                System.err.println("error: " + e.getMessage());
                System.exit(1);
            }
        }
    }

    @Command(name = "get", description = "get flag details")
    static class Get implements Runnable {
        @ParentCommand FlagsCommand flags;
        @Parameters(index = "0", description = "flag key") String flagKey;
        @Option(names = "--project", description = "project key") String project;

        @Override
        public void run() {
            try {
                var cli = flags.parent;
                String p = project != null ? project : cli.resolveProject();
                String response = cli.http().get("/api/v1/projects/" + p + "/flags/" + flagKey);
                OutputFormatter.print(response, cli.resolveOutput());
            } catch (Exception e) {
                System.err.println("error: " + e.getMessage());
                System.exit(1);
            }
        }
    }

    @Command(name = "create", description = "create a new flag")
    static class Create implements Runnable {
        @ParentCommand FlagsCommand flags;
        @Option(names = "--key", required = true, description = "flag key") String key;
        @Option(names = "--name", description = "display name") String name;
        @Option(names = "--type", required = true, description = "flag type") String type;
        @Option(names = "--project", description = "project key") String project;

        @Override
        public void run() {
            try {
                var cli = flags.parent;
                String p = project != null ? project : cli.resolveProject();
                Map<String, Object> body = Map.of(
                    "key", key,
                    "name", name != null ? name : key,
                    "description", "",
                    "flagType", type.toUpperCase(),
                    "defaultVariant", "off",
                    "variants", java.util.List.of(
                        Map.of("key", "on", "value", "true"),
                        Map.of("key", "off", "value", "false"))
                );
                String response = cli.http().post("/api/v1/projects/" + p + "/flags", body);
                OutputFormatter.print(response, cli.resolveOutput());
            } catch (Exception e) {
                System.err.println("error: " + e.getMessage());
                System.exit(1);
            }
        }
    }

    @Command(name = "toggle", description = "toggle a flag on/off")
    static class Toggle implements Runnable {
        @ParentCommand FlagsCommand flags;
        @Parameters(index = "0", description = "flag key") String flagKey;
        @Option(names = "--project", description = "project key") String project;
        @Option(names = "--env", required = true, description = "environment key") String env;

        @Override
        public void run() {
            try {
                var cli = flags.parent;
                String p = project != null ? project : cli.resolveProject();
                cli.http().patch("/api/v1/projects/" + p + "/flags/" + flagKey
                    + "/environments/" + env + "/toggle");
                System.out.println("toggled " + flagKey + " in " + env);
            } catch (Exception e) {
                System.err.println("error: " + e.getMessage());
                System.exit(1);
            }
        }
    }

    @Command(name = "rollout", description = "set rollout percentage")
    static class Rollout implements Runnable {
        @ParentCommand FlagsCommand flags;
        @Parameters(index = "0", description = "flag key") String flagKey;
        @Option(names = "--percentage", required = true, description = "rollout percentage") int percentage;
        @Option(names = "--project", description = "project key") String project;
        @Option(names = "--env", required = true, description = "environment key") String env;

        @Override
        public void run() {
            try {
                var cli = flags.parent;
                String p = project != null ? project : cli.resolveProject();
                cli.http().put("/api/v1/projects/" + p + "/flags/" + flagKey
                    + "/environments/" + env + "/rollout", Map.of("percentage", percentage));
                System.out.println("set rollout for " + flagKey + " to " + percentage + "% in " + env);
            } catch (Exception e) {
                System.err.println("error: " + e.getMessage());
                System.exit(1);
            }
        }
    }

    @Command(name = "delete", description = "delete a flag")
    static class Delete implements Runnable {
        @ParentCommand FlagsCommand flags;
        @Parameters(index = "0", description = "flag key") String flagKey;
        @Option(names = "--project", description = "project key") String project;

        @Override
        public void run() {
            try {
                var cli = flags.parent;
                String p = project != null ? project : cli.resolveProject();
                cli.http().delete("/api/v1/projects/" + p + "/flags/" + flagKey);
                System.out.println("deleted " + flagKey);
            } catch (Exception e) {
                System.err.println("error: " + e.getMessage());
                System.exit(1);
            }
        }
    }
}
