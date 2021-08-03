package com.example.application.views.dashboard;

import com.example.application.components.StyledTextComponent;
import com.example.application.data.entity.*;
import com.example.application.data.entity.data.DataElectric;
import com.example.application.data.entity.data.DataWater;
import com.example.application.data.service.*;
import com.example.application.data.service.data.DataElectricService;
import com.example.application.data.service.data.DataWaterService;
import com.example.application.utils.Colors;
import com.example.application.utils.PatternStringUtils;
import com.example.application.views.main.MainView;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.UIDetachedException;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.charts.Chart;
import com.vaadin.flow.component.charts.model.*;
import com.vaadin.flow.component.charts.model.style.SolidColor;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.littemplate.LitTemplate;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.progressbar.ProgressBar;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.template.Id;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.ParentLayout;
import com.vaadin.flow.server.VaadinSession;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import static java.time.temporal.TemporalAdjusters.firstDayOfMonth;
import static java.time.temporal.TemporalAdjusters.lastDayOfMonth;

/**
 * A Designer generated component for the sensor-wat-dashboard template.
 *
 * Designer will add and remove fields with @Id mappings but
 * does not overwrite or otherwise change this file.
 */
@Tag("sensor-wat-dashboard")
@JsModule("./views/dashboard/sensor-wat-dashboard.ts")
@CssImport("./views/dashboard/sensor-wat-dashboard.css")
@ParentLayout(MainView.class)
@PageTitle("Water sensor dashboard")
@EnableScheduling
public class SensorWaterDashboard extends LitTemplate {

    private SensorWaterService sensorWaterService;
    private DataWaterService dataWaterService;
    private SensorService sensorService;
    private UserService userService;
    private Chart dailyConsumptionChart;

    private SensorWater sensorWater;
    private HardwareService hardwareService;
    private HardwareLiveService hardwareLiveService;
    private Sensor sensor;

    private StyledTextComponent consumptionText;
    private StyledTextComponent consumptionMonthText;
    private StyledTextComponent priceMonthText;
    private StyledTextComponent priceText;
    private StyledTextComponent priceForADayText;
    private StyledTextComponent priceForAMonthText;

    private List<DataWater> dataWaterList;
    private int signalStrength;
    private HardwareLive associatedHwLive;

    @Id("titleNameField")
    private H2 titleNameField;
    @Id("ownerSpanField")
    private Span ownerSpanField;
    @Id("consumptionDivText")
    private Div consumptionDivText;
    @Id("consumptionProgressBar")
    private ProgressBar consumptionProgressBar;
    @Id("priceDiv")
    private Div priceDiv;
    @Id("actualizedDivField")
    private Div actualizedDivField;
    @Id("hwNameField")
    private Span hwNameField;
    @Id("activeStatusHwField")
    private Div activeStatusHwField;
    @Id("hardwareStatusButton")
    private Button hardwareStatusButton;
    @Id("hardwareStatusActualizedField")
    private Div hardwareStatusActualizedField;
    @Id("monthlyChart")
    private Chart monthlyChart;
    @Id("monthlyChartDiv")
    private Div monthlyChartDiv;
    @Id("dailyConsumptionDiv")
    private Div dailyConsumptionDiv;
    @Id("consumptionDatePicker")
    private DatePicker consumptionDatePicker;
    @Id("calculateBtn")
    private Button calculateBtn;
    @Id("calculateWholeYear")
    private Button calculateWholeYear;
    @Id("priceForAMonth")
    private Div priceForAMonth;
    @Id("priceForDayDatePicker")
    private DatePicker priceForDayDatePicker;
    @Id("priceForADayDiv")
    private Div priceForADayDiv;
    @Id("yearSelecterField")
    private TextField yearSelecterField;
    @Id("monthSelecter")
    private Select<Month> monthSelecter;
    @Id("consumptionMonthDivText")
    private Div consumptionMonthDivText;
    @Id("monthLimitProgressBar")
    private ProgressBar monthLimitProgressBar;
    @Id("priceThisMonthDiv")
    private Div priceThisMonthDiv;

