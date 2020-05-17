package gr.uoa.di.softeng.cli;

import static picocli.CommandLine.*;

/**
 *
 */
@Command
public class LimitsCliArgs extends BaseCliArgs {

    @Option(
        names = "--start",
        description = "Results OFFSET"
    )
    protected Long start;

    @Option(
        names = "--count",
        description = "Results LIMIT"
    )
    protected Integer count;

}
