package com.example.application.views.dashboard;

import com.example.application.components.StyledTextComponent;
import com.example.application.data.entity.*;
import com.example.application.data.entity.data.DataElectric;
import com.example.application.data.entity.data.DataWater;
import com.example.application.data.service.*;
import com.example.application.data.service.data.DataWaterService;
import com.example.application.utils.Colors;
import com.example.application.utils.PatternStringUtils;
import com.example.application.views.main.MainLayout;
import com.example.application.views.sensors.components.SensorsUtil;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.UIDetachedException;
import com.vaadin.flow.component.board.Board;
import com.vaadin.flow.component.board.Row;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.charts.Chart;
import com.vaadin.flow.component.charts.model.*;
import com.vaadin.flow.component.charts.model.style.SolidColor;
import com.vaadin.flow.component.charts.model.style.Style;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.littemplate.LitTemplate;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.progressbar.ProgressBar;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.radiobutton.RadioGroupVariant;
import com.vaadin.flow.component.template.Id;
import com.vaadin.flow.dom.Element;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.ParentLayout;
import com.vaadin.flow.server.VaadinSession;
import elemental.json.Json;
import elemental.json.JsonObject;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.awt.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * A Designer generated component for the sensor-wat-dashboard template.
 *
 * Designer will add and remove fields with @Id mappings but
 * does not overwrite or otherwise change this file.
 */
@Tag("sensor-wat-dashboard")
@JsModule("./views/dashboard/sensor-wat-dashboard.ts")
@CssImport("./views/dashboard/sensor-wat-dashboard.css")
@ParentLayout(MainLayout.class)
@PageTitle("Water sensor dashboard")
@EnableScheduling
public class SensorWaterDashboard extends LitTemplate {

    private SensorWaterService sensorWaterService;
    private DataWaterService dataWaterService;
    private SensorService sensorService;
    private UserService userService;
    private Chart mainChart;
    private Icon onlineStatusIcon;

    private SensorWater sensorWater;
    private HardwareService hardwareService;
    private HardwareLiveService hardwareLiveService;
    private Sensor sensor;

    private StyledTextComponent consumptionText;
    private StyledTextComponent consumptionPercentText;
    private StyledTextComponent consumptionMonthText;
    private StyledTextComponent consumptionTodayText;
    private StyledTextComponent priceMonthText;
    private StyledTextComponent priceTodayText;

    private List<DataWater> dataWaterList;
    private int signalStrength;
    private HardwareLive associatedHwLive;
    private NotificationLogHwService notificationLogHwService;

    private RadioButtonGroup<String> consPricesRadioButtonGroup;
    private RadioButtonGroup<String> periodRadioButtonGroup;

    @Id("titleNameField")
    private H2 titleNameField;
    @Id("ownerSpanField")
    private Span ownerSpanField;
    @Id("todayLimitProgressBar")
    private ProgressBar todayLimitProgressBar;
    @Id("priceTodayDiv")
    private Div priceTodayDiv;
    @Id("actualizedDivField")
    private Div actualizedDivField;
    @Id("hwNameField")
    private Span hwNameField;
    @Id("activeStatusHwField")
    private Div activeStatusHwField;
    @Id("hardwareStatusActualizedField")
    private Div hardwareStatusActualizedField;
    @Id("dailyConsumptionDiv")
    private Div dailyConsumptionDiv;
    @Id("consumptionDatePicker")
    private DatePicker consumptionDatePicker;
    @Id("consumptionMonthDivText")
    private Div consumptionMonthDivText;
    @Id("monthLimitProgressBar")
    private ProgressBar monthLimitProgressBar;
    @Id("priceThisMonthDiv")
    private Div priceThisMonthDiv;
    @Id("horizontalLayoutAboveChart")
    private HorizontalLayout horizontalLayoutAboveChart;
    @Id("typeOfChartDiv")
    private Div typeOfChartDiv;
    @Id("periodChangerChartDiv")
    private Div periodChangerChartDiv;
    @Id("previousButton")
    private Button previousButton;
    @Id("nextButton")
    private Button nextButton;
    @Id("consumptionTodayDivText")
    private Div consumptionTodayDivText;
    @Id("consumptionDivText")
    private Div consumptionDivText;
    @Id("consumptionProgressBar")
    private ProgressBar currentConsumptionProgressBar;
    @Id("onlineStatusDiv")
    private Div onlineStatusDiv;
    @Id("signalPowerHwField")
    private Div signalPowerHwField;
    @Id("createdField")
    private Span createdField;

