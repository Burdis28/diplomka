package com.example.application.views.sensors;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

import com.example.application.data.entity.Sensor;
import com.example.application.data.entity.SensorTypes;
import com.example.application.data.service.SensorRepository;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.router.ParentLayout;
import com.vaadin.flow.server.VaadinSession;
import org.apache.commons.lang3.StringUtils;

import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.grid.dataview.GridListDataView;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.Grid.SelectionMode;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.grid.HeaderRow;
import com.vaadin.flow.component.gridpro.GridPro;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.littemplate.LitTemplate;
import com.vaadin.flow.component.template.Id;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.renderer.LocalDateRenderer;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.example.application.views.main.MainView;
import org.apache.tomcat.jni.Local;
import org.springframework.beans.factory.annotation.Autowired;

@JsModule("./views/sensors/sensors-view.ts")
@CssImport("./views/sensors/sensors-view.css")
@Tag("sensors-view")
@ParentLayout(MainView.class)
@PageTitle("Sensors")
public class SensorsView extends LitTemplate {

    @Id("grid")
    private GridPro<Sensor> grid;

    private GridListDataView<Sensor> gridListDataView;

    private Grid.Column<Sensor> idColumn;
    private Grid.Column<Sensor> nameColumn;
    private Grid.Column<Sensor> limitDay;
    private Grid.Column<Sensor> limitMonth;
    private Grid.Column<Sensor> typeColumn;
    private Grid.Column<Sensor> dateColumn;
    private Grid.Column<Sensor> buttonColumn;


    private final SensorRepository sensorRepository;

    public SensorsView(@Autowired SensorRepository sensorRepository) {
        this.sensorRepository = sensorRepository;
        createGrid();
    }

    private void createGrid() {
        createGridComponent();
        addColumnsToGrid();
        addFiltersToGrid();
    }

    private void createGridComponent() {
        grid.setSelectionMode(SelectionMode.MULTI);
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER, GridVariant.LUMO_COLUMN_BORDERS);
        grid.setHeight("100%");