    /**
     * Creates a new SensorWatDashboard.
     */
    public SensorWaterDashboard(SensorWaterService sensorWaterService,
                                SensorService sensorService, DataWaterService dataWaterService,
                                UserService userService, HardwareService hardwareService, HardwareLiveService hardwareLiveService) {
        this.sensorWaterService = sensorWaterService;
        this.sensorService = sensorService;
        this.dataWaterService = dataWaterService;
        this.userService = userService;
        this.hardwareService = hardwareService;
        this.hardwareLiveService = hardwareLiveService;
        dailyConsumptionChart = new Chart(ChartType.COLUMN);

        int sensorId = -1;
        sensorId = (int) VaadinSession.getCurrent().getAttribute("sensorId");
        if (sensorId != -1) {
            this.sensorWaterService.get(sensorId).ifPresent(water -> sensorWater = water);
            this.sensorService.get(sensorId).ifPresent(sensor -> this.sensor = sensor);

            setInfoData();
            setConsumptionBar();
            setDailyConsumptionChart();
            setHwInfoData();
            setMonthlyConsumptionChart();
            setMonthlyLimitBar();

            setPriceOfDayPicker();
            setPriceCalculationCards();

            dailyConsumptionDiv.add(dailyConsumptionChart);
        }
    }

    private void setPriceCalculationCards() {
        yearSelecterField.setPattern(PatternStringUtils.yearRegex);
        yearSelecterField.setErrorMessage(PatternStringUtils.yearErrorMessage);
        yearSelecterField.setPlaceholder("Type in a year.");
        yearSelecterField.setRequired(true);

        monthSelecter.removeAll();
        monthSelecter.setItems(PatternStringUtils.months);
        monthSelecter.setTextRenderer(Enum::name);
        monthSelecter.setValue(LocalDate.now().getMonth());

        priceForAMonthText = new StyledTextComponent("");
        priceForAMonth.add(priceForAMonthText);
        priceForAMonth.setClassName("priceForADayOrMonthDiv");

        calculateWholeYear.setThemeName("secondary");

        calculateBtn.addClickListener(event -> {
            if (!monthSelecter.isInvalid() && !yearSelecterField.isInvalid()) {
                LocalDate date = getDateFromSelecterFields();

                List<DataWater> dataElectrics = dataWaterService.findAllBySensorIdAndDate(sensor.getId(),
                        date.with(firstDayOfMonth()), date.with(lastDayOfMonth()));
                Double price = dataElectrics.stream().mapToDouble(value -> value.getM3() * sensorWater.getPrice_per_m3()).sum();
                priceForAMonthText.setText("Monthly price: <b>" + PatternStringUtils.formatNumberToText(
                        getNumberOfDecimalPrecision(price, 3)) + " [" + sensor.getCurrencyString() + "]");
                Notification.show("Price successfully calculated.");
            }
        });

        calculateWholeYear.addClickListener(event -> {
            if (!yearSelecterField.isInvalid()) {
                int year;
                try {
                    year = Integer.parseInt(yearSelecterField.getValue());
                } catch (NumberFormatException e) {
                    year = LocalDate.now().getYear();
                    Dialog dialog = new Dialog();
                    dialog.setModal(true);
                    dialog.add("Wrong year input. This year chosen by default.");
                    dialog.open();
                    yearSelecterField.setValue(LocalDate.now().getYear()+"");
                }
                LocalDate dateFrom = LocalDate.of(year, 1, 1);
                LocalDate dateTo = LocalDate.of(year, 12, 31);

                List<DataWater> dataElectrics = dataWaterService.findAllBySensorIdAndDate(sensor.getId(),
                        dateFrom, dateTo);
                Double price = dataElectrics.stream().mapToDouble(value -> value.getM3() * sensorWater.getPrice_per_m3()).sum();
                priceForAMonthText.setText("Yearly price: <b>" + PatternStringUtils.formatNumberToText(
                        getNumberOfDecimalPrecision(price, 3)) + " [" + sensor.getCurrencyString() + "]");
                Notification.show("Price successfully calculated.");
            }
        });
    }

