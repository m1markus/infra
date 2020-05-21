package ch.m1m.infra.authserver;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MainView extends AppLayout {

    static private final Logger log = LoggerFactory.getLogger(MainView.class);

    public MainView() {
        setPrimarySection(AppLayout.Section.DRAWER);
        Image img = new Image("https://i.imgur.com/GPpnszs.png", "Vaadin Logo");
        img.setHeight("44px");
        addToNavbar(new DrawerToggle(), img);

        Tabs tabs = new Tabs();
        tabs.setOrientation(Tabs.Orientation.VERTICAL);
        addToDrawer(tabs);

        addNewMenuTab(tabs, "Home", HomeView.class);
        addNewMenuTab(tabs, "Login", LoginView.class);
        addNewMenuTab(tabs, "About", AboutView.class);

        log.info("Vaadin app is starting...");
    }

    private void addNewMenuTab(Tabs tabs, String menuText, Class clazz) {
        RouterLink linkHome = new RouterLink(null, clazz);
        //linkHome.add(VaadinIcon.ARROW_RIGHT.create());
        linkHome.add(menuText);
        Tab tab = new Tab();
        tab.add(linkHome);
        tabs.add(tab);
    }
}