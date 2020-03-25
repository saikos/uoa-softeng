package gr.uoa.di.softeng20b.api.resource;

import org.restlet.data.Status;
import org.restlet.representation.Representation;
import org.restlet.resource.ResourceException;

/**
 *
 */
public class Reset extends BaseResource {

    @Override
    protected Representation post(Representation entity) throws ResourceException {

        // Reset the database ...

        throw new ResourceException(Status.SERVER_ERROR_NOT_IMPLEMENTED);
    }

}
