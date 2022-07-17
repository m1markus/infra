package ch.m1m.scan;

import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.jboss.logging.Logger;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.UUID;

@Path("/hello")
public class GreetingResource {

    private static final Logger LOG = Logger.getLogger(GreetingResource.class);

    // https://docs.redpanda.com/docs/quickstart/quick-start-docker/
    @Channel("quote-requests")
    Emitter<String> quoteRequestEmitter;

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {
        int i = 10;
        int ii = 15;
        int iii = i + ii;

        LOG.info("REST /hello got called");

        UUID uuid = UUID.randomUUID();
        String uuidAsString = uuid.toString();
        quoteRequestEmitter.send(uuidAsString);

        LOG.info

        return "Hello from RESTEasy Reactive kafka producer uuid=" + uuidAsString;
    }
}