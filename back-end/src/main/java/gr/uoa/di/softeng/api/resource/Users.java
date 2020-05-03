package gr.uoa.di.softeng.api.resource;

import org.restlet.data.Form;
import org.restlet.data.Status;
import org.restlet.representation.Representation;
import org.restlet.resource.ResourceException;

/**
 *
 */
public class Users extends BaseResource {

    @Override
    protected Representation post(Representation entity) throws ResourceException {

        Form form = getRequest().getResourceRef().getQueryAsForm();
        String username  = form.getFirstValue("username");
        String password  = form.getFirstValue("password");
        String firstName = form.getFirstValue("firstName");
        String lastName  = form.getFirstValue("lastName");
        String role      = form.getFirstValue("role");
        String agency    = form.getFirstValue("agency");

        // Create a new user record ...

        throw new ResourceException(Status.SERVER_ERROR_NOT_IMPLEMENTED);
    }

    @Override
    protected Representation get() throws ResourceException {

        // Read the optional query parameters
        String start = getQueryValue("start");
        String count = getQueryValue("count");
        // Create a "Limits" object based on "start" and "count".

        // Return a list of users ...

        throw new ResourceException(Status.SERVER_ERROR_NOT_IMPLEMENTED);
    }

}
