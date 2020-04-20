package ch.m1m.infra.authserver;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.router.Route;

@Route(value = "about", layout = MainView.class)
public class AboutView extends Div {
    public AboutView() {

        add(new Label("about label view..."));
    }
}