    private Double consumptionMaxValue;
    private Image signalImage;
    private LocalDate dateForChart;
    @Id("hwSerialCodeField")
    private Span hwSerialCodeField;
    @Id("chartTitle")
    private H4 chartTitle;
    @Id("gaugeMonthLayout")
    private VerticalLayout gaugeMonthLayout;
    @Id("gaugeLayout")
    private VerticalLayout gaugeLayout;

    /**
     * Creates a new SensorWatDashboard.
     */
    public SensorWaterDashboard(SensorWaterService sensorWaterService,
                                SensorService sensorService, DataWaterService dataWaterService,
                                UserService userService, HardwareService hardwareService, HardwareLiveService hardwareLiveService,
                                NotificationLogHwService notificationLogHwService) {
        this.sensorWaterService = sensorWaterService;
        this.sensorService = sensorService;
        this.dataWaterService = dataWaterService;
        this.userService = userService;
        this.hardwareService = hardwareService;
        this.hardwareLiveService = hardwareLiveService;
        this.notificationLogHwService = notificationLogHwService;
        signalImage = new Image();
        mainChart = new Chart(ChartType.COLUMN);
        consumptionMaxValue = 0.0;

        consPricesRadioButtonGroup = new RadioButtonGroup<>();
        periodRadioButtonGroup = new RadioButtonGroup<>();

        int sensorId = -1;
        sensorId = (int) VaadinSession.getCurrent().getAttribute("sensorId");
        if (sensorId != -1) {
            this.sensorWaterService.get(sensorId).ifPresent(water -> sensorWater = water);
            this.sensorService.get(sensorId).ifPresent(sensor -> this.sensor = sensor);
            dateForChart = LocalDate.now();

            setInfoData();
            setHwInfoData();
            setDailyConsumptionChart();

            setConsumptionPricesChangerForChart();
            setPeriodChangerForChart();
            setMainChartControlButtons();

            setConsumptionBar();
            setTodayLimitBar();
            setMonthlyLimitBar();

            dailyConsumptionDiv.add(mainChart);
            typeOfChartDiv.add(consPricesRadioButtonGroup);
            periodChangerChartDiv.add(periodRadioButtonGroup);
            consumptionDatePicker.setValue(dateForChart);

            setProgressBarColor();
        }
    }


    private void setProgressBarColor() {
        currentConsumptionProgressBar.getStyle().set("--progress-color", Colors.BLUE.getRgb());
        monthLimitProgressBar.getStyle().set("--progress-color", Colors.BLUE.getRgb());
        todayLimitProgressBar.getStyle().set("--progress-color", Colors.BLUE.getRgb());
    }

    private void setTodayLimitBar() {
        LocalDate date = LocalDate.now();
        List<DataWater> todayData = dataWaterService.findAllBySensorIdAndDate(sensor.getId(),
                date, date);
        double price = 0.0;
        double consumption = 0.0;
        for (DataWater data : todayData) {
            price += data.getM3() * sensorWater.getPrice_per_m3();
            consumption += data.getM3();
        }
        consumptionTodayText = new StyledTextComponent("Consumption: <b>" + PatternStringUtils.formatNumberToText(
                getNumberOfDecimalPrecision(consumption, 1)) + " / " + sensor.getLimit_day() + " [m3]");
        priceTodayText = new StyledTextComponent("Price: <b>" + PatternStringUtils.formatNumberToText(
                getNumberOfDecimalPrecision(price, 1))
                + " " + sensor.getCurrencyString() + "</b>");
        consumptionTodayDivText.setText("");
        consumptionTodayDivText.add(consumptionTodayText);
        priceTodayDiv.setText("");
        priceTodayDiv.add(priceTodayText);
        todayLimitProgressBar.setMin(0);
        todayLimitProgressBar.setMax(sensor.getLimit_day());
        if (consumption >= sensor.getLimit_day()) {
            todayLimitProgressBar.setValue(sensor.getLimit_day());
        } else {
            todayLimitProgressBar.setValue(consumption);
        }
        gaugeLayout.add(DashBoardUtils.createConsumptionGauge(consumption, sensor.getLimit_day(), false));
        todayLimitProgressBar.setHeight("15px");
        todayLimitProgressBar.setVisible(false);
    }

