package gr.uoa.di.softeng.api.representation;

import com.google.gson.stream.JsonWriter;
import gr.uoa.di.softeng.data.Incident;
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

        public Representation generateRepresentation(List<Incident> results) {

            return new CustomJsonRepresentation((JsonWriter w) -> {
                try {
                    w.beginArray(); // [
                    for(Incident incident: results) {
                        w.beginObject(); // {
                        w.name("id").value(incident.getId());
                        w.name("name").value(incident.getName());
                        w.name("description").value(incident.getDescription());
                        w.name("creation_date").value(incident.getCreationDate().toString());
                        w.endObject(); // }
                        w.flush();
                    }
                    w.endArray(); // ]
                } catch (IOException e) {
                    throw new ResourceException(Status.SERVER_ERROR_INTERNAL);
                }
            });
        }
    },

    CSV {

        public Representation generateRepresentation(List<Incident> results) {

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
