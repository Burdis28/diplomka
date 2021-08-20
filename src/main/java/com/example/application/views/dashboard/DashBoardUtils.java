package com.example.application.views.dashboard;

import com.example.application.utils.Colors;
import com.example.application.utils.MathUtils;
import com.vaadin.flow.component.charts.Chart;
import com.vaadin.flow.component.charts.model.*;
import com.vaadin.flow.component.charts.model.style.SolidColor;

public class DashBoardUtils {

    public static Chart createConsumptionGauge(double consumption, double consumptionMaxValue, boolean electric) {
        Chart chart = new Chart(ChartType.SOLIDGAUGE);
        chart.setClassName("gauge");

        Configuration configuration = chart.getConfiguration();

        Pane pane = configuration.getPane();
        pane.setCenter(new String[] {"50%", "50%"});
        pane.setStartAngle(-140);
        pane.setEndAngle(140);
        chart.setHeight("250px");
        chart.setWidth("300px");

        Background paneBackground = new Background();
        paneBackground.setInnerRadius("60%");
        paneBackground.setOuterRadius("100%");
        paneBackground.setShape(BackgroundShape.ARC);
        pane.setBackground(paneBackground);

        YAxis yAxis = configuration.getyAxis();
        yAxis.setTickAmount(2);
        //yAxis.setTitle("Consumption");
        yAxis.setMinorTickInterval("null");
        yAxis.getTitle().setY(-50);
        yAxis.getLabels().setEnabled(false);
        //yAxis.getLabels().setY(16);
        yAxis.setMin(0);
        yAxis.setMax(100);

        PlotOptionsSolidgauge plotOptionsSolidgauge = new PlotOptionsSolidgauge();

        DataLabels dataLabels = plotOptionsSolidgauge.getDataLabels();
        dataLabels.setY(0);
        dataLabels.setUseHTML(true);

        configuration.setPlotOptions(plotOptionsSolidgauge);

        DataSeries series = new DataSeries("Consumption");

        DataSeriesItem item = new DataSeriesItem();
        item.setY(MathUtils.round(((consumption/consumptionMaxValue) * 100), 2));
        item.setColor(new SolidColor(Colors.CEZ_TYPE_ORANGE.getRgb()));
        item.setClassName("myClassName");
        DataLabels dataLabelsSeries = new DataLabels();
        if (electric) {
            dataLabelsSeries.setFormat("<div style=\"text-align:center \"><span style=\"font-size:16px;"
                    + "color: #666666;' + '\">{y} %</span><br/>"
                    + "<span style=\"text-align:center font-size:12px;color:silver\">kWh</span></div>");
        } else {
            dataLabelsSeries.setFormat("<div style=\"text-align:center \"><span style=\"font-size:16px;"
                    + "color: #666666;' + '\">{y} %</span><br/>"
                    + "<span style=\"text-align:center font-size:12px;color:silver\">m3</span></div>");
        }
        dataLabelsSeries.setY(-25);
        configuration.getChart().setBackgroundColor(new SolidColor(255,255,255,0.0));
        item.setDataLabels(dataLabelsSeries);

        series.add(item);
        configuration.addSeries(series);
        return chart;
    }
}
