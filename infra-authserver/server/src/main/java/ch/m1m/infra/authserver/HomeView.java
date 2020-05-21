package ch.m1m.infra.authserver;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.NativeButton;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

@Route(value = "home", layout = MainView.class)
@RouteAlias(value = "", layout = MainView.class)
public class HomeView extends Div {

    static private final Logger log = LoggerFactory.getLogger(HomeView.class);

    private Label label1;

    @Inject
    DummyComponent textComponent;

    public HomeView() {
        label1 = new Label("HOME view: YOU WILL NEVER SEE THIS TEXT (it get's replaced)");
        add(label1);
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {

        log.info("HomeView#onAttach() running");

        //Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        //log.info("Spring auth: {}", auth);

        String preText = textComponent.toString();

        String authUser = (String) UI.getCurrent().getSession().getAttribute("auth.user");
        if (authUser == null) {
            label1.setText(preText + "nobody");
        } else {
            label1.setText(preText + authUser);
        }

        Label content = new Label("This notification hides after 5 seconds");
        NativeButton buttonInside = new NativeButton("Click me");
        Notification notification = new Notification(content, buttonInside);
        notification.setDuration(5_000);
        notification.open();
    }

    @Override
    protected void onDetach(DetachEvent event) {

    }
}