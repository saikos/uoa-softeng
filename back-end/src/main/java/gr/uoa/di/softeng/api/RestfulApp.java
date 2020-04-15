package gr.uoa.di.softeng.api;

import gr.uoa.di.softeng.api.resource.*;
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

        // Authenticate the user.
        router.attach("/login", Login.class);

        // This endpoint should be available to all authenticated users.
        router.attach("/logout", Logout.class);

        // CRUD actions on "incidents" resource.
        router.attach("/incidents", Incidents.class);
        router.attach("/incidents/{name}", Incidents.class);

        // CRUD actions on "incident" resource.
        router.attach("/incident/{id}", Incident.class);

        return router;
    }

}