    private void setMonthlyLimitBar() {
        List<DataWater> thisMonthData = dataWaterService.findAllBySensorIdAndDate(sensor.getId(), LocalDate.now().withDayOfMonth(1), LocalDate.now());
        double price = 0.0;
        double consumption = 0.0;
        for (DataWater data : thisMonthData) {
            price += data.getM3() * sensorWater.getPrice_per_m3();
            consumption += data.getM3();
        }
        consumptionMonthText = new StyledTextComponent("Consumption: <b>" + PatternStringUtils.formatNumberToText(
                getNumberOfDecimalPrecision(consumption, 3)) + " / " + sensor.getLimit_month() + " [m3]</b>");
        priceMonthText = new StyledTextComponent("Monthly price so far: <b>" + PatternStringUtils.formatNumberToText(
                getNumberOfDecimalPrecision(price, 3))
                + " " + sensor.getCurrencyString() + "</b>");
        consumptionMonthDivText.setText("");
        consumptionMonthDivText.add(consumptionMonthText);
        priceThisMonthDiv.setText("");
        priceThisMonthDiv.add(priceMonthText);
        monthLimitProgressBar.setMin(0);
        monthLimitProgressBar.setMax(sensor.getLimit_month());
        if (consumption >= sensor.getLimit_month()) {
            monthLimitProgressBar.setValue(sensor.getLimit_month());
        } else {
            monthLimitProgressBar.setValue(consumption);
        }
        monthLimitProgressBar.setHeight("15px");
        monthLimitProgressBar.setVisible(true);
    }

    private LocalDate getDateFromSelecterFields() {
        Month month = monthSelecter.getValue();
        if (month == null) {
            month = LocalDate.now().getMonth();
        }
        int year;
        try {
            year = Integer.parseInt(yearSelecterField.getValue());
        } catch (NumberFormatException e) {
            year = LocalDate.now().getYear();
            Dialog dialog = new Dialog();
            dialog.setModal(true);
            dialog.add("Wrong year input. This year chosen by default.");
            dialog.open();
            yearSelecterField.setValue(LocalDate.now().getYear()+"");
        }
        return LocalDate.of(year, month, 1);
    }

    private void setInfoData() {
        titleNameField.setText(sensor.getName());
        try {
            ownerSpanField.setText("Owner: " + userService.getHardwareOwner(sensor.getIdHw()).getFullName());
        } catch (Exception e) {
            ownerSpanField.setText("...");
        }
    }

    private void setConsumptionBar() {
        consumptionText = new StyledTextComponent("Consumption: <b>" + sensor.getConsumptionActual() + " / " + sensor.getLimit_day() + " [m3]</b>");
        priceText = new StyledTextComponent("Price: <b>" + PatternStringUtils.formatNumberToText(sensor.getConsumptionActual() * sensorWater.getPrice_per_m3())
                + " " + sensor.getCurrencyString() + "</b>");
        consumptionDivText.setText("");
        consumptionDivText.add(consumptionText);
        priceDiv.setText("");
        priceDiv.add(priceText);
        consumptionProgressBar.setMin(0);
        consumptionProgressBar.setMax(sensor.getLimit_day());
        consumptionProgressBar.setValue(sensor.getConsumptionActual());
        consumptionProgressBar.setHeight("15px");
        consumptionProgressBar.setVisible(true);
    }

    private void setDailyConsumptionChart() {
        LocalDate date = LocalDate.now();
        consumptionDatePicker.setValue(date);

        Configuration configuration = dailyConsumptionChart.getConfiguration();
        configuration.setTitle("Daily consumption of " + sensor.getName());
        configuration.setSubTitle("Date: " + LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));

        List<DataWater> dataWaters = dataWaterService.findAllBySensorIdAndDate(sensor.getId(), date, date);
        configuration.setSeries(new ListSeries("Consumption m3", dataWaters.stream().map(DataWater::getM3)
                .collect(Collectors.toCollection(ArrayList::new))));

        XAxis x = new XAxis();
        x.setCrosshair(new Crosshair());
        x.setCategories("00", "01", "02", "03", "04", "05", "06", "07", "08",
                "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20",
                "21", "22", "23", "24");
        x.setTitle("Hour");
        configuration.addxAxis(x);

