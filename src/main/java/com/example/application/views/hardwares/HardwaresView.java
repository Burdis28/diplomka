package com.example.application.views.hardwares;

import com.example.application.data.entity.*;
import com.example.application.data.service.*;
import com.example.application.utils.Colors;
import com.example.application.views.hardwares.components.HardwareTile;
import com.example.application.views.main.MainLayout;
import com.example.application.views.sensors.components.SensorsUtil;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.grid.HeaderRow;
import com.vaadin.flow.component.grid.contextmenu.GridContextMenu;
import com.vaadin.flow.component.grid.dataview.GridListDataView;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.littemplate.LitTemplate;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.template.Id;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.ParentLayout;
import com.vaadin.flow.server.VaadinSession;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@JsModule("./views/hardwares/hardwares-view.ts")
@CssImport("./views/hardwares/hardwares-view.css")
@Tag("hardwares-view")
@ParentLayout(MainLayout.class)
@PageTitle("Hardware management")
public class HardwaresView extends LitTemplate {

    private HardwareService hardwareService;
    private HardwareLiveService hardwareLiveService;
    private NotificationLogHwService notificationLogHwService;
    private SensorService sensorService;
    private UserService userService;
    private User loggedUser;

    private Grid.Column<Hardware> nameColumn;
    private Grid.Column<Hardware> serialHwColumn;
    private Grid.Column<Hardware> onlineColumn;
    private Grid.Column<Hardware> signalColumn;
    private Grid.Column<Hardware> ownerColumn;
    private Grid.Column<Hardware> lastOnlineColumn;
    private Grid.Column<Hardware> attachedSensorsColumn;
    private Grid.Column<Hardware> toolsColumn;
    @Id("hardwareGrid")
    private Grid<Hardware> grid;
    private GridListDataView<Hardware> gridListDataView;


    public HardwaresView(@Autowired HardwareService hardwareService, HardwareLiveService hardwareLiveService,
                         SensorService sensorService, UserService userService,
                         NotificationLogHwService notificationLogHwService) {
        this.hardwareService = hardwareService;
        this.hardwareLiveService = hardwareLiveService;
        this.sensorService = sensorService;
        this.userService = userService;
        this.notificationLogHwService = notificationLogHwService;
        loggedUser = VaadinSession.getCurrent().getAttribute(User.class);

        grid.setSelectionMode(Grid.SelectionMode.NONE);
        grid.setClassName("my-grid");
        grid.addThemeVariants(GridVariant.LUMO_WRAP_CELL_CONTENT);
        grid.addThemeVariants(GridVariant.MATERIAL_COLUMN_DIVIDERS);
        grid.setColumnReorderingAllowed(true);
        createGrid();
        createTiles();
    }

    private void createGrid() {
        createGridComponent();
        addColumnsToGrid();
        addFiltersToGrid();
    }

    private void createGridComponent() {
        grid.setSelectionMode(Grid.SelectionMode.SINGLE);
        grid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        grid.setHeight("100%");

        List<Hardware> hardwares = getAllHardware();
        grid.addItemClickListener(event -> {
            grid.select(event.getItem());
        });

        gridListDataView = grid.setItems(hardwares);
        GridContextMenu<Hardware> contextMenu = grid.addContextMenu();
        createContextMenu(contextMenu);
        contextMenu.add(new Hr());
        createDeleteContextMenu(contextMenu);
    }

    private List<Hardware> getAllHardware() {
        if(loggedUser.getAdmin()) {
            return hardwareService.findAll();
        } else {
            return hardwareService.findByOwner(loggedUser.getId());
        }
    }

    @Scheduled(fixedDelay = 10000)
    public void refreshGrid(){
        getUI().ifPresent(ui -> ui.access(() -> {
            grid.setItems(getAllHardware());
            gridListDataView.refreshAll();
            ui.push();
        }));
    }

    private void createDeleteContextMenu(GridContextMenu<Hardware> contextMenu) {
        contextMenu.addItem("Delete", event -> {
            Hardware hardware = event.getItem().isPresent() ? event.getItem().get() : null;
            if(hardware != null) {
                createDeleteHardwareDialog(hardware);
            }
        });
    }

