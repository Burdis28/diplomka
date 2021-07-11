package com.example.application.views.dashboard;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.charts.Chart;
import com.vaadin.flow.component.charts.model.Configuration;
import com.vaadin.flow.component.charts.model.Crosshair;
import com.vaadin.flow.component.charts.model.ListSeries;
import com.vaadin.flow.component.charts.model.XAxis;
import com.vaadin.flow.component.charts.model.YAxis;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.template.Id;
import com.vaadin.flow.component.littemplate.LitTemplate;
import com.vaadin.flow.data.renderer.TemplateRenderer;
import com.vaadin.flow.router.ParentLayout;
import com.vaadin.flow.router.PageTitle;
import com.example.application.views.main.MainView;

@JsModule("./views/dashboard/dashboard-view.ts")
@CssImport("./views/dashboard/dashboard-view.css")
@Tag("dashboard-view")
@ParentLayout(MainView.class)
@PageTitle("Dashboard")
public class DashboardView extends LitTemplate {

    @Id
    private Grid<HealthGridItem> grid;

    @Id
    private Chart monthlyVisitors;
    @Id
    private Chart responseTimes;

    @Id
    private H2 currentUsers;
    @Id
    private H2 numEvents;
    @Id
    private H2 conversionRate;

    public DashboardView() {
        TemplateRenderer<HealthGridItem> template = TemplateRenderer
                .<HealthGridItem>of("<span theme$=\"[[item.theme]]\">[[item.status]]</span>")
                .withProperty("theme", HealthGridItem::getTheme).withProperty("status", HealthGridItem::getStatus);

        grid.addColumn(HealthGridItem::getCity).setHeader("City");
        grid.addColumn(template).setHeader("Status").setFlexGrow(0).setAutoWidth(true);
        grid.addColumn(HealthGridItem::getDate).setHeader("Date").setFlexGrow(0).setAutoWidth(true);

        populateCharts();
    }

    private void populateCharts() {
        // Set some data when this view is displayed.

        // Top row widgets
        currentUsers.setText("745");
        numEvents.setText("54.6k");
        conversionRate.setText("18%");

        // First chart
        Configuration configuration = monthlyVisitors.getConfiguration();
        configuration.addSeries(new ListSeries("Tokyo", 49.9, 71.5, 106.4, 129.2, 144.0, 176.0, 135.6, 148.5, 216.4,
                194.1, 95.6, 54.4));
        configuration.addSeries(
                new ListSeries("New York", 83.6, 78.8, 98.5, 93.4, 106.0, 84.5, 105.0, 104.3, 91.2, 83.5, 106.6, 92.3));
        configuration.addSeries(
                new ListSeries("London", 48.9, 38.8, 39.3, 41.4, 47.0, 48.3, 59.0, 59.6, 52.4, 65.2, 59.3, 51.2));
        configuration.addSeries(
                new ListSeries("Berlin", 42.4, 33.2, 34.5, 39.7, 52.6, 75.5, 57.4, 60.4, 47.6, 39.1, 46.8, 51.1));

        XAxis x = new XAxis();
        x.setCrosshair(new Crosshair());
        x.setCategories("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec");
        configuration.addxAxis(x);

        YAxis y = new YAxis();
        y.setMin(0);
        configuration.addyAxis(y);

        // Grid
        List<HealthGridItem> gridItems = new ArrayList<>();
        gridItems.add(new HealthGridItem(LocalDate.of(2019, 1, 14), "M\u00FCnster", "Germany", "Good", "badge"));
        gridItems.add(new HealthGridItem(LocalDate.of(2019, 1, 14), "Cluj-Napoca", "Romania", "Good", "badge"));
        gridItems.add(new HealthGridItem(LocalDate.of(2019, 1, 14), "Ciudad Victoria", "Mexico", "Good", "badge"));
        gridItems.add(new HealthGridItem(LocalDate.of(2019, 1, 14), "Ebetsu", "Japan", "Excellent", "badge success"));
        gridItems.add(
                new HealthGridItem(LocalDate.of(2019, 1, 14), "S\u00E3o Bernardo do Campo", "Brazil", "Good", "badge"));
        gridItems.add(new HealthGridItem(LocalDate.of(2019, 1, 14), "Maputo", "Mozambique", "Good", "badge"));
        gridItems.add(new HealthGridItem(LocalDate.of(2019, 1, 14), "Warsaw", "Poland", "Good", "badge"));
        gridItems.add(new HealthGridItem(LocalDate.of(2019, 1, 14), "Kasugai", "Japan", "Failing", "badge error"));
        gridItems.add(new HealthGridItem(LocalDate.of(2019, 1, 14), "Lancaster", "United States", "Excellent",
                "badge success"));

        grid.setItems(gridItems);

        // Second chart
        configuration = responseTimes.getConfiguration();
        configuration
                .addSeries(new ListSeries("Tokyo", 7.0, 6.9, 9.5, 14.5, 18.2, 21.5, 25.2, 26.5, 23.3, 18.3, 13.9, 9.6));
        configuration
                .addSeries(new ListSeries("London", 3.9, 4.2, 5.7, 8.5, 11.9, 15.2, 17.0, 16.6, 14.2, 10.3, 6.6, 4.8));

        x = new XAxis();
        x.setCrosshair(new Crosshair());
        x.setCategories("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec");
        configuration.addxAxis(x);

        y = new YAxis();
        y.setMin(0);
        configuration.addyAxis(y);
    }
}
