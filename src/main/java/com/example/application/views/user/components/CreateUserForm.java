package com.example.application.views.user.components;

import com.example.application.data.entity.User;
import com.example.application.utils.PatternStringUtils;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import org.apache.commons.codec.digest.DigestUtils;
import com.vaadin.flow.component.html.H2;

public class CreateUserForm extends VerticalLayout {

    private final H2 title = new H2("Create new user");
    private final TextField loginField = new TextField();
    private final TextField firstNameField = new TextField();
    private final TextField lastNameFieldField = new TextField();
    private final TextField emailField = new TextField();
    private final PasswordField passwordField = new PasswordField();
    private final PasswordField passwordField2 = new PasswordField();

    public CreateUserForm() {
        setLabels();
        setPatterns();
        add(title, loginField, firstNameField, lastNameFieldField, emailField, passwordField, passwordField2);
        this.setAlignItems(Alignment.CENTER);
    }

    private void setPatterns() {
        loginField.setRequired(true);
        loginField.setMaxLength(40);
        firstNameField.setRequired(true);
        firstNameField.setMaxLength(20);
        lastNameFieldField.setRequired(true);
        lastNameFieldField.setMaxLength(25);
        emailField.setRequired(true);
        emailField.setPattern(PatternStringUtils.emailRegex);
        emailField.setErrorMessage(PatternStringUtils.emailErrorMessage);
        passwordField.setRequired(true);
        passwordField.setPattern(PatternStringUtils.passwordRegex);
        passwordField.setErrorMessage(PatternStringUtils.passwordErrorMessage);
        passwordField2.setRequired(true);
        passwordField2.setPattern(PatternStringUtils.passwordRegex);
        passwordField2.setErrorMessage(PatternStringUtils.passwordErrorMessage);
    }

    private void setLabels() {
        loginField.setLabel("Login");
        firstNameField.setLabel("First name");
        lastNameFieldField.setLabel("Last name");
        emailField.setLabel("Email");
        passwordField.setLabel("Password");
        passwordField2.setLabel("Password again");
    }

    public TextField getLoginField() {
        return loginField;
    }

    public TextField getFirstNameField() {
        return firstNameField;
    }

    public TextField getLastNameFieldField() {
        return lastNameFieldField;
    }

    public TextField getEmailField() {
        return emailField;
    }

    public boolean areFieldsValid() {
        return !loginField.isEmpty() && !firstNameField.isEmpty()
                && !lastNameFieldField.isEmpty()
                && !emailField.isInvalid() && !emailField.isEmpty()
                && !passwordField.isInvalid() && !passwordField.isEmpty()
                && !passwordField2.isInvalid() && !passwordField2.isEmpty();
    }

    public boolean arePasswordsValid() {
        return passwordField.getValue().equals(passwordField2.getValue());
    }

    public User createUser() {
        User newUser = new User();
        newUser.setLogin(loginField.getValue());
        newUser.setFirstName(firstNameField.getValue());
        newUser.setSurname(lastNameFieldField.getValue());
        newUser.setFullName(newUser.getFirstName() + " " + newUser.getSurname());
        newUser.setEmail(emailField.getValue());
        newUser.setPasswordHash(DigestUtils.md5Hex(passwordField.getValue()));
        newUser.setActive(true);
        newUser.setPushToken("");
        newUser.setPhone("...");
        return newUser;
    }
}
