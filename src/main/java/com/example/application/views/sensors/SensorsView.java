package com.example.application.views.sensors;

import com.example.application.data.entity.*;
import com.example.application.data.service.*;
import com.example.application.utils.Colors;
import com.example.application.views.main.MainLayout;
import com.example.application.views.sensors.components.SensorsUtil;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.Grid.SelectionMode;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.grid.HeaderRow;
import com.vaadin.flow.component.grid.contextmenu.GridContextMenu;
import com.vaadin.flow.component.grid.dataview.GridListDataView;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.littemplate.LitTemplate;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.template.Id;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.function.SerializablePredicate;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.ParentLayout;
import com.vaadin.flow.server.VaadinSession;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;

@JsModule("./views/sensors/sensors-view.ts")
@CssImport("./views/sensors/sensors-view.css")
@CssImport(value = "./views/sensors/my-grid-styles.css", themeFor = "vaadin-grid")
@Tag("sensors-view")
@ParentLayout(MainLayout.class)
@PageTitle("Sensors management")
public class SensorsView extends LitTemplate {

    @Id("grid")
    private Grid<Sensor> grid;
    private GridListDataView<Sensor> gridListDataView;

    private Grid.Column<Sensor> typeColumn;
    private Grid.Column<Sensor> nameColumn;
    private Grid.Column<Sensor> onlineColumn;
    private Grid.Column<Sensor> signalColumn;
    private Grid.Column<Sensor> infoColumn;
    private Grid.Column<Sensor> tarifColumn;
    private Grid.Column<Sensor> valveColumn;
    private Grid.Column<Sensor> pinIdColumn;
    private Grid.Column<Sensor> hardwareColumn;
    private Grid.Column<Sensor> toolsColumn;

    private TextField nameFilter;
    private ComboBox<String> typeFilter;
    private NumberField pinIdFilter;

    private final SensorService sensorService;
    private final SensorElectricService sensorElectricService;
    private final SensorWaterService sensorWaterService;
    private final StateValveService stateValveService;
    private final NotificationLogHwService notificationLogHwService;
    private final HardwareLiveService hardwareLiveService;
    private final HardwareService hardwareService;
    private final User loggedUser;
    @Id("createSensorButton")
    private Button createSensorButton;

    public SensorsView(@Autowired SensorService sensorService, SensorElectricService sensorElectricService,
                       SensorWaterService sensorWaterService, StateValveService stateValveService,
                       NotificationLogHwService notificationLogHwService,
                       HardwareLiveService hardwareLiveService,
                       HardwareService hardwareService,
                       @Value("${generateTestData}") boolean generateTestData) {
        this.sensorService = sensorService;
        this.sensorElectricService = sensorElectricService;
        this.sensorWaterService = sensorWaterService;
        this.stateValveService = stateValveService;
        this.notificationLogHwService = notificationLogHwService;
        this.hardwareLiveService = hardwareLiveService;
        this.hardwareService = hardwareService;

        grid.setSelectionMode(SelectionMode.NONE);
        loggedUser = VaadinSession.getCurrent().getAttribute(User.class);
        grid.addThemeVariants(GridVariant.LUMO_WRAP_CELL_CONTENT);
        grid.addThemeVariants(GridVariant.MATERIAL_COLUMN_DIVIDERS);
        grid.setColumnReorderingAllowed(true);

        createSensorButton.addClickListener(event ->  UI.getCurrent().navigate("create-sensor"));

        createGrid();

//        //TEST DATA
//        if(generateTestData) {
//            DevelopmentDataCreator testData = new DevelopmentDataCreator(dataElectricService, sensorService, dataWaterService);
//            testData.createElectricData();
//            testData.createWaterData();
//        }
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
        createContextMenu(contextMenu);
        contextMenu.add(new Hr());
        createDeleteContextMenu(contextMenu);
    }


    private List<Sensor> getSensors() {
        if(loggedUser.getAdmin()) {
            return sensorService.findAll();
        } else {
            return sensorService.findAllByOwner(loggedUser.getId());
        }
    }

