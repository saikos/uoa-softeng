package gr.uoa.di.softeng.cli.subcommands;

import gr.uoa.di.softeng.cli.LimitsCliArgs;
import gr.uoa.di.softeng.client.RestAPI;
import gr.uoa.di.softeng.data.model.Incident;
import gr.uoa.di.softeng.data.model.Limits;
import java.util.List;
import java.util.concurrent.Callable;
import picocli.CommandLine;
import static picocli.CommandLine.Command;

/**
 *
 */
@Command(
    name="incidents"
)
public class Incidents extends LimitsCliArgs implements Callable<Integer> {

    @Override
    public Integer call() {

        CommandLine cli = spec.commandLine();

        if (usageHelpRequested) {
            cli.usage(cli.getOut());
            return 0;
        }

        try {
            List<Incident> incidents = new RestAPI().getIncidents(new Limits(start, count));
            System.out.println("Fetched " + incidents.size() + " incident records");
            return 0;
        }
        catch (RuntimeException e) {
            cli.getOut().println(e.getMessage());
            e.printStackTrace(cli.getOut());
            return -1;
        }
    }

}
