package ch.m1m.infra.authserver;

import com.unboundid.ldap.sdk.LDAPConnection;
import com.unboundid.ldap.sdk.LDAPException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AuthProviderLdap {

    static private final Logger log = LoggerFactory.getLogger(AuthProviderLdap.class);

    static private String ldapHost;
    static private int ldapPort;
    static private String ldapBindDNTemplate;

    public AuthProviderLdap(String host, int port, String bindDNTemplate) {
        ldapHost = host;
        ldapPort = port;
        ldapBindDNTemplate = bindDNTemplate;
    }

    public boolean checkPassword(String user, String password) {
        boolean result = false;
        LDAPConnection ldapConnection = null;
        String bindDN = String.format(ldapBindDNTemplate, user);

        String logPassword = password;
        // FIXME: encript log password
        log.info("Checking password for bindDN={} password={} at host={} port={}",
                bindDN, logPassword, ldapHost, ldapPort);

        try {
            ldapConnection = new LDAPConnection(ldapHost, ldapPort);
            ldapConnection.bind(bindDN, password);
            result = true;

        } catch (LDAPException e) {

            log.error("LDAPException during checkPassword(): {}", e.getMessage());

        } finally {
            if (ldapConnection != null)
                ldapConnection.close();
        }

        log.info("Checking password returned: {}", result);
        return result;
    }
}
