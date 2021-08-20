package com.example.application.views.sensors;

import com.example.application.components.notifications.ErrorNotification;
import com.example.application.data.entity.Hardware;
import com.example.application.data.entity.Sensor;
import com.example.application.data.entity.SensorElectric;
import com.example.application.data.entity.User;
import com.example.application.data.service.HardwareService;
import com.example.application.data.service.SensorElectricService;
import com.example.application.data.service.SensorService;
import com.example.application.utils.PatternStringUtils;
import com.example.application.utils.RegexDoubleStringValidator;
import com.example.application.views.main.MainLayout;
import com.example.application.views.sensors.components.SensorInfoComponent;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
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

import java.math.BigDecimal;
import java.util.List;

/**
 * A Designer generated component for the water-sensor template.
 * <p>
 * Designer will add and remove fields with @Id mappings but
 * does not overwrite or otherwise change this file.
 */
@Tag("electric-sensor-view")
@JsModule("./views/sensors/electric-sensor-view.ts")
@CssImport("./views/sensors/electric-sensor-view.css")
@ParentLayout(MainLayout.class)
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
    @Id("pricePerKwLowSuffix")
    private Div pricePerKwLowFieldSuffix;
    @Id("priceFixedSuffix")
    private Div priceFixedSuffix;
    @Id("priceServiceSuffix")
    private Div priceServiceSuffix;
    @Id("implPerKwSuffix")
    private Div implPerKwSuffix;
    @Id("vaadinHorizontalLayout")
    private HorizontalLayout vaadinHorizontalLayout;
    @Id("electricAttributesTitle")
    private H3 electricAttributesTitle;
    @Id("firstLayout")
    private HorizontalLayout firstLayout;

    private final SensorService sensorService;
    private final SensorElectricService sensorElectricService;
    private final HardwareService hardwareService;
    private SensorElectric sensorElectric;
    private Sensor sensor;
    private SensorInfoComponent sensorInfo;

    private Binder<Sensor> sensorBinder = new Binder<>();
    private Binder<SensorElectric> sensorElectricBinder = new Binder<>();
    private List<Hardware> hardwareList;

    public ElectricSensorView(SensorElectricService sensorElectricService,
                              SensorService sensorService, HardwareService hardwareService) {
        this.sensorElectricService = sensorElectricService;
        this.sensorService = sensorService;
        this.hardwareService = hardwareService;
        User loggedUser = VaadinSession.getCurrent().getAttribute(User.class);
        if (loggedUser.getAdmin()) {
            hardwareList = hardwareService.findAll();
        } else {
            hardwareList = hardwareService.findByOwner(loggedUser.getId());
        }

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

            setButton(saveButton, true);
            setButton(returnButton, false);
            setButton(editButton, false);
            setButton(cancelButton, true);

        });

        cancelButton.addClickListener(buttonClickEvent -> {
            // do nothing, maybe reload forms
            setButton(saveButton, false);
            setButton(returnButton, true);
            setButton(editButton, true);
            setButton(cancelButton, false);

            setReadOnlyFields(true);

            sensorBinder.readBean(sensor);
            sensorElectricBinder.readBean(sensorElectric);
            setAttachedHardwareValue();
            sensorInfo.getPinIdField().setValue(sensor.getPinId());
        });

        saveButton.addClickListener(buttonClickEvent -> {
            try {
                sensorElectricBinder.writeBean(sensorElectric);
                sensorBinder.writeBean(sensor);
                validatePinOnNewHardware();
                setPin();

                sensorElectricService.update(sensorElectric);
                sensor.setIdHw(sensorInfo.getAttachToHardwareSelect().getValue().getSerial_HW());
                sensorService.update(sensor);
                pricePerKwLowField.setInvalid(false);
                pricePerKwHighField.setInvalid(false);

                setButton(saveButton, false);
                setButton(returnButton, true);
                setButton(cancelButton, false);
                setButton(editButton, true);

                setReadOnlyFields(true);
                sensorInfo.getPinIdField().setReadOnly(true);

                sensorBinder.readBean(sensor);
                sensorElectricBinder.readBean(sensorElectric);
            } catch (Exception e) {
                ErrorNotification error = new ErrorNotification();
                if (e.getMessage().equals("Sensor pin")) {
                    error.setErrorText("Pin is already in use on new selected HW.");
                } else {
                    error.setErrorText("Wrong form data input.");
                }
                error.open();
            }
        });

        returnButton.addClickListener(buttonClickEvent -> {
            UI.getCurrent().navigate("sensors");
        });
    }

    private void setPin() {
        sensor.setPinId(sensorInfo.getPinIdField().getValue());
    }

    private void setAttachedHardwareValue() {
        sensorInfo.getAttachToHardwareSelect().setValue(hardwareList.stream()
                .filter(hardware -> sensor.getIdHw().equals(hardware.getSerial_HW()))
                .findFirst()
                .orElse(null)
        );

        sensorInfo.getAttachToHardwareSelect().addValueChangeListener(event -> {
            if (event.getValue().getSerial_HW().equals(this.sensor.getIdHw())) {
                sensorInfo.getPinIdField().setReadOnly(true);
                sensorInfo.getPinIdField().setValue(sensor.getPinId());
            } else {
                sensorInfo.getPinIdField().setReadOnly(false);
            }
        });
    }

    private void validatePinOnNewHardware() throws Exception {
        List<Sensor> sensors = sensorService.findSensorByIdHw(sensorInfo.getAttachToHardwareSelect().getValue().getSerial_HW());
        sensors.removeIf(sensor1 -> sensor1.getId() == this.sensor.getId());
        for (Sensor sensor : sensors) {
            if (sensor.getType().equals(this.sensor.getType()) && sensor.getPinId() == sensorInfo.getPinIdField().getValue()) {
                throw new Exception("Sensor pin");
            }
        }
    }

    public void setSensor() {
        int sensorId = -1;
        sensorId = (int) VaadinSession.getCurrent().getAttribute("sensorId");
        if (sensorId != -1) {
            this.sensorElectricService.get(sensorId).ifPresent(electric -> sensorElectric = electric);
            this.sensorService.get(sensorId).ifPresent(sensor -> this.sensor = sensor);
        }
        setCurrencySuffixes();

        sensorInfo = new SensorInfoComponent(sensor, hardwareList);
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

        setAttachedHardwareValue();

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


        sensorInfo.getSensorName().setReadOnly(b);
        sensorInfo.getLimitDay().setReadOnly(b);
        sensorInfo.getLimitMonth().setReadOnly(b);
        sensorInfo.getCurrency().setReadOnly(b);
        sensorInfo.getAttachToHardwareSelect().setReadOnly(b);
        sensorInfo.getConsumptionCorrelation().setReadOnly(b);

    }
}