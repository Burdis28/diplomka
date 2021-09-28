package com.example.application.views.sensors;

import com.example.application.data.entity.*;
import com.example.application.data.entity.data.DataElectric;
import com.example.application.data.entity.data.DataWater;
import com.example.application.data.service.*;
import com.example.application.data.service.data.DataElectricService;
import com.example.application.data.service.data.DataWaterService;
import com.example.application.utils.Colors;
import com.example.application.utils.MathUtils;
import com.example.application.views.main.MainLayout;
import com.example.application.views.sensors.components.SensorsUtil;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.UIDetachedException;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.Grid.SelectionMode;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.grid.HeaderRow;
import com.vaadin.flow.component.grid.contextmenu.GridContextMenu;
import com.vaadin.flow.component.grid.dataview.GridListDataView;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.littemplate.LitTemplate;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.template.Id;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.ParentLayout;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.server.VaadinSession;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@JsModule("./views/sensors/sensors-view.ts")
@CssImport("./views/sensors/sensors-view.css")
@CssImport(value = "./views/sensors/my-grid-styles.css", themeFor = "vaadin-grid")
@Tag("sensors-view")
@ParentLayout(MainLayout.class)
@PageTitle("Sensors management")
public class SensorsView extends LitTemplate {

    @Id("grid")
    private Grid<SensorGridRepresentation> grid;
    private GridListDataView<SensorGridRepresentation> gridListDataView;

    private Grid.Column<SensorGridRepresentation> typeColumn;
    private Grid.Column<SensorGridRepresentation> nameColumn;
    private Grid.Column<SensorGridRepresentation> onlineColumn;
    private Grid.Column<SensorGridRepresentation> signalColumn;
    private Grid.Column<SensorGridRepresentation> infoColumn;
    private Grid.Column<SensorGridRepresentation> pinIdColumn;
    private Grid.Column<SensorGridRepresentation> hardwareColumn;
    private Grid.Column<SensorGridRepresentation> toolsColumn;
    private Grid.Column<SensorGridRepresentation> exportColumn;


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
    private final DataWaterService dataWaterService;
    private final DataElectricService dataElectricService;
    private final User loggedUser;
    @Id("createSensorButton")
    private Button createSensorButton;
    @Id("lastUpdateText")
    private H5 lastUpdateText;