    private void setConsumptionPricesChangerForChart() {
        consPricesRadioButtonGroup.addThemeVariants(RadioGroupVariant.LUMO_VERTICAL);
        consPricesRadioButtonGroup.setItems("Consumption", "Prices");
        consPricesRadioButtonGroup.setValue("Consumption");
        consPricesRadioButtonGroup.addValueChangeListener(event -> {
            if (event.getValue().equals("Prices")) {
                alterChartToPrices();
            } else {
                Tooltip tooltip = new Tooltip();
                tooltip.setShared(true);
                tooltip.setValueDecimals(5);
                tooltip.setValueSuffix(" [m3]");
                mainChart.getConfiguration().setTooltip(tooltip);
                alterChartToConsumptions();
            }
        });
    }


    private void setConsumptionBar() {
        calculateConsumptionHighestValue();
        if (consumptionMaxValue == 0.0) {
            consumptionText = new StyledTextComponent("Current consumption: <b>" + sensor.getConsumptionActual() + " / " + 0.0 + "</b> [m3/h] -> <b>" +
                    sensor.getConsumptionActual() + "%</b>");
        } else {
            consumptionText = new StyledTextComponent("Current consumption: <b>" + sensor.getConsumptionActual() + " / " + consumptionMaxValue  + "</b> [m3/h] -> <b>" +
                    String.format("%.1f",(sensor.getConsumptionActual()/(consumptionMaxValue*1000)*100)) + "%</b>");
        }
        consumptionDivText.setText("");
        consumptionDivText.addComponentAtIndex(0, consumptionText);
        currentConsumptionProgressBar.setMin(0);
        currentConsumptionProgressBar.setMax(consumptionMaxValue * 1000);
        setConsumptionProgressBar(sensor);
        currentConsumptionProgressBar.setHeight("15px");
        currentConsumptionProgressBar.setVisible(true);
    }

    private void calculateConsumptionHighestValue() {
        List<DataWater> dataWaterList = dataWaterService.findAllBySensorIdAndDate(sensor.getId(), LocalDate.now().minusDays(30), LocalDate.now());
        dataWaterList.forEach(dataWater -> {
            if (dataWater.getM3() > consumptionMaxValue) {
                consumptionMaxValue = dataWater.getM3();
            }
        });
    }


    private void updateChartToNextData() {
        if (periodRadioButtonGroup.getValue().equals("Day")) {
            dateForChart = dateForChart.plusDays(1);
            updateMainChartData(dateForChart, dateForChart, consPricesRadioButtonGroup.getValue().equals("Consumption"));
        } else {
            if (periodRadioButtonGroup.getValue().equals("Month")) {
                dateForChart = dateForChart.withDayOfMonth(1).plusMonths(1);
                updateMainChartData(YearMonth.from(dateForChart).atDay(1), YearMonth.from(dateForChart).atEndOfMonth(), consPricesRadioButtonGroup.getValue().equals("Consumption"));
            } else {
                dateForChart = dateForChart.withDayOfYear(1).plusYears(1);
                updateMainChartData(dateForChart.withDayOfYear(1), LocalDate.of(dateForChart.getYear(), 12, 31), consPricesRadioButtonGroup.getValue().equals("Consumption"));
            }
        }
        //Notification.show("Teď jsem se posunul o něco dále").setPosition(Notification.Position.MIDDLE);
    }

    private void updateChartToPreviousData() {
        if (periodRadioButtonGroup.getValue().equals("Day")) {
            dateForChart = dateForChart.minusDays(1);
            updateMainChartData(dateForChart, dateForChart, consPricesRadioButtonGroup.getValue().equals("Consumption"));
        } else {
            if (periodRadioButtonGroup.getValue().equals("Month")) {
                dateForChart = dateForChart.withDayOfMonth(1).minusMonths(1);
                updateMainChartData(YearMonth.from(dateForChart).atDay(1), YearMonth.from(dateForChart).atEndOfMonth(), consPricesRadioButtonGroup.getValue().equals("Consumption"));
            } else {
                dateForChart = dateForChart.withDayOfYear(1).minusYears(1);
                updateMainChartData(dateForChart.withDayOfYear(1), LocalDate.of(dateForChart.getYear(), 12, 31), consPricesRadioButtonGroup.getValue().equals("Consumption"));
            }
        }
        //Notification.show("Teď jsem se posunul o něco dřív").setPosition(Notification.Position.MIDDLE);
    }

