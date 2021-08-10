package com.example.application.views.login;

import com.example.application.data.entity.User;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteConfiguration;
import com.vaadin.flow.router.RouteData;
import com.vaadin.flow.server.VaadinSession;

import java.util.List;

@PageTitle("Logout")
@Tag("logout")
public class LogoutView extends Composite<VerticalLayout> {

    public LogoutView() {
        ConfirmDialog dialog = new ConfirmDialog("Confirm",
                "Do you really want to logout?", "Logout", this::onConfirm,
                "Cancel", this::onCancel);
        dialog.open();
    }

    private void onCancel(ConfirmDialog.CancelEvent cancelEvent) {
        cancelEvent.getSource().close();
        UI.getCurrent().navigate("sensors");
    }

    private void onConfirm(ConfirmDialog.ConfirmEvent confirmEvent) {
        VaadinSession.getCurrent().setAttribute(User.class, null);
        List<RouteData> routes = RouteConfiguration.forSessionScope().getAvailableRoutes();
        for (RouteData route : routes) {
            RouteConfiguration.forSessionScope().removeRoute(route.getTemplate());
        }
        UI.getCurrent().navigate("login");
    }
}