    public SensorsView(@Autowired SensorService sensorService, SensorElectricService sensorElectricService,
                       SensorWaterService sensorWaterService, StateValveService stateValveService,
                       NotificationLogHwService notificationLogHwService,
                       HardwareLiveService hardwareLiveService,
                       HardwareService hardwareService,
                       DataElectricService dataElectricService,
                       DataWaterService dataWaterService,
                       @Value("${generateTestData}") boolean generateTestData) {
        this.sensorService = sensorService;
        this.sensorElectricService = sensorElectricService;
        this.sensorWaterService = sensorWaterService;
        this.stateValveService = stateValveService;
        this.notificationLogHwService = notificationLogHwService;
        this.hardwareLiveService = hardwareLiveService;
        this.hardwareService = hardwareService;
        this.dataElectricService = dataElectricService;
        this.dataWaterService = dataWaterService;
        lastUpdateText.setText("Updated on: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")));

        grid.setSelectionMode(SelectionMode.NONE);
        loggedUser = VaadinSession.getCurrent().getAttribute(User.class);
        grid.addThemeVariants(GridVariant.LUMO_WRAP_CELL_CONTENT);
        grid.addThemeVariants(GridVariant.MATERIAL_COLUMN_DIVIDERS);
        grid.setColumnReorderingAllowed(true);

        createSensorButton.addClickListener(event -> UI.getCurrent().navigate("create-sensor"));

        createGrid();
        if(!loggedUser.getAdmin()) {
            createSensorButton.setVisible(false);
        }

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

        List<SensorGridRepresentation> sensors = getSensors();
        grid.addItemClickListener(event -> {
            grid.select(event.getItem());
        });

        gridListDataView = grid.setItems(sensors);
        GridContextMenu<SensorGridRepresentation> contextMenu = grid.addContextMenu();
        createContextMenu(contextMenu);
        contextMenu.add(new Hr());
        createDeleteContextMenu(contextMenu);
    }


    private List<SensorGridRepresentation> getSensors() {
        if (loggedUser.getAdmin()) {
            return sensorService.findAllForGrid();
        } else {
            return sensorService.findAllByOwnerForGrid(loggedUser.getId());
        }
    }

    @Scheduled(fixedDelay = 60000)
    public void refreshGrid() {
        try {
            getUI().ifPresent(ui -> ui.access(() -> {
                grid.setItems(getSensors());
                gridListDataView.refreshAll();
                lastUpdateText.setText("Updated on: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")));
                ui.push();
            }));
        } catch (Exception e) {
            // UI Detach exception - do nothing, its harmless
        }
    }

    private void createDeleteContextMenu(GridContextMenu<SensorGridRepresentation> contextMenu) {
        if (loggedUser.getAdmin()) {
            contextMenu.addItem("Delete", event -> {
                SensorGridRepresentation sensor = event.getItem().isPresent() ? event.getItem().get() : null;
                if (sensor != null) {
                    createDeleteSensorDialog(sensor);
                }
            });
        }
    }

    private void createDeleteSensorDialog(SensorGridRepresentation sensor) {
        ConfirmDialog dialog = new ConfirmDialog();
        dialog.setHeader("Confirm delete");
        dialog.setText("Are you sure you want to delete \"" + sensor.getName() + "\" sensor?");
        dialog.setCancelable(true);
        dialog.setCancelText("Cancel");
        dialog.setConfirmText("Delete");
        dialog.addCancelListener(event1 -> {
        });
        dialog.addConfirmListener(event1 -> {
            sensorService.delete(sensor.getId(), SensorTypes.valueOf(sensor.getType()));
            gridListDataView.removeItem(sensor);
        });
        dialog.setConfirmButtonTheme("error primary");
        dialog.open();
        gridListDataView.refreshAll();
    }

    private void createContextMenu(GridContextMenu<SensorGridRepresentation> contextMenu) {
        contextMenu.addItem("View & edit", event -> {
            SensorGridRepresentation sensor = event.getItem().isPresent() ? event.getItem().get() : null;
            if (sensor != null) {
                SensorsUtil.navigateToSensorDetail(sensor);
            }
        });
        contextMenu.addItem("Go to dashboard", event -> {
            SensorGridRepresentation sensor = event.getItem().isPresent() ? event.getItem().get() : null;
            if (sensor != null) {
                if (sensor.getType().equals(SensorTypes.g.name())) {
                    Dialog dialog = new Dialog();
                    dialog.add("Dashboard for Gas type sensors are not yet available in this version of the application.");
                    dialog.open();
                } else {
                    SensorsUtil.navigateToSensorDashboard(sensor);
                }
            }
        });
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
        createExportColumn();
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
        }).setHeader("Name").setWidth("300px").setComparator(SensorGridRepresentation::getName).setFlexGrow(0);
    }

    private void createPidIdColumn() {
        pinIdColumn = grid.addColumn(SensorGridRepresentation::getPinId, "pin").setHeader("Pin")
                .setComparator(SensorGridRepresentation::getPinId).setWidth("100px").setFlexGrow(0);
    }

    private void createSignalColumn() {
        signalColumn = grid.addComponentColumn(sensor -> {
            Integer signalStrenght = hardwareLiveService.getSignalStrenght(sensor.getIdHw());
            Div span = new Div();
            span.setWidthFull();
            span.addClassName("signalImage");
            Image image = new Image();
            SensorsUtil.setSignalImage(signalStrenght, span, image);
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
            String hw = hardwareService.getHardwareName(sensor.getIdHw());
            Span hwName = new Span();
            hwName.add(hw);
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
        }).setHeader("Type").setWidth("140px").setComparator(SensorGridRepresentation::getType).setFlexGrow(0);
    }

    private void createInfoColumn() {
        infoColumn = grid.addComponentColumn(sensor -> {
            Span span = new Span();
            if (sensor.getType().equals(SensorTypes.e.name())) {
                boolean isHighRate = sensorElectricService.getHighRate(sensor.getId());
                Icon tarifIcon;
                if (isHighRate) {
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
                int stateId = sensorWaterService.getState(sensor.getId());
                StateValve state = stateValveService.get(stateId).get();
                Icon stateIcon;
                Text text = new Text("");
                if (state.getState().equals("open_confirm")) {
                    stateIcon = new Icon(VaadinIcon.CHECK_CIRCLE_O);
                    stateIcon.setClassName("bottomMarginIcon");
                    stateIcon.setColor(Colors.GREEN.getRgb());
                    text.setText(" Valve open");
                } else if (state.getState().equals("close_confirm")) {
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
                    SensorsUtil.navigateToSensorDashboard(sensor);
                }
            });
            dasboardIcon.getElement().setAttribute("theme", "badge secondary");
            Icon trashCanIcon = new Icon(VaadinIcon.TRASH);
            trashCanIcon.addClickListener(event -> createDeleteSensorDialog(sensor));
            trashCanIcon.getElement().setAttribute("theme", "badge error secondary");

            return new HorizontalLayout() {
                {
                    addComponentAsFirst(toolboxIcon);
                    addComponentAtIndex(1, dasboardIcon);
                    setSpacing(true);
                    if(loggedUser.getAdmin()) {
                        addComponentAtIndex(2, trashCanIcon);
                    }
                }
            };
        }).setHeader("Tools").setWidth("150px").setFlexGrow(0);
    }

    private void createExportColumn() {
        exportColumn = grid.addComponentColumn(sensor -> {
            Icon exportIcon = new Icon(VaadinIcon.CLOUD_DOWNLOAD);
            Button exportBtn = new Button();
            exportIcon.setSize("35px");
            exportBtn.setIcon(exportIcon);
            exportBtn.setThemeName("secondary");
            exportBtn.setText("Export data");
            exportBtn.addClickListener(event ->
            {
                if (sensor.getType().equals(SensorTypes.g.name())) {
                    Dialog dialog = new Dialog();
                    dialog.add("Export for Gas sensors is not available in this version of the app.");
                    dialog.setModal(true);
                    dialog.open();
                } else {
                    createExportDialog(sensor);
                }
            });
            return new HorizontalLayout() {
                {
                    addComponentAsFirst(exportBtn);
                }
            };
        }).setHeader("Export").setWidth("150px");
    }

    private void createExportDialog(SensorGridRepresentation sensor) {
        Dialog dialog = new Dialog();
        VerticalLayout layout = new VerticalLayout();
        H3 title = new H3("Export " + sensor.getName() + " to excel file.");
        layout.setAlignItems(FlexComponent.Alignment.CENTER);
        RadioButtonGroup<String> radioButtonGroup = new RadioButtonGroup<>();
        radioButtonGroup.setItems("Daily", "Monthly", "Yearly");
        radioButtonGroup.setValue("Daily");
        DatePicker pickerFrom = new DatePicker();
        pickerFrom.setLabel("Date from");
        pickerFrom.setValue(LocalDate.now().minusDays(1));
        DatePicker pickerTo = new DatePicker();
        pickerTo.setLabel("Date to");
        pickerTo.setValue(LocalDate.now());
        Icon exportIcon = new Icon(VaadinIcon.CLOUD_DOWNLOAD);
        Button exportBtn = new Button();
        exportIcon.setSize("35px");
        exportBtn.setIcon(exportIcon);
        exportBtn.setThemeName("primary");
        exportBtn.setText("Export to Excel");
        Anchor download;

        if (sensor.getType().equals(SensorTypes.w.name())) {
            StreamResource streamResource = new StreamResource(
                    "Export-" + sensor.getName() + ".xlsx", () -> {
                XSSFWorkbook workbook = new XSSFWorkbook();
                XSSFSheet sheet = workbook.createSheet("Data of " + sensor.getName());

                getHeaderRow(sensor, sheet, workbook);
                getSecondHeaderRow(sensor, sheet, workbook, pickerFrom.getValue(), pickerTo.getValue());

                createWaterExcelWorkBook(sensor, sheet, radioButtonGroup.getValue(), pickerFrom.getValue(), pickerTo.getValue());
                return getByteArrayInputStream(workbook);
            });
            download = new Anchor(streamResource, "");

            download.add(exportBtn);
            layout.add(download);
            exportBtn.addClickListener(event -> {
                dialog.close();
            });
        } else if (sensor.getType().equals(SensorTypes.e.name())) {
            StreamResource streamResource = new StreamResource(
                    "Export-" + sensor.getName() + ".xlsx", () -> {
                XSSFWorkbook workbook = new XSSFWorkbook();
                XSSFSheet sheet = workbook.createSheet("Data of " + sensor.getName());

                getHeaderRow(sensor, sheet, workbook);
                getSecondHeaderRow(sensor, sheet, workbook, pickerFrom.getValue(), pickerTo.getValue());

                createElectricExcelWorkBook(sensor, sheet, radioButtonGroup.getValue(), pickerFrom.getValue(), pickerTo.getValue());
                return getByteArrayInputStream(workbook);
            });
            download = new Anchor(streamResource, "");

            download.add(exportBtn);
            layout.add(download);
            exportBtn.addClickListener(event -> {
                dialog.close();
            });
        } else {
            download = new Anchor();
        }

        layout.add(title);
        layout.add(radioButtonGroup);
        layout.add(pickerFrom);
        layout.add(pickerTo);
        layout.add(download);
        dialog.add(layout);
        dialog.open();
    }

    private InputStream getByteArrayInputStream(XSSFWorkbook workbook) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            workbook.write(bos);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                bos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        byte[] bytes = bos.toByteArray();
        return new ByteArrayInputStream(bytes);
    }

    private void getHeaderRow(SensorGridRepresentation sensor, XSSFSheet sheet, XSSFWorkbook workbook) {
        XSSFRow row = sheet.createRow(0);
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        style.setFont(font);
        style.setAlignment(HorizontalAlignment.CENTER);

        if (sensor.getType().equals(SensorTypes.e.name())) {
            createCellWithStyle(row, style, 0, "Data of " + sensor.getName());
            createCellWithStyle(row, style, 1, "Data of " + sensor.getName());
            createCellWithStyle(row, style, 2, "Data of " + sensor.getName());
            createCellWithStyle(row, style, 3, "Data of " + sensor.getName());
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 3));
        } else {
            createCellWithStyle(row, style, 0, "Data of " + sensor.getName());
            createCellWithStyle(row, style, 1, "Data of " + sensor.getName());
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 1));
        }
        //row.getCell(0).setCellStyle(style);
    }

    private void getSecondHeaderRow(SensorGridRepresentation sensor, XSSFSheet sheet, XSSFWorkbook workbook, LocalDate from, LocalDate to) {
        XSSFRow row = sheet.createRow(1);
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        style.setFont(font);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setBorderBottom(BorderStyle.MEDIUM);

        Cell cellHeader = row.createCell(0);
        cellHeader.setCellValue(DateTimeFormatter.ofPattern("dd.MM.yyyy").format(from) + " - "
                + DateTimeFormatter.ofPattern("dd.MM.yyyy").format(to));
        cellHeader.setCellStyle(style);
        //sheet.setColumnWidth(cellHeader.getColumnIndex(),500);
        if (sensor.getType().equals(SensorTypes.w.name())) {
            Cell cellHeader2 = row.createCell(1);
            cellHeader2.setCellValue(sensor.getType().equals(SensorTypes.w.name()) ? "m3" : "kWh");
            cellHeader2.setCellStyle(style);
        } else {
            createCellWithStyle(row, style, 1, "Low rate");
            createCellWithStyle(row, style, 2, "High rate");
            createCellWithStyle(row, style, 3, "Sum of both");
        }
        sheet.autoSizeColumn(0);
        sheet.autoSizeColumn(1);
        sheet.autoSizeColumn(2);
        sheet.autoSizeColumn(3);
    }

    private void createCellWithStyle(XSSFRow row, CellStyle style, int i, String s) {
        Cell cellHeader = row.createCell(i);
        cellHeader.setCellValue(s);
        cellHeader.setCellStyle(style);
    }

    private void createWaterExcelWorkBook(SensorGridRepresentation sensor, XSSFSheet sheet, String value, LocalDate from, LocalDate to) {
        List<DataWater> data = dataWaterService.findAllBySensorIdAndDate(sensor.getId(), from, to);
        if (from.isBefore(to)) {
            switch (value) {
                case "Daily": {
                    Map<LocalDate, Double> dataDailyMap = new HashMap<>();
                    getConsumptionM3Daily(data, dataDailyMap, from, to);

                    int rowNum = 2;
                    for (LocalDate date : dataDailyMap.keySet().stream().sorted().collect(Collectors.toList())) {
                        XSSFRow row = sheet.createRow(rowNum++);
                        Cell cellDate = row.createCell(0);
                        cellDate.setCellValue(DateTimeFormatter.ofPattern("dd.MM.yyyy").format(date));
                        Cell cellValue = row.createCell(1);
                        cellValue.setCellValue(MathUtils.round(dataDailyMap.get(date), 3));
                    }
                    return;
                }
                case "Monthly": {
                    Map<LocalDate, Double> dataMonthlyMap = new HashMap<>();
                    getConsumptionM3Monthly(data, dataMonthlyMap, from, to);

                    int rowNum = 2;
                    for (LocalDate month : dataMonthlyMap.keySet()
                            .stream().sorted().collect(Collectors.toList())) {
                        XSSFRow row = sheet.createRow(rowNum++);
                        Cell cellDate = row.createCell(0);
                        cellDate.setCellValue(month.getMonth().name()
                                .toLowerCase(Locale.ROOT) + "|" + month.getYear());
                        Cell cellValue = row.createCell(1);
                        cellValue.setCellValue(
                                MathUtils.round(dataMonthlyMap.get(month), 3));
                    }
                    return;
                }
                case "Yearly": {
                    Map<Integer, Double> dataYearlyMap = new HashMap<>();
                    getConsumptionM3Yearly(data, dataYearlyMap, from, to);

                    int rowNum = 2;
                    for (Integer year : dataYearlyMap.keySet()) {
                        XSSFRow row = sheet.createRow(rowNum++);
                        Cell cellDate = row.createCell(0);
                        cellDate.setCellValue(year);
                        Cell cellValue = row.createCell(1);
                        cellValue.setCellValue(MathUtils.round(dataYearlyMap.get(year), 3));
                    }
                    return;
                }
            }
        }
    }

    private void createElectricExcelWorkBook(SensorGridRepresentation sensor, XSSFSheet sheet, String value, LocalDate from, LocalDate to) {
        List<DataElectric> data = dataElectricService.findAllBySensorIdAndDate(sensor.getId(), from, to);
        if (from.isBefore(to)) {
            switch (value) {
                case "Daily": {
                    Map<LocalDate, Double> lowRates = new HashMap<>();
                    Map<LocalDate, Double> highRates = new HashMap<>();
                    getConsumptionElectricDaily(data, lowRates, highRates, from, to);

                    int rowNum = 2;
                    for (LocalDate date : lowRates.keySet().stream().sorted().collect(Collectors.toList())) {
                        XSSFRow row = sheet.createRow(rowNum++);
                        Cell cellDate = row.createCell(0);
                        cellDate.setCellValue(DateTimeFormatter.ofPattern("dd.MM.yyyy").format(date));
                        Cell cellLowRate = row.createCell(1);
                        cellLowRate.setCellValue(MathUtils.round(lowRates.get(date), 3));
                        Cell cellHighRate = row.createCell(2);
                        cellHighRate.setCellValue(MathUtils.round(highRates.get(date), 3));
                        Cell cellSums = row.createCell(3);
                        cellSums.setCellValue(MathUtils.round((highRates.get(date) + lowRates.get(date)), 3));
                    }
                    return;
                }
                case "Monthly": {
                    Map<LocalDate, Double> lowRates = new HashMap<>();
                    Map<LocalDate, Double> highRates = new HashMap<>();
                    getConsumptionElectricMonthly(data, lowRates, highRates, from, to);

                    int rowNum = 2;
                    for (LocalDate month : lowRates.keySet().stream().sorted().collect(Collectors.toList())) {
                        XSSFRow row = sheet.createRow(rowNum++);
                        Cell cellDate = row.createCell(0);
                        cellDate.setCellValue(month.getMonth().name().toLowerCase(Locale.ROOT) + "|" + month.getYear());
                        Cell cellLowRate = row.createCell(1);
                        cellLowRate.setCellValue(MathUtils.round(lowRates.get(month), 3));
                        Cell cellHighRate = row.createCell(2);
                        cellHighRate.setCellValue(MathUtils.round(highRates.get(month), 3));
                        Cell cellSums = row.createCell(3);
                        cellSums.setCellValue(MathUtils.round((highRates.get(month) + lowRates.get(month)), 3));
                    }
                    return;
                }
                case "Yearly": {
                    Map<Integer, Double> lowRates = new HashMap<>();
                    Map<Integer, Double> highRates = new HashMap<>();

                    getConsumptionElectricYearly(data, lowRates, highRates, from, to);

                    int rowNum = 2;
                    for (Integer year : lowRates.keySet()) {
                        XSSFRow row = sheet.createRow(rowNum++);
                        Cell cellDate = row.createCell(0);
                        cellDate.setCellValue(year);
                        Cell cellLowRate = row.createCell(1);
                        cellLowRate.setCellValue(MathUtils.round(lowRates.get(year), 3));
                        Cell cellHighRate = row.createCell(2);
                        cellHighRate.setCellValue(MathUtils.round(highRates.get(year), 3));
                        Cell cellSums = row.createCell(3);
                        cellSums.setCellValue(MathUtils.round((highRates.get(year) + lowRates.get(year)), 3));
                    }
                    return;
                }
            }
        }
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

    private boolean areTypesEqual(SensorGridRepresentation sensor, ComboBox<String> statusFilter) {
        String statusFilterValue = statusFilter.getValue();
        if (statusFilterValue != null) {
            return StringUtils.equals(SensorTypes.valueOf(sensor.getType()).toString(), statusFilterValue);
        }
        return true;
    }


    private void getConsumptionM3Monthly(List<DataWater> dataWaterList, Map<LocalDate, Double> consumptionMap, LocalDate from, LocalDate to) {
        List<LocalDate> listOfDates = from.datesUntil(to).sorted().collect(Collectors.toList());
        for (LocalDate date : listOfDates) {
            consumptionMap.putIfAbsent(date.withDayOfMonth(1), 0.0);
        }

        for (DataWater data : dataWaterList) {
            LocalDate date = data.getTime().toLocalDateTime().toLocalDate();
            consumptionMap.replace(date.withDayOfMonth(1), consumptionMap.get(date.withDayOfMonth(1)) + data.getM3());
        }
    }

    private void getConsumptionM3Daily(List<DataWater> dataWaterList, Map<LocalDate, Double> consumptionMap, LocalDate from, LocalDate to) {
        for (LocalDate date : from.datesUntil(to).sorted().collect(Collectors.toList())) {
            consumptionMap.putIfAbsent(date, 0.0);
        }

        for (DataWater data : dataWaterList) {
            LocalDate day = data.getTime().toLocalDateTime().toLocalDate();
            consumptionMap.replace(day, consumptionMap.get(day) == null ? 0.0 : consumptionMap.get(day) + data.getM3());
        }
    }

    private void getConsumptionM3Yearly(List<DataWater> dataWaterList, Map<Integer, Double> consumptionMap, LocalDate from, LocalDate to) {
        for (LocalDate date : from.datesUntil(to).sorted().collect(Collectors.toList())) {
            consumptionMap.putIfAbsent(date.getYear(), 0.0);
        }

        for (DataWater data : dataWaterList) {
            Integer year = data.getTime().toLocalDateTime().toLocalDate().getYear();
            consumptionMap.replace(year, consumptionMap.get(year) == null ? 0.0 : consumptionMap.get(year) + data.getM3());
        }
    }

    private void getConsumptionElectricMonthly(List<DataElectric> dataElectrics, Map<LocalDate, Double> lowRates,
                                               Map<LocalDate, Double> highRates, LocalDate from, LocalDate to) {
        List<LocalDate> listOfDates = from.datesUntil(to).sorted().collect(Collectors.toList());
        for (LocalDate date : listOfDates) {
            lowRates.putIfAbsent(date.withDayOfMonth(1), 0.0);
            highRates.putIfAbsent(date.withDayOfMonth(1), 0.0);
        }

        for (DataElectric data : dataElectrics) {
            LocalDate date = data.getTime().toLocalDateTime().toLocalDate();
            lowRates.replace(date.withDayOfMonth(1), lowRates.get(date.withDayOfMonth(1)) + data.getLowRate());
            highRates.replace(date.withDayOfMonth(1), highRates.get(date.withDayOfMonth(1)) + data.getHighRate());
        }
    }
//
//    private MonthYearKey getMonthYear(LocalDate date) {
//        return new MonthYearKey(date.withDayOfMonth(1), date.getMonth().name().toLowerCase(Locale.ROOT) + "|" + date.getYear());
//    }

    private void getConsumptionElectricDaily(List<DataElectric> dataElectrics, Map<LocalDate, Double> lowRate,
                                             Map<LocalDate, Double> highRates, LocalDate from, LocalDate to) {
        for (LocalDate date : from.datesUntil(to).sorted().collect(Collectors.toList())) {
            lowRate.putIfAbsent(date, 0.0);
            highRates.putIfAbsent(date, 0.0);
        }

        for (DataElectric data : dataElectrics) {
            LocalDate day = data.getTime().toLocalDateTime().toLocalDate();
            lowRate.replace(day, lowRate.get(day) == null ? 0.0 : lowRate.get(day) + data.getLowRate());
            highRates.replace(day, highRates.get(day) == null ? 0.0 : highRates.get(day) + data.getHighRate());

        }
    }

    private void getConsumptionElectricYearly(List<DataElectric> dataElectrics, Map<Integer, Double> lowRates,
                                              Map<Integer, Double> highRates, LocalDate from, LocalDate to) {
        for (LocalDate date : from.datesUntil(to).sorted().collect(Collectors.toList())) {
            lowRates.putIfAbsent(date.getYear(), 0.0);
            highRates.putIfAbsent(date.getYear(), 0.0);
        }

        for (DataElectric data : dataElectrics) {
            Integer year = data.getTime().toLocalDateTime().toLocalDate().getYear();
            lowRates.replace(year, lowRates.get(year) == null ? 0.0 : lowRates.get(year) + data.getLowRate());
            highRates.replace(year, highRates.get(year) == null ? 0.0 : highRates.get(year) + data.getHighRate());
        }
    }
}
