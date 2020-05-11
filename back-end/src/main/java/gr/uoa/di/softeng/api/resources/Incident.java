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
public class Incident extends BaseResource {

    private final DataAccess dataAccess = Configuration.getInstance().getDataAccess();

    @Override
    protected Representation get() throws ResourceException {

        String incidentId = getMandatoryAttribute("incidentId", "incidentId is missing");

        // Read the optional query parameters
        Format format = parseFormat(getQueryValue("format"));

        try {
            gr.uoa.di.softeng.data.model.Incident fetchedIncident = dataAccess.fetchIncident(incidentId);
            return format.generateIncidentRepresentation(fetchedIncident);
        }
        catch (Exception e) {
            // Handle exception more carefully. Log error message. Do not externalize error cause by default.
            throw new ResourceException(Status.SERVER_ERROR_INTERNAL, e.getMessage(), e);
        }
    }

    @Override
    protected Representation put(Representation entity) throws ResourceException {

        String incidentId = getMandatoryAttribute("incidentId", "incidentId is missing");

        Form form = getRequest().getResourceRef().getQueryAsForm();
        String id           = form.getFirstValue("id");
        String title        = form.getFirstValue("title");
        String description  = form.getFirstValue("description");
        String x            = form.getFirstValue("x");
        String y            = form.getFirstValue("y");
        String startDate    = form.getFirstValue("start_date");
        String endDate      = form.getFirstValue("end_date");

        // assert(id == null || id == incidentId)

        // Read the optional query parameters
        Format format = parseFormat(getQueryValue("format"));

        try {
            gr.uoa.di.softeng.data.model.Incident updatedIncident =
                dataAccess.updateIncident(id, title, description, x, y, startDate, endDate);
            return format.generateIncidentRepresentation(updatedIncident);
        }
        catch (Exception e) {
            // Handle exception more carefully. Log error message. Do not externalize error cause by default.
            throw new ResourceException(Status.SERVER_ERROR_INTERNAL, e.getMessage(), e);
        }
    }

    @Override
    protected Representation delete() throws ResourceException {

        String incidentId = getMandatoryAttribute("incidentId", "incidentId is missing");

        try {
            dataAccess.deleteIncident(incidentId);
            return new JsonMapRepresentation(Map.of("status", "ok"));
        }
        catch (Exception e) {
            // Handle exception more carefully. Log error message. Do not externalize error cause by default.
            throw new ResourceException(Status.SERVER_ERROR_INTERNAL, e.getMessage(), e);
        }
    }

}
