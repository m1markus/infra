package ch.m1m.sprinkler;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class AppConfig {

    private String dataDirectory;

    public String getDataDirectory() {
        return dataDirectory;
    }

    public void setDataDirectory(String dataDirectory) {
        this.dataDirectory = dataDirectory;
    }
}
