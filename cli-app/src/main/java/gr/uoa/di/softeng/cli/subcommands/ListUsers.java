package gr.uoa.di.softeng.cli.subcommands;

import gr.uoa.di.softeng.cli.LimitsCliArgs;
import gr.uoa.di.softeng.client.RestAPI;
import gr.uoa.di.softeng.data.model.Limits;
import gr.uoa.di.softeng.data.model.User;
import java.util.List;
import java.util.concurrent.Callable;
import picocli.CommandLine;
import static picocli.CommandLine.Command;

/**
 *
 */
@Command(
    name="list-users"
)
public class ListUsers extends LimitsCliArgs implements Callable<Integer> {

    @Override
    public Integer call() {

        CommandLine cli = spec.commandLine();

        if (usageHelpRequested) {
            cli.usage(cli.getOut());
            return 0;
        }

        try {
            // Read the token from a user's file.
            RestAPI restApi = new RestAPI("token");
            List<User> users = restApi.getUsers(new Limits(start, count));
            // TODO: Print the users 
            System.out.println("Fetched " + users.size() + " users records");
            return 0;
        }
        catch (RuntimeException e) {
            cli.getOut().println(e.getMessage());
            e.printStackTrace(cli.getOut());
            return -1;
        }
    }

}
