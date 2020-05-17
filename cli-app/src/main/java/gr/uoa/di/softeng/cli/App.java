package gr.uoa.di.softeng.cli;

import gr.uoa.di.softeng.cli.subcommands.Incidents;
import gr.uoa.di.softeng.cli.subcommands.HealthCheck;
import gr.uoa.di.softeng.cli.subcommands.Users;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.net.http.HttpClient;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.concurrent.Callable;
import picocli.CommandLine;
import static picocli.CommandLine.*;

/**
 *
 */
@Command(
    name="ccenter",
    mixinStandardHelpOptions = true,
    version = "ccenter 0.1",
    subcommands = {
        HealthCheck.class,
        Incidents.class,
        Users.class
    }
)
public class App implements Callable<Integer> {

    private static final TrustManager[] trustAllCerts = new TrustManager[] {
        new X509TrustManager() {
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return null;
            }
            public void checkClientTrusted(
                java.security.cert.X509Certificate[] certs, String authType) {
            }
            public void checkServerTrusted(
                java.security.cert.X509Certificate[] certs, String authType) {
            }
        }
    };

    public static void main(String[] args) {

        CommandLine commandLine = new CommandLine(new App());
        commandLine.setCaseInsensitiveEnumValuesAllowed(true);
        commandLine.setStopAtUnmatched(true);
        int exitCode = commandLine.execute(args);

        // If there's no sub-command, show the usage.
        if (commandLine.getParseResult().subcommand() == null) {
            commandLine.usage(System.out);
        }

        System.exit(exitCode);
    }

    /**
     * Helper method to create a new http client that can tolerate self-signed or improper ssl certificates.
     */
    static HttpClient newHttpClient() throws NoSuchAlgorithmException, KeyManagementException {

        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, trustAllCerts, new SecureRandom());

        return HttpClient.newBuilder().sslContext(sslContext).build();
    }

    @Override
    public Integer call() throws Exception {

        return 0;
    }

}
