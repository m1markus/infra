package ch.m1m.infra.ldap;

import com.unboundid.ldap.sdk.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class AuthProviderLdap {

    private static final Logger log = LoggerFactory.getLogger(AuthProviderLdap.class);

    private String ldapHost;
    private int ldapPort;

    private String ldapAdminUser;
    private String ldapAdminPassword;

    private String ldapBindDNTemplate;

    public AuthProviderLdap(String host, int port,
                            String adminUser, String adminPassword,
                            String bindDNTemplate) {
        ldapHost = host;
        ldapPort = port;
        ldapAdminUser = adminUser;
        ldapAdminPassword = adminPassword;
        ldapBindDNTemplate = bindDNTemplate;
    }

    public boolean checkPassword(String user, String password) {
        boolean result = false;
        LDAPConnection ldapConnection = null;
        String bindDN = String.format(ldapBindDNTemplate, user);

        String logPassword = password;
        // FIXME: encrypt log password
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

    LDAPConnection getAdminSession() throws LDAPException {
        LDAPConnection ldapConnection = null;

        ldapConnection = new LDAPConnection(ldapHost, ldapPort);
        ldapConnection.bind(ldapAdminUser, ldapAdminPassword);

        return ldapConnection;
    }

    // ldapsearch -x -H ldap://localhost -b dc=m1m,dc=ch -D "cn=admin,dc=m1m,dc=ch" -w toSecret2beTrue,2022
    //            "(&(objectClass=inetOrgPerson)(cn=sam))" memberOf
    //
    public List<String> readAllUserGroups(String user) throws LDAPException {
        List<String> groupList = new ArrayList<>();

        String ldapFilter = String.format("(&(objectClass=inetOrgPerson)(cn=%s))", user);
        Filter filter = Filter.create(ldapFilter);
        String baseDN = "ou=Users,dc=m1m,dc=ch";

        SearchRequest searchRequest = new SearchRequest(baseDN,
                SearchScope.SUB, filter, "memberOf");

        LDAPConnection conn = getAdminSession();
        SearchResult searchResult = conn.search(searchRequest);
        ResultCode rc = searchResult.getResultCode();

        log.info("LDAP filter={} ResultCode={} EntryCount={}",
                ldapFilter, rc.getName(), searchResult.getEntryCount());

        for (SearchResultEntry entry : searchResult.getSearchEntries()) {
            for (Attribute attr : entry.getAttributes()) {
                if ("memberOf".equals(attr.getName())) {
                    String[] values = attr.getValues();
                    for (String groupString : values) {
                        String groupWithRemovedCN = LDAPUtils.removeLdapAttrPrefix(groupString);
                        groupList.add(groupWithRemovedCN);

                        //DN dn = new DN(groupString);
                        //if (dn != null) groupList.add(dn);
                    }
                }
            }
        }

        return groupList;
    }
}
