package com.example.application.views.sensors;

import com.example.application.components.notifications.ErrorNotification;
import com.example.application.data.entity.Sensor;
import com.example.application.data.entity.SensorElectric;
import com.example.application.data.service.SensorElectricService;
import com.example.application.data.service.SensorService;
import com.example.application.helpers.validators.ElectricSensorValidator;
import com.example.application.views.main.MainView;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.littemplate.LitTemplate;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.template.Id;
import com.vaadin.flow.component.textfield.BigDecimalField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.ParentLayout;
import com.vaadin.flow.server.VaadinSession;

import java.math.BigDecimal;

/**
 * A Designer generated component for the water-sensor template.
 *
 * Designer will add and remove fields with @Id mappings but
 * does not overwrite or otherwise change this file.
 */
@Tag("electric-sensor-view")
@JsModule("./views/sensors/electric-sensor-view.ts")
@CssImport("./views/sensors/electric-sensor-view.css")
@ParentLayout(MainView.class)
@PageTitle("Electric sensor detail")
public class ElectricSensorView extends LitTemplate {

    @Id("priceFixedField")
    private BigDecimalField priceFixedField;
    @Id("editButton")
    private Button editButton;
    @Id("cancelButton")
    private Button cancelButton;
    @Id("saveButton")
    private Button saveButton;
    @Id("returnButton")
    private Button returnButton;
    @Id("pricePerKwHighField")
    private BigDecimalField pricePerKwHighField;
    @Id("pricePerKwLowField")
    private BigDecimalField pricePerKwLowField;
    @Id("priceServiceField")
    private BigDecimalField priceServiceField;
    @Id("implPerKWField")
    private BigDecimalField implPerKWField;
    @Id("pricePerKwHighSuffix")
    private Div pricePerKwHighSuffix;
    @Id("pricePerKwLowFieldSuffix")
    private Div pricePerKwLowFieldSuffix;
    @Id("priceFixedSuffix")
    private Div priceFixedSuffix;
    @Id("priceServiceSuffix")
    private Div priceServiceSuffix;
    @Id("implPerKwSuffix")
    private Div implPerKwSuffix;
    @Id("highRateCheckbox")
    private Checkbox highRateCheckbox;
    @Id("vaadinHorizontalLayout")
    private HorizontalLayout vaadinHorizontalLayout;

    private final SensorService sensorService;
    private final SensorElectricService sensorElectricService;
    private SensorElectric sensorElectric;
    private Sensor sensor;

    public ElectricSensorView(SensorElectricService sensorElectricService,
                              SensorService sensorService) {
        this.sensorElectricService = sensorElectricService;
        this.sensorService = sensorService;

        cancelButton.setIcon(new Icon(VaadinIcon.CLOSE_CIRCLE_O));
        saveButton.setIcon(new Icon(VaadinIcon.CHECK_CIRCLE));
        returnButton.setIcon(new Icon(VaadinIcon.REPLY));

        setReadOnlyFields(true);
        setSensor();
        attachListeners();

        setButton(editButton, true);
        setButton(returnButton, true);
        setButton(cancelButton, false);
        setButton(saveButton, false);
    }

    private void attachListeners() {
        editButton.addClickListener(buttonClickEvent -> {
            setReadOnlyFields(false);

            setButton(saveButton,true);
            setButton(returnButton, false);
            setButton(editButton,false);
            setButton(cancelButton, true);

        });

        cancelButton.addClickListener(buttonClickEvent -> {
            // do nothing, maybe reload forms
            setButton(saveButton,false);
            setButton(returnButton, true);
            setButton(editButton,true);
            setButton(cancelButton, false);

            setReadOnlyFields(true);

            setSensorFields(sensorElectric);
        });

        saveButton.addClickListener(buttonClickEvent -> {
            try {
                sensorElectric.setPricePerKwHigh(pricePerKwHighField.getValue().floatValue());
                sensorElectric.setPricePerKwLow(pricePerKwLowField.getValue().floatValue());
                sensorElectric.setPriceFix(priceFixedField.getValue().floatValue());
                sensorElectric.setPriceService(priceServiceField.getValue().floatValue());
                sensorElectric.setImplPerKw(implPerKWField.getValue().intValue());
                sensorElectric.setHighRate(highRateCheckbox.getValue());

                if (!ElectricSensorValidator.validateLowHigh(
                        pricePerKwLowField.getValue().floatValue(),
                        pricePerKwHighField.getValue().floatValue())) {
                    ErrorNotification error = new ErrorNotification();
                    error.setErrorText("[Price per KW - low] musí být menší, než [Price per KW - high]");
                    error.open();
                    pricePerKwLowField.setInvalid(true);
                    pricePerKwHighField.setInvalid(true);
                } else {
                    sensorElectricService.update(sensorElectric);
                    pricePerKwLowField.setInvalid(false);
                    pricePerKwHighField.setInvalid(false);

                    setButton(saveButton, false);
                    setButton(returnButton, true);
                    setButton(cancelButton, false);
                    setButton(editButton, true);

                    setReadOnlyFields(true);

                    setSensorFields(sensorElectric);
                }
            } catch (Exception e) {
                ErrorNotification error = new ErrorNotification();
                error.setErrorText("Špatně zadaná vstupní data formuláře.");
                error.open();
            }
        });

        returnButton.addClickListener(buttonClickEvent -> {
            UI.getCurrent().navigate("sensors");
        });
    }

    public void setSensor() {
        int sensorId = -1;
        sensorId = (int) VaadinSession.getCurrent().getAttribute("sensorId");
        if(sensorId != -1) {
            this.sensorElectricService.get(sensorId).ifPresent(electric -> sensorElectric = electric);
            this.sensorService.get(sensorId).ifPresent(sensor -> this.sensor = sensor);
        }
        String currency = sensor.getCurrencyString();
        priceFixedSuffix.setText(currency);
        pricePerKwHighSuffix.setText(currency);
        pricePerKwLowFieldSuffix.setText(currency);
        priceServiceSuffix.setText(currency);
        implPerKwSuffix.setText(currency);

        setSensorFields(sensorElectric);
    }

    private void setSensorFields(SensorElectric sensorElectric) {
        pricePerKwLowField.setValue(BigDecimal.valueOf(sensorElectric.getPricePerKwLow()));
        pricePerKwHighField.setValue(BigDecimal.valueOf(sensorElectric.getPricePerKwHigh()));
        priceFixedField.setValue(BigDecimal.valueOf(sensorElectric.getPriceFix()));
        priceServiceField.setValue(BigDecimal.valueOf(sensorElectric.getPriceService()));
        implPerKWField.setValue(BigDecimal.valueOf(sensorElectric.getImplPerKw()));
        highRateCheckbox.setValue(sensorElectric.isHighRate());
    }

    private void setButton(Button button, boolean b) {
        button.setEnabled(b);
        button.setVisible(b);
    }

    private void setReadOnlyFields(boolean b) {
        pricePerKwLowField.setReadOnly(b);
        pricePerKwHighField.setReadOnly(b);
        priceFixedField.setReadOnly(b);
        priceServiceField.setReadOnly(b);
        implPerKWField.setReadOnly(b);
        highRateCheckbox.setReadOnly(b);
    }
}
