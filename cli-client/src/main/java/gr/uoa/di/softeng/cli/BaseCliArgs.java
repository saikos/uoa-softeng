package gr.uoa.di.softeng.cli;

import java.util.ArrayList;
import java.util.Arrays;
import static picocli.CommandLine.*;

/**
 *
 */
@Command
public class BaseCliArgs {

    @Option(
        names = {"-h", "--help"},
        usageHelp = true,
        description = "Display this help message"
    )
    protected boolean usageHelpRequested;

    protected static class CandidateFormats extends ArrayList<String> {
        CandidateFormats() { super(Arrays.asList("JSON", "XML")); }
    }

    @Option(
        names = "--format",
        defaultValue = "JSON",
        fallbackValue = "JSON",
        completionCandidates = CandidateFormats.class,
        description = "The format of the response; supported values: ${COMPLETION-CANDIDATES} (default is ${DEFAULT-VALUE})."
    )
    protected String format;

    @Spec
    protected Model.CommandSpec spec;

}
