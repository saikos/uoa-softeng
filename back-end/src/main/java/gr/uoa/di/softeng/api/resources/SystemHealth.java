package gr.uoa.di.softeng.api.resources;

import gr.uoa.di.softeng.api.BaseResource;
import gr.uoa.di.softeng.api.representation.JsonMapRepresentation;
import gr.uoa.di.softeng.conf.Configuration;
import gr.uoa.di.softeng.data.DataAccess;
import gr.uoa.di.softeng.data.DataAccessException;
import org.restlet.data.Status;
import org.restlet.representation.Representation;
import org.restlet.resource.ResourceException;
import java.util.Map;

/**
 *
 */
public class SystemHealth extends BaseResource {

    private final DataAccess dataAccess = Configuration.getInstance().getDataAccess();

    @Override
    protected Representation get() throws ResourceException {

        try {
            dataAccess.accessDataCheck();
            return new JsonMapRepresentation(Map.of("status", "OK"));
        }
        catch (DataAccessException e) {
            throw new ResourceException(Status.SERVER_ERROR_INTERNAL, "Data access exception: " + e.getMessage(), e);
        }
    }

}