        YAxis y = new YAxis();
        y.setMin(0);
        y.setTitle("Consumption");
        configuration.addyAxis(y);

        Tooltip tooltip = new Tooltip();
        tooltip.setShared(true);
        tooltip.setValueSuffix(" [m3]");
        configuration.setTooltip(tooltip);

        consumptionDatePicker.addValueChangeListener(event -> {
            updateDailyConsumptionChartData(event.getValue());
        });
    }

    private void setMonthlyConsumptionChart() {
        LocalDate date = LocalDate.now();

        Configuration configuration = monthlyChart.getConfiguration();
        configuration.getChart().setType(ChartType.COLUMN);

        configuration.getSubTitle().setText("Prices aggregated by daily consumptions.");

        YAxis yAxis = configuration.getyAxis();
        yAxis.setTitle(new AxisTitle("Aggregated price [" + sensor.getCurrencyString() + "]"));

        PlotOptionsLine plotOptions = new PlotOptionsLine();
        plotOptions.setEnableMouseTracking(false);
        configuration.setPlotOptions(plotOptions);

        List<DataWater> dataWaters = dataWaterService.findAllBySensorIdAndDate(
                sensor.getId(), date.minusDays(30), date);

        DataSeries ds = new DataSeries();

        PlotOptionsColumn plotOpts = new PlotOptionsColumn();
        plotOpts.setColor(SolidColor.DARKSLATEGRAY);
        ds.setPlotOptions(plotOpts);
        ds.setName("Price");

        ArrayList<DataSeriesItem> dailyPrices = new ArrayList<>();
        Map<LocalDate, Double> pricesMap = new TreeMap<>(Comparator.naturalOrder());
        for (DataWater data : dataWaters) {
            LocalDate time = data.getTime().toLocalDateTime().toLocalDate();
            Double price = data.getM3() * sensorWater.getPrice_per_m3();
            if (pricesMap.containsKey(time)) {
                pricesMap.replace(time, (pricesMap.get(time) + price));
            } else {
                pricesMap.put(time, price);
            }
        }

        pricesMap.forEach((localDate, aDouble) -> {
            configuration.getxAxis().addCategory(localDate.format(DateTimeFormatter.ofPattern("EEE, dd.MM.yyyy")));
            dailyPrices.add(new DataSeriesItem("",aDouble));
        });
        configuration.getxAxis().setVisible(false);

        Tooltip tooltip = new Tooltip();
        tooltip.setShared(true);
        tooltip.setValueSuffix(" " + sensor.getCurrencyString());
        configuration.setTooltip(tooltip);

        ds.setData(dailyPrices);
        configuration.addSeries(ds);
    }

    private void updateDailyConsumptionChartData(LocalDate value) {
        Configuration configuration = dailyConsumptionChart.getConfiguration();
        List<DataWater> dataWaters = dataWaterService.findAllBySensorIdAndDate(sensor.getId(), value, value);
        configuration.setSeries(new ListSeries("Consumption m3", dataWaters.stream().map(DataWater::getM3)
                .collect(Collectors.toCollection(ArrayList::new))));
        try {
            getUI().ifPresent(ui -> ui.access(() -> {
                configuration.setSubTitle("Date: " + value.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
                dailyConsumptionChart.setConfiguration(configuration);
                Notification.show("Daily consumption updated.");
                ui.push();
            }));
        } catch (UIDetachedException exception) {
            //ignore, harmless exception in this case of a push
        }
    }

    private void setHwInfoData() {
        Optional<Hardware> hw = hardwareService.getBySerialHW(sensor.getIdHw());
        if (hw.isPresent()) {
            hwNameField.setText("HW serial code: " + hw.get().getName());
        } else {
            hwNameField.setText("HW serial code: ...");
        }
        associatedHwLive = hardwareLiveService.findByHardwareId(sensor.getIdHw());
        signalStrength = associatedHwLive != null ? associatedHwLive.getSignal_strength() : 0;
        activeStatusHwField.setText("Signal power: " + signalStrength +"% ");
        Icon statusIcon = new Icon(VaadinIcon.SIGNAL);
        colorIcon(statusIcon, signalStrength);
        statusIcon.setSize("40px");
        hardwareStatusButton.setIcon(statusIcon);
        hardwareStatusButton.setEnabled(false);
    }

    private void setPriceOfDayPicker() {
        priceForADayText = new StyledTextComponent("");
        priceForADayDiv.add(priceForADayText);
        priceForDayDatePicker.addValueChangeListener(event -> {
            List<DataWater> dataWaters = dataWaterService.findAllBySensorIdAndDate(sensor.getId(),
                    event.getValue(), event.getValue());
            priceForADayDiv.setClassName("priceForADayOrMonthDiv");
            Double price = dataWaters.stream().mapToDouble(value -> value.getM3() * sensorWater.getPrice_per_m3()).sum();
            priceForADayText.setText("Price: <b>" + PatternStringUtils.formatNumberToText(
                    getNumberOfDecimalPrecision(price, 3)) + " [" + sensor.getCurrencyString() + "]" );
            Notification.show("Price successfully calculated.");
        });
    }

    private void colorIcon(Icon signalIcon, int signal) {
        if (signal >= 75 && signal <= 100) {
            signalIcon.setColor(Colors.GREEN.getRgb());
        } else if (signal >= 50 && signal < 75) {
            signalIcon.setColor(Colors.YELLOW.getRgb());
        } else if (signal >= 25 && signal < 50) {
            signalIcon.setColor(Colors.ORANGE.getRgb());
        } else if (signal >= 0 && signal < 25) {
            signalIcon.setColor(Colors.RED.getRgb());
        }
    }

    @Scheduled(fixedDelay = 10000)
    public void refreshDashboardData() {
        try {
            Optional<Sensor> refreshedSensor = sensorService.get(sensor.getId());
            refreshConsumptionChart(refreshedSensor);
            refreshHardwareStatus();
            getUI().ifPresent(ui -> ui.access(() ->
                    {
                        LocalDateTime now = LocalDateTime.now();
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
                        String formatDateTime = now.format(formatter);
                        refreshPriceDiv(refreshedSensor.get());
                        refreshConsumptionText(refreshedSensor.get());
                        actualizedDivField.setText("Updated: " + formatDateTime);
                        hardwareStatusActualizedField.setText("Updated: " + formatDateTime);
                        ui.push();
                    }
            ));
        } catch (UIDetachedException exception) {
            //ignore, harmless exception in this case
        }
    }

    private void refreshPriceDiv(Sensor sensor) {
        priceText.setText("Price: <b>" + PatternStringUtils.formatNumberToText(sensor.getConsumptionActual() * sensorWater.getPrice_per_m3())
                + " " + sensor.getCurrencyString() + "</b>");
    }

    private void refreshHardwareStatus() {
        try {
            associatedHwLive = hardwareLiveService.findByHardwareId(sensor.getIdHw());
            signalStrength = associatedHwLive != null ? associatedHwLive.getSignal_strength() : 0;
            getUI().ifPresent(ui -> ui.access(() ->
                    {
                        activeStatusHwField.setText("Signal power: " + signalStrength + "% ");
                        Icon icon = (Icon) hardwareStatusButton.getIcon();
                        colorIcon(icon, signalStrength);
                        ui.push();
                    }
            ));
        } catch (UIDetachedException exception) {
            //ignore, harmless exception in this case
        }
    }

    private void refreshConsumptionChart(Optional<Sensor> refreshedSensor) {
        try {
            refreshedSensor.flatMap(value -> getUI()).ifPresent(ui -> ui.access(() -> {
                consumptionProgressBar.setValue(refreshedSensor.get().getConsumptionActual());
                ui.push();
            }));
        } catch (UIDetachedException exception) {
            //ignore, harmless exception in this case
        }
    }

    private void refreshConsumptionText(Sensor sensor) {
        consumptionText.setText("Consumption: <b>" + sensor.getConsumptionActual() + " / " + sensor.getLimit_day() + " [m3]</b>");
    }

    private double getNumberOfDecimalPrecision(Double number, int numberOfDecimalPlaces) {
        return BigDecimal.valueOf(number)
                .setScale(numberOfDecimalPlaces, RoundingMode.HALF_UP)
                .doubleValue();
    }
}
