package ch.m1m.sprinkler;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;

import io.quarkus.runtime.ShutdownEvent;
import io.quarkus.runtime.StartupEvent;

import org.eclipse.microprofile.config.ConfigProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationScoped
public class AppLifecycleBean {

    private static final String SPRINKLER_DATA_DIR = "sprinkler.data.directory";

    private static Logger LOG = LoggerFactory.getLogger(AppLifecycleBean.class);

    void onStart(@Observes StartupEvent event, AppConfig appConfig) {

        LOG.info("The application is starting...");

        appConfig.setDataDirectory(getConfigValue(SPRINKLER_DATA_DIR));

        LOG.info("config DataDirectory is {}", appConfig.getDataDirectory());
    }

    void onStop(@Observes ShutdownEvent event) {
        LOG.info("The application is stopping...");
    }

    private String getConfigValue(String keyName) {
        String value = System.getProperty(keyName);
        if (value == null) {
            value = ConfigProvider.getConfig().getValue(keyName, String.class);
        }
        return value;
    }
}
