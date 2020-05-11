package gr.uoa.di.softeng.api.resources;

import gr.uoa.di.softeng.api.BaseResource;
import gr.uoa.di.softeng.api.representation.Format;
import gr.uoa.di.softeng.api.representation.JsonMapRepresentation;
import gr.uoa.di.softeng.conf.Configuration;
import gr.uoa.di.softeng.data.DataAccess;
import org.restlet.data.Form;
import org.restlet.data.Status;
import org.restlet.representation.Representation;
import org.restlet.resource.ResourceException;

import java.util.Map;

/**
 *
 */
public class User extends BaseResource {

    private final DataAccess dataAccess = Configuration.getInstance().getDataAccess();


    @Override
    protected Representation get() throws ResourceException {

        String userId = getMandatoryAttribute("userId", "userId is missing");

        // Read the optional query parameters
        Format format = parseFormat(getQueryValue("format"));

        try {
            gr.uoa.di.softeng.data.model.User fetchedUser = dataAccess.fetchUser(userId);
            return format.generateUserRepresentation(fetchedUser);
        }
        catch (Exception e) {
            // Handle exception more carefully. Log error message. Do not externalize error cause by default.
            throw new ResourceException(Status.SERVER_ERROR_INTERNAL, e.getMessage(), e);
        }
    }

    @Override
    protected Representation put(Representation entity) throws ResourceException {

        String userId = getMandatoryAttribute("userId", "userId is missing");

        Form form = getRequest().getResourceRef().getQueryAsForm();
        String username  = form.getFirstValue("username");
        String password  = form.getFirstValue("password");
        String firstName = form.getFirstValue("firstName");
        String lastName  = form.getFirstValue("lastName");
        String role      = form.getFirstValue("role");
        String agency    = form.getFirstValue("agency");

        // assert(username == null || username == userId)

        // Read the optional query parameters
        Format format = parseFormat(getQueryValue("format"));

        try {
            gr.uoa.di.softeng.data.model.User updatedUser =
                dataAccess.updateUser(userId, password, firstName, lastName, role, agency);
            return format.generateUserRepresentation(updatedUser);
        }
        catch (Exception e) {
            // Handle exception more carefully. Log error message. Do not externalize error cause by default.
            throw new ResourceException(Status.SERVER_ERROR_INTERNAL, e.getMessage(), e);
        }
    }

    @Override
    protected Representation delete() throws ResourceException {

        String userId = getMandatoryAttribute("userId", "userId is missing");

        try {
            dataAccess.deleteUser(userId);
            return new JsonMapRepresentation(Map.of("status", "ok"));
        }
        catch (Exception e) {
            // Handle exception more carefully. Log error message. Do not externalize error cause by default.
            throw new ResourceException(Status.SERVER_ERROR_INTERNAL, e.getMessage(), e);
        }
    }

}
