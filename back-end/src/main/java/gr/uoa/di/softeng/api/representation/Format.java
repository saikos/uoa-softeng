package gr.uoa.di.softeng.api.representation;

import com.google.gson.stream.JsonWriter;
import gr.uoa.di.softeng.data.model.Incident;
import gr.uoa.di.softeng.data.model.User;
import org.restlet.data.MediaType;
import org.restlet.data.Status;
import org.restlet.representation.Representation;
import org.restlet.representation.WriterRepresentation;
import org.restlet.resource.ResourceException;
import java.io.IOException;
import java.io.Writer;
import java.util.List;
import java.util.function.Consumer;

/**
 *
 */
public enum Format implements RepresentationGenerator {

    JSON {

        public Representation generateUserRepresentation(User user) {

            return new CustomJsonRepresentation((JsonWriter w) -> writeUser(w, user));
        }

        public Representation generateUsersRepresentation(List<User> users) {

            return new CustomJsonRepresentation((JsonWriter w) -> {
                try {
                    w.beginArray(); // [
                    for (User user: users) {
                        writeUser(w, user);
                    }
                    w.endArray(); // ]
                } catch (IOException e) {
                    throw new ResourceException(Status.SERVER_ERROR_INTERNAL);
                }
            });
        }

        public Representation generateIncidentRepresentation(Incident incident) {

            return new CustomJsonRepresentation((JsonWriter w) -> writeIncident(w, incident));
        }

        public Representation generateIncidentsRepresentation(List<Incident> incidents) {

            return new CustomJsonRepresentation((JsonWriter w) -> {
                try {
                    w.beginArray(); // [
                    for (Incident incident: incidents) {
                        writeIncident(w, incident);
                    }
                    w.endArray(); // ]
                }
                catch (IOException e) {
                    throw new ResourceException(Status.SERVER_ERROR_INTERNAL);
                }
            });
        }

        private void writeUser(JsonWriter w, User user) {

            try {
                w.beginObject(); // {
                w.name("username").value(user.getUsername());
                w.name("first_name").value(user.getFirstName());
                w.name("last_name").value(user.getLastName());
                w.name("role").value(user.getRole());
                w.name("agency").value(user.getAgency());
                w.endObject(); // }
                w.flush();
            }
            catch (IOException e) {
                throw new ResourceException(Status.SERVER_ERROR_INTERNAL);
            }
        }

        private void writeIncident(JsonWriter w, Incident incident) {

            try {
                w.beginObject(); // {
                w.name("id").value(incident.getId());
                w.name("title").value(incident.getTitle());
                w.name("description").value(incident.getDescription());
                w.name("x").value(incident.getX().toString());
                w.name("y").value(incident.getY().toString());
                w.name("start_date").value(incident.getStartDate() == null ? null : incident.getStartDate().toString());
                w.name("end_date").value(incident.getEndDate() == null ? null : incident.getEndDate().toString());
                w.endObject(); // }
                w.flush();
            }
            catch (IOException e) {
                throw new ResourceException(Status.SERVER_ERROR_INTERNAL);
            }
        }
    },

    XML {

        public Representation generateUserRepresentation(User user) {

            throw new UnsupportedOperationException("Implement this ...");
        }

        public Representation generateUsersRepresentation(List<User> users) {

            throw new UnsupportedOperationException("Implement this ...");
        }

        public Representation generateIncidentRepresentation(Incident incident) {

            throw new UnsupportedOperationException("Implement this ...");
        }

        public Representation generateIncidentsRepresentation(List<Incident> incidents) {

            throw new UnsupportedOperationException("Implement this ...");
        }
    };

    /**
     *
     */
    private static final class CustomJsonRepresentation extends WriterRepresentation {

        private final Consumer<JsonWriter> consumer;

        CustomJsonRepresentation(Consumer<JsonWriter> consumer) {

            super(MediaType.APPLICATION_JSON);

            this.consumer = consumer;
        }

        @Override
        public void write(Writer writer) throws NullPointerException {

            consumer.accept(new JsonWriter(writer));
        }

    }

}
