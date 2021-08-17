package com.example.application.views.sensors.components;

import com.example.application.data.entity.Hardware;
import com.example.application.data.entity.Sensor;
import com.example.application.data.entity.SensorTypes;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.BigDecimalField;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;

import java.math.BigDecimal;
import java.time.ZoneOffset;
import java.util.List;

public class SensorInfoComponent extends VerticalLayout {

    private VerticalLayout layout;
    private final H3 sensorAttributesTitle = new H3();
    private final HorizontalLayout nameAndTypeLayout = new HorizontalLayout();
    private TextField sensorName = new TextField();
    private final Span type;
    private DateTimePicker createdDatePicker = new DateTimePicker();
    private BigDecimalField limitDay = new BigDecimalField();
    private BigDecimalField limitMonth = new BigDecimalField();
    private BigDecimalField consumptionActual = new BigDecimalField();
    private BigDecimalField consumptionCorrelation = new BigDecimalField();
    private Select<Hardware> attachToHardwareSelect = new Select<>();
    private IntegerField pinIdField = new IntegerField();
    private TextField currency = new TextField();

    public SensorInfoComponent(Sensor sensor, List<Hardware> hardwares) {
        layout = getLayout();
        if(layout == null) {
            layout = new VerticalLayout();
        }

        layout.setAlignItems(FlexComponent.Alignment.CENTER);
        layout.setId("sensorInfoLayout");
        sensorName.setLabel("Sensor name");
        sensorName.setValue(sensor.getName());
        sensorName.setWidthFull();
        type = getTypeBadge(sensor);
        sensorAttributesTitle.setText("Sensor info");
        sensorAttributesTitle.setId("sensorAttributesH3");
        add(sensorAttributesTitle);
        type.setId("typeSpan");
        nameAndTypeLayout.add(sensorName, type);
        add(nameAndTypeLayout);
        createdDatePicker.setLabel("Created date");
        createdDatePicker.setValue(sensor.getCreatedDate().toInstant().atZone(ZoneOffset.UTC).toLocalDateTime());
        createdDatePicker.setReadOnly(true);
        add(createdDatePicker);
        currency.setLabel("Currency");
        currency.setValue(sensor.getCurrencyString());
        currency.setWidthFull();
        add(currency);
        consumptionCorrelation.setLabel("Consumption correlation");
        consumptionCorrelation.setValue(BigDecimal.valueOf(sensor.getConsumptionCorrelation()));
        consumptionCorrelation.setWidthFull();
        add(consumptionCorrelation);
        limitDay.setLabel("Limit day");
        limitDay.setValue(BigDecimal.valueOf(sensor.getLimit_day()));
        limitDay.setWidthFull();
        add(limitDay);
        limitMonth.setLabel("Limit month");
        limitMonth.setValue(BigDecimal.valueOf(sensor.getLimit_month()));
        limitMonth.setWidthFull();
        add(limitMonth);
        attachToHardwareSelect.setLabel("Attached to hardware");
        attachToHardwareSelect.setItems(hardwares);
        attachToHardwareSelect.setTextRenderer(item -> item.getName() + " [" + item.getSerial_HW() + "]");
        attachToHardwareSelect.setWidthFull();
        add(attachToHardwareSelect);
        pinIdField.setLabel("Pin ID");
        pinIdField.setValue(sensor.getPinId());
        pinIdField.setReadOnly(true);
        pinIdField.setWidthFull();
        add(pinIdField);
    }

    private Span getTypeBadge(Sensor sensor) {
        Span span = new Span();
        span.setText(SensorTypes.valueOf(sensor.getType()).toString());
        if (sensor.getType().equals(SensorTypes.e.name())) {
            span.setClassName("cez_color");
        } else {
            span.getElement().setAttribute("theme", SensorsUtil.getBadgeType(sensor));
        }
        span.setWidth("80px");
        span.setHeight("30px");
        return span;
    }

    public VerticalLayout getLayout() {
        return layout;
    }

    public void setLayout(VerticalLayout layout) {
        this.layout = layout;
    }

    public TextField getSensorName() {
        return sensorName;
    }

    public void setSensorName(TextField textField) {
        this.sensorName = textField;
    }

    public Span getType() {
        return type;
    }

    public DateTimePicker getCreatedDatePicker() {
        return createdDatePicker;
    }

    public void setCreatedDatePicker(DateTimePicker createdDatePicker) {
        this.createdDatePicker = createdDatePicker;
    }

    public BigDecimalField getLimitDay() {
        return limitDay;
    }

    public void setLimitDay(BigDecimalField limitDay) {
        this.limitDay = limitDay;
    }

    public BigDecimalField getLimitMonth() {
        return limitMonth;
    }

    public void setLimitMonth(BigDecimalField limitMonth) {
        this.limitMonth = limitMonth;
    }

    public BigDecimalField getConsumptionActual() {
        return consumptionActual;
    }

    public void setConsumptionActual(BigDecimalField consumptionActual) {
        this.consumptionActual = consumptionActual;
    }

    public BigDecimalField getConsumptionCorrelation() {
        return consumptionCorrelation;
    }

    public void setConsumptionCorrelation(BigDecimalField consumptionCorrelation) {
        this.consumptionCorrelation = consumptionCorrelation;
    }

    public TextField getCurrency() {
        return currency;
    }

    public void setCurrency(TextField currency) {
        this.currency = currency;
    }

    public Select<Hardware> getAttachToHardwareSelect() {
        return attachToHardwareSelect;
    }

    public void setAttachToHardwareSelect(Select<Hardware> attachToHardwareSelect) {
        this.attachToHardwareSelect = attachToHardwareSelect;
    }

    public IntegerField getPinIdField() {
        return pinIdField;
    }

    public void setPinIdField(IntegerField pinIdField) {
        this.pinIdField = pinIdField;
    }
}
