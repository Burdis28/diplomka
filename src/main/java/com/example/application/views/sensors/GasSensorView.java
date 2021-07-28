package com.example.application.views.sensors;

import com.example.application.components.notifications.ErrorNotification;
import com.example.application.data.entity.Sensor;
import com.example.application.data.entity.SensorGas;
import com.example.application.data.entity.SensorWater;
import com.example.application.data.service.SensorGasService;
import com.example.application.data.service.SensorService;
import com.example.application.views.main.MainView;
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
import com.vaadin.flow.component.template.Id;
import com.vaadin.flow.dom.Element;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.ParentLayout;
import com.vaadin.flow.server.VaadinSession;

import java.math.BigDecimal;

/**
 * A Designer generated component for the gas-sensor-view template.
 *
 * Designer will add and remove fields with @Id mappings but
 * does not overwrite or otherwise change this file.
 */
@Tag("gas-sensor-view")
@JsModule("./views/sensors/gas-sensor-view.ts")
@CssImport("./views/sensors/gas-sensor-view.css")
@ParentLayout(MainView.class)
@PageTitle("Gas sensor detail")
public class GasSensorView extends LitTemplate {

    @Id("editButton")
    private Button editButton;
    @Id("returnButton")
    private Button returnButton;
    @Id("saveButton")
    private Button saveButton;
    @Id("cancelButton")
    private Button cancelButton;
    @Id("gasAttributesTitle")
    private H3 gasAttributesTitle;
    @Id("verticalLayoutGas")
    private Element verticalLayoutGas;
    @Id("firstLayout")
    private HorizontalLayout firstLayout;
    @Id("vaadinHorizontalLayout")
    private HorizontalLayout vaadinHorizontalLayout;

    private final SensorService sensorService;
    private final SensorGasService sensorGasService;
    private SensorGas sensorGas;
    private Sensor sensor;
    private SensorInfoComponent sensorInfo;

    /**
     * Creates a new GasSensorView.
     * @param sensorService
     * @param sensorGasService
     */
    public GasSensorView(SensorService sensorService, SensorGasService sensorGasService) {
        // You can initialise any data required for the connected UI components here.
        this.sensorService = sensorService;
        this.sensorGasService = sensorGasService;

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

            setSensorFields();
        });

        saveButton.addClickListener(buttonClickEvent -> {
            try {
                sensor.setName(sensorInfo.getSensorName().getValue());
                sensor.setLimit_day(sensorInfo.getLimitDay().getValue().doubleValue());
                sensor.setLimit_month(sensorInfo.getLimitMonth().getValue().doubleValue());
                sensor.setCurrencyString(sensorInfo.getCurrency().getValue());

                if(sensorGas != null) {
                    sensorGasService.update(sensorGas);
                }
                sensorService.update(sensor);

                setButton(saveButton, false);
                setButton(returnButton, true);
                setButton(cancelButton, false);
                setButton(editButton, true);

                setReadOnlyFields(true);

                setSensorFields();
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
            this.sensorGasService.get(sensorId).ifPresent(gas -> sensorGas = gas);
            this.sensorService.get(sensorId).ifPresent(sensor -> this.sensor = sensor);
        }
        sensorInfo = new SensorInfoComponent(sensor);
        sensorInfo.setId("sensorInfoLayout");
        sensorInfo.setVisible(true);
        firstLayout.addComponentAsFirst(sensorInfo);

        sensorInfo.getConsumptionActual().setReadOnly(true);
        sensorInfo.getConsumptionCorrelation().setReadOnly(true);
        setSensorFields();
    }

    private void setSensorFields() {
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
        sensorInfo.getSensorName().setReadOnly(b);
        sensorInfo.getLimitDay().setReadOnly(b);
        sensorInfo.getLimitMonth().setReadOnly(b);
        sensorInfo.getCurrency().setReadOnly(b);
    }
}
