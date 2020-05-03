package gr.uoa.di.softeng.api.resource;

import org.restlet.data.Status;
import org.restlet.representation.Representation;
import org.restlet.resource.ResourceException;

/**
 *
 */
public class User extends BaseResource {

    @Override
    protected Representation get() throws ResourceException {

        String userId = getMandatoryAttribute("userId", "userId is missing");

        // Return user data ...

        throw new ResourceException(Status.SERVER_ERROR_NOT_IMPLEMENTED);
    }

    @Override
    protected Representation put(Representation entity) throws ResourceException {

        String userId = getMandatoryAttribute("userId", "userId is missing");

        // Update existing user record ...

        throw new ResourceException(Status.SERVER_ERROR_NOT_IMPLEMENTED);
    }

    @Override
    protected Representation delete() throws ResourceException {

        String userId = getMandatoryAttribute("userId", "userId is missing");

        // Delete user record ...

        throw new ResourceException(Status.SERVER_ERROR_NOT_IMPLEMENTED);
    }

}
