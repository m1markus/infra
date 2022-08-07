package ch.m1m.sprinkler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class AppConfig {

    private static Logger LOG = LoggerFactory.getLogger(AppConfig.class);

    private String dataDirectory;

    public String getDataDirectory() {
        return dataDirectory;
    }

    public void setDataDirectory(String dataDirectory) {
        LOG.info("setting DataDirectory to: {}", dataDirectory);
        this.dataDirectory = dataDirectory;
    }
}
