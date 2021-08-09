package com.example.application.views.sensors;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import com.example.application.data.entity.Sensor;
import com.example.application.data.entity.SensorTypes;
import com.example.application.data.service.SensorRepository;
import com.vaadin.flow.component.UI;

import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.grid.contextmenu.GridContextMenu;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
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
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.example.application.views.main.MainView;
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
    private Grid.Column<Sensor> consumptionActualColumn;
    private Grid.Column<Sensor> consumptionCorrelationColumn;
    private Grid.Column<Sensor> toolsColumn;

    private final SensorRepository sensorRepository;

    public SensorsView(@Autowired SensorRepository sensorRepository) {
        this.sensorRepository = sensorRepository;
        grid.setSelectionMode(SelectionMode.NONE);
        createGrid();
    }

    private void createGrid() {
        createGridComponent();
        addColumnsToGrid();
        addFiltersToGrid();
    }

    private void createGridComponent() {
        grid.setSelectionMode(SelectionMode.SINGLE);
        grid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        grid.setHeight("100%");

        List<Sensor> sensors = getSensors();
        grid.addItemClickListener(event -> {
            grid.select(event.getItem());
        });

        gridListDataView = grid.setItems(sensors);
        GridContextMenu<Sensor> contextMenu = grid.addContextMenu();
        createContextEditMenu(contextMenu);
        contextMenu.add(new Hr());
        createDeleteContextMenu(contextMenu);
    }

    private void createDeleteContextMenu(GridContextMenu<Sensor> contextMenu) {
        contextMenu.addItem("Delete", event -> {
            Sensor sensor = event.getItem().isPresent() ? event.getItem().get() : null;
            if(sensor != null) {
                createDeleteSensorDialog(sensor);
            }
        });
    }

    private void createDeleteSensorDialog(Sensor sensor) {
        Notification.show(sensor.getId().toString());
        ConfirmDialog dialog = new ConfirmDialog();
        dialog.setHeader("Confirm delete");
        dialog.setText("Are you sure you want to delete \"" + sensor.getName() + "\" sensor?");
        dialog.setCancelable(true);
        dialog.setCancelText("Cancel");
        dialog.setConfirmText("Delete");
        dialog.addCancelListener(event1 -> {});
        dialog.addConfirmListener(event1 -> {
            sensorRepository.delete(sensor);
        });
        dialog.setConfirmButtonTheme("error primary");
        dialog.open();
        gridListDataView.refreshAll();
    }

    private void createContextEditMenu(GridContextMenu<Sensor> contextMenu) {
        contextMenu.addItem("View & edit", event -> {
            Sensor sensor = event.getItem().isPresent() ? event.getItem().get() : null;
            if(sensor != null) {
                navigateToSensorDetail(sensor);
            }
        });
    }

    private void navigateToSensorDetail(Sensor sensor) {
        Notification.show(sensor.getId().toString());
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

    private void addColumnsToGrid() {
        createIdColumn();
        createNameColumn();
        createLimitDayColumn();
        createLimitMonthColumn();
        createTypeColumn();
        createDateColumn();
        createConsumptionActualColumn();
        createConsumptionCorrelationColumn();
        createToolsColumn();

    }

    private void createIdColumn() {
        idColumn = grid.addColumn(Sensor::getId, "id").setHeader("ID").setWidth("100px").setFlexGrow(0);
    }

    private void createNameColumn() {
        nameColumn = grid.addColumn(Sensor::getName, "name").setHeader("Name")
                .setComparator(Sensor::getName).setWidth("200px").setFlexGrow(0);;
    }

    private void createLimitDayColumn() {
        limitDay = grid.addColumn(Sensor::getLimit_day, "limit_day").setHeader("Daily limit")
                .setWidth("150px").setFlexGrow(0).setComparator(Sensor::getLimit_day).setFlexGrow(0);;
    }

    private void createLimitMonthColumn() {
        limitMonth = grid.addColumn(Sensor::getLimit_month, "limit_month").setHeader("Monthly limit")
                .setWidth("150px").setFlexGrow(0).setComparator(Sensor::getLimit_month).setFlexGrow(0);;
    }

    private void createTypeColumn() {
        typeColumn = grid.addComponentColumn(sensor -> {
            Span span = new Span();
            span.setText(SensorTypes.valueOf(sensor.getType()).toString());
            span.getElement().setAttribute("theme", getBadgeType(sensor));
            span.setWidth("115px");
            return span;
        }).setHeader("Type").setWidth("150px").setComparator(Sensor::getType).setFlexGrow(0);;
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
                .setComparator(Sensor::getCreatedDate).setHeader("Created date").setWidth("200px").setFlexGrow(0);;
    }


    private void createConsumptionActualColumn() {
        consumptionActualColumn = grid.addColumn(Sensor::getConsumptionActual).setComparator(Sensor::getConsumptionActual)
            .setHeader("Actual consumption").setWidth("150px").setFlexGrow(0);
    }

    private void createConsumptionCorrelationColumn() {
        consumptionCorrelationColumn = grid.addColumn(Sensor::getConsumptionCorrelation).setComparator(Sensor::getConsumptionCorrelation)
                .setHeader("Correlation consumption").setWidth("150px").setFlexGrow(0);
    }

    private void createToolsColumn() {
        toolsColumn = grid.addComponentColumn(sensor -> {

            Icon toolboxIcon = new Icon(VaadinIcon.TOOLS);
            toolboxIcon.addClickListener(event -> navigateToSensorDetail(sensor));
            toolboxIcon.getElement().setAttribute("theme", "badge secondary");
            Icon trashCanIcon = new Icon(VaadinIcon.TRASH);
            trashCanIcon.addClickListener(event -> createDeleteSensorDialog(sensor));
            trashCanIcon.getElement().setAttribute("theme", "badge error secondary");

            return new HorizontalLayout(){
                {
                    addComponentAsFirst(toolboxIcon);
                    setSpacing(true);
                    addComponentAtIndex(1, trashCanIcon);
                }
            };
        }).setHeader("Tools").setWidth("80px");
    }

    /*
    TODO
    private String parseDate(Timestamp date) {
        return date.get
    }
    */

    private void addFiltersToGrid() {
        HeaderRow filterRow = grid.appendHeaderRow();

        TextField idFilter = getIdFilter();
        filterRow.getCell(idColumn).setComponent(idFilter);

        TextField nameFilter = getNameFilter();
        filterRow.getCell(nameColumn).setComponent(nameFilter);

        TextField dailyLimitFilter = getDailyLimitFilter();
        filterRow.getCell(limitDay).setComponent(dailyLimitFilter);

        TextField monthlyLimitFilter = getMonthlyLimitFilter();
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

        TextField actualConsumptionFilter = new TextField();
        actualConsumptionFilter.setPlaceholder("Filter");
        actualConsumptionFilter.setClearButtonVisible(true);
        actualConsumptionFilter.setWidth("100%");
        actualConsumptionFilter.setValueChangeMode(ValueChangeMode.EAGER);
        actualConsumptionFilter.addValueChangeListener(event -> gridListDataView.addFilter(sensor -> StringUtils
                .containsIgnoreCase(Double.toString(sensor.getConsumptionActual()), actualConsumptionFilter.getValue())));
        filterRow.getCell(consumptionActualColumn).setComponent(actualConsumptionFilter);

        TextField correlationConsumptionFilter = new TextField();
        correlationConsumptionFilter.setPlaceholder("Filter");
        correlationConsumptionFilter.setClearButtonVisible(true);
        correlationConsumptionFilter.setWidth("100%");
        correlationConsumptionFilter.setValueChangeMode(ValueChangeMode.EAGER);
        correlationConsumptionFilter.addValueChangeListener(event -> gridListDataView.addFilter(sensor -> StringUtils
                .containsIgnoreCase(Double.toString(sensor.getConsumptionCorrelation()), correlationConsumptionFilter.getValue())));
        filterRow.getCell(consumptionCorrelationColumn).setComponent(correlationConsumptionFilter);
    }

    private TextField getMonthlyLimitFilter() {
        TextField monthlyLimitFilter = new TextField();
        monthlyLimitFilter.setPlaceholder("Filter");
        monthlyLimitFilter.setClearButtonVisible(true);
        monthlyLimitFilter.setWidth("100%");
        monthlyLimitFilter.setValueChangeMode(ValueChangeMode.EAGER);
        monthlyLimitFilter.addValueChangeListener(event -> gridListDataView.addFilter(sensor -> StringUtils
                .containsIgnoreCase(Double.toString(sensor.getLimit_month()), monthlyLimitFilter.getValue())));
        return monthlyLimitFilter;
    }

    private TextField getDailyLimitFilter() {
        TextField dailyLimitFilter = new TextField();
        dailyLimitFilter.setPlaceholder("Filter");
        dailyLimitFilter.setClearButtonVisible(true);
        dailyLimitFilter.setWidth("100%");
        dailyLimitFilter.setValueChangeMode(ValueChangeMode.EAGER);
        dailyLimitFilter.addValueChangeListener(event -> gridListDataView.addFilter(sensor -> StringUtils
                .containsIgnoreCase(Double.toString(sensor.getLimit_day()), dailyLimitFilter.getValue())));
        return dailyLimitFilter;
    }

    private TextField getNameFilter() {
        TextField nameFilter = new TextField();
        nameFilter.setPlaceholder("Filter");
        nameFilter.setClearButtonVisible(true);
        nameFilter.setWidth("100%");
        nameFilter.setValueChangeMode(ValueChangeMode.EAGER);
        nameFilter.addValueChangeListener(event -> gridListDataView
                .addFilter(sensor -> StringUtils.containsIgnoreCase(sensor.getName(), nameFilter.getValue())));
        return nameFilter;
    }

    private TextField getIdFilter() {
        TextField idFilter = new TextField();
        idFilter.setPlaceholder("Filter");
        idFilter.setClearButtonVisible(true);
        idFilter.setWidth("100%");
        idFilter.setValueChangeMode(ValueChangeMode.EAGER);
        idFilter.addValueChangeListener(event -> gridListDataView.addFilter(
                client -> StringUtils.containsIgnoreCase(Integer.toString(client.getId()), idFilter.getValue())));
        return idFilter;
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
