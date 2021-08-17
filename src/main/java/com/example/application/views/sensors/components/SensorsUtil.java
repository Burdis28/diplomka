package com.example.application.views.sensors.components;

import com.example.application.data.entity.HardwareLive;
import com.example.application.data.entity.Sensor;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.charts.Chart;
import com.vaadin.flow.component.charts.model.*;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.server.VaadinSession;

public class SensorsUtil {

    public static void navigateToSensorDetail(Sensor sensor) {
        VaadinSession.getCurrent().setAttribute("sensorId", sensor.getId());
        switch (sensor.getType()) {
            case "e":
                UI.getCurrent().navigate("sensor-el-detail");
                return;
            case "w":
                UI.getCurrent().navigate("sensor-wat-detail");
                return;
            case "g":
                UI.getCurrent().navigate("sensor-gas-detail");
                return;
            default:
        }
    }


    public static Component createConsumptionChart(Sensor sensor, Chart consumptionChart, String height,
                                                   String width, String title, String paneSize) {
        Configuration configuration = consumptionChart.getConfiguration();
        if(title != null) configuration.setTitle(title);
        consumptionChart.setClassName("solidGaugeConsumption");
        consumptionChart.setHeight(height);
        consumptionChart.setWidth(width);
        String unit = sensor.getType().equals("e") ? "kWh": "m³";

        Pane pane = configuration.getPane();
        pane.setSize(paneSize);
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

        DataSeries series = new DataSeries("Consumption");

        DataSeriesItem item = new DataSeriesItem();
        item.setY(sensor.getConsumptionActual());
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

    public static void setSignalImage(HardwareLive live, Div span, Image image) {
        if(live!= null) {
            if (live.getSignal_strength() >= 75) {
                image.setSrc("images/signal_100.png");
                image.setHeight("30px");
                image.setWidth("30px");
                span.add(image);
            } else if (live.getSignal_strength() >= 50) {
                image.setSrc("images/signal_75.png");
                image.setHeight("30px");
                image.setWidth("30px");
                span.add(image);
            } else if (live.getSignal_strength() >= 25) {
                image.setSrc("images/signal_50.png");
                image.setHeight("30px");
                image.setWidth("30px");
                span.add(image);
            } else if (live.getSignal_strength() < 25 && live.getSignal_strength() != 0){
                image.setSrc("images/signal_25.png");
                image.setHeight("30px");
                image.setWidth("30px");
                span.add(image);
            } else {
                image.setSrc("images/signal_0.png");
                image.setHeight("30px");
                image.setWidth("30px");
                span.add(image);
            }
        } else {
            image.setSrc("images/signal_0.png");
            image.setHeight("30px");
            image.setWidth("30px");
            span.add(image);
        }
    }

    public static void updateSignalImage(HardwareLive live, Image image) {
        if(live!= null) {
            if (live.getSignal_strength() >= 75) {
                image.setSrc("images/signal_100.png");
            } else if (live.getSignal_strength() >= 50) {
                image.setSrc("images/signal_75.png");
            } else if (live.getSignal_strength() >= 25) {
                image.setSrc("images/signal_50.png");
            } else if (live.getSignal_strength() < 25 && live.getSignal_strength() != 0){
                image.setSrc("images/signal_25.png");
            } else {
                image.setSrc("images/signal_0.png");
            }
        } else {
            image.setSrc("images/signal_0.png");
        }
    }

    public static String getBadgeType(Sensor sensor) {
        switch (sensor.getType()) {
            case "w": return "badge primary";
            //case "e": return "badge error primary";
            case "g": return "badge success primary";
            default: return "";
        }
    }
}
