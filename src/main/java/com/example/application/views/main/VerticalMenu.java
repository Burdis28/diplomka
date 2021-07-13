package com.example.application.views.main;

import com.example.application.data.entity.User;
import com.example.application.data.service.AuthService;
import com.example.application.utils.RouteNames;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentUtil;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.server.VaadinSession;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CssImport(themeFor = "vaadin-menu-bar", value = "./views/main/vertical-menu.css")
public class VerticalMenu extends VerticalLayout {

    private final AuthService authService;

    public VerticalMenu(AuthService authService) {
        this.authService = authService;
        MenuBar menuBar = new MenuBar();
        Map<String, Component> menuItems = createMenuItems();

        // Set menubar to be vertical with css
        menuBar.getElement().setAttribute("theme", "menu-vertical");

        menuBar.setWidth("220px");
        menuBar.addItem(menuItems.get(RouteNames.DASHBOARD.getName()));
        MenuItem sensorManagement = menuBar.addItem(createMenuManagementItem("Sensors management", VaadinIcon.LIST));
        sensorManagement.getSubMenu().addItem(menuItems.get(RouteNames.SENSORS.getName()));
        sensorManagement.getSubMenu().addItem(menuItems.get(RouteNames.SENSOR_CREATE.getName()));
        menuBar.addItem(menuItems.get(RouteNames.USER.getName()));
        menuBar.addItem("Sixth");
        menuBar.setOpenOnHover(true);

        // Adjust the opening position with JavaScript
        menuBar.getElement().executeJs("this._subMenu.addEventListener('opened-changed', function(e) {" +
                "const rootMenu = e.target;" +
                "const button = rootMenu._context.target;" +
                "if(!button) return;" +
                "const rect = button.getBoundingClientRect();" +
                "rootMenu.__x = rect.right;" +
                "rootMenu.__y = rect.top;" +
                "rootMenu.__alignOverlayPosition();" +
                "});");

        add(menuBar);
    }

    private Map<String, Component> createMenuItems() {
        // Využiju Vaadin session, získám usera a podle jeho role vytvořím jednotlivé taby, které může navštívit
        User user = VaadinSession.getCurrent().getAttribute(User.class);
        Map<String, Component> map = new HashMap<>();

        List<AuthService.AuthorizedRoute> menuRoutes = authService.getAuthorizedRoutes(user.getAdmin());

        for (AuthService.AuthorizedRoute route: menuRoutes) {
            map.putIfAbsent(route.name(), createMenuItem(route.name(), route.view(), route.icon()));
        }
        return map;
    }

    private static Component createMenuManagementItem(String text, VaadinIcon icon) {
        Div div = new Div();
        Icon iconObj = new Icon(icon);
        iconObj.setSize("25px");
        iconObj.setClassName("iconLeft");
        div.add(iconObj);
        Text menuText = new Text(text);
        div.add(menuText);
        div.add(VaadinIcon.ANGLE_RIGHT.create());
        return div;
    }

    private static Component createMenuItem(String text, Class<? extends Component> navigationTarget, VaadinIcon icon) {
        Div div = new Div();
        Icon iconObj = new Icon(icon);
        iconObj.setSize("25px");
        iconObj.setClassName("iconLeft");
        div.add(iconObj);
        div.add(new RouterLink(text, navigationTarget));
        ComponentUtil.setData(div, Class.class, navigationTarget);

        return div;
    }
}
