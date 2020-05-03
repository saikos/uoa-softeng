package gr.uoa.di.softeng.api.resource;

import org.restlet.data.Status;
import org.restlet.representation.Representation;
import org.restlet.resource.ResourceException;

/**
 *
 */
public class Reports extends BaseResource {

    @Override
    protected Representation post(Representation entity) throws ResourceException {

        // Create a new report record ...

        throw new ResourceException(Status.SERVER_ERROR_NOT_IMPLEMENTED);
    }

    @Override
    protected Representation get() throws ResourceException {

        // Read the optional query parameters
        String start = getQueryValue("start");
        String count = getQueryValue("count");
        // Create a "Limits" object based on "start" and "count".

        // Return a list of reports ...

        throw new ResourceException(Status.SERVER_ERROR_NOT_IMPLEMENTED);
    }

}
