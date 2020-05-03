package gr.uoa.di.softeng.api.resource;

import org.restlet.data.Status;
import org.restlet.representation.Representation;
import org.restlet.resource.ResourceException;

/**
 *
 */
public class Incident extends BaseResource {

    @Override
    protected Representation get() throws ResourceException {

        String incidentId = getMandatoryAttribute("incidentId", "incidentId is missing");

        // Return incident record ...

        throw new ResourceException(Status.SERVER_ERROR_NOT_IMPLEMENTED);
    }

    @Override
    protected Representation delete() throws ResourceException {

        String incidentId = getMandatoryAttribute("incidentId", "incidentId is missing");

        // Delete incident record ...

        throw new ResourceException(Status.SERVER_ERROR_NOT_IMPLEMENTED);
    }

}
