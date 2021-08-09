package com.example.application.views.sensors;

import com.example.application.components.notifications.ErrorNotification;
import com.example.application.data.entity.Sensor;
import com.example.application.data.entity.SensorWater;
import com.example.application.data.service.SensorService;
import com.example.application.data.service.SensorWaterService;
import com.example.application.views.main.MainView;
import com.example.application.views.sensors.components.SensorInfoComponent;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
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
 * A Designer generated component for the water-sensor-view template.
 * <p>
 * Designer will add and remove fields with @Id mappings but
 * does not overwrite or otherwise change this file.
 */
@Tag("water-sensor-view")
@JsModule("./views/sensors/water-sensor-view.ts")
@CssImport("./views/sensors/water-sensor-view.css")
@ParentLayout(MainView.class)
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
    @Id("pricePerM3Field")
    private BigDecimalField pricePerM3;
    @Id("countStopField")
    private BigDecimalField countStopField;
    @Id("implPerLiterField")
    private BigDecimalField implPerLiterField;
    @Id("countStopNightField")
    private BigDecimalField countStopNightField;
    @Id("nightStartHourField")
    private BigDecimalField nightStartHourField;
    @Id("pricePerM3FieldSuffix")
    private Div pricePerM3FieldSuffix;

    private final SensorService sensorService;
    private final SensorWaterService sensorWaterService;
    private SensorWater sensorWater;
    private Sensor sensor;
    private SensorInfoComponent sensorInfo;

    /**
     * Creates a new WaterSensorView.
     *
     * @param sensorService
     * @param sensorWaterService
     */
    public WaterSensorView(SensorService sensorService, SensorWaterService sensorWaterService) {
        // You can initialise any data required for the connected UI components here.
        this.sensorService = sensorService;
        this.sensorWaterService = sensorWaterService;

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

            setSensorFields(sensorWater);
        });

        saveButton.addClickListener(buttonClickEvent -> {
            try {
                //TODO finish all attributes
                sensorWater.setPrice_per_m3(pricePerM3.getValue().floatValue());

                sensor.setName(sensorInfo.getSensorName().getValue());
                sensor.setLimit_day(sensorInfo.getLimitDay().getValue().doubleValue());
                sensor.setLimit_month(sensorInfo.getLimitMonth().getValue().doubleValue());
                sensor.setConsumptionActual(sensorInfo.getConsumptionActual().getValue().doubleValue());
                sensor.setConsumptionCorrelation(sensorInfo.getConsumptionCorrelation().getValue().doubleValue());
                sensor.setCurrencyString(sensorInfo.getCurrency().getValue());

                sensorWaterService.update(sensorWater);
                sensorService.update(sensor);

                setButton(saveButton, false);
                setButton(returnButton, true);
                setButton(cancelButton, false);
                setButton(editButton, true);

                setReadOnlyFields(true);

                setSensorFields(sensorWater);
            } catch (Exception e) {
                ErrorNotification error = new ErrorNotification();
                error.setErrorText("Špatně zadaná vstupní data formuláře.");
                error.open();
            }
        });
        returnButton.addClickListener(buttonClickEvent ->
        {
            UI.getCurrent().navigate("sensors");
        });
    }

    public void setSensor() {
        int sensorId = -1;
        sensorId = (int) VaadinSession.getCurrent().getAttribute("sensorId");
        if (sensorId != -1) {
            this.sensorWaterService.get(sensorId).ifPresent(water -> sensorWater = water);
            this.sensorService.get(sensorId).ifPresent(sensor -> this.sensor = sensor);
        }
        String currency = sensor.getCurrencyString();
        pricePerM3FieldSuffix.setText(currency);

        sensorInfo = new SensorInfoComponent(sensor);
        sensorInfo.setId("sensorInfoLayout");
        sensorInfo.setVisible(true);
        firstLayout.addComponentAsFirst(sensorInfo);
        //vaadinHorizontalLayout.addComponentAtIndex(0, sensorInfo);
        setSensorFields(sensorWater);
    }

    private void setSensorFields(SensorWater sensorWater) {
        //TODO finish all attributes
        pricePerM3.setValue(BigDecimal.valueOf(sensorWater.getPrice_per_m3()));

        sensorInfo.getSensorName().setValue(sensor.getName());
        sensorInfo.getLimitDay().setValue(BigDecimal.valueOf(sensor.getLimit_day()));
        sensorInfo.getLimitMonth().setValue(BigDecimal.valueOf(sensor.getLimit_month()));
        sensorInfo.getConsumptionActual().setValue(BigDecimal.valueOf(sensor.getConsumptionActual()));
        sensorInfo.getConsumptionCorrelation().setValue(BigDecimal.valueOf(sensor.getConsumptionCorrelation()));
        sensorInfo.getCurrency().setValue(sensor.getCurrencyString());
    }

    private void setButton(Button button, boolean b) {
        button.setEnabled(b);
        button.setVisible(b);
    }

    private void setReadOnlyFields(boolean b) {
        //TODO finish all attributes
        pricePerM3.setReadOnly(b);
        countStopField.setReadOnly(b);
        implPerLiterField.setReadOnly(b);
        countStopNightField.setReadOnly(b);
        nightStartHourField.setReadOnly(b);

        sensorInfo.getSensorName().setReadOnly(b);
        sensorInfo.getLimitDay().setReadOnly(b);
        sensorInfo.getLimitMonth().setReadOnly(b);
        sensorInfo.getConsumptionActual().setReadOnly(b);
        sensorInfo.getConsumptionCorrelation().setReadOnly(b);
        sensorInfo.getCurrency().setReadOnly(b);
    }
}