    @Scheduled(fixedDelay = 30000)
    public void refreshGrid(){
        try {
            getUI().ifPresent(ui -> ui.access(() -> {
                grid.setItems(getSensors());
                gridListDataView.refreshAll();
                ui.push();
            }));
        } catch (Exception e) {
            // UI Detach exception - do nuthin
        }
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
        ConfirmDialog dialog = new ConfirmDialog();
        dialog.setHeader("Confirm delete");
        dialog.setText("Are you sure you want to delete \"" + sensor.getName() + "\" sensor?");
        dialog.setCancelable(true);
        dialog.setCancelText("Cancel");
        dialog.setConfirmText("Delete");
        dialog.addCancelListener(event1 -> {});
        dialog.addConfirmListener(event1 -> {
            sensorService.delete(sensor.getId(), SensorTypes.valueOf(sensor.getType()));
            gridListDataView.removeItem(sensor);
        });
        dialog.setConfirmButtonTheme("error primary");
        dialog.open();
        gridListDataView.refreshAll();
    }

    private void createContextMenu(GridContextMenu<Sensor> contextMenu) {
        contextMenu.addItem("View & edit", event -> {
            Sensor sensor = event.getItem().isPresent() ? event.getItem().get() : null;
            if(sensor != null) {
                SensorsUtil.navigateToSensorDetail(sensor);
            }
        });
        contextMenu.addItem("Go to dashboard", event -> {
            Sensor sensor = event.getItem().isPresent() ? event.getItem().get() : null;
            if(sensor != null) {
                if (sensor.getType().equals(SensorTypes.g.name())) {
                    Dialog dialog = new Dialog();
                    dialog.add("Dashboard for Gas type sensors are not yet available in this version of the application.");
                    dialog.open();
                } else {
                    navigateToSensorDashboard(sensor);
                }
            }
        });
    }

    private void navigateToSensorDashboard(Sensor sensor) {
        VaadinSession.getCurrent().setAttribute("sensorId", sensor.getId());
        switch (sensor.getType()) {
            case "e":
                UI.getCurrent().navigate("sensor-el-dashboard");
                return;
            case "w":
                UI.getCurrent().navigate("sensor-wat-dashboard");
                return;
            case "g":
                UI.getCurrent().navigate("sensor-gas-dashboard");
                return;
            default:
        }
    }

    private void addColumnsToGrid() {
        createOnlineStatusColumn();
        createSignalColumn();
        createNameColumn();
        createTypeColumn();

        createInfoColumn();

        createHardwareColumn();
        createPidIdColumn();
        createToolsColumn();
    }

    private void createNameColumn() {
        nameColumn = grid.addComponentColumn(sensor -> {
            NotificationLogHw logHw = notificationLogHwService.findBySerialHw(sensor.getIdHw());
            VerticalLayout verticalLayout = new VerticalLayout();
            verticalLayout.setWidthFull();
            verticalLayout.setClassName("alignLeft");
            Div nameText = new Div();
            nameText.setText(sensor.getName());
            nameText.setWidthFull();
            Div lastOnlineText = new Div();
            lastOnlineText.setWidthFull();
            lastOnlineText.setClassName("smallerFontSize");
            lastOnlineText.setWidthFull();
            if (logHw != null) {
                String formattedDate = new SimpleDateFormat("dd.MM.yyyy HH:mm").format(logHw.getLastSent().getTime());
                lastOnlineText.add("Last online: " + formattedDate);
                verticalLayout.add(lastOnlineText);
            }
            verticalLayout.addComponentAsFirst(nameText);
            return verticalLayout;
        }).setHeader("Name").setWidth("300px").setComparator(Sensor::getName).setFlexGrow(0);
    }

    private void createPidIdColumn() {
        pinIdColumn = grid.addColumn(Sensor::getPinId, "pin").setHeader("Pin")
                .setComparator(Sensor::getPinId).setWidth("100px").setFlexGrow(0);
    }

