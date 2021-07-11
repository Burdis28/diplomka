package com.example.application.views.user;

import com.example.application.data.entity.User;
import com.example.application.data.service.UserRepository;
import com.example.application.views.main.MainView;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.charts.Chart;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.formlayout.FormLayout.FormItem;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.littemplate.LitTemplate;
import com.vaadin.flow.component.template.Id;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.ParentLayout;
import com.vaadin.flow.server.VaadinSession;
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
    private TextField emailField;
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
    @Id("vaadinChart")
    private Chart vaadinChart;
    @Id("formDiv")
    private Div formDiv;

    /**
     * Creates a new UserView.
     */
    public UserView(@Autowired UserRepository userRepository) {
        // You can initialise any data required for the connected UI components here.
        User user = VaadinSession.getCurrent().getAttribute(User.class);
        setUserFields(user);

        cancelButton.setIcon(new Icon(VaadinIcon.CLOSE_CIRCLE_O));
        saveButton.setIcon(new Icon(VaadinIcon.CHECK_CIRCLE));

        setButton(saveButton,false);
        setButton(cancelButton, false);

        editButton.addClickListener(buttonClickEvent -> {
            setReadOnlyFields(false);

            setButton(saveButton,true);
            setButton(editButton,false);
            setButton(cancelButton, true);

        });

        cancelButton.addClickListener(buttonClickEvent -> {
            // do nothing, maybe reload forms
            setButton(saveButton,false);
            setButton(editButton,true);
            setButton(cancelButton, false);

            setReadOnlyFields(true);

            setUserFields(user);
        });

        saveButton.addClickListener(buttonClickEvent -> {

            user.setEmail(emailField.getValue());
            user.setFirstName(firstNameField.getValue());
            user.setSurname(lastNameField.getValue());
            user.setFullName(user.getFirstName() + " " + user.getSurname());
            user.setPhone(phoneField.getValue());

            userRepository.save(user);
            VaadinSession.getCurrent().setAttribute(User.class, user);

            setButton(saveButton,false);
            setButton(cancelButton, false);
            setButton(editButton,true);

            setReadOnlyFields(true);
        });
    }

    private void setUserFields(User user) {
        firstNameField.setValue(user.getFirstName());
        lastNameField.setValue(user.getSurname());
        emailField.setValue(user.getEmail());
        phoneField.setValue(user.getPhone());
    }

    private void setButton(Button button,boolean b) {
        button.setEnabled(b);
        button.setVisible(b);
    }

    private void setReadOnlyFields(boolean b) {
        firstNameField.setReadOnly(b);
        lastNameField.setReadOnly(b);
        phoneField.setReadOnly(b);
    }

}
