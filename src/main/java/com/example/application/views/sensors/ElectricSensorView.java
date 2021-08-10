package com.example.application.views.sensors;

import com.example.application.components.notifications.ErrorNotification;
import com.example.application.data.entity.Sensor;
import com.example.application.data.entity.SensorElectric;
import com.example.application.data.service.SensorElectricService;
import com.example.application.data.service.SensorService;
import com.example.application.helpers.validators.ElectricSensorValidator;
import com.example.application.utils.PatternStringUtils;
import com.example.application.utils.RegexDoubleStringValidator;
import com.example.application.views.main.MainLayout;
import com.example.application.views.sensors.components.SensorInfoComponent;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.littemplate.LitTemplate;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.template.Id;
import com.vaadin.flow.component.textfield.BigDecimalField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.ParentLayout;
import com.vaadin.flow.server.VaadinSession;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.math.BigDecimal;
import java.util.Optional;

/**
 * A Designer generated component for the water-sensor template.
 *
 * Designer will add and remove fields with @Id mappings but
 * does not overwrite or otherwise change this file.
 */
@Tag("electric-sensor-view")
@JsModule("./views/sensors/electric-sensor-view.ts")
@CssImport("./views/sensors/electric-sensor-view.css")
@ParentLayout(MainLayout.class)
@PageTitle("Electric sensor detail")
@EnableScheduling
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
    @Id("pricePerKwLowSuffix")
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
    @Id("electricAttributesTitle")
    private H3 electricAttributesTitle;
    @Id("firstLayout")
    private HorizontalLayout firstLayout;

    private final SensorService sensorService;
    private final SensorElectricService sensorElectricService;
    private SensorElectric sensorElectric;
    private Sensor sensor;
    private SensorInfoComponent sensorInfo;

    private Binder<Sensor> sensorBinder = new Binder<>();
    private Binder<SensorElectric> sensorElectricBinder = new Binder<>();


    public ElectricSensorView(SensorElectricService sensorElectricService,
                              SensorService sensorService) {
        this.sensorElectricService = sensorElectricService;
        this.sensorService = sensorService;

        cancelButton.setIcon(new Icon(VaadinIcon.CLOSE_CIRCLE_O));
        saveButton.setIcon(new Icon(VaadinIcon.CHECK_CIRCLE));
        returnButton.setIcon(new Icon(VaadinIcon.REPLY));

        setSensor();
        setReadOnlyFields(true);
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

            //setSensorFields(sensorElectric);
        });

        saveButton.addClickListener(buttonClickEvent -> {
            try {
                if (!ElectricSensorValidator.validateLowHigh(
                        pricePerKwLowField.getValue().doubleValue(),
                        pricePerKwHighField.getValue().doubleValue())) {
                    ErrorNotification error = new ErrorNotification();
                    error.setErrorText("[Price per KW - low] musí být menší, než [Price per KW - high]");
                    error.open();
                    pricePerKwLowField.setInvalid(true);
                    pricePerKwHighField.setInvalid(true);
                } else {
                    sensorElectricBinder.writeBean(sensorElectric);
                    sensorBinder.writeBean(sensor);

                    sensorElectricService.update(sensorElectric);
                    sensorService.update(sensor);
                    pricePerKwLowField.setInvalid(false);
                    pricePerKwHighField.setInvalid(false);
                    sensorInfo.actualizeConsumptionChart(sensor);

                    setButton(saveButton, false);
                    setButton(returnButton, true);
                    setButton(cancelButton, false);
                    setButton(editButton, true);

                    setReadOnlyFields(true);

                    //setSensorFields(sensorElectric);
                }
            } catch (Exception e) {
                ErrorNotification error = new ErrorNotification();
                error.setErrorText("Wrong form data input.");
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
        setCurrencySuffixes();

        sensorInfo = new SensorInfoComponent(sensor);
        sensorInfo.setId("sensorInfoLayout");
        sensorInfo.setVisible(true);
        firstLayout.addComponentAsFirst(sensorInfo);

        sensorInfo.getConsumptionActual().setReadOnly(true);
        sensorInfo.getConsumptionCorrelation().setReadOnly(true);
        setSensorFields();
    }

    private void setCurrencySuffixes() {
        String currency = sensor.getCurrencyString();
        priceFixedSuffix.setText(currency);
        pricePerKwHighSuffix.setText(currency);
        pricePerKwLowFieldSuffix.setText(currency);
        priceServiceSuffix.setText(currency);
        implPerKwSuffix.setText(currency);
    }

    private void setSensorFields() {
        sensorElectricBinder.forField(pricePerKwLowField).asRequired("Required field.")
                .withValidator(new RegexDoubleStringValidator(PatternStringUtils.fieldIsRequired, PatternStringUtils.doubleNumberRegex62))
                .withConverter(BigDecimal::doubleValue, BigDecimal::valueOf)
                .bind(SensorElectric::getPricePerKwLow, SensorElectric::setPricePerKwLow);
        sensorElectricBinder.forField(pricePerKwHighField).asRequired("Required field.")
                .withValidator(new RegexDoubleStringValidator(PatternStringUtils.fieldIsRequired, PatternStringUtils.doubleNumberRegex62))
                .withConverter(BigDecimal::doubleValue, BigDecimal::valueOf)
                .bind(SensorElectric::getPricePerKwHigh, SensorElectric::setPricePerKwHigh);
        sensorElectricBinder.forField(priceFixedField).asRequired("Required field.")
                .withValidator(new RegexDoubleStringValidator(PatternStringUtils.fieldIsRequired, PatternStringUtils.doubleNumberRegex62))
                .withConverter(BigDecimal::doubleValue, BigDecimal::valueOf)
                .bind(SensorElectric::getPriceFix, SensorElectric::setPriceFix);
        sensorElectricBinder.forField(priceServiceField).asRequired("Required field.")
                .withValidator(new RegexDoubleStringValidator(PatternStringUtils.fieldIsRequired, PatternStringUtils.doubleNumberRegex62))
                .withConverter(BigDecimal::doubleValue, BigDecimal::valueOf)
                .bind(SensorElectric::getPriceService, SensorElectric::setPriceService);
        sensorElectricBinder.forField(implPerKWField).asRequired("Required field.").withConverter(BigDecimal::intValue, BigDecimal::valueOf)
                .bind(SensorElectric::getImplPerKw, SensorElectric::setImplPerKw);
        sensorElectricBinder.forField(highRateCheckbox).bind(SensorElectric::isHighRate, SensorElectric::setHighRate);

        sensorElectricBinder.readBean(sensorElectric);

        sensorBinder.forField(sensorInfo.getSensorName()).asRequired("Required field.").bind(Sensor::getName, Sensor::setName);
        sensorBinder.forField(sensorInfo.getLimitDay()).asRequired("Required field.")
                .withValidator(new RegexDoubleStringValidator(PatternStringUtils.fieldIsRequired, PatternStringUtils.doubleNumberRegex63))
                .withConverter(BigDecimal::doubleValue, BigDecimal::valueOf)
                .bind(Sensor::getLimit_day, Sensor::setLimit_day);
        sensorBinder.forField(sensorInfo.getLimitMonth()).asRequired("Required field.")
                .withValidator(new RegexDoubleStringValidator(PatternStringUtils.fieldIsRequired, PatternStringUtils.doubleNumberRegex103))
                .withConverter(BigDecimal::doubleValue, BigDecimal::valueOf)
                .withValidator(aDouble -> aDouble >= sensorInfo.getLimitDay().getValue().doubleValue(), "Monthly limit has to be bigger than daily limit.")
                .bind(Sensor::getLimit_month, Sensor::setLimit_month);
        sensorBinder.forField(sensorInfo.getConsumptionActual()).asRequired("Required field.").withConverter(BigDecimal::doubleValue, BigDecimal::valueOf)
                .bind(Sensor::getConsumptionActual, Sensor::setConsumptionActual);
        sensorBinder.forField(sensorInfo.getConsumptionCorrelation()).asRequired("Required field.").withConverter(BigDecimal::doubleValue, BigDecimal::valueOf)
                .bind(Sensor::getConsumptionCorrelation, Sensor::setConsumptionCorrelation);
        sensorBinder.forField(sensorInfo.getCurrency()).asRequired("Required field.").bind(Sensor::getCurrencyString, Sensor::setCurrencyString);

        sensorBinder.readBean(sensor);
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

        sensorInfo.getSensorName().setReadOnly(b);
        sensorInfo.getLimitDay().setReadOnly(b);
        sensorInfo.getLimitMonth().setReadOnly(b);
        sensorInfo.getCurrency().setReadOnly(b);
    }

    // every 30 seconds refresh data for gauge chart
    @Scheduled(fixedDelay=30000)
    public void refreshSensorConsumption() {
        Optional<Sensor> refreshedSensor = sensorService.get(sensor.getId());
        refreshedSensor.ifPresent(value -> sensorInfo.actualizeConsumptionChart(value.getConsumptionActual()));
    }
}