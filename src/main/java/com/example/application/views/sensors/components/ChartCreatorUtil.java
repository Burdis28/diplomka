package com.example.application.views.sensors.components;

import com.example.application.data.entity.Sensor;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.charts.Chart;
import com.vaadin.flow.component.charts.model.*;

public class ChartCreatorUtil {

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
}
