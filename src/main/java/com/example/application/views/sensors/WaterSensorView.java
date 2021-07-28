package com.example.application.views.sensors;

import com.example.application.components.notifications.ErrorNotification;
import com.example.application.data.entity.Sensor;
import com.example.application.data.entity.SensorElectric;
import com.example.application.data.entity.SensorWater;
import com.example.application.data.entity.StateValve;
import com.example.application.data.service.SensorService;
import com.example.application.data.service.SensorWaterService;
import com.example.application.views.main.MainView;
import com.example.application.views.sensors.components.SensorInfoComponent;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.littemplate.LitTemplate;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.template.Id;
import com.vaadin.flow.component.textfield.BigDecimalField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.timepicker.TimePicker;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.converter.StringToIntegerConverter;
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
    @Id("countStopField")
    private TextField countStopField;
    @Id("countStopNightField")
    private TextField countStopNightField;

    private final SensorService sensorService;
    private final SensorWaterService sensorWaterService;
    private SensorWater sensorWater;
    private Sensor sensor;
    private SensorInfoComponent sensorInfo;
    @Id("implPerLitField")
    private TextField implPerLitField;
    @Id("stateSelect")
    private Select<StateValve> stateSelect;
    @Id("stateModifiedDateField")
    private TextField stateModifiedDateField;
    @Id("stateModifiedBy")
    private TextField stateModifiedBy;
    @Id("nightStartTimeField")
    private TimePicker nightStartTimeField;
    @Id("nightEndTimeField")
    private TimePicker nightEndTimeField;
    @Id("pricePerM3Field")
    private TextField pricePerM3Field;

    private Binder<Sensor> sensorBinder = new Binder<>();
    private Binder<SensorWater> sensorWaterBinder = new Binder<>();
    @Id("timeBetweenImplField")
    private TextField timeBetweenImplField;

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

            //setSensorFields(sensorWater);
        });

        saveButton.addClickListener(buttonClickEvent -> {
            try {
                sensorWaterBinder.writeBean(sensorWater);
                sensorBinder.writeBean(sensor);

                sensorWaterService.update(sensorWater);
                sensorService.update(sensor);

                setButton(saveButton, false);
                setButton(returnButton, true);
                setButton(cancelButton, false);
                setButton(editButton, true);

                setReadOnlyFields(true);

                //setSensorFields(sensorWater);
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
        sensorInfo = new SensorInfoComponent(sensor);
        sensorInfo.setId("sensorInfoLayout");
        sensorInfo.setVisible(true);
        firstLayout.addComponentAsFirst(sensorInfo);

        sensorInfo.getConsumptionActual().setReadOnly(true);
        sensorInfo.getConsumptionCorrelation().setReadOnly(true);
        setSensorFields(sensorWater);

        String currency = sensor.getCurrencyString();
        //pricePerM3Field.(new Text(currency));
    }

    private void setSensorFields(SensorWater sensorWater) {
        sensorWaterBinder.forField(pricePerM3Field).asRequired("Required field.").withConverter(Double::valueOf, String::valueOf)
                .bind(SensorWater::getPrice_per_m3, SensorWater::setPrice_per_m3);
        sensorWaterBinder.forField(implPerLitField).asRequired("Required field.").withConverter(Integer::valueOf, String::valueOf)
                .bind(SensorWater::getImplPerLit, SensorWater::setImplPerLit);
        // state field

        sensorWaterBinder.forField(timeBetweenImplField).asRequired("Required field.").withConverter(Integer::valueOf, String::valueOf)
                .bind(SensorWater::getTimeBtwImpl, SensorWater::setTimeBtwImpl);
        sensorWaterBinder.forField(countStopField).asRequired("Required field.").withConverter(Integer::valueOf, String::valueOf)
                .bind(SensorWater::getCountStop, SensorWater::setCountStop);
        sensorWaterBinder.forField(countStopNightField).asRequired("Required field.").withConverter(Integer::valueOf, String::valueOf)
                .bind(SensorWater::getCountStopNight, SensorWater::setCountStopNight);

        sensorWaterBinder.readBean(sensorWater);

        sensorBinder.forField(sensorInfo.getSensorName()).asRequired("Required field.").bind(Sensor::getName, Sensor::setName);
        sensorBinder.forField(sensorInfo.getLimitDay()).asRequired("Required field.").withConverter(BigDecimal::doubleValue, BigDecimal::new)
                .bind(Sensor::getLimit_day, Sensor::setLimit_day);
        sensorBinder.forField(sensorInfo.getLimitMonth()).asRequired("Required field.").withConverter(BigDecimal::doubleValue, BigDecimal::new)
                .bind(Sensor::getLimit_month, Sensor::setLimit_month);
        sensorBinder.forField(sensorInfo.getConsumptionActual()).asRequired("Required field.").withConverter(BigDecimal::doubleValue, BigDecimal::new)
                .bind(Sensor::getConsumptionActual, Sensor::setConsumptionActual);
        sensorBinder.forField(sensorInfo.getConsumptionCorrelation()).asRequired("Required field.").withConverter(BigDecimal::doubleValue, BigDecimal::new)
                .bind(Sensor::getConsumptionCorrelation, Sensor::setConsumptionCorrelation);
        sensorBinder.forField(sensorInfo.getCurrency()).asRequired("Required field.").bind(Sensor::getCurrencyString, Sensor::setCurrencyString);

        sensorBinder.readBean(sensor);
    }

    private void setButton(Button button, boolean b) {
        button.setEnabled(b);
        button.setVisible(b);
    }

    private void setReadOnlyFields(boolean b) {
        pricePerM3Field.setReadOnly(b);
        countStopField.setReadOnly(b);
        implPerLitField.setReadOnly(b);
        stateSelect.setReadOnly(b);
        countStopNightField.setReadOnly(b);
        countStopField.setReadOnly(b);
        nightStartTimeField.setReadOnly(b);
        nightEndTimeField.setReadOnly(b);

        sensorInfo.getSensorName().setReadOnly(b);
        sensorInfo.getLimitDay().setReadOnly(b);
        sensorInfo.getLimitMonth().setReadOnly(b);
        sensorInfo.getCurrency().setReadOnly(b);
    }
}
