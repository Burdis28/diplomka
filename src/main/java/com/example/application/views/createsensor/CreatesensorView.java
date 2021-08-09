package com.example.application.views.createsensor;

import com.example.application.data.entity.SamplePerson;
import com.example.application.data.service.SamplePersonService;

import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.customfield.CustomField;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.template.Id;
import com.vaadin.flow.component.littemplate.LitTemplate;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.ParentLayout;
import com.vaadin.flow.router.PageTitle;
import com.example.application.views.main.MainView;

/**
 * A Designer generated component for the person-form-view template.
 *
 * Designer will add and remove fields with @Id mappings but does not overwrite
 * or otherwise change this file.
 */
@JsModule("./views/createsensor/createsensor-view.ts")
@CssImport("./views/createsensor/createsensor-view.css")
@Tag("createsensor-view")
@ParentLayout(MainView.class)
@PageTitle("Create sensor")
public class CreatesensorView extends LitTemplate {

    @Id("firstName")
    private TextField firstName;
    @Id("lastName")
    private TextField lastName;
    @Id("email")
    private EmailField email;
    @Id("occupation")
    private TextField occupation;
    @Id("birthday")
    private DatePicker dateOfBirth;
    @Id("pnCountryCode")
    private ComboBox<String> countryCode;
    @Id("pnNumber")
    private TextField number;
    @Id("phoneNumber")
    private PhoneNumberField phone;

    @Id("save")
    private Button save;
    @Id("cancel")
    private Button cancel;

    private Binder<SamplePerson> binder = new Binder(SamplePerson.class);
    @Id("div")
    private Div div;

    public CreatesensorView(SamplePersonService personService) {
        countryCode.setItems("+354", "+91", "+62", "+98", "+964", "+353", "+44", "+972", "+39", "+225");
        countryCode.addCustomValueSetListener(e -> countryCode.setValue(e.getDetail()));

        phone.setForm(this);

        binder.bindInstanceFields(this);
        clearForm();

        cancel.addClickListener(e -> clearForm());
        save.addClickListener(e -> {
            personService.update(binder.getBean());
            Notification.show(binder.getBean().getClass().getSimpleName() + " details stored.");
            clearForm();
        });
    }

    private void clearForm() {
        binder.setBean(new SamplePerson());
    }

    private static class PhoneNumberField extends CustomField<String> {
        private CreatesensorView form;

        @Override
        protected String generateModelValue() {
            if (getCountryCode().getValue() != null && getNumber().getValue() != null) {
                return getCountryCode().getValue() + " " + getNumber().getValue();
            }
            return "";
        }

        @Override
        protected void setPresentationValue(String phoneNumber) {
            String[] parts = phoneNumber != null ? phoneNumber.split(" ", 2) : new String[0];
            if (parts.length == 1) {
                getCountryCode().clear();
                getNumber().setValue(parts[0]);
            } else if (parts.length == 2) {
                getCountryCode().setValue(parts[0]);
                getNumber().setValue(parts[1]);
            } else {
                getCountryCode().clear();
                getNumber().clear();
            }
        }

        public void setForm(CreatesensorView form) {
            this.form = form;
        }

        private TextField getNumber() {
            return form.number;
        }

        private ComboBox<String> getCountryCode() {
            return form.countryCode;
        }
    }

}