    private void updateMainChartData(LocalDate dateFrom, LocalDate dateTo, boolean consumption) {
        Configuration configuration = mainChart.getConfiguration();
        if (consumption) {
            Tooltip tooltip = configuration.getTooltip();
            tooltip.setValueSuffix(" [m3]");
            configuration.setTooltip(tooltip);
            configuration.getyAxis().setTitle("Consumption");
            if (periodRadioButtonGroup.getValue().equals("Day")) {
                List<DataWater> dataWater = dataWaterService.findAllBySensorIdAndDate(sensor.getId(), dateFrom, dateTo);
                Map<Integer, Number> consumptionMap = new TreeMap<>();
                getConsumptionM3Day(dataWater, consumptionMap);

                XAxis x = new XAxis();
                x.setCrosshair(new Crosshair());
                x.setTitle("Hour");
                x.setCategories(PatternStringUtils.hoursOfDay);
                configuration.removexAxes();
                configuration.addxAxis(x);

                setSeriesToConfiguration(configuration, consumptionMap.values());
                colorLegendToGray(configuration, x);
            } else if (periodRadioButtonGroup.getValue().equals("Month")) {
                List<DataWater> dataWater = dataWaterService.findAllBySensorIdAndDate(sensor.getId(), dateFrom, dateTo);
                Map<LocalDate, Number> consumptionMap = new TreeMap<>();
                getConsumptionM3Month(dataWater, consumptionMap);

                chartTitle.setText(dateForChart.getMonth().name() + " of " + dateForChart.getYear());
                LocalDate firstOfMonth = dateForChart.withDayOfMonth(1);
                LocalDate firstOfFollowingMonth = dateForChart.plusMonths(1).withDayOfMonth(1);

                XAxis x = new XAxis();
                x.setCrosshair(new Crosshair());
                x.setTitle("Day");
                x.setCategories(firstOfMonth.datesUntil(firstOfFollowingMonth).map(date ->
                                date.format(DateTimeFormatter.ofPattern("EEE, dd.MM.yyyy")))
                        .toArray(String[]::new));
                configuration.removexAxes();
                configuration.addxAxis(x);
                setSeriesToConfiguration(configuration, consumptionMap.values());
                colorLegendToGray(configuration, x);
            } else {
                List<DataWater> dataWater = dataWaterService.findAllBySensorIdAndDate(sensor.getId(), dateFrom, dateTo);
                Map<Month, Number> consumptionMap = new TreeMap<>();
                getConsumptionM3Year(dataWater, consumptionMap);

                XAxis x = new XAxis();
                x.setCrosshair(new Crosshair());
                x.setTitle("Month");
                x.setCategories(Arrays.stream(Month.values()).map(Enum::name).toArray(String[]::new));
                configuration.removexAxes();
                configuration.addxAxis(x);
                setSeriesToConfiguration(configuration, consumptionMap.values());
                colorLegendToGray(configuration, x);
            }
        } else {
            Tooltip tooltip = configuration.getTooltip();
            tooltip.setValueSuffix(" " + sensor.getCurrencyString());
            configuration.setTooltip(tooltip);
            configuration.getyAxis().setTitle("Prices");

            DataSeries ds = new DataSeries();
            PlotOptionsColumn plotOpts = new PlotOptionsColumn();
            plotOpts.setColor(SolidColor.ORANGERED);
            ds.setPlotOptions(plotOpts);
            colorLegendToGray(configuration, configuration.getxAxis());
            if (periodRadioButtonGroup.getValue().equals("Day")) {
                XAxis x = new XAxis();
                x.setCrosshair(new Crosshair());
                x.setTitle("Hour");
                x.setCategories(PatternStringUtils.hoursOfDay);
                configuration.removexAxes();
                configuration.addxAxis(x);
            } else if (periodRadioButtonGroup.getValue().equals("Month")) {

                LocalDate firstOfMonth = dateFrom;
                LocalDate firstOfFollowingMonth = dateTo;
                System.out.println("Ted vypisuju dny od prvniho do posledniho pro datum: " + dateForChart);
                firstOfMonth.datesUntil(firstOfFollowingMonth).forEach(System.out::println);

                XAxis x = new XAxis();
                x.setCrosshair(new Crosshair());
                x.setTitle("Day");
                x.setCategories(firstOfMonth.datesUntil(firstOfFollowingMonth).map(date ->
                                date.format(DateTimeFormatter.ofPattern("EEE, dd.MM.yyyy")))
                        .toArray(String[]::new));
                configuration.removexAxes();
                configuration.addxAxis(x);
            } else {
                XAxis x = new XAxis();
                x.setCrosshair(new Crosshair());
                x.setTitle("Month");
                x.setCategories(Arrays.stream(Month.values()).map(Enum::name).toArray(String[]::new));
                configuration.removexAxes();
                configuration.addxAxis(x);
            }

            ds.setName("Price");
            ds.setData(getPricesForTime(configuration, dateFrom, dateTo));
            configuration.setSeries(ds);
        }
        try {
            getUI().ifPresent(ui -> ui.access(() -> {
                if (periodRadioButtonGroup.getValue().equals("Day")) {
                    chartTitle.setText(dateFrom.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
                } else if (periodRadioButtonGroup.getValue().equals("Month")) {
                    chartTitle.setText(dateFrom.getMonth().name() + " " + dateFrom.getYear());
                } else {
                    chartTitle.setText("Year " + dateFrom.getYear());
                }
                mainChart.setConfiguration(configuration);
                Notification.show("Chart updated.");
                ui.push();
            }));
        } catch (UIDetachedException exception) {
            //ignore, harmless exception in this case of a push
        }
    }

    private void colorLegendToGray(Configuration configuration, XAxis x) {
        Style gray = new Style();
        gray.setColor(new SolidColor("#666666"));
        x.getLabels().setStyle(gray);
        configuration.getLegend().setItemStyle(gray);
    }

    private ArrayList<DataSeriesItem> getPricesForTime(Configuration configuration,
                                                       LocalDate dateFrom, LocalDate dateTo) {
        List<DataWater> dataWaterList = dataWaterService.findAllBySensorIdAndDate(
                sensor.getId(), dateFrom, dateTo);

        ArrayList<DataSeriesItem> aggregatedPrices = new ArrayList<>();

        if (periodRadioButtonGroup.getValue().equals("Day")) {
            Map<Integer, Double> pricesDailyMap = calculatePricesForEachHourOfDay(dataWaterList);
            pricesDailyMap.forEach((hour, aDouble) -> {
                aggregatedPrices.add(new DataSeriesItem(String.valueOf(hour), aDouble));
            });
        } else if (periodRadioButtonGroup.getValue().equals("Month")) {
            Map<LocalDate, Double> pricesDailyMap = calculatePricesForEachDayOfMonth(dataWaterList);
            Month month = dateFrom.plusDays(3).getMonth();
            pricesDailyMap.forEach((localDate, aDouble) -> {
                if(localDate.getMonth() == month) {
                    chartTitle.setText(localDate.getMonth().name() + " of " + localDate.getYear());
                    aggregatedPrices.add(new DataSeriesItem(localDate.format(DateTimeFormatter.ofPattern("EEE, dd.MM.yyyy")), aDouble));
                }
            });

        } else {
            Map<Month, Double> pricesMonthMap = calculatePricesForEachMonthOfYear(dataWaterList);
            pricesMonthMap.forEach((month, aDouble) -> {
                aggregatedPrices.add(new DataSeriesItem(month.name(), aDouble));
            });
        }
        return aggregatedPrices;
    }

    private Map<Integer, Double> calculatePricesForEachHourOfDay(List<DataWater> dataWaterList) {
        Map<Integer, Double> pricesMap = new TreeMap<>(Comparator.naturalOrder());
        for (DataWater data : dataWaterList) {
            Integer hour = data.getTime().toLocalDateTime().getHour();
            Double price = data.getM3() * sensorWater.getPrice_per_m3();
            if (pricesMap.containsKey(hour)) {
                pricesMap.replace(hour, (pricesMap.get(hour) + price));
            } else {
                pricesMap.put(hour, price);
            }
        }

        for (int i = 0; i < 24; i++) {
            if (!pricesMap.containsKey(i)) {
                pricesMap.put(i, 0.0);
            }
        }

        return pricesMap;
    }

    private Map<LocalDate, Double> calculatePricesForEachDayOfMonth(List<DataWater> dataWaterList) {
        Map<LocalDate, Double> pricesMap = new TreeMap<>(Comparator.naturalOrder());
        for (DataWater data : dataWaterList) {
            LocalDate time = data.getTime().toLocalDateTime().toLocalDate();
            Double price = data.getM3() * sensorWater.getPrice_per_m3();
            if (pricesMap.containsKey(time)) {
                pricesMap.replace(time, (pricesMap.get(time) + price));
            } else {
                pricesMap.put(time, price);
            }
        }

        for (LocalDate date : dateForChart.withDayOfMonth(1)
                .datesUntil(dateForChart.withDayOfMonth(1).plusMonths(1)).collect(Collectors.toList())) {
            if (!pricesMap.containsKey(date)) {
                pricesMap.put(date, 0.0);
            }
        }

        return pricesMap;
    }

    private Map<Month, Double> calculatePricesForEachMonthOfYear(List<DataWater> dataWaterList) {
        Map<Month, Double> pricesMap = new TreeMap<>(Comparator.naturalOrder());
        for (DataWater data : dataWaterList) {
            Month time = data.getTime().toLocalDateTime().toLocalDate().getMonth();
            Double price = data.getM3() * sensorWater.getPrice_per_m3();
            if (pricesMap.containsKey(time)) {
                pricesMap.replace(time, (pricesMap.get(time) + price));
            } else {
                pricesMap.put(time, price);
            }
        }

        for (Month month : Month.values()) {
            if (!pricesMap.containsKey(month)) {
                pricesMap.put(month, 0.0);
            }
        }
        return pricesMap;
    }


    private void setSeriesToConfiguration(Configuration configuration, Collection<Number> values) {
        ListSeries series = getColoredListSeries(new ArrayList<>(values), "m3", new SolidColor(Colors.BLUE.getRgb()));
        configuration.setSeries(series);
    }

    private ListSeries getColoredListSeries(List<Number> rates, String s, SolidColor black) {
        ListSeries series = new ListSeries(s, rates);
        PlotOptionsColumn options = new PlotOptionsColumn();
        options.setColor(black);
        series.setPlotOptions(options);
        return series;
    }

    private void getConsumptionM3Day(List<DataWater> dataWaterList, Map<Integer, Number> consumptionMap) {
        for (int i = 0; i < 24; i++) {
            consumptionMap.put(i, 0.0);
        }
        for (DataWater data : dataWaterList) {
            Integer hour = data.getTime().toLocalDateTime().getHour();
            consumptionMap.replace(hour, consumptionMap.get(hour).doubleValue() + data.getM3());
        }
    }

    private void getConsumptionM3Year(List<DataWater> dataWaterList, Map<Month, Number> consumptionMap) {
        for (Month month : Month.values()) {
            consumptionMap.put(month, 0.0);
        }

        for (DataWater data : dataWaterList) {
            Month month = data.getTime().toLocalDateTime().getMonth();
            consumptionMap.replace(month, consumptionMap.get(month).doubleValue() + data.getM3());
        }
    }

    private void getConsumptionM3Month(List<DataWater> dataWaterList, Map<LocalDate, Number> consumptionMap) {
        System.out.println(dateForChart);
        for (LocalDate date : dateForChart.withDayOfMonth(1)
                .datesUntil(dateForChart.withDayOfMonth(1).plusMonths(1)).collect(Collectors.toList())) {
            consumptionMap.put(date, 0.0);
        }

        for (DataWater data : dataWaterList) {
            LocalDate day = data.getTime().toLocalDateTime().toLocalDate();
            consumptionMap.replace(day, consumptionMap.get(day) == null ? 0.0 : consumptionMap.get(day).doubleValue() + data.getM3());
        }
    }

    private void setPeriodChangerForChart() {
        //periodRadioButtonGroup.addThemeVariants(RadioGroupVariant.LUMO_VERTICAL);
        periodRadioButtonGroup.setItems("Day", "Month", "Year");
        periodRadioButtonGroup.setValue("Day");
        periodRadioButtonGroup.addValueChangeListener(event -> {
            switch (event.getValue()) {
                case "Day":
                    dateForChart = LocalDate.now();
                    alterChartToDay();
                    consumptionDatePicker.setVisible(true);
                    return;
                case "Month":
                    alterChartToMonth();
                    consumptionDatePicker.setVisible(false);
                    return;
                case "Year":
                    alterChartToYear();
                    consumptionDatePicker.setVisible(false);
            }
        });
    }

    private void setMainChartControlButtons() {
        previousButton.setIcon(VaadinIcon.ANGLE_LEFT.create());
        nextButton.setIcon(VaadinIcon.ANGLE_RIGHT.create());
        nextButton.setIconAfterText(true);

        previousButton.addClickListener(event -> {
            updateChartToPreviousData();
//            super.getUI().get().getPage().executeJs("document.body.scrollTo(0,500);");
//            UI.getCurrent().getPage().executeJs("window.scroll(0,500);");
//            scrollDownDiv.getElement().callFunction("scrollIntoView(false)");
//            ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);
//            executorService.schedule(this::scrollDown, 3000, TimeUnit.MILLISECONDS);
        });

        nextButton.addClickListener(event -> {
            updateChartToNextData();
//            super.getUI().get().getPage().executeJs("document.body.scrollTo(0,500);");
//            UI.getCurrent().getPage().executeJs("window.scroll(0,500);");
//            scrollDownDiv.getElement().callFunction("scrollIntoView(false)");
//            ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);
//            executorService.schedule(this::scrollDown, 3000, TimeUnit.MILLISECONDS);
        });
    }

    private void scrollDown() {
        UI.getCurrent().access(() -> {
            JsonObject arg1 = Json.createObject();
            arg1.put("alignToTop", false);
            mainChart.getElement().executeJs("scrollIntoView", arg1);
        });
    }

    private void alterChartToYear() {
        dateForChart = dateForChart.withDayOfYear(1);
        updateMainChartData(dateForChart, LocalDate.of(dateForChart.getYear(), 12, 31), consPricesRadioButtonGroup.getValue().equals("Consumption"));
        //Notification.show("Teď ukazuju v grafu ročně").setPosition(Notification.Position.BOTTOM_START);
    }

    private void alterChartToMonth() {
        dateForChart = dateForChart.withDayOfMonth(1);
        updateMainChartData(dateForChart, YearMonth.from(dateForChart).atEndOfMonth(), consPricesRadioButtonGroup.getValue().equals("Consumption"));
        //Notification.show("Teď ukazuju v grafu měsíčně").setPosition(Notification.Position.BOTTOM_START);
    }

    private void alterChartToDay() {
        consumptionDatePicker.setValue(dateForChart);
        updateMainChartData(dateForChart, dateForChart, consPricesRadioButtonGroup.getValue().equals("Consumption"));
        //Notification.show("Teď ukazuju v grafu denně").setPosition(Notification.Position.BOTTOM_START);
    }

    private void alterChartToPrices() {
        changeChartBasedOnPeriod();
        //Notification.show("Teď ukazuju v grafu prices").setPosition(Notification.Position.BOTTOM_START);
    }

    private void alterChartToConsumptions() {
        changeChartBasedOnPeriod();
        //Notification.show("Teď ukazuju v grafu consumption").setPosition(Notification.Position.BOTTOM_START);
    }

    private void changeChartBasedOnPeriod() {
        if (periodRadioButtonGroup.getValue().equals("Day")) {
            alterChartToDay();
        } else if (periodRadioButtonGroup.getValue().equals("Month")) {
            alterChartToMonth();
        } else {
            alterChartToYear();
        }
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
                getNumberOfDecimalPrecision(consumption, 1)) + " / " + sensor.getLimit_month() + " [m3]");
        priceMonthText = new StyledTextComponent("Price: <b>" + PatternStringUtils.formatNumberToText(
                getNumberOfDecimalPrecision(price, 1))
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
        gaugeMonthLayout.add(DashBoardUtils.createConsumptionGauge(consumption, sensor.getLimit_month(), false));
        monthLimitProgressBar.setHeight("15px");
        monthLimitProgressBar.setVisible(false);
    }

