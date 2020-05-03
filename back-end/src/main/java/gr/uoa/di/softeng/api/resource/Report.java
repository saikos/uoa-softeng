package gr.uoa.di.softeng.api.resource;

import org.restlet.data.Status;
import org.restlet.representation.Representation;
import org.restlet.resource.ResourceException;

/**
 *
 */
public class Report extends BaseResource {

    @Override
    protected Representation get() throws ResourceException {

        String reportId = getMandatoryAttribute("reportId", "reportId is missing");

        // Return report data ...

        throw new ResourceException(Status.SERVER_ERROR_NOT_IMPLEMENTED);
    }

    @Override
    protected Representation put(Representation entity) throws ResourceException {

        String reportId = getMandatoryAttribute("reportId", "reportId is missing");

        // Update existing report data ...

        throw new ResourceException(Status.SERVER_ERROR_NOT_IMPLEMENTED);
    }

    @Override
    protected Representation delete() throws ResourceException {

        String reportId = getMandatoryAttribute("reportId", "reportId is missing");

        // Delete report data ...

        throw new ResourceException(Status.SERVER_ERROR_NOT_IMPLEMENTED);
    }

}
