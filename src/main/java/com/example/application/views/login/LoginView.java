package com.example.application.views.login;

import com.example.application.data.entity.User;
import com.example.application.data.entity.UserPasswordReset;
import com.example.application.data.exceptions.AuthException;
import com.example.application.data.service.AuthService;
import com.example.application.data.service.EmailService;
import com.example.application.data.service.UserPasswordResetService;
import com.example.application.data.service.UserService;
import com.example.application.utils.PatternStringUtils;
import com.vaadin.flow.component.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.littemplate.LitTemplate;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.template.Id;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.dom.Element;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import com.vaadin.flow.server.VaadinSession;
import org.apache.commons.lang3.RandomStringUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.mail.*;
import javax.mail.internet.*;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Properties;

/**
 * A Designer generated component for the login-view template.
 * <p>
 * Designer will add and remove fields with @Id mappings but
 * does not overwrite or otherwise change this file.
 */
@Tag("login-view")
@JsModule("./views/login/login-view.ts")
@Route("login")
@RouteAlias("")
@PageTitle("Login")
@CssImport("./views/login/login-view.css")
public class LoginView extends LitTemplate {

    @Value("${adminContact}")
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
    @Id("imageDiv")
    private Div imageDiv;

    private final UserService userService;
    private final EmailService emailService;
    private final UserPasswordResetService userPasswordResetService;
    private PasswordEncoder passwordEncoder;

    /**
     * Creates a new LoginView.
     */
    public LoginView(@Autowired AuthService authService, UserService userService, EmailService emailService,
                     UserPasswordResetService userPasswordResetService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.emailService = emailService;
        this.userPasswordResetService = userPasswordResetService;
        this.passwordEncoder = passwordEncoder;
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
        Image image = new Image("images/smart_home_dark.png", "Diplomová práce");
        imageDiv.add(image);
        imageDiv.setClassName("alignCenter");
    }

    private void showRegisterNotification() {
        String code = RandomStringUtils.randomAlphanumeric(30);
        Dialog popUpDialog = new Dialog();
        VerticalLayout layout = new VerticalLayout();
        EmailField emailField = new EmailField();
        emailField.setLabel("Your email");
        Button btnSendCode = new Button();
        btnSendCode.setText("Send new validation code");

        TextField codeField = new TextField();
        codeField.setVisible(false);
        codeField.setLabel("Enter code: ");
        Button btnValidate = new Button();
        btnValidate.setVisible(false);
        btnValidate.setText("Validate code");

        PasswordField passwordField1 = new PasswordField();
        passwordField1.setLabel("Set new password:");
        PasswordField passwordField2 = new PasswordField();
        passwordField2.setLabel("Repeat the password");
        setPassWordFields(passwordField1, passwordField2);
        Button changePasswordButton = new Button();
        changePasswordButton.setVisible(false);
        changePasswordButton.setText("Set new password");

        btnSendCode.addClickListener(event -> {
            if (!emailField.isInvalid()) {
                try {
                    sendEmail(emailField.getValue(), emailField, code);
                    codeField.setVisible(true);
                    btnValidate.setVisible(true);
                    emailField.setVisible(false);
                    btnSendCode.setVisible(false);
                } catch (Exception e) {

                }
            } else {
                emailField.setErrorMessage("Invalid email format.");
            }
        });

        btnValidate.addClickListener(event -> {
           String enteredCode = codeField.getValue();
           if (enteredCode == null || enteredCode.equals("")) {
               codeField.setInvalid(true);
               codeField.setErrorMessage("Fill in the code.");
           } else {
               String email = emailField.getValue();
               List<UserPasswordReset> reset = userPasswordResetService.findByEmail(email, LocalDateTime.now());
               boolean codeValid = false;
               for (UserPasswordReset userPasswordReset : reset) {
                   if (userPasswordReset.getCode().equals(enteredCode)) {
                       codeValid = true;
                   }
               }
               if (codeValid) {
                   if (!reset.isEmpty()) {
                       reset.forEach(userPasswordReset -> {
                           if (userPasswordReset.isValid() && userPasswordReset.getValidTill().isAfter(LocalDateTime.now())) {
                               userPasswordReset.setValid(false);
                               userPasswordResetService.update(userPasswordReset);
                               passwordField1.setVisible(true);
                               passwordField2.setVisible(true);
                               changePasswordButton.setVisible(true);
                               codeField.setVisible(false);
                               btnValidate.setVisible(false);
                               emailField.setVisible(false);
                           }
                       });
                   }
               } else {
                   codeField.setInvalid(true);
                   codeField.setErrorMessage("Code is not valid. Please enter a valid code from your email or generate a new one.");
               }
           }
        });

        changePasswordButton.addClickListener(event -> {
            User user = userService.findByEmail(emailField.getValue());
            String pw1 = passwordField1.getValue();
            String pw2 = passwordField2.getValue();
            if (!pw1.equals(pw2)) {
                Dialog dialog = new Dialog();
                dialog.setModal(true);
                dialog.add("New passwords do not match. Try again.");
                dialog.open();
            } else {
                user.setPasswordHash(passwordEncoder.encode(pw1));
                userService.update(user);
                Notification notification = new Notification();
                notification.setPosition(Notification.Position.MIDDLE);
                notification.setText("New password set successfully. You can login now.");
                notification.setDuration(5000);
                notification.open();
                popUpDialog.close();
            }
        });

        popUpDialog.setModal(true);
        layout.add(emailField);
        layout.add(btnSendCode);
        layout.add(codeField);
        layout.add(btnValidate);
        layout.add(passwordField1);
        layout.add(passwordField2);
        layout.add(changePasswordButton);
        popUpDialog.add(layout);
        popUpDialog.open();
    }

    private void setPassWordFields(PasswordField passwordField1, PasswordField passwordField2) {
        passwordField1.setVisible(false);
        passwordField1.setPattern(PatternStringUtils.passwordRegex);
        passwordField1.setErrorMessage(PatternStringUtils.passwordErrorMessage);
        passwordField2.setVisible(false);
        passwordField2.setPattern(PatternStringUtils.passwordRegex);
        passwordField2.setErrorMessage(PatternStringUtils.passwordErrorMessage);
    }

    private void sendEmail(String email, EmailField emailField, String code) throws Exception{
        User user = userService.findByEmail(email);
        if (user == null) {
            emailField.setInvalid(true);
            emailField.setErrorMessage("There is no account for given email.");
            throw new Exception();
        } else {
            emailService.sendSimpleMessage(email, "Code to reset password", "Reset password for account ["
                    + user.getLogin() + "]. Insert this code in the web app.\n\nCode: " + code);
            UserPasswordReset reset = new UserPasswordReset();
            reset.setEmail(email);
            reset.setValid(true);
            reset.setValidTill(LocalDateTime.now().plusMinutes(30));
            reset.setCode(code);
            userPasswordResetService.update(reset);
        }
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
                UI.getCurrent().navigate("sensors");
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
