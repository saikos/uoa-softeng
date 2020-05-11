package gr.uoa.di.softeng.api;

import gr.uoa.di.softeng.data.model.Limits;
import org.restlet.data.Status;
import org.restlet.resource.ResourceException;

/**
 *
 */
public abstract class PaginationResource extends BaseResource {

    protected Limits getLimitsFromQuery() {

        String startQueryValue = getQueryValue("start");
        Long start;
        try {
            // "startQueryValue" equals to "null" when the "start" query parameter has not been provided.
            start = startQueryValue == null ? null : Long.valueOf(startQueryValue);
        }
        catch(NumberFormatException ex) {
            throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST,
                "Invalid 'start' pagination query parameter value: " + startQueryValue
                + "\nMust be a positive long/integer.");
        }

        String countQueryValue = getQueryValue("count");
        Integer count;
        try {
            // "countQueryValue" equals to "null" when the "count" query parameter has not been provided.
            count = countQueryValue == null ? null : Integer.valueOf(countQueryValue);
        }
        catch(NumberFormatException ex) {
            throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST,
                "Invalid 'count' pagination query parameter value: " + countQueryValue
                    + "\nMust be a positive integer.");
        }

        return new Limits(start, count);
    }

}
