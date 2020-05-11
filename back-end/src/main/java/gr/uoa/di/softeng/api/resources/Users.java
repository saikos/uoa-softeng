package gr.uoa.di.softeng.api.resources;

import gr.uoa.di.softeng.api.PaginationResource;
import gr.uoa.di.softeng.api.representation.Format;
import gr.uoa.di.softeng.conf.Configuration;
import gr.uoa.di.softeng.data.DataAccess;
import gr.uoa.di.softeng.data.model.Limits;
import org.restlet.data.Form;
import org.restlet.data.Status;
import org.restlet.representation.Representation;
import org.restlet.resource.ResourceException;

import java.util.List;

/**
 *
 */
public class Users extends PaginationResource {

    private final DataAccess dataAccess = Configuration.getInstance().getDataAccess();

    @Override
    protected Representation post(Representation entity) throws ResourceException {

        Form form = getRequest().getResourceRef().getQueryAsForm();
        String username  = form.getFirstValue("username");
        String password  = form.getFirstValue("password");
        String firstName = form.getFirstValue("firstName");
        String lastName  = form.getFirstValue("lastName");
        String role      = form.getFirstValue("role");
        String agency    = form.getFirstValue("agency");

        // Read the optional query parameters
        Format format = parseFormat(getQueryValue("format"));

        try {
            gr.uoa.di.softeng.data.model.User createdUser =
                dataAccess.createUser(username, password, firstName, lastName, role, agency);
            return format.generateUserRepresentation(createdUser);
        }
        catch (Exception e) {
            // Handle exception more carefully. Log error message. Do not externalize error cause by default.
            throw new ResourceException(Status.SERVER_ERROR_INTERNAL, e.getMessage(), e);
        }
    }

    @Override
    protected Representation get() throws ResourceException {

        // Read the optional query parameters
        Limits limits = getLimitsFromQuery();
        Format format = parseFormat(getQueryValue("format"));

        try {
            List<gr.uoa.di.softeng.data.model.User> fetchedUsers = dataAccess.fetchUsers(limits);
            return format.generateUsersRepresentation(fetchedUsers);
        }
        catch (Exception e) {
            // Handle exception more carefully. Log error message. Do not externalize error cause by default.
            throw new ResourceException(Status.SERVER_ERROR_INTERNAL, e.getMessage(), e);
        }
    }

}