    private void createDeleteHardwareDialog(Hardware hardware) {
        ConfirmDialog dialog = new ConfirmDialog();
        dialog.setHeader("Confirm delete");
        dialog.setText("Are you sure you want to delete \"" + hardware.getName() + "\" hardware?");
        dialog.setCancelable(true);
        dialog.setCancelText("Cancel");
        dialog.setConfirmText("Delete");
        dialog.addCancelListener(event1 -> {});
        dialog.addConfirmListener(event1 -> {
            hardwareService.delete(hardware.getId_HW(), hardware.getSerial_HW());
            gridListDataView.removeItem(hardware);
        });
        dialog.setConfirmButtonTheme("error primary");
        dialog.open();
        gridListDataView.refreshAll();
    }

    private void createContextMenu(GridContextMenu<Hardware> contextMenu) {
        contextMenu.addItem("View & edit", event -> {
            Hardware hardware = event.getItem().isPresent() ? event.getItem().get() : null;
            if (hardware != null) {
                navigateToHardwareDetail(hardware);
            }
        });
    }

    private void addColumnsToGrid() {
        createNameColumn();
        createSerialHwColumn();
        createOnlineStatusColumn();
        createSignalColumn();
        createOwnerColumn();
        createLastOnlineStatusColumn();
        createToolsColumn();
    }

    private void createNameColumn() {
        nameColumn = grid.addColumn(Hardware::getName, "name").setHeader("Name")
                .setComparator(Hardware::getName).setWidth("300px").setFlexGrow(0);
    }

    private void createSerialHwColumn() {
        serialHwColumn = grid.addColumn(Hardware::getSerial_HW, "serialHw").setHeader("Serial code")
                .setComparator(Hardware::getSerial_HW).setWidth("300px").setFlexGrow(0);
    }

    private void createSignalColumn() {
        signalColumn = grid.addComponentColumn(hardware -> {
            HardwareLive live = hardwareLiveService.findByHardwareId(hardware.getSerial_HW());
            Div span = new Div();
            span.setWidthFull();
            span.addClassName("signalImage");
            Image image = new Image();
            SensorsUtil.setSignalImage(live, span, image);
            return span;
        }).setHeader("Signal").setWidth("70px").setFlexGrow(0);
    }

    private void createOnlineStatusColumn() {
        onlineColumn = grid.addComponentColumn(hardware -> {
            NotificationLogHw logHw = notificationLogHwService.findBySerialHw(hardware.getSerial_HW());
            Div span = new Div();
            span.setWidthFull();
            //span.setClassName("alignCenter");
            Icon icon;
            if (logHw == null) {
                icon = VaadinIcon.CIRCLE.create();
                icon.setColor(Colors.GREEN.getRgb());
                icon.setClassName("bottomMarginIcon");
                //icon.setSize("30px");
                span.add(icon);
            } else {
                icon = VaadinIcon.CIRCLE.create();
                icon.setColor(Colors.RED.getRgb());
                icon.setClassName("bottomMarginIcon");
                //icon.setSize("30px");
                span.add(icon);
            }
            return span;
        }).setHeader(" ").setWidth("60px").setFlexGrow(0);
    }

    private void createOwnerColumn() {
        onlineColumn = grid.addComponentColumn(hardware -> {
            List<User> owners = userService.getHardwareOwner(hardware.getSerial_HW());
            User owner = null;
            if (!owners.isEmpty()) {
                owner = owners.get(0);
            }
            Div span = new Div();
            span.setWidthFull();
            if (owner == null) {
                span.setText("...");
            } else {
                span.setText(owner.getFullName());
            }
            return span;
        }).setHeader("Owner").setWidth("160px").setFlexGrow(0);
    }

