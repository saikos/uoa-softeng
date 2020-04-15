package gr.uoa.di.softeng.api;

import gr.uoa.di.softeng.api.resource.*;
import org.restlet.Application;
import org.restlet.Restlet;
import org.restlet.data.Method;
import org.restlet.engine.application.CorsFilter;
import org.restlet.routing.Router;

import java.util.Set;

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

        // CRUD actions on "incident" resource.
        router.attach("/incidents/{id}", Incident.class);

        ////////////////////////////////////////////////////////////////////////////////////////////////
        // Enable CORS for all origins (don't use this in a production service).

        CorsFilter corsFilter = new CorsFilter(getContext(), router);
        corsFilter.setAllowedOrigins(Set.of("*"));
        corsFilter.setAllowedCredentials(true);
        corsFilter.setAllowedHeaders(Set.of("X-OBSERVATORY-AUTH"));
        corsFilter.setDefaultAllowedMethods(Set.of(Method.GET, Method.PUT, Method.POST, Method.DELETE));
        corsFilter.setAllowingAllRequestedHeaders(true);
        corsFilter.setSkippingResourceForCorsOptions(true);
        corsFilter.setMaxAge(10);

        return corsFilter;
    }

}
