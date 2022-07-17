package ch.m1m.scan;

import java.util.Random;

import javax.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import io.smallrye.reactive.messaging.annotations.Blocking;
import org.jboss.logging.Logger;

/**
 * A bean consuming data from the "quote-requests" Kafka topic (mapped to "requests" channel) and giving out a random quote.
 * The result is pushed to the "quotes" Kafka topic.
 */
@ApplicationScoped
public class KQuoteProcessor {

    private static final Logger LOG = Logger.getLogger(KQuoteProcessor.class);

    @Incoming("requests")
    //@Outgoing("quotes")
    @Blocking
    public void process(String quoteRequestUUID) throws InterruptedException {
        // simulate some hard working task
        LOG.info("received kafa record with uuid=" + quoteRequestUUID);
        //Thread.sleep(200);
    }
}