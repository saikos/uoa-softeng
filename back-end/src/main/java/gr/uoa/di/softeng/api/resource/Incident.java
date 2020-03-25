package gr.uoa.di.softeng.api.resource;

import org.restlet.data.Status;
import org.restlet.representation.Representation;
import org.restlet.resource.ResourceException;

/**
 *
 */
public class Incident extends BaseResource {

    @Override
    protected Representation delete() throws ResourceException {

        String id = getMandatoryAttribute("id", "id is missing");

        // Delete incident record ...

        throw new ResourceException(Status.SERVER_ERROR_NOT_IMPLEMENTED);
    }

}
