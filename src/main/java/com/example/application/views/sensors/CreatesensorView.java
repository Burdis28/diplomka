package com.example.application.views.sensors;

import com.example.application.data.entity.Sensor;
import com.example.application.data.entity.SensorTypes;
import com.example.application.data.service.HardwareService;
import com.example.application.data.service.SensorService;
import com.example.application.views.main.MainView;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.littemplate.LitTemplate;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.template.Id;
import com.vaadin.flow.component.textfield.BigDecimalField;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.ParentLayout;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * A Designer generated component for the person-form-view template.
 *
 * Designer will add and remove fields with @Id mappings but does not overwrite
 * or otherwise change this file.
 */
@JsModule("./views/sensors/createsensor-view.ts")
@CssImport("./views/sensors/createsensor-view.css")
@Tag("createsensor-view")
@ParentLayout(MainView.class)
@PageTitle("Create sensor")
public class CreatesensorView extends LitTemplate {

    private Binder<Sensor> binder = new Binder(Sensor.class);
    private final SensorService sensorService;
    private final HardwareService hardwareService;

    @Id("nameField")
    private TextField nameField;
    @Id("div")
    private Div div;
    @Id("basicSensorAttributesForm")
    private FormLayout basicSensorAttributesForm;
    @Id("limitDayField")
    private NumberField limitDayField;
    @Id("currencyField")
    private TextField currencyField;
    @Id("limitMonthField")
    private NumberField limitMonthField;
    @Id("typeSelect")
    private Select<String> typeSelect;

    @Id("save")
    private Button save;
    @Id("cancel")
    private Button cancel;

    //Electric components
    @Id("electricLayout")
    private FormLayout electricLayout;
    @Id("highRateCheckBox")
    private Checkbox highRateCheckBox;
    @Id("pricePerKwHighField")
    private BigDecimalField pricePerKwHighField;
    @Id("pricePerKwLowField")
    private BigDecimalField pricePerKwLowField;
    @Id("fixedPriceField")
    private BigDecimalField fixedPriceField;
    @Id("servicePriceField")
    private BigDecimalField servicePriceField;
    @Id("implPerKwField")
    private BigDecimalField implPerKwField;
    @Id("attachToHardware")
    private Select<String> attachToHardwareSelect;

    public CreatesensorView(SensorService sensorService, HardwareService hardwareService) {
        this.sensorService = sensorService;
        this.hardwareService = hardwareService;
        electricLayout.setVisible(false);

        setFormFieldsPatterns();
        setSelectFields();
        clearForm();

        typeSelect.addValueChangeListener(event -> {
            electricLayout.setVisible(typeSelect.getValue().equals(SensorTypes.e.toString()));
            //typeSelect.setValue(typeSelect.getValue());
        });

        cancel.addClickListener(e -> clearForm());
        save.addClickListener(e -> {
            validateFields();
            Sensor sensor = new Sensor();
            sensor.setName(nameField.getValue());

            sensorService.update(new Sensor());
            Notification.show("Create sensor");
            clearForm();
        });
    }

    private void validateFields() {
        
    }

    private void setFormFieldsPatterns() {
        nameField.setMaxLength(45);
        nameField.setRequired(true);
    }

    private void setSelectFields() {
        attachToHardwareSelect.setItems(hardwareService.listHardwareSerials());
        typeSelect.removeAll();
        typeSelect.setItems(Arrays.stream(SensorTypes.values()).map(SensorTypes::toString).collect(Collectors.toList()));
    }

    private void clearForm() {
        nameField.clear();
    }
}
