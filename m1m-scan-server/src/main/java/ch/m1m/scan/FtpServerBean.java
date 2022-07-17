package ch.m1m.scan;

import io.quarkus.runtime.Startup;
import org.apache.ftpserver.FtpServer;
import org.apache.ftpserver.FtpServerFactory;
import org.apache.ftpserver.ftplet.FtpException;
import org.apache.ftpserver.listener.ListenerFactory;
import org.apache.ftpserver.usermanager.PropertiesUserManagerFactory;
import org.jboss.logging.Logger;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import java.io.File;

// from: https://mina.apache.org/ftpserver-project/

@Startup
@ApplicationScoped
public class FtpServerBean {

    private static final Logger LOG = Logger.getLogger(FtpServerBean.class);

    // user:     anonymous
    // password: me@m1m.ch
    @PostConstruct
    public void onInit() {
        int ftpListeningPort = 2221;

        LOG.info("FtpServerBean config begin...");

        FtpServerFactory serverFactory = new FtpServerFactory();
        ListenerFactory listenerFactory = new ListenerFactory();
        PropertiesUserManagerFactory userManagerFactory = new PropertiesUserManagerFactory();

        userManagerFactory.setFile(new File("ftpusers.properties"));
        serverFactory.setUserManager(userManagerFactory.createUserManager());

        // set the port of the listener
        listenerFactory.setPort(ftpListeningPort);

        // replace the default listener
        serverFactory.addListener("default", listenerFactory.createListener());

        // start the server
        FtpServer server = serverFactory.createServer();
        try {
            server.start();
            LOG.info("ftp server is listening on port " + ftpListeningPort);
        } catch (FtpException e) {
            LOG.error("failed to start ftp server", e);
        }
    }
}
