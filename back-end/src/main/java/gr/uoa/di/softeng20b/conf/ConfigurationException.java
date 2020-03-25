package gr.uoa.di.softeng20b.conf;

/**
 *
 */
public class ConfigurationException extends RuntimeException {

    public ConfigurationException(String msg) {

        super(msg);
    }

    public ConfigurationException(String msg, Throwable cause) {

        super(msg, cause);
    }

}