    private void createSignalColumn() {
        signalColumn = grid.addComponentColumn(sensor -> {
            HardwareLive live = hardwareLiveService.findByHardwareId(sensor.getIdHw());
            Div span = new Div();
            span.setWidthFull();
            span.addClassName("signalImage");
            Image image = new Image();
            SensorsUtil.setSignalImage(live, span, image);
            return span;
        }).setHeader("Signal").setWidth("90px").setFlexGrow(0);
    }

    private void createOnlineStatusColumn() {
        onlineColumn = grid.addComponentColumn(sensor -> {
            NotificationLogHw logHw = notificationLogHwService.findBySerialHw(sensor.getIdHw());
            Div span = new Div();
            span.setWidthFull();
            Icon icon;
            if (logHw == null) {
                icon = VaadinIcon.CIRCLE.create();
                icon.setColor(Colors.GREEN.getRgb());
                icon.setClassName("onlineStatusIcon");
                span.add(icon);
            } else {
                icon = VaadinIcon.CIRCLE.create();
                icon.setColor(Colors.RED.getRgb());
                icon.setClassName("onlineStatusIcon");
                span.add(icon);
            }
            return span;
        }).setHeader("Status").setWidth("80px").setFlexGrow(0);
    }

    private void createHardwareColumn() {
        hardwareColumn = grid.addComponentColumn(sensor -> {
            Hardware hw = hardwareService.getBySerialHW(sensor.getIdHw()).get();
            Span hwName = new Span();
            hwName.add(hw.getName());
            return hwName;
        }).setHeader("Hardware").setWidth("250px").setFlexGrow(0);
    }

    private void createTypeColumn() {
        typeColumn = grid.addComponentColumn(sensor -> {
            Div span = new Div();
            span.setWidthFull();
            span.setClassName("alignCenter");
            Icon icon;
            if (sensor.getType().equals(SensorTypes.w.name())) {
                icon = VaadinIcon.DROP.create();
                icon.setColor(Colors.BLUE.getRgb());
                icon.setSize("30px");
                span.add(icon);
            } else if (sensor.getType().equals(SensorTypes.e.name())) {
                icon = VaadinIcon.BOLT.create();
                icon.setColor(Colors.CEZ_TYPE_ORANGE.getRgb());
                icon.setSize("30px");
                span.add(icon);
            } else {
                icon = VaadinIcon.CLOUD.create();
                icon.setColor(Colors.GREEN.getRgb());
                icon.setSize("30px");
                span.add(icon);
            }
            span.setWidthFull();
            return span;
        }).setHeader("Type").setWidth("140px").setComparator(Sensor::getType).setFlexGrow(0);
    }

    private void createInfoColumn() {
        infoColumn = grid.addComponentColumn(sensor -> {
            Span span = new Span();
            if (sensor.getType().equals(SensorTypes.e.name())) {
                SensorElectric sensorElectric = sensorElectricService.get(sensor.getId()).get();
                Icon tarifIcon;
                if (sensorElectric.isHighRate()) {
                    tarifIcon = new Icon(VaadinIcon.ANGLE_DOUBLE_UP);
                    tarifIcon.setColor(Colors.BLACK.getRgb());
                    span.setText(" High rate");
                } else {
                    tarifIcon = new Icon(VaadinIcon.ANGLE_DOWN);
                    tarifIcon.setColor(Colors.BLACK.getRgb());
                    span.setText(" Low rate");
                }
                span.addComponentAsFirst(tarifIcon);
            } else if (sensor.getType().equals(SensorTypes.w.name())) {
                SensorWater sensorWater = sensorWaterService.get(sensor.getId()).get();
                StateValve state = stateValveService.get(sensorWater.getState()).get();
                Icon stateIcon;
                Text text = new Text("");
                if (state.getState().equals("open_confirm")) {
                    stateIcon = new Icon(VaadinIcon.CHECK_CIRCLE_O);
                    stateIcon.setClassName("bottomMarginIcon");
                    stateIcon.setColor(Colors.GREEN.getRgb());
                    text.setText(" Valve open");
                } else if(state.getState().equals("close_confirm")) {
                    stateIcon = new Icon(VaadinIcon.CLOSE_CIRCLE_O);
                    stateIcon.setClassName("bottomMarginIcon");
                    stateIcon.setColor(Colors.RED.getRgb());
                    text.setText(" Valve close");
                } else {
                    stateIcon = new Icon(VaadinIcon.REFRESH);
                    stateIcon.setClassName("bottomMarginIcon");
                    stateIcon.setColor(Colors.GRAY.getRgb());
                    text.setText(" Valve changing");
                }
                span.addComponentAsFirst(text);
                span.addComponentAsFirst(stateIcon);
            }
            return span;
        }).setHeader("Sensor info").setWidth("220px").setFlexGrow(0);
    }

