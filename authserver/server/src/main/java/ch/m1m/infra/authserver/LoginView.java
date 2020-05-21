package ch.m1m.infra.authserver;

import ch.m1m.infra.ldap.AuthProviderLdap;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.login.LoginOverlay;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Route(value = "login", layout = MainView.class)
@PageTitle("Login")
public class LoginView extends VerticalLayout {

    static private final Logger log = LoggerFactory.getLogger(LoginView.class);

    private static AuthProviderLdap authProvider;

    private LoginOverlay loginOverlay = new LoginOverlay();

    public LoginView() {

        // FIXME: read config values from file
        String ldapHost = "localhost";
        int ldapPort = 389;
        String ldapBindDNTemplate = "cn=%s,ou=users,dc=m1m,dc=ch";
        authProvider = new AuthProviderLdap(ldapHost, ldapPort, ldapBindDNTemplate);

        this.loginOverlay.setOpened(true);
        this.loginOverlay.setForgotPasswordButtonVisible(false);
        //this.loginOverlay.setDescription(null);
        this.loginOverlay.setEnabled(true);
        this.loginOverlay.setTitle("my login");

        this.loginOverlay.addLoginListener(e -> {
            String user = e.getUsername();
            String password = e.getPassword();

            if (authProvider.checkPassword(user, password)) {
                this.loginOverlay.close();
                UI.getCurrent().getSession().setAttribute("auth.user", user);
                UI.getCurrent().navigate("home");
            } else {
                this.loginOverlay.setError(true);
                //UI.getCurrent().getSession().close();
            }
        });

        this.add(loginOverlay);
    }
}
