package com.example.application.views.login;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;

@Route("logout")
@PageTitle("Logout")
public class LogoutView extends Composite<VerticalLayout> {

    public LogoutView() {
        ConfirmDialog dialog = new ConfirmDialog("Confirm",
                "Do you really want to logout?", "Logout", this::onConfirm,
                "Cancel", this::onCancel);
        dialog.open();
    }

    private void onCancel(ConfirmDialog.CancelEvent cancelEvent) {
        cancelEvent.getSource().close();
        UI.getCurrent().navigate("dashboard");
    }

    private void onConfirm(ConfirmDialog.ConfirmEvent confirmEvent) {
        UI.getCurrent().getPage().setLocation("login");
        VaadinSession.getCurrent().getSession().invalidate();
        VaadinSession.getCurrent().close();
    }
}
