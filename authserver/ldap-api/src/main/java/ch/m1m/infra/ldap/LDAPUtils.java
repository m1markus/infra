package ch.m1m.infra.ldap;

public class LDAPUtils {

    public static String removeLdapAttrPrefix(String input) {
        int pos = input.indexOf('=');
        if (pos == -1) {
            String errText = String.format("removeLdapAttrPrefix() char '=' not found in input %s", input);
            throw new IllegalStateException(errText);
        }
        String output = input.substring(pos + 1);
        return output;
    }
}
