package gr.uoa.di.softeng.api.resources;

import gr.uoa.di.softeng.api.BaseResource;
import gr.uoa.di.softeng.api.representation.JsonMapRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.ResourceException;

import java.util.Collections;

/**
 *
 */
public class Logout extends BaseResource {

    @Override
    protected Representation post(Representation entity) throws ResourceException {

        // This is a dummy login implementation for the sake of the front-end example app.

        return new JsonMapRepresentation(Collections.emptyMap());
    }

}
