package gr.uoa.di.softeng.api.representation;

import gr.uoa.di.softeng.data.model.Incident;
import gr.uoa.di.softeng.data.model.User;
import org.restlet.representation.Representation;
import java.util.List;

/**
 *
 */
public interface RepresentationGenerator {

    Representation generateUserRepresentation(User user);
    Representation generateUsersRepresentation(List<User> users);

    Representation generateIncidentRepresentation(Incident incident);
    Representation generateIncidentsRepresentation(List<Incident> incidents);

}
