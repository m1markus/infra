package ch.m1m.infra.ldap;

import com.unboundid.ldap.sdk.DN;
import com.unboundid.ldap.sdk.LDAPConnection;
import com.unboundid.ldap.sdk.LDAPException;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AuthProviderLdapTest {

    private static String ldapHost = "localhost";
    private static int ldapPort = 389;

    private static String ldapAdminUser = "cn=admin,dc=m1m,dc=ch";
    private static String ldapAdminPassword = "toSecret2beTrue,2022";

    private static String ldapBindDNTemplate = "cn=%s,ou=users,dc=m1m,dc=ch";

    AuthProviderLdap ldapProvider = new AuthProviderLdap(ldapHost, ldapPort,
            ldapAdminUser, ldapAdminPassword, ldapBindDNTemplate);

    /*
    @BeforeAll
    void initJustOnce() {
        ldapProvider = new AuthProviderLdap(ldapHost, ldapPort, ldapBindDNTemplate);
    }
    */

    @Test
    void givenCorrectPassword_whenCheckPassword_thenReturnSuccess() {

        // GIVEN
        String user = "mue";
        String password = "mue";

        // WHEN
        boolean isPasswordCorrect = ldapProvider.checkPassword(user, password);

        // THEN
        assertTrue(isPasswordCorrect);
    }

    @Test
    void givenWrongPassword_whenCheckPassword_thenReturnFalse() {

        // GIVEN
        String user = "mue";
        String password = "thisIsNotTheCorrectPassword";

        // WHEN
        boolean isPasswordCorrect = ldapProvider.checkPassword(user, password);

        // THEN
        assertFalse(isPasswordCorrect);
    }

    @Test
    void givenValidAdminCred_whenCallingGetAdminSession_thenGetAValidConnection() throws LDAPException {
        // GIVEN
        // WHEN
        LDAPConnection conn = ldapProvider.getAdminSession();

        // THEN
        assertNotNull(conn);
    }

    @Test
    void givenAUserWithGrups_whenCalligget_user_groups_session() throws LDAPException {

        // GIVEN
        // WHEN
        LDAPConnection conn = ldapProvider.getAdminSession();
        List<String> groupList = ldapProvider.readAllUserGroups("mue", conn);

        // THEN
        System.out.println("user 'mue' has groups: " + groupList);
        assertTrue(groupList.size() > 0);
    }
}
