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
    private String unit;
    private Double consumptionActualNumber;
    private DataSeries series;
    private DataSeriesItem item;

    public SensorInfoComponent(Sensor sensor) {
        layout = getLayout();
        if(layout == null) {
            layout = new VerticalLayout();
        }
        unit = sensor.getType().equals("e") ? "kWh": "m³";
        consumptionActualNumber = sensor.getConsumptionActual();
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
        Configuration configuration = consumptionChart.getConfiguration();
        configuration.setTitle("Today's consumption");
        consumptionChart.setClassName("solidGaugeConsumption");
        consumptionChart.setHeight("350px");
        consumptionChart.setWidth("400px");

        Pane pane = configuration.getPane();
        pane.setSize("125%");
        pane.setCenter(new String[] {"50%", "70%"});
        pane.setStartAngle(-90);
        pane.setEndAngle(90);

        Background paneBackground = new Background();
        paneBackground.setInnerRadius("60%");
        paneBackground.setOuterRadius("100%");
        paneBackground.setShape(BackgroundShape.ARC);
        pane.setBackground(paneBackground);

        YAxis yAxis = configuration.getyAxis();
        yAxis.getTitle().setY(-50);
        yAxis.getLabels().setY(20);
        yAxis.setMin(0);
        yAxis.setMax(sensor.getLimit_day().intValue());

        PlotOptionsSolidgauge plotOptionsSolidgauge = new PlotOptionsSolidgauge();

        DataLabels dataLabels = plotOptionsSolidgauge.getDataLabels();
        dataLabels.setY(5);
        dataLabels.setUseHTML(true);

        configuration.setPlotOptions(plotOptionsSolidgauge);

        series = new DataSeries("Consumption");

        item = new DataSeriesItem();
        item.setY(consumptionActualNumber);
        item.setClassName("myConsumptionGauge");
        DataLabels dataLabelsSeries = new DataLabels();
        dataLabelsSeries.setFormat("<div style=\"text-align:center\"><span style=\"font-size:25px;"
                + "color:black' + '\">{y}</span><br/>"
                + "<span style=\"font-size:12px;color:silver\">" + unit +"</span></div>");

        item.setDataLabels(dataLabelsSeries);

        series.add(item);

        configuration.addSeries(series);

        return consumptionChart;
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
            item.setY(consumption);
            series.update(item);
            ui.push();
        }));
    }
}
