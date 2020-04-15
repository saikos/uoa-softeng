package gr.uoa.di.softeng.api.resource;

import gr.uoa.di.softeng.api.representation.Format;
import gr.uoa.di.softeng.conf.Configuration;
import gr.uoa.di.softeng.data.DataAccess;
import gr.uoa.di.softeng.data.Incident;
import org.restlet.data.Status;
import org.restlet.representation.Representation;
import org.restlet.resource.ResourceException;
import org.restlet.data.Form;

import java.util.List;

/**
 *
 */
public class Incidents extends BaseResource {

    private final DataAccess dataAccess = Configuration.getInstance().getDataAccess();

    @Override
    protected Representation get() throws ResourceException {

        // Read the optional name attribute
        String name = getQueryValue("name");

        // Read the format query parameter
        Format format = parseFormat(getQueryValue("format"));

        try {
            List<Incident> results = dataAccess.fetchIncidents(name);
            return format.generateRepresentation(results);
        } catch (Exception e) {
            throw new ResourceException(Status.SERVER_ERROR_INTERNAL, e.getMessage(), e);
        }
    }

    @Override
    protected Representation post(Representation entity) throws ResourceException {

        Form form = getRequest().getResourceRef().getQueryAsForm();
        String id = form.getFirstValue("profile");
        String name = form.getFirstValue("name");
        String description = form.getFirstValue("description");
        String creationDate = form.getFirstValue("creation_date");

        // Create a new incident record ...

        throw new ResourceException(Status.SERVER_ERROR_NOT_IMPLEMENTED);
    }

}