    private void createToolsColumn() {
        toolsColumn = grid.addComponentColumn(sensor -> {
            Icon toolboxIcon = new Icon(VaadinIcon.TOOLS);
            toolboxIcon.addClickListener(event -> SensorsUtil.navigateToSensorDetail(sensor));
            toolboxIcon.getElement().setAttribute("theme", "badge secondary");
            Icon dasboardIcon = new Icon(VaadinIcon.DASHBOARD);
            dasboardIcon.addClickListener(event ->
                {
                    if (sensor.getType().equals(SensorTypes.g.name())) {
                        Dialog dialog = new Dialog();
                        dialog.add("Dashboard for Gas type sensors are not yet available in this version of the application.");
                        dialog.open();
                    } else {
                        navigateToSensorDashboard(sensor);
                    }
                });
            dasboardIcon.getElement().setAttribute("theme", "badge secondary");
            Icon trashCanIcon = new Icon(VaadinIcon.TRASH);
            trashCanIcon.addClickListener(event -> createDeleteSensorDialog(sensor));
            trashCanIcon.getElement().setAttribute("theme", "badge error secondary");

            return new HorizontalLayout(){
                {
                    addComponentAsFirst(toolboxIcon);
                    addComponentAtIndex(1, dasboardIcon);
                    setSpacing(true);
                    addComponentAtIndex(2, trashCanIcon);
                }
            };
        }).setHeader("Tools").setWidth("150px");
    }

    private void addFiltersToGrid() {
        HeaderRow filterRow = grid.appendHeaderRow();

        nameFilter = getNameFilter();
        filterRow.getCell(nameColumn).setComponent(nameFilter);

        typeFilter = getTypeFilter();
        filterRow.getCell(typeColumn).setComponent(typeFilter);

        pinIdFilter = getPinIdFilter();
        filterRow.getCell(pinIdColumn).setComponent(pinIdFilter);
    }


    private ComboBox<String> getTypeFilter() {
        ComboBox<String> typeFilter = new ComboBox<>();
        typeFilter.setItems(Arrays.asList("Water", "Electric", "Gas"));
        typeFilter.setPlaceholder("Filter");
        typeFilter.setClearButtonVisible(true);
        typeFilter.setWidth("100%");
        typeFilter.addValueChangeListener(
                event -> gridListDataView.addFilter(sensor -> areTypesEqual(sensor, typeFilter)));
        return typeFilter;
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

    private NumberField getPinIdFilter() {
        NumberField pinIdFilter = new NumberField();
        pinIdFilter.setClearButtonVisible(true);
        pinIdFilter.setPlaceholder("Filter");
        pinIdFilter.setWidth("100%");
        pinIdFilter.setValueChangeMode(ValueChangeMode.EAGER);
        pinIdFilter.addValueChangeListener(event -> gridListDataView
                .addFilter(sensor -> {
                    if (pinIdFilter.isEmpty()) {
                        return true;
                    }
                    return sensor.getPinId() == (pinIdFilter.getValue() != null ? pinIdFilter.getValue() : 0);
                }));
        return pinIdFilter;
    }

    private boolean areTypesEqual(Sensor sensor, ComboBox<String> statusFilter) {
        String statusFilterValue = statusFilter.getValue();
        if (statusFilterValue != null) {
            return StringUtils.equals(SensorTypes.valueOf(sensor.getType()).toString(), statusFilterValue);
        }
        return true;
    }
}
