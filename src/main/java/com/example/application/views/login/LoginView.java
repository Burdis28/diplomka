package com.example.application.views.login;

import com.example.application.data.exceptions.AuthException;
import com.example.application.data.service.AuthService;
import com.example.application.views.main.MainView;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.littemplate.LitTemplate;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.template.Id;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;

/**
 * A Designer generated component for the login-view template.
 *
 * Designer will add and remove fields with @Id mappings but
 * does not overwrite or otherwise change this file.
 */
@Tag("login-view")
@JsModule("./views/login/login-view.ts")
@Route(value = "login")
@RouteAlias(value = "")
@PageTitle("Login")
public class LoginView extends LitTemplate {

    @Id("Title")
    private H1 h1;
    @Id("vaadinVerticalLayout")
    private VerticalLayout vaadinVerticalLayout;
    @Id("username")
    private TextField username;
    @Id("password")
    private PasswordField password;
    @Id("vaadinFormLayout")
    private FormLayout vaadinFormLayout;
    @Id("vaadinVerticalLayout1")
    private VerticalLayout vaadinVerticalLayout1;
    @Id("loginButton")
    private Button loginButton;

    /**
     * Creates a new LoginView.
     */
    public LoginView(AuthService authService) {
        username.setRequired(true);
        password.setRequired(true);
        loginButton.addClickListener(buttonClickEvent -> {
            if (username.isEmpty() || password.isEmpty()) {
                Notification notification = new Notification();
                notification.setThemeName(NotificationVariant.LUMO_ERROR.getVariantName());
                notification.setText("Musíš zadat přístupové údaje.");
                notification.setDuration(3000);
                notification.setPosition(Notification.Position.MIDDLE);
                notification.open();
            } else {
                try {
                    authService.authenticate(username.getValue(), password.getValue());
                    Notification.show("Hello");
                    UI.getCurrent().navigate("dashboard");
                } catch (AuthException e) {
                    Notification notification = new Notification();
                    notification.setThemeName(NotificationVariant.LUMO_ERROR.getVariantName());
                    notification.setText("Špatně zadané přístupové údaje.");
                    notification.setDuration(3000);
                    notification.setPosition(Notification.Position.MIDDLE);
                    notification.open();
                }
            }
        });
    }
}
