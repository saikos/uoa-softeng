package gr.uoa.di.softeng.api.resource;

import gr.uoa.di.softeng.api.representation.Format;
import gr.uoa.di.softeng.conf.Configuration;
import gr.uoa.di.softeng.data.DataAccess;
import gr.uoa.di.softeng.data.model.Incident;
import gr.uoa.di.softeng.data.model.Limits;
import org.restlet.data.Status;
import org.restlet.representation.Representation;
import org.restlet.resource.ResourceException;
import org.restlet.data.Form;

import java.util.ArrayList;//!!!!
import java.util.List;

/**
 *
 */
public class Incidents extends BaseResource {

    private final DataAccess dataAccess = Configuration.getInstance().getDataAccess();

    @Override
    protected Representation get() throws ResourceException {

        // Read the optional query parameters
        String title = getQueryValue("title");
        Format format = parseFormat(getQueryValue("format"));

        // Read the optional query parameters
        String start = getQueryValue("start");
        String count = getQueryValue("count");
        // Create a "Limits" object based on "start" and "count".
        Limits limits = new Limits();

        try {
            /*!!!!
            List<Incident> results = dataAccess.fetchIncidents(title, limits);
            return format.generateRepresentation(results);
            */
            return format.generateRepresentation(new ArrayList<Incident>());
        } catch (Exception e) {
            throw new ResourceException(Status.SERVER_ERROR_INTERNAL, e.getMessage(), e);
        }
    }

    @Override
    protected Representation post(Representation entity) throws ResourceException {

        Form form = getRequest().getResourceRef().getQueryAsForm();
        String name         = form.getFirstValue("name");
        String description  = form.getFirstValue("description");
        String creationDate = form.getFirstValue("creation_date");
        String x            = form.getFirstValue("x");
        String y            = form.getFirstValue("y");

        // Create a new incident record ...

        throw new ResourceException(Status.SERVER_ERROR_NOT_IMPLEMENTED);
    }

}
