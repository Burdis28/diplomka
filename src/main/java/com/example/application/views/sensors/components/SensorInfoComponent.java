package com.example.application.views.sensors.components;

import com.example.application.data.entity.Sensor;
import com.example.application.data.entity.SensorTypes;
import com.example.application.utils.SensorsUtils;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.charts.Chart;
import com.vaadin.flow.component.charts.model.*;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.component.textfield.BigDecimalField;
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
    private Chart consumptionChart = new Chart(ChartType.SOLIDGAUGE);
    private TextField currency = new TextField();

    public SensorInfoComponent(Sensor sensor) {
        layout = getLayout();
        if(layout == null) {
            layout = new VerticalLayout();
        }
        consumptionChart.drawChart(true);

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
        limitDay.setLabel("Limit day");
        limitDay.setValue(BigDecimal.valueOf(sensor.getLimit_day()));
        limitDay.setWidthFull();
        add(limitDay);
        limitMonth.setLabel("Limit month");
        limitMonth.setValue(BigDecimal.valueOf(sensor.getLimit_month()));
        limitMonth.setWidthFull();
        add(limitMonth);
        add(setConsumptionChart(sensor));
    }

    private Component setConsumptionChart(Sensor sensor) {
        return ChartCreatorUtil.createConsumptionChart(sensor, consumptionChart,
                "350px", "400px", "Today's consumption", "125%");
    }

    private Span getTypeBadge(Sensor sensor) {
        Span span = new Span();
        span.setText(SensorTypes.valueOf(sensor.getType()).toString());
        span.getElement().setAttribute("theme", SensorsUtils.getBadgeType(sensor));
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

    public Chart getConsumptionChart() {
        return consumptionChart;
    }

    public void setConsumptionChart(Chart consumptionChart) {
        this.consumptionChart = consumptionChart;
    }

    public void actualizeConsumptionChart(Double consumption) {
        getUI().ifPresent(ui -> ui.access(() -> {
            DataSeries series = (DataSeries)consumptionChart.getConfiguration().getSeries().get(0);
            DataSeriesItem item = series.get(0);
            item.setY(consumption);
            series.update(item);
            ui.push();
        }));
    }
}