       private void setInfoData() {
        titleNameField.setText(sensor.getName());
        try {
            List<User> users = userService.getHardwareOwner(sensor.getIdHw());
            if (!users.isEmpty()) {
                User user = users.get(0);
                ownerSpanField.setText("Owner: " + user.getFullName());
            }
        } catch (Exception e) {
            ownerSpanField.setText("Owner: ...");
        }
        createdField.setText("Created: " + new SimpleDateFormat("dd.MM.yyyy HH:mm").format(sensor.getCreatedDate()));
    }

    private Icon addOnlineStatusIcon() {
        NotificationLogHw logHw = notificationLogHwService.findBySerialHw(sensor.getIdHw());
        if (logHw == null) {
            onlineStatusIcon = VaadinIcon.CIRCLE.create();
            onlineStatusIcon.setColor(Colors.GREEN.getRgb());
            onlineStatusIcon.setClassName("onlineIcon");
            onlineStatusIcon.setSize("20px");
        } else {
            onlineStatusIcon = VaadinIcon.CIRCLE.create();
            onlineStatusIcon.setColor(Colors.RED.getRgb());
            onlineStatusIcon.setClassName("onlineIcon");
            onlineStatusIcon.setSize("20px");
        }
        return onlineStatusIcon;
    }

    private void setDailyConsumptionChart() {
        //consumptionDatePicker.setValue(dateForChart);

        Configuration configuration = mainChart.getConfiguration();

        XAxis x = new XAxis();
        x.setCrosshair(new Crosshair());
        x.setTitle("Hour");
        configuration.addxAxis(x);

        YAxis y = new YAxis();
        y.setMin(0);
        y.setTitle("Consumption");
        configuration.addyAxis(y);

        Tooltip tooltip = new Tooltip();
        tooltip.setShared(true);
        tooltip.setValueSuffix(" [kW]");
        configuration.setTooltip(tooltip);

        configuration.getChart().setBackgroundColor(new SolidColor(255,255,255,0));
        chartTitle.setText(dateForChart.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));

