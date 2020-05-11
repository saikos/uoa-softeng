package gr.uoa.di.softeng.api.resources;

import gr.uoa.di.softeng.api.PaginationResource;
import gr.uoa.di.softeng.api.representation.Format;
import gr.uoa.di.softeng.conf.Configuration;
import gr.uoa.di.softeng.data.DataAccess;
import gr.uoa.di.softeng.data.model.Incident;
import gr.uoa.di.softeng.data.model.Limits;
import org.restlet.data.Status;
import org.restlet.representation.Representation;
import org.restlet.resource.ResourceException;
import org.restlet.data.Form;

import java.util.List;

/**
 *
 */
public class Incidents extends PaginationResource {

    private final DataAccess dataAccess = Configuration.getInstance().getDataAccess();

    @Override
    protected Representation post(Representation entity) throws ResourceException {

        Form form = getRequest().getResourceRef().getQueryAsForm();
        String title        = form.getFirstValue("title");
        String description  = form.getFirstValue("description");
        String x            = form.getFirstValue("x");
        String y            = form.getFirstValue("y");
        String startDate    = form.getFirstValue("start_date");
        String endDate      = form.getFirstValue("end_date");

        // Read the optional query parameters
        Format format = parseFormat(getQueryValue("format"));

        try {
            gr.uoa.di.softeng.data.model.Incident createdIncident =
                dataAccess.createIncident(title, description, x, y, startDate, endDate);
            return format.generateIncidentRepresentation(createdIncident);
        }
        catch (Exception e) {
            // Handle exception more carefully. Log error message. Do not externalize error cause by default.
            throw new ResourceException(Status.SERVER_ERROR_INTERNAL, e.getMessage(), e);
        }
    }

    @Override
    protected Representation get() throws ResourceException {

        // Read the optional query parameters
        String title = getQueryValue("title");
        Limits limits = getLimitsFromQuery();
        Format format = parseFormat(getQueryValue("format"));

        try {
            List<Incident> results = dataAccess.fetchIncidents(title, limits);
            return format.generateIncidentsRepresentation(results);
        }
        catch (Exception e) {
            // Handle exception more carefully. Log error message. Do not externalize error cause by default.
            throw new ResourceException(Status.SERVER_ERROR_INTERNAL, e.getMessage(), e);
        }
    }

}
