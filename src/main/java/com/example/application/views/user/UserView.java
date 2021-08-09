package com.example.application.views.user;

import com.example.application.data.entity.User;
import com.example.application.data.service.UserRepository;
import com.example.application.utils.PatternStringUtils;
import com.example.application.views.main.MainView;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout.FormItem;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.littemplate.LitTemplate;
import com.vaadin.flow.component.template.Id;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.validator.EmailValidator;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.ParentLayout;
import com.vaadin.flow.server.VaadinSession;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * A Designer generated component for the user-view template.
 *
 * Designer will add and remove fields with @Id mappings but
 * does not overwrite or otherwise change this file.
 */
@Tag("user-view")
@JsModule("./views/user/user-view.ts")
@ParentLayout(MainView.class)
@PageTitle("User administration")
@CssImport("./views/user/user-view.css")
public class UserView extends LitTemplate {

    @Id("firstNameField")
    private TextField firstNameField;
    @Id("emailField")
    private EmailField emailField;
    @Id("lastNameField")
    private TextField lastNameField;
    @Id("phoneField")
    private TextField phoneField;
    @Id("cancelButton")
    private Button cancelButton;
    @Id("saveButton")
    private Button saveButton;
    @Id("editButton")
    private Button editButton;
    @Id("formDiv")
    private Div formDiv;
    @Id("changePasswordButton")
    private Button changePasswordButton;
    @Id("passwordChangeForm1")
    private FormItem passwordChangeForm1;
    @Id("passwordField2")
    private PasswordField passwordField2;
    @Id("label1")
    private Label label1;
    @Id("passwordChangeForm2")
    private FormItem passwordChangeForm2;
    @Id("passwordField1")
    private PasswordField passwordField1;

    Binder<User> userBinder = new Binder<>();

    /**
     * Creates a new UserView.
     */
    public UserView(@Autowired UserRepository userRepository) {
        User user = VaadinSession.getCurrent().getAttribute(User.class);
        setUserFields(user);
        firstNameField.setMaxLength(20);
        firstNameField.setRequired(true);
        lastNameField.setMaxLength(25);
        lastNameField.setRequired(true);
        emailField.setClearButtonVisible(true);

        phoneField.setClearButtonVisible(true);

        passwordField1.setPattern(PatternStringUtils.passwordRegex);
        passwordField1.setErrorMessage(PatternStringUtils.passwordErrorMessage);
        passwordField2.setPattern(PatternStringUtils.passwordRegex);
        passwordField2.setErrorMessage(PatternStringUtils.passwordErrorMessage);

        cancelButton.setIcon(new Icon(VaadinIcon.CLOSE_CIRCLE_O));
        saveButton.setIcon(new Icon(VaadinIcon.CHECK_CIRCLE));

        setButton(saveButton,false);
        setButton(cancelButton, false);
        setButton(changePasswordButton, false);
        passwordsVisible(false);
        passwordField2.setReadOnly(true);

        editButton.addClickListener(buttonClickEvent -> {
            setReadOnlyFields(false);

            setButton(saveButton,true);
            setButton(editButton,false);
            setButton(cancelButton, true);
            setButton(changePasswordButton, true);
        });

        cancelButton.addClickListener(buttonClickEvent -> {
            // do nothing, maybe reload forms
            setButton(saveButton,false);
            setButton(editButton,true);
            setButton(cancelButton, false);
            setButton(changePasswordButton, false);
            passwordsVisible(false);

            setReadOnlyFields(true);
            passwordField1.setValue("");
            passwordField2.setValue("");
            passwordField1.setInvalid(false);
            passwordField2.setInvalid(false);
            passwordField2.setReadOnly(true);

            setUserFields(user);
        });

        saveButton.addClickListener(buttonClickEvent -> {
            if (isEverythingValid()) {
                try {
                    userBinder.writeBean(user);
                    user.setFullName(user.getFirstName() + " " + user.getSurname());

                    if(passwordChangeForm1.isVisible()) {
                        String pw1 = passwordField1.getValue();
                        String pw2 = passwordField2.getValue();
                        if (!pw1.equals(pw2)) {
                            Dialog dialog = new Dialog();
                            dialog.setModal(true);
                            dialog.add("New passwords do not match. Try again.");
                            dialog.open();
                            throw new Exception();
                        } else {
                            user.setPasswordHash(DigestUtils.md5Hex(pw1));
                        }
                    }

                    userRepository.save(user);
                    VaadinSession.getCurrent().setAttribute(User.class, user);

                    setButton(saveButton, false);
                    setButton(cancelButton, false);
                    setButton(editButton, true);
                    setButton(changePasswordButton, false);
                    passwordsVisible(false);

                    setReadOnlyFields(true);
                } catch (Exception e) {
                    //do nothin
                }
            }
        });

        changePasswordButton.addClickListener(event -> {
            passwordsVisible(true);
            passwordField1.setValue("");
            passwordField2.setValue("");
        });

        passwordField1.addValueChangeListener(event -> {
            passwordField2.setReadOnly(passwordField1.getValue() == null);
        });
    }

    private boolean isEverythingValid() {
        return !passwordField1.isInvalid() && !passwordField2.isInvalid() && !emailField.isInvalid() && !phoneField.isInvalid();
    }

    private void passwordsVisible(boolean b) {
        passwordChangeForm1.setVisible(b);
        passwordChangeForm2.setVisible(b);
    }

    private void setUserFields(User user) {
        userBinder.forField(firstNameField).asRequired("Required field.").bind(User::getFirstName, User::setFirstName);
        userBinder.forField(lastNameField).asRequired("Required field.").bind(User::getSurname, User::setSurname);
        userBinder.forField(phoneField).withValidator(s -> s.matches(PatternStringUtils.phoneRegex),
                PatternStringUtils.phoneErrorMessage).bind(User::getPhone, User::setPhone);
        userBinder.forField(emailField)
                .asRequired("Required field.")
                .withValidator(new EmailValidator(
                        PatternStringUtils.emailErrorMessage))
                .bind(User::getEmail, User::setEmail);
        userBinder.readBean(user);
    }

    private void setButton(Button button, boolean b) {
        button.setEnabled(b);
        button.setVisible(b);
    }

    private void setReadOnlyFields(boolean b) {
        firstNameField.setReadOnly(b);
        lastNameField.setReadOnly(b);
        phoneField.setReadOnly(b);
        emailField.setReadOnly(b);
    }
}