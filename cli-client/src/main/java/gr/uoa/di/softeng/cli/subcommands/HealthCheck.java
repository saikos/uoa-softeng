package gr.uoa.di.softeng.cli.subcommands;

import gr.uoa.di.softeng.cli.BaseCliArgs;
import gr.uoa.di.softeng.client.RestAPI;
import java.util.concurrent.Callable;
import picocli.CommandLine;
import static picocli.CommandLine.Command;

/**
 *
 */
@Command(
    name="health-check"
)
public class HealthCheck extends BaseCliArgs implements Callable<Integer> {

    @Override
    public Integer call() {

        CommandLine cli = spec.commandLine();

        if (usageHelpRequested) {
            cli.usage(cli.getOut());
            return 0;
        }

        try {
            new RestAPI().healthCheck();
            System.out.println("Health check succeeded.");
            return 0;
        }
        catch (RuntimeException e) {
            cli.getOut().println(e.getMessage());
            e.printStackTrace(cli.getOut());
            return -1;
        }        
    }

}