        consumptionDatePicker.addValueChangeListener(event -> {
            dateForChart = event.getValue();
            updateMainChartData(event.getValue(), event.getValue(), consPricesRadioButtonGroup.getValue().equals("Consumption"));
        });
    }

    private void setHwInfoData() {
        Optional<Hardware> hw = hardwareService.getBySerialHW(sensor.getIdHw());
        if (hw.isPresent()) {
            hwNameField.setText("HW name: " + hw.get().getName());
            hwSerialCodeField.setText("HW serial code: " + hw.get().getSerial_HW());

        } else {
            hwNameField.setText("HW name: ...");
            hwSerialCodeField.setText("HW serial code: ...");

        }
        associatedHwLive = hardwareLiveService.findByHardwareId(sensor.getIdHw());
        onlineStatusDiv.add(addOnlineStatusIcon());
        signalStrength = associatedHwLive != null ? associatedHwLive.getSignal_strength() : 0;

        SensorsUtil.setSignalImage(signalStrength, signalPowerHwField, signalImage);
        signalImage.setClassName("statusImage");
    }

    @Scheduled(fixedDelay = 10000)
    public void refreshDashboardData() {
        try {
            Optional<Sensor> refreshedSensor = sensorService.get(sensor.getId());
            getUI().ifPresent(ui -> ui.access(() ->
                    {
                        refreshHardwareStatus();
                        refreshConsumptionText(refreshedSensor.get());
                        refreshConsumptionChart(refreshedSensor.get());
                        refreshOnlineStatusIcon();

                        LocalDateTime now = LocalDateTime.now();
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
                        String formatDateTime = now.format(formatter);
                        actualizedDivField.setText("Updated: " + formatDateTime);
                        hardwareStatusActualizedField.setText("Updated: " + formatDateTime);
                        ui.push();
                    }
            ));
        } catch (UIDetachedException exception) {
            //ignore, harmless exception in this case
        }
    }

    private void refreshHardwareStatus() {
            associatedHwLive = hardwareLiveService.findByHardwareId(sensor.getIdHw());
            signalStrength = associatedHwLive != null ? associatedHwLive.getSignal_strength() : 0;
            SensorsUtil.updateSignalImage(associatedHwLive, signalImage);
    }

    private void refreshConsumptionText(Sensor sensor) {
        if (consumptionMaxValue == 0.0) {
            consumptionText.setText("Current consumption: <b>" + sensor.getConsumptionActual() + " / " + 0.0 + "</b> [m3/h]");
        } else {
            consumptionText.setText("Current consumption: <b>" + sensor.getConsumptionActual() + " / " + consumptionMaxValue + "</b> [m3/h]");
        }
    }

    private void setConsumptionProgressBarValue(Sensor sensor) {
        if (sensor.getConsumptionActual() >= sensor.getLimit_day()) {
            todayLimitProgressBar.setValue(todayLimitProgressBar.getMax());
        } else {
            todayLimitProgressBar.setValue(sensor.getConsumptionActual());
        }
    }

    private void refreshOnlineStatusIcon() {
        NotificationLogHw logHw = notificationLogHwService.findBySerialHw(sensor.getIdHw());
        if (logHw == null) {
            onlineStatusIcon.setColor(Colors.GREEN.getRgb());
        } else {
            onlineStatusIcon.setColor(Colors.RED.getRgb());
        }
    }

    private void refreshConsumptionChart(Sensor refreshedSensor) {
        setConsumptionProgressBar(refreshedSensor);
    }

    private double getNumberOfDecimalPrecision(Double number, int numberOfDecimalPlaces) {
        return BigDecimal.valueOf(number)
                .setScale(numberOfDecimalPlaces, RoundingMode.HALF_UP)
                .doubleValue();
    }

    private void setConsumptionProgressBar(Sensor sens) {
        if (sens.getConsumptionActual() >= consumptionMaxValue) {
            currentConsumptionProgressBar.setValue(consumptionMaxValue);
        } else {
            currentConsumptionProgressBar.setValue(sens.getConsumptionActual());
        }
    }
}
