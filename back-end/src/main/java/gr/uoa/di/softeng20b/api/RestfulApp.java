package gr.uoa.di.softeng20b.api;

import gr.uoa.di.softeng20b.api.resource.*;
import org.restlet.Application;
import org.restlet.Restlet;
import org.restlet.routing.Router;

/**
 *
 */
public class RestfulApp extends Application {

    @Override
    public synchronized Restlet createInboundRoot() {

        Router router = new Router(getContext());

        // Perform a heath check.
        router.attach("/health-check", SystemHealth.class);

        // Init a new (empty) database with the default admin user.
        router.attach("/reset", Reset.class);

        // CRUD actions on "incidents" resource.
        router.attach("/incidents", Incidents.class);

        // CRUD actions on "incident" resource.
        router.attach("/incidents", Incident.class);

        return router;
    }

}
