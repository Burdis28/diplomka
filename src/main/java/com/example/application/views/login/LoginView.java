package com.example.application.views.login;

import com.example.application.data.exceptions.AuthException;
import com.example.application.data.service.AuthService;
import com.vaadin.flow.component.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.littemplate.LitTemplate;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.template.Id;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.dom.Element;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import org.springframework.beans.factory.annotation.Value;

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
@CssImport("./views/login/login-view.css")
public class LoginView extends LitTemplate {

    @Value( "${adminContact}" )
    private String adminContact;

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
    @Id("vaadinVerticalLayout")
    private Element vaadinVerticalLayout;
    @Id("loginTitleH3")
    private H3 loginTitleH3;
    @Id("registerButton")
    private Button registerButton;

    /**
     * Creates a new LoginView.
     */
    public LoginView(AuthService authService) {
        loginButton.setIcon(VaadinIcon.SIGN_IN.create());
        loginButton.setIconAfterText(true);
        registerButton.setIcon(VaadinIcon.HANDS_UP.create());
        registerButton.setIconAfterText(true);
        username.setRequired(true);
        password.setRequired(true);
        loginButton.addClickListener(buttonClickEvent -> {
            performLogin(authService);
        });
        password.addKeyDownListener(Key.ENTER, keyDownEvent -> performLogin(authService));
        registerButton.addClickListener(buttonClickEvent -> {
            showRegisterNotification();
        });
    }

    private void showRegisterNotification() {
        Dialog popUpDialog = new Dialog();
        popUpDialog.setModal(true);
        popUpDialog.add("If you want to register, please contact an administrator and he'll setup an account for you. Contact: " + adminContact);
        popUpDialog.open();
    }

    private void performLogin(AuthService authService) {
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
    }
}
