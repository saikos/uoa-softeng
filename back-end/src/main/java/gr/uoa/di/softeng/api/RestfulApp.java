package gr.uoa.di.softeng.api;

import gr.uoa.di.softeng.api.resources.*;
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

        // You should make the user-related REST endpoints available to admin users only (i.e. users with the admin role).
        // CRUD actions on "users" resource.
        router.attach("/admin/users", Users.class);
        // CRUD actions on "user" resource.
        router.attach("/admin/users/{userId}", User.class);

        // CRUD actions on "incidents" resource.
        router.attach("/incidents", Incidents.class);
        // CRUD actions on "incident" resource.
        router.attach("/incidents/{incidentId}", Incident.class);

        ////////////////////////////////////////////////////////////////////////////////////////////////
        // Enable CORS for all origins (don't use this in a production service).

        CorsFilter corsFilter = new CorsFilter(getContext(), router);
        corsFilter.setAllowedOrigins(Set.of("*"));
        corsFilter.setAllowedCredentials(true);
        corsFilter.setAllowedHeaders(Set.of("X-CONTROL-CENTER-AUTH"));
        corsFilter.setDefaultAllowedMethods(Set.of(Method.GET, Method.PUT, Method.POST, Method.DELETE));
        corsFilter.setAllowingAllRequestedHeaders(true);
        corsFilter.setSkippingResourceForCorsOptions(true);
        corsFilter.setMaxAge(10);

        return corsFilter;
    }

}
