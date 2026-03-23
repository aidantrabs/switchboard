package com.switchboard.cli;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@Command(name = "switchboard", mixinStandardHelpOptions = true,
    version = "switchboard-cli 0.1.0",
    description = "switchboard feature flag management cli",
    subcommands = {
        FlagsCommand.class,
        ProjectsCommand.class,
        EnvsCommand.class,
        EvaluateCommand.class,
        AuditCommand.class
    })
public class SwitchboardCli implements Runnable {

    @Option(names = "--api-url", description = "switchboard api url")
    String apiUrl;

    @Option(names = "--api-key", description = "api key")
    String apiKey;

    @Option(names = "--config", description = "config file path")
    String configPath;

    @Option(names = "--output", description = "output format: json|table")
    String output;

    @Option(names = "--project", description = "default project key")
    String project;

    private CliConfig config;
    private CliHttpClient httpClient;

    @Override
    public void run() {
        new CommandLine(this).usage(System.out);
    }

    CliConfig config() {
        if (config == null) {
            config = configPath != null
                ? CliConfig.load(java.nio.file.Path.of(configPath))
                : CliConfig.load();
        }
        return config;
    }

    CliHttpClient http() {
        if (httpClient == null) {
            String url = apiUrl != null ? apiUrl : config().getApiUrl();
            String key = apiKey != null ? apiKey : config().getApiKey();

            if (url == null) {
                System.err.println("error: no api url configured. use --api-url or set it in ~/.switchboard/config.yml");
                System.exit(1);
            }

            httpClient = new CliHttpClient(url, key);
        }
        return httpClient;
    }

    String resolveProject() {
        if (project != null) return project;
        if (config().getDefaultProject() != null) return config().getDefaultProject();
        System.err.println("error: no project specified. use --project or set default-project in config");
        System.exit(1);
        return null;
    }

    String resolveOutput() {
        if (output != null) return output;
        return config().getOutput();
    }

    public static void main(String[] args) {
        int exitCode = new CommandLine(new SwitchboardCli()).execute(args);
        System.exit(exitCode);
    }
}
