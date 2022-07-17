package ch.m1m.sprinkler;

import ch.m1m.sprinkler.api.SprinklerAppState;
import ch.m1m.sprinkler.api.SprinklerState;
import ch.m1m.sprinkler.api.Version;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/api")
public class ApiResource {

    private static Logger LOG = LoggerFactory.getLogger(ApiResource.class);

    @Inject
    StateManager stateManager;

    @GET
    @Path("version")
    @Produces(MediaType.APPLICATION_JSON)
    public Version getVersion() {
        String vString = "0.0.1";
        Version version = new Version();
        version.setVersion(vString);
        LOG.info("getVersion returned {}", vString);
        return version;
    }

    @GET
    @Path("state")
    @Produces(MediaType.APPLICATION_JSON)
    public SprinklerAppState getSprinklerAppState() {
        return stateManager.getSprinklerAppState();
    }

    @GET
    @Path("state/pipe")
    @Produces(MediaType.APPLICATION_JSON)
    public SprinklerState getSprinklerState() {
        return stateManager.getSprinklerState();
    }
}
