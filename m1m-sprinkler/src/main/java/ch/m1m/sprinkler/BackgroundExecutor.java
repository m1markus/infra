package ch.m1m.sprinkler;

import io.quarkus.runtime.ShutdownEvent;
import io.quarkus.runtime.StartupEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

@ApplicationScoped
public class BackgroundExecutor implements Runnable {

    private static Logger LOG = LoggerFactory.getLogger(BackgroundExecutor.class);

    private AtomicBoolean isRunning = new AtomicBoolean(true);
    private Thread bgThread;

    @Inject
    StateManager stateManager;

    void onStart(@Observes StartupEvent event) {
        LOG.info("starting background thread...");

        bgThread = new Thread(this);
        bgThread.start();
    }

    void onStop(@Observes ShutdownEvent event) throws InterruptedException {
        isRunning.set(false);
        LOG.info("stopping background thread...");
    }

    public void run() {
        while (isRunning.get()) {
            LOG.info("executing run() from background thread, stateManager is: " + stateManager);
            try {
                stateManager.updatePipeState();
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        LOG.info("exiting run() from background thread");
    }
}
