package gr.uoa.di.softeng.api.resource;

import gr.uoa.di.softeng.api.representation.Format;
import org.restlet.data.Status;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Optional;

/**
 *
 */
public class BaseResource extends ServerResource {

    private static final String EMPTY_STRING = "";

    static Format parseFormat(String format) {

        Optional<Format> optional = Arrays.stream(Format.values()).
            filter((Format f) -> f.name().equalsIgnoreCase(format)).
            findFirst();

        return optional.orElse(Format.JSON);
    }

    protected String getAttributeDecoded(String attr) {

        String value = getAttribute(attr);

        return value == null ?  null : sanitize(URLDecoder.decode(value, StandardCharsets.UTF_8));
    }

    protected String getMandatoryAttribute(String attr, String message) {

        String value = getAttributeDecoded(attr);

        if (value.length() == 0) {
            throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, message);
        }

        return value;
    }

    static String sanitize(String s) {

        return s == null ? EMPTY_STRING : s.trim();
    }

}
