package com.example.application.views.sensors;

import com.example.application.components.notifications.ErrorNotification;
import com.example.application.data.entity.*;
import com.example.application.data.service.*;
import com.example.application.utils.PatternStringUtils;
import com.example.application.views.main.MainLayout;
import com.example.application.views.sensors.components.SensorInfoComponent;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.littemplate.LitTemplate;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.template.Id;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.ParentLayout;
import com.vaadin.flow.server.VaadinSession;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

/**
 * A Designer generated component for the water-sensor-view template.
 * <p>
 * Designer will add and remove fields with @Id mappings but
 * does not overwrite or otherwise change this file.
 */
@Tag("water-sensor-view")
@JsModule("./views/sensors/water-sensor-view.ts")
@CssImport("./views/sensors/water-sensor-view.css")
@ParentLayout(MainLayout.class)
@PageTitle("Water sensor detail")
public class WaterSensorView extends LitTemplate {

    @Id("editButton")
    private Button editButton;
    @Id("returnButton")
    private Button returnButton;
    @Id("saveButton")
    private Button saveButton;
    @Id("cancelButton")
    private Button cancelButton;
    @Id("vaadinHorizontalLayout")
    private HorizontalLayout vaadinHorizontalLayout;
    @Id("firstLayout")
    private HorizontalLayout firstLayout;
    @Id("countStopField")
    private TextField countStopField;
    @Id("countStopNightField")
    private TextField countStopNightField;

    @Id("implPerLitField")
    private TextField implPerLitField;
    @Id("stateModifiedDateField")
    private TextField stateModifiedDateField;
    @Id("stateModifiedBy")
    private TextField stateModifiedBy;
    @Id("pricePerM3Field")
    private TextField pricePerM3Field;
    @Id("startNightField")
    private TextField startNightField;
    @Id("endNightField")
    private TextField endNightField;

    private Binder<Sensor> sensorBinder = new Binder<>();
    private Binder<SensorWater> sensorWaterBinder = new Binder<>();
    private final SensorService sensorService;
    private final SensorWaterService sensorWaterService;
    private final StateValveService stateValveService;
    private final HardwareService hardwareService;
    private final UserService userService;
    private SensorWater sensorWater;
    private Sensor sensor;
    private SensorInfoComponent sensorInfo;
    private List<StateValve> states;
    private User loggedUser;
    private int initialStateId;
    private List<Hardware> hardwareList;
    @Id("stateTextField")
    private TextField stateTextField;
    @Id("openCloseValveButton")
    private Button openCloseValveButton;
    @Id("configurationTitle")
    private H3 configurationTitle;
    @Id("configurationLayout")
    private HorizontalLayout configurationLayout;

