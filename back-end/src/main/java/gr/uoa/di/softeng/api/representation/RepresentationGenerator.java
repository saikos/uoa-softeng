package gr.uoa.di.softeng.api.representation;

import gr.uoa.di.softeng.data.model.Incident;
import org.restlet.representation.Representation;
import java.util.List;

/**
 *
 */
public interface RepresentationGenerator {

    Representation generateRepresentation(List<Incident> results);

}
