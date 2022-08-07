package ch.m1m.sprinkler;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;

import io.quarkus.runtime.ShutdownEvent;
import io.quarkus.runtime.StartupEvent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationScoped
public class AppLifecycleBean {

    private static Logger LOG = LoggerFactory.getLogger(AppLifecycleBean.class);

    void onStart(@Observes StartupEvent event, AppConfig appConfig) {

        appConfig.setDataDirectory("/tmp");

        LOG.info("The application is starting...");
        LOG.info("config DataDirectory is {}", appConfig.getDataDirectory());
    }

    void onStop(@Observes ShutdownEvent event) {
        LOG.info("The application is stopping...");
    }
}