        List<Sensor> sensors = getSensors();
        gridListDataView = grid.setItems(sensors);
    }

    private void addColumnsToGrid() {
        createIdColumn();
        createNameColumn();
        createLimitDayColumn();
        createLimitMonthColumn();
        createTypeColumn();
        createDateColumn();
        createButtonColumn();
    }

    private void createIdColumn() {
        idColumn = grid.addColumn(Sensor::getId, "id").setHeader("ID").setWidth("120px").setFlexGrow(0);
    }

    private void createNameColumn() {
        nameColumn = grid.addColumn(Sensor::getName, "name").setHeader("Name")
                .setComparator(Sensor::getName);
    }

    private void createLimitDayColumn() {
        limitDay = grid.addColumn(Sensor::getLimit_day, "limit_day").setHeader("Daily limit")
                .setWidth("200px").setFlexGrow(0).setComparator(Sensor::getLimit_day);
    }

    private void createLimitMonthColumn() {
        limitMonth = grid.addColumn(Sensor::getLimit_day, "limit_month").setHeader("Monthly limit")
                .setWidth("200px").setFlexGrow(0).setComparator(Sensor::getLimit_day);
    }

    private void createTypeColumn() {
        typeColumn = grid.addColumn(new ComponentRenderer<>(sensor -> {
            Span span = new Span();
            span.setText(SensorTypes.valueOf(sensor.getType()).toString());
            span.getElement().setAttribute("theme", getBadgeType(sensor));
            return span;
        })).setComparator(Sensor::getType).setHeader("Type");
    }

    private String getBadgeType(Sensor sensor) {
        return switch (sensor.getType()) {
            case "w" -> "badge primary";
            case "e" -> "badge error primary";
            case "g" -> "badge success primary";
            default -> "";
        };
    }

    private void createDateColumn() {
        dateColumn = grid
                .addColumn(Sensor::getCreatedDate)
                .setComparator(Sensor::getCreatedDate).setHeader("Created date");
    }

    private void createButtonColumn() {
        buttonColumn = grid.addColumn(new ComponentRenderer<>(sensor -> {
            Button detailButton = new Button();
            detailButton.setText("Detail");
            detailButton.setThemeName("secondary");
            detailButton.addClickListener(buttonClickEvent -> {
                Notification.show(sensor.getId().toString());
                VaadinSession.getCurrent().setAttribute("sensorId", sensor.getId());
                switch (sensor.getType()) {
                    case "e": UI.getCurrent().navigate("sensor-el-detail");
                        return;
                    case "w": UI.getCurrent().navigate("sensor-wat-detail");
                        return;
                    case "g": UI.getCurrent().navigate("sensor-gas-detail");
                        return;
                    default:
                }
            });
            return detailButton;
        })).setHeader("").setWidth("120px").setFlexGrow(0);
    }

    /*
    TODO
    private String parseDate(Timestamp date) {
        return date.get
    }
    */

    private void addFiltersToGrid() {
        HeaderRow filterRow = grid.appendHeaderRow();

        TextField idFilter = new TextField();
        idFilter.setPlaceholder("Filter");
        idFilter.setClearButtonVisible(true);
        idFilter.setWidth("100%");
        idFilter.setValueChangeMode(ValueChangeMode.EAGER);
        idFilter.addValueChangeListener(event -> gridListDataView.addFilter(
                client -> StringUtils.containsIgnoreCase(Integer.toString(client.getId()), idFilter.getValue())));
        filterRow.getCell(idColumn).setComponent(idFilter);

        TextField nameFilter = new TextField();
        nameFilter.setPlaceholder("Filter");
        nameFilter.setClearButtonVisible(true);
        nameFilter.setWidth("100%");
        nameFilter.setValueChangeMode(ValueChangeMode.EAGER);
        nameFilter.addValueChangeListener(event -> gridListDataView
                .addFilter(sensor -> StringUtils.containsIgnoreCase(sensor.getName(), nameFilter.getValue())));
        filterRow.getCell(nameColumn).setComponent(nameFilter);

        TextField dailyLimitFilter = new TextField();
        dailyLimitFilter.setPlaceholder("Filter");
        dailyLimitFilter.setClearButtonVisible(true);
        dailyLimitFilter.setWidth("100%");
        dailyLimitFilter.setValueChangeMode(ValueChangeMode.EAGER);
        dailyLimitFilter.addValueChangeListener(event -> gridListDataView.addFilter(sensor -> StringUtils
                .containsIgnoreCase(Double.toString(sensor.getLimit_day()), dailyLimitFilter.getValue())));
        filterRow.getCell(limitDay).setComponent(dailyLimitFilter);

        TextField monthlyLimitFilter = new TextField();
        monthlyLimitFilter.setPlaceholder("Filter");
        monthlyLimitFilter.setClearButtonVisible(true);
        monthlyLimitFilter.setWidth("100%");
        monthlyLimitFilter.setValueChangeMode(ValueChangeMode.EAGER);
        monthlyLimitFilter.addValueChangeListener(event -> gridListDataView.addFilter(sensor -> StringUtils
                .containsIgnoreCase(Double.toString(sensor.getLimit_month()), monthlyLimitFilter.getValue())));
        filterRow.getCell(limitMonth).setComponent(monthlyLimitFilter);

        ComboBox<String> typeFilter = new ComboBox<>();
        typeFilter.setItems(Arrays.asList("Water", "Electric", "Gas"));
        typeFilter.setPlaceholder("Filter");
        typeFilter.setClearButtonVisible(true);
        typeFilter.setWidth("100%");
        typeFilter.addValueChangeListener(
                event -> gridListDataView.addFilter(sensor -> areTypesEqual(sensor, typeFilter)));
        filterRow.getCell(typeColumn).setComponent(typeFilter);

        DatePicker dateFilter = new DatePicker();
        dateFilter.setPlaceholder("Filter");
        dateFilter.setClearButtonVisible(true);
        dateFilter.setWidth("100%");
        dateFilter.addValueChangeListener(
                event -> gridListDataView.addFilter(sensor -> areDatesEqual(sensor, dateFilter)));
        filterRow.getCell(dateColumn).setComponent(dateFilter);
    }

    private boolean areTypesEqual(Sensor sensor, ComboBox<String> statusFilter) {
        String statusFilterValue = statusFilter.getValue();
        if (statusFilterValue != null) {
            return StringUtils.equals(SensorTypes.valueOf(sensor.getType()).toString(), statusFilterValue);
        }
        return true;
    }

    private boolean areDatesEqual(Sensor sensor, DatePicker dateFilter) {
        LocalDate dateFilterValue = dateFilter.getValue();
        if (dateFilterValue != null) {
            Timestamp timestamp = Timestamp.valueOf(dateFilterValue.atStartOfDay());
            return timestamp.equals(sensor.getCreatedDate());
        }
        return true;
    }

    private List<Sensor> getSensors() {
        return sensorRepository.findAll();
    }
}
