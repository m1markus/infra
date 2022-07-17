package ch.m1m.model;

import java.util.Random;

import javax.enterprise.context.ApplicationScoped;

import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Outgoing;

import io.smallrye.reactive.messaging.annotations.Blocking;

/**
 * A bean consuming data from the "quote-requests" Kafka topic (mapped to "requests" channel) and giving out a random quote.
 * The result is pushed to the "quotes" Kafka topic.
 */
@ApplicationScoped
public class QuotesConsumer {

    private Random random = new Random();

    @Incoming("quote-created")
    //@Outgoing("quotes")
    @Blocking
    public Quote process(String quoteRequestId) throws InterruptedException {
        // simulate some hard working task
        //Thread.sleep(200);
        System.out.println("TRACE consumed Kafka message..." + quoteRequestId);
        return new Quote(quoteRequestId, random.nextInt(100));
    }
}