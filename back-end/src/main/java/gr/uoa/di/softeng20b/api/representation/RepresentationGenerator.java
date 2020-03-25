package gr.uoa.di.softeng20b.api.representation;

import gr.uoa.di.softeng20b.data.Incident;
import org.restlet.representation.Representation;
import java.util.List;

/**
 *
 */
public interface RepresentationGenerator {

    Representation generateRepresentation(List<Incident> results);

}