    private void createLastOnlineStatusColumn() {
        lastOnlineColumn = grid.addComponentColumn(sensor -> {
            NotificationLogHw logHw = notificationLogHwService.findBySerialHw(sensor.getSerial_HW());
            Div span = new Div();
            span.setClassName("smallerFontSize");
            span.setWidthFull();
            if (logHw != null) {
                String formattedDate = new SimpleDateFormat("dd.MM.yyyy HH:mm").format(logHw.getLastSent().getTime());
                span.add("Last online: " + formattedDate);
            } else {
                span.add(" ");
            }
            return span;
        }).setHeader(" ").setWidth("220px").setFlexGrow(0);
    }

    private void createToolsColumn() {
        toolsColumn = grid.addComponentColumn(sensor -> {
            Icon toolboxIcon = new Icon(VaadinIcon.TOOLS);
            toolboxIcon.addClickListener(event -> navigateToHardwareDetail(sensor));
            toolboxIcon.getElement().setAttribute("theme", "badge secondary");
            Icon trashCanIcon = new Icon(VaadinIcon.TRASH);
            trashCanIcon.addClickListener(event -> createDeleteHardwareDialog(sensor));
            trashCanIcon.getElement().setAttribute("theme", "badge error secondary");

            return new HorizontalLayout(){
                {
                    addComponentAsFirst(toolboxIcon);
                    setSpacing(true);
                    addComponentAtIndex(1, trashCanIcon);
                }
            };
        }).setHeader("Tools").setWidth("150px").setFlexGrow(0);
    }


    private void addFiltersToGrid() {
        HeaderRow filterRow = grid.appendHeaderRow();

        TextField nameFilter = getNameFilter();
        filterRow.getCell(nameColumn).setComponent(nameFilter);

        TextField serialHwFilter = getSerialHwFilter();
        filterRow.getCell(serialHwColumn).setComponent(serialHwFilter);
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

    private TextField getSerialHwFilter() {
        TextField serialHwField = new TextField();
        serialHwField.setPlaceholder("Filter");
        serialHwField.setClearButtonVisible(true);
        serialHwField.setWidth("100%");
        serialHwField.setValueChangeMode(ValueChangeMode.EAGER);
        serialHwField.addValueChangeListener(event -> gridListDataView
                .addFilter(sensor -> StringUtils.containsIgnoreCase(sensor.getSerial_HW(), serialHwField.getValue())));
        return serialHwField;
    }

    private void navigateToHardwareDetail(Hardware hardware) {
        VaadinSession.getCurrent().setAttribute("hardwareSerialCode", hardware.getSerial_HW());
        UI.getCurrent().navigate("hardware-detail");
    }

    private void createTiles() {
//        List<Hardware> hardwareList;
//        List<HardwareLive> hardwareLiveList;
//        if (loggedUser.getAdmin()) {
//            hardwareList = hardwareService.findAll();
//            hardwareLiveList = hardwareLiveService.findByHardwareId(hardwareList.stream()
//                    .map(Hardware::getSerial_HW).collect(Collectors.toList()));
//        } else {
//            hardwareList = hardwareService.findByOwner(loggedUser.getId());
//            hardwareLiveList = hardwareLiveService.findByHardwareId(hardwareList.stream()
//                    .map(Hardware::getSerial_HW).collect(Collectors.toList()));
//        }
//
//        List<Sensor> attachedSensors;
//
//        Map<String, HardwareLive> hardwareLiveMap = new HashMap<>();
//        for (HardwareLive live : hardwareLiveList) {
//            hardwareLiveMap.putIfAbsent(live.getHwId(), live);
//        }
//
//        for (Hardware hardware : hardwareList ) {
//            attachedSensors = new ArrayList<>(sensorService.findSensorByIdHw(hardware.getSerial_HW()));
//
//            try {
//                HardwareTile tile = new HardwareTile(hardware, hardwareLiveMap.get(hardware.getSerial_HW()), attachedSensors,
//                        userService.getHardwareOwner(hardware.getSerial_HW()).getFullName(), hardwareService, hardwareLiveService);
//                tile.addClassName("tile");
////                verticalBaseLayout.add(tile);
//            } catch (Exception e) {
//                System.out.println(e.getMessage());
//            }
//        }
    }
}