    /**
     * Creates a new WaterSensorView.
     *
     * @param sensorService
     * @param sensorWaterService
     */
    public WaterSensorView(SensorService sensorService,
                           SensorWaterService sensorWaterService,
                           StateValveService stateValveService,
                           UserService userService,
                           HardwareService hardwareService) {
        this.sensorService = sensorService;
        this.sensorWaterService = sensorWaterService;
        this.stateValveService = stateValveService;
        this.userService = userService;
        this.hardwareService = hardwareService;
        loggedUser = VaadinSession.getCurrent().getAttribute(User.class);
        if (loggedUser.getAdmin()) {
            hardwareList = hardwareService.findAll();
        } else {
            hardwareList = hardwareService.findByOwner(loggedUser.getId());
            configurationLayout.setVisible(false);
            configurationTitle.setVisible(false);

        }
        states = stateValveService.listAll();

        cancelButton.setIcon(new Icon(VaadinIcon.CLOSE_CIRCLE_O));
        saveButton.setIcon(new Icon(VaadinIcon.CHECK_CIRCLE));
        returnButton.setIcon(new Icon(VaadinIcon.REPLY));

        setSensor();
        setReadOnlyFields(true);
        stateTextField.setReadOnly(true);
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
            sensorWaterBinder.readBean(sensorWater);
            setAttachedHardwareValue();
            setStateFields(sensorWater);
            sensorInfo.getPinIdField().setValue(sensor.getPinId());
        });

        saveButton.addClickListener(buttonClickEvent -> {
            try {
                sensorWaterBinder.writeBean(sensorWater);
                sensorBinder.writeBean(sensor);
                updateNightTimeFields(sensorWater);
                if (loggedUser.getAdmin()) {
                    validatePinOnNewHardware();
                    setPin();
                }
                setState();

                sensorWaterService.update(sensorWater);
                sensor.setIdHw(sensorInfo.getAttachToHardwareSelect().getValue().getSerial_HW());
                sensorService.update(sensor);
                setStateValveFields(sensorWater);
                setNightTimesFields(sensorWater);

                setButton(saveButton, false);
                setButton(returnButton, true);
                setButton(cancelButton, false);
                setButton(editButton, true);

                setReadOnlyFields(true);
                sensorInfo.getPinIdField().setReadOnly(true);
                sensorBinder.readBean(sensor);
                sensorWaterBinder.readBean(sensorWater);
            } catch (Exception e) {
                ErrorNotification error = new ErrorNotification();
                if (e.getMessage() != null) {
                    if (e.getMessage().equals("Sensor pin")) {
                        error.setErrorText("Pin is already in use on new selected HW.");
                    }
                } else {
                    error.setErrorText("Wrong form data input.");
                }
                error.open();
            }
        });
        returnButton.addClickListener(buttonClickEvent ->
        {
            UI.getCurrent().navigate("sensors");
        });

        openCloseValveButton.addClickListener(event -> {
            Optional<StateValve> state = states.stream().filter(stateValve -> stateValve.getId() == sensorWater.getState()).findFirst();
            if (state.isPresent()) {
                if (stateTextField.getValue().equals("Opened") || stateTextField.getValue().equals("Request to open")) {
                    stateTextField.setValue("Request to close");
                    openCloseValveButton.setText("Open valve");
                } else {
                    stateTextField.setValue("Request to open");
                    openCloseValveButton.setText("Close valve");
                }
            }
        });
    }

    private void setPin() {
        sensor.setPinId(sensorInfo.getPinIdField().getValue());
    }

    private void setState() {
        if (stateTextField.getValue().equals("Request to open")) {
            if (sensorWater.getState() == 1) {
                sensorWater.setState(1);
                stateTextField.setValue("Opened");
            } else {
                sensorWater.setState(2);
            }
        } else if (stateTextField.getValue().equals("Request to close")) {
            if (sensorWater.getState() == 3) {
                sensorWater.setState(3);
                stateTextField.setValue("Closed");
            } else {
                sensorWater.setState(4);
            }
        }
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

    public void setSensor() {
        int sensorId = -1;
        sensorId = (int) VaadinSession.getCurrent().getAttribute("sensorId");
        if (sensorId != -1) {
            this.sensorWaterService.get(sensorId).ifPresent(water -> sensorWater = water);
            this.sensorService.get(sensorId).ifPresent(sensor -> this.sensor = sensor);
        }
        initialStateId = sensorWater.getState();
        sensorInfo = new SensorInfoComponent(sensor, hardwareList);
        sensorInfo.setId("sensorInfoLayout");
        sensorInfo.setVisible(true);
        firstLayout.addComponentAsFirst(sensorInfo);

        setSensorFields(sensorWater);
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

    private void setSensorFields(SensorWater sensorWater) {
        sensorWaterBinder.forField(pricePerM3Field).asRequired("Required field.")
                .withConverter(Double::valueOf, String::valueOf)
                .bind(SensorWater::getPrice_per_m3, SensorWater::setPrice_per_m3);
        sensorWaterBinder.forField(implPerLitField).asRequired("Required field.")
                .withConverter(Integer::valueOf, String::valueOf)
                .bind(SensorWater::getImplPerLit, SensorWater::setImplPerLit);
        setStateFields(sensorWater);

        setStateValveFields(sensorWater);

        sensorWaterBinder.forField(countStopField).asRequired("Required field.")
                .withConverter(Integer::valueOf, String::valueOf)
                .bind(SensorWater::getCountStop, SensorWater::setCountStop);
        sensorWaterBinder.forField(countStopNightField).asRequired("Required field.")
                .withConverter(Integer::valueOf, String::valueOf)
                .bind(SensorWater::getCountStopNight, SensorWater::setCountStopNight);

        setNightTimesFields(sensorWater);

        sensorWaterBinder.readBean(sensorWater);

        sensorBinder.forField(sensorInfo.getSensorName()).asRequired("Required field.")
                .bind(Sensor::getName, Sensor::setName);
        sensorBinder.forField(sensorInfo.getLimitDay()).asRequired("Required field.")
                .withConverter(BigDecimal::doubleValue, BigDecimal::valueOf)
                .bind(Sensor::getLimit_day, Sensor::setLimit_day);
        sensorBinder.forField(sensorInfo.getLimitMonth()).asRequired("Required field.")
                .withConverter(BigDecimal::doubleValue, BigDecimal::valueOf)
                .bind(Sensor::getLimit_month, Sensor::setLimit_month);
        sensorBinder.forField(sensorInfo.getConsumptionCorrelation()).asRequired("Required field.")
                .withConverter(BigDecimal::doubleValue, BigDecimal::valueOf)
                .bind(Sensor::getConsumptionCorrelation, Sensor::setConsumptionCorrelation);
        sensorBinder.forField(sensorInfo.getCurrency()).asRequired("Required field.")
                .bind(Sensor::getCurrencyString, Sensor::setCurrencyString);

        setAttachedHardwareValue();

        sensorBinder.readBean(sensor);
    }

    private void setStateFields(SensorWater sensorWater) {
        Optional<StateValve> state = states.stream().filter(stateValve -> stateValve.getId() == sensorWater.getState()).findFirst();
        if (state.isPresent()) {
            if (state.get().getId() == 1) {
                stateTextField.setValue("Opened");
                openCloseValveButton.setText("Close valve");
            } else if (state.get().getId() == 2) {
                stateTextField.setValue("Request to open");
                openCloseValveButton.setText("Close valve");
            } else if (state.get().getId() == 3) {
                stateTextField.setValue("Closed");
                openCloseValveButton.setText("Open valve");
            } else if (state.get().getId() == 4) {
                stateTextField.setValue("Request to close");
                openCloseValveButton.setText("Open valve");
            }
        }
    }

    private void setStateValveFields(SensorWater sensorWater) {
        stateModifiedDateField.setReadOnly(true);
        stateModifiedDateField.setValue(sensorWater.getStateModifiedDate() == null ? "" : sensorWater.getStateModifiedDate()
                .toLocalDateTime().format(DateTimeFormatter.ofPattern("HH:mm:ss yyyy-MM-dd")));

        Optional<User> user = userService.get(sensorWater.getStateModifierUserId());
        user.ifPresent(value -> stateModifiedBy.setValue(value.getFullName()));
    }

    private void setNightTimesFields(SensorWater sensorWater) {
        startNightField.setPattern(PatternStringUtils.nightHoursRegex);
        startNightField.setErrorMessage(PatternStringUtils.fieldIsRequired);
        startNightField.setValue((sensorWater.getNightStartHour() == 0 ? "00" : sensorWater.getNightStartHour())
                + ":" + (sensorWater.getNightStartMinute() == 0 ? "00" : sensorWater.getNightStartMinute()));
        endNightField.setPattern(PatternStringUtils.nightHoursRegex);
        endNightField.setErrorMessage(PatternStringUtils.fieldIsRequired);
        endNightField.setValue((sensorWater.getNightEndHour() == 0 ? "00" : sensorWater.getNightEndHour())
                + ":" + (sensorWater.getNightEndMinute() == 0 ? "00" : sensorWater.getNightEndMinute()));
    }

    private void setButton(Button button, boolean b) {
        button.setEnabled(b);
        button.setVisible(b);
    }


    private void updateNightTimeFields(SensorWater sensorWater) {
        String hoursStart = startNightField.getValue();
        int indexOfDecimal = hoursStart.indexOf(":");
        String hour = hoursStart.substring(0, indexOfDecimal);
        String minute = hoursStart.substring(indexOfDecimal);
        minute = minute.substring(1);

        String hoursEnd = endNightField.getValue();
        int indexOfDecimal2 = hoursEnd.indexOf(":");
        String hour2 = hoursEnd.substring(0, indexOfDecimal2);
        String minute2 = hoursEnd.substring(indexOfDecimal2);
        minute2 = minute2.substring(1);

        sensorWater.setNightStartHour(Integer.parseInt(hour));
        sensorWater.setNightStartMinute(Integer.parseInt(minute));
        sensorWater.setNightEndHour(Integer.parseInt(hour2));
        sensorWater.setNightEndMinute(Integer.parseInt(minute2));
    }

    private void setReadOnlyFields(boolean b) {
        pricePerM3Field.setReadOnly(b);
        countStopField.setReadOnly(b);
        implPerLitField.setReadOnly(b);
        countStopNightField.setReadOnly(b);
        countStopField.setReadOnly(b);
        startNightField.setReadOnly(b);
        endNightField.setReadOnly(b);
        openCloseValveButton.setVisible(!b);

        sensorInfo.getSensorName().setReadOnly(b);
        sensorInfo.getLimitDay().setReadOnly(b);
        sensorInfo.getLimitMonth().setReadOnly(b);
        sensorInfo.getCurrency().setReadOnly(b);
        sensorInfo.getConsumptionCorrelation().setReadOnly(b);
        sensorInfo.getAttachToHardwareSelect().setReadOnly(b);
    }
}