package com.example.application.views.dashboard;

import com.example.application.components.StyledTextComponent;
import com.example.application.data.entity.*;
import com.example.application.data.entity.data.DataElectric;
import com.example.application.data.service.*;
import com.example.application.data.service.data.DataElectricService;
import com.example.application.utils.Colors;
import com.example.application.utils.PatternStringUtils;
import com.example.application.views.main.MainLayout;
import com.example.application.views.sensors.components.SensorsUtil;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.UIDetachedException;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.charts.Chart;
import com.vaadin.flow.component.charts.model.*;
import com.vaadin.flow.component.charts.model.style.SolidColor;
import com.vaadin.flow.component.charts.model.style.Style;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.html.*;
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
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.ParentLayout;
import com.vaadin.flow.server.VaadinSession;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * A Designer generated component for the sensor-el-dashboard template.
 * <p>
 * Designer will add and remove fields with @Id mappings but
 * does not overwrite or otherwise change this file.
 */
@Tag("sensor-el-dashboard")
@JsModule("./views/dashboard/sensor-el-dashboard.ts")
@CssImport("./views/dashboard/sensor-el-dashboard.css")
@ParentLayout(MainLayout.class)
@PageTitle("Electric sensor dashboard")
@EnableScheduling
@CssImport(
        themeFor = "vaadin-progress-bar",
        value = "./views/dashboard/dynamically-change-progressbar-color.css"
)
public class SensorElectricDashboard extends LitTemplate {

    private Chart mainChart;
    private SensorElectricService sensorElectricService;
    private DataElectricService dataElectricService;
    private SensorService sensorService;
    private UserService userService;

    private SensorElectric sensorElectric;
    private HardwareService hardwareService;
    private HardwareLiveService hardwareLiveService;
    private NotificationLogHwService notificationLogHwService;
    private Sensor sensor;

    private StyledTextComponent consumptionText;
    private StyledTextComponent consumptionPercentText;
    private StyledTextComponent consumptionMonthText;
    private StyledTextComponent consumptionTodayText;
    private StyledTextComponent priceMonthText;
    private StyledTextComponent priceTodayText;

    private Double consumptionMaxValue;
    private Image signalImage;
    private Icon onlineStatusIcon;

    private List<DataElectric> dataElectricList;
    private int signalStrength;
    private HardwareLive associatedHwLive;

    private LocalDate dateForChart;

    @Id("titleNameField")
    private H2 titleNameField;
    @Id("consumptionDivText")
    private Div consumptionDivText;
    @Id("consumptionProgressBar")
    private ProgressBar consumptionProgressBar;
    @Id("signalPowerHwField")
    private Div signalPowerHwField;
    @Id("hwNameField")
    private Span hwNameField;
    @Id("actualizedDivField")
    private Div actualizedDivField;
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

    private RadioButtonGroup<String> consPricesRadioButtonGroup;
    private RadioButtonGroup<String> periodRadioButtonGroup;
    @Id("previousButton")
    private Button previousButton;
    @Id("nextButton")
    private Button nextButton;
    @Id("typeOfChartDiv")
    private Div typeOfChartDiv;
    @Id("periodChangerChartDiv")
    private Div periodChangerChartDiv;

    @Id("consumptionTodayDivText")
    private Div consumptionTodayDivText;
    @Id("todayLimitProgressBar")
    private ProgressBar todayLimitProgressBar;
    @Id("priceTodayDiv")
    private Div priceTodayDiv;
    @Id("onlineStatusDiv")
    private Div onlineStatusDiv;
    @Id("createdField")
    private Span createdField;
    @Id("ownerSpanField")
    private Span ownerSpanField;
    @Id("hwSerialCodeField")
    private Span hwSerialCodeField;
    @Id("chartTitle")
    private H4 chartTitle;
    @Id("todayConsumptionDiv")
    private Div todayConsumptionDiv;
    @Id("monthlyConsumptionDiv")
    private Div monthlyConsumptionDiv;
    @Id("gaugeLayout")
    private VerticalLayout gaugeLayout;
    @Id("gaugeMonthLayout")
    private VerticalLayout gaugeMonthLayout;

    /**
     * Dashboard for Sensor of Electric type.
     */
    public SensorElectricDashboard(SensorElectricService sensorElectricService,
                                   SensorService sensorService, DataElectricService dataElectricService,
                                   UserService userService, HardwareService hardwareService, HardwareLiveService hardwareLiveService,
                                   NotificationLogHwService notificationLogHwService) {
        this.sensorElectricService = sensorElectricService;
        this.sensorService = sensorService;
        this.dataElectricService = dataElectricService;
        this.userService = userService;
        this.hardwareService = hardwareService;
        this.hardwareLiveService = hardwareLiveService;
        this.notificationLogHwService = notificationLogHwService;
        mainChart = new Chart(ChartType.COLUMN);
        consPricesRadioButtonGroup = new RadioButtonGroup<>();
        periodRadioButtonGroup = new RadioButtonGroup<>();
        consumptionMaxValue = 0.0;
        signalImage = new Image();

        int sensorId = -1;
        sensorId = (int) VaadinSession.getCurrent().getAttribute("sensorId");
        if (sensorId != -1) {
            this.sensorElectricService.get(sensorId).ifPresent(electric -> sensorElectric = electric);
            this.sensorService.get(sensorId).ifPresent(sensor -> this.sensor = sensor);
            dateForChart = LocalDate.now();

            setInfoData();
            setConsumptionBar();
            setMainChart();

            setConsumptionPricesChangerForChart();
            setPeriodChangerForChart();
            setMainChartControlButtons();

            setHwInfoData();
            setTodayLimitBar();
            setMonthlyLimitBar();

            dailyConsumptionDiv.add(mainChart);
            typeOfChartDiv.add(consPricesRadioButtonGroup);
            periodChangerChartDiv.add(periodRadioButtonGroup);
            consumptionDatePicker.setValue(dateForChart);
        }
    }


    /**
     * Method that creates and sets configuration for progress bar, that represents Today consumption on
     * an electric sensor.
     */
    private void setTodayLimitBar() {
        LocalDate date = LocalDate.now();
        List<DataElectric> todayData = dataElectricService.findAllBySensorIdAndDate(sensor.getId(),
                date, date);
        double price = 0.0;
        double consumption = 0.0;
        for (DataElectric data : todayData) {
            price += data.getHighRate() * sensorElectric.getPricePerKwHigh() + data.getLowRate() * sensorElectric.getPricePerKwLow();
            consumption += data.getHighRate() + data.getLowRate();
        }
        consumptionTodayText = new StyledTextComponent("Consumption: <b>" + PatternStringUtils.formatNumberToText(
                getNumberOfDecimalPrecision(consumption, 1)) + " / " + sensor.getLimit_day() + "</b> [kW]");
        priceTodayText = new StyledTextComponent("Price: <b>" + PatternStringUtils.formatNumberToText(
                getNumberOfDecimalPrecision(price, 1), "###,###,###,###,##0.0")
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
        gaugeLayout.add(DashBoardUtils.createConsumptionGauge(consumption, sensor.getLimit_day(), true));
        todayLimitProgressBar.setHeight("15px");
        todayLimitProgressBar.setVisible(false);
    }

    private void setMainChartControlButtons() {
        previousButton.setIcon(VaadinIcon.ANGLE_LEFT.create());
        nextButton.setIcon(VaadinIcon.ANGLE_RIGHT.create());
        nextButton.setIconAfterText(true);

        previousButton.addClickListener(event -> {
            updateChartToPreviousData();
        });

        nextButton.addClickListener(event -> {
            updateChartToNextData();
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
                tooltip.setValueSuffix(" [kW]");
                mainChart.getConfiguration().setTooltip(tooltip);
                alterChartToConsumptions();
            }
        });
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

    /**
     * Method that creates and sets configuration for progress bar, that represents Monthly consumption on
     * an electric sensor.
     */
    private void setMonthlyLimitBar() {
        List<DataElectric> thisMonthData = dataElectricService.findAllBySensorIdAndDate(sensor.getId(), LocalDate.now().withDayOfMonth(1), LocalDate.now());
        double price = 0.0;
        double consumption = 0.0;
        for (DataElectric data : thisMonthData) {
            price += data.getHighRate() * sensorElectric.getPricePerKwHigh() + data.getLowRate() * sensorElectric.getPricePerKwLow();
            consumption += data.getHighRate() + data.getLowRate();
        }
        consumptionMonthText = new StyledTextComponent("Consumption: <b>" + PatternStringUtils.formatNumberToText(
                getNumberOfDecimalPrecision(consumption, 1), "###,###,###,###,##0.0") + " / " + sensor.getLimit_month() + "</b> [kW]");
        priceMonthText = new StyledTextComponent("Price: <b>" + PatternStringUtils.formatNumberToText(
                getNumberOfDecimalPrecision(price, 1), "###,###,###,###,##0.0")
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
        monthLimitProgressBar.setVisible(false);
        gaugeMonthLayout.add(DashBoardUtils.createConsumptionGauge(consumption, sensor.getLimit_month(), true));
    }


    /**
     * Method that returns aggregated prices for given time period.
     */
    private ArrayList<DataSeriesItem> getPricesForTime(LocalDate dateFrom, LocalDate dateTo) {
        List<DataElectric> dataElectrics = dataElectricService.findAllBySensorIdAndDate(
                sensor.getId(), dateFrom, dateTo);

        ArrayList<DataSeriesItem> aggregatedPrices = new ArrayList<>();

        if (periodRadioButtonGroup.getValue().equals("Day")) {
            Map<Integer, Double> pricesDailyMap = calculatePricesForEachHourOfDay(dataElectrics);
            pricesDailyMap.forEach((hour, aDouble) -> {
                aggregatedPrices.add(new DataSeriesItem(String.valueOf(hour), aDouble));
            });
        } else if (periodRadioButtonGroup.getValue().equals("Month")) {
            Map<LocalDate, Double> pricesDailyMap = calculatePricesForEachDayOfMonth(dataElectrics);
            Month month = dateFrom.plusDays(3).getMonth();
            pricesDailyMap.forEach((localDate, aDouble) -> {
                if(localDate.getMonth() == month) {
                    chartTitle.setText(localDate.getMonth().name() + " of " + localDate.getYear());
                    aggregatedPrices.add(new DataSeriesItem(localDate.format(DateTimeFormatter.ofPattern("EEE, dd.MM.yyyy")), aDouble));
                }
            });

        } else {
            Map<Month, Double> pricesMonthMap = calculatePricesForEachMonthOfYear(dataElectrics);
            pricesMonthMap.forEach((month, aDouble) -> {
                aggregatedPrices.add(new DataSeriesItem(month.name(), aDouble));
            });
        }
        return aggregatedPrices;
    }

    /**
     * Method that returns a map with representation of calculated prices for each hour of the day,
     * based on given Electrics data. Hour is the key, prices are value.
     */
    private Map<Integer, Double> calculatePricesForEachHourOfDay(List<DataElectric> dataElectrics) {
        Map<Integer, Double> pricesMap = new TreeMap<>(Comparator.naturalOrder());
        for (DataElectric data : dataElectrics) {
            Integer hour = data.getTime().toLocalDateTime().getHour();
            Double price = data.getLowRate() * sensorElectric.getPricePerKwLow();
            price += data.getHighRate() * sensorElectric.getPricePerKwHigh();
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

    /**
     * Method that returns a map with representation of calculated prices for each day of a month,
     * based on given Electrics data. Day is the key, prices are value.
     */
    private Map<LocalDate, Double> calculatePricesForEachDayOfMonth(List<DataElectric> dataElectrics) {
        Map<LocalDate, Double> pricesMap = new TreeMap<>(Comparator.naturalOrder());
        for (DataElectric data : dataElectrics) {
            LocalDate time = data.getTime().toLocalDateTime().toLocalDate();
            Double price = data.getLowRate() * sensorElectric.getPricePerKwLow();
            price += data.getHighRate() * sensorElectric.getPricePerKwHigh();
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

    /**
     * Method that returns a map with representation of calculated prices for each month of a year,
     * based on given Electrics data. Month is the key, prices are value.
     */
    private Map<Month, Double> calculatePricesForEachMonthOfYear(List<DataElectric> dataElectrics) {
        Map<Month, Double> pricesMap = new TreeMap<>(Comparator.naturalOrder());
        for (DataElectric data : dataElectrics) {
            Month time = data.getTime().toLocalDateTime().toLocalDate().getMonth();
            Double price = data.getLowRate() * sensorElectric.getPricePerKwLow();
            price += data.getHighRate() * sensorElectric.getPricePerKwHigh();
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

    /**
     * Creates a main chart for consumption and prices, that are aggregated based on selected
     * time frame.
     */
    private void setMainChart() {
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

    private void colorLegendToGray(Configuration configuration, XAxis x) {
        Style gray = new Style();
        gray.setColor(new SolidColor("#666666"));
        x.getLabels().setStyle(gray);
        configuration.getLegend().setItemStyle(gray);
    }

    private double getNumberOfDecimalPrecision(Double number, int numberOfDecimalPlaces) {
        return BigDecimal.valueOf(number)
                .setScale(numberOfDecimalPlaces, RoundingMode.HALF_UP)
                .doubleValue();
    }

    private void updateMainChartData(LocalDate dateFrom, LocalDate dateTo, boolean consumption) {
        Configuration configuration = mainChart.getConfiguration();
        if (consumption) {
            Tooltip tooltip = configuration.getTooltip();
            tooltip.setValueSuffix(" [kW/h]");
            configuration.setTooltip(tooltip);
            configuration.getyAxis().setTitle("Consumption");

            if (periodRadioButtonGroup.getValue().equals("Day")) {
                List<DataElectric> dataElectrics = dataElectricService.findAllBySensorIdAndDate(sensor.getId(), dateFrom, dateTo);
                Map<Integer, Number> highRates = new TreeMap<>();
                Map<Integer, Number> lowRates = new TreeMap<>();
                getHighLowRatesDay(dataElectrics, highRates, lowRates);

                XAxis x = new XAxis();
                x.setCrosshair(new Crosshair());
                x.setTitle("Hour");
                x.setCategories(PatternStringUtils.hoursOfDay);
                configuration.removexAxes();
                configuration.addxAxis(x);
                setSeriesToConfiguration(configuration, lowRates.values(), highRates.values());
                colorLegendToGray(configuration, x);
            } else if (periodRadioButtonGroup.getValue().equals("Month")) {
                List<DataElectric> dataElectrics = dataElectricService.findAllBySensorIdAndDate(sensor.getId(), dateFrom, dateTo);
                Map<LocalDate, Number> highRates = new TreeMap<>();
                Map<LocalDate, Number> lowRates = new TreeMap<>();
                getHighLowRatesMonth(dataElectrics, highRates, lowRates);

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
                setSeriesToConfiguration(configuration, lowRates.values(), highRates.values());
                colorLegendToGray(configuration, x);
            } else {
                List<DataElectric> dataElectrics = dataElectricService.findAllBySensorIdAndDate(sensor.getId(), dateFrom, dateTo);
                Map<Month, Number> highRates = new TreeMap<>();
                Map<Month, Number> lowRates = new TreeMap<>();
                getHighLowRatesYear(dataElectrics, highRates, lowRates);

                XAxis x = new XAxis();
                x.setCrosshair(new Crosshair());
                x.setTitle("Month");
                x.setCategories(Arrays.stream(Month.values()).map(Enum::name).toArray(String[]::new));
                configuration.removexAxes();
                configuration.addxAxis(x);
                setSeriesToConfiguration(configuration, lowRates.values(), highRates.values());
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
                XAxis x = new XAxis();
                x.setCrosshair(new Crosshair());
                x.setTitle("Day");
                x.setCategories(dateFrom.datesUntil(dateTo).map(date ->
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
            ds.setData(getPricesForTime(dateFrom, dateTo));
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

    private void setSeriesToConfiguration(Configuration configuration, Collection<Number> values, Collection<Number> values2) {
        ListSeries lowRate = getColoredListSeries(new ArrayList<>(values), "Low rate", SolidColor.BLACK);
        ListSeries highRate = getColoredListSeries(new ArrayList<>(values2), "High rate", SolidColor.DARKGRAY);
        List<Number> sums = getSumListOfHighAndLowRates(new ArrayList<>(values),
                new ArrayList<>(values2));
        ListSeries sum = getColoredListSeries(sums, "Sum of both rates", new SolidColor(Colors.CEZ_TYPE_ORANGE.getRgb()));
        configuration.setSeries(lowRate, highRate, sum);
    }

    private ListSeries getColoredListSeries(List<Number> rates, String s, SolidColor black) {
        ListSeries series = new ListSeries(s, rates);
        PlotOptionsColumn options = new PlotOptionsColumn();
        options.setColor(black);
        series.setPlotOptions(options);
        return series;
    }

    private List<Number> getSumListOfHighAndLowRates(List<Number> highRates, List<Number> lowRates) {
        List<Number> sumValues = new ArrayList<>();
        for (int i = 0; i < lowRates.size(); i++) {
            sumValues.add(lowRates.get(i).doubleValue() + highRates.get(i).doubleValue());
        }
        return sumValues;
    }

    private void getHighLowRatesDay(List<DataElectric> dataElectrics, Map<Integer, Number> highRates, Map<Integer, Number> lowRates) {
        for (int i = 0; i < 24; i++) {
            highRates.put(i, 0.0);
            lowRates.put(i, 0.0);
        }
        for (DataElectric data : dataElectrics) {
            Integer hour = data.getTime().toLocalDateTime().getHour();
            highRates.replace(hour, highRates.get(hour).doubleValue() + data.getHighRate());
            lowRates.replace(hour, lowRates.get(hour).doubleValue() + data.getLowRate());
        }
    }

    private void getHighLowRatesYear(List<DataElectric> dataElectrics, Map<Month, Number> highRates, Map<Month, Number> lowRates) {
        for (Month month : Month.values()) {
            highRates.put(month, 0.0);
            lowRates.put(month, 0.0);
        }

        for (DataElectric data : dataElectrics) {
            Month month = data.getTime().toLocalDateTime().getMonth();
            highRates.replace(month, highRates.get(month).doubleValue() + data.getHighRate());
            lowRates.replace(month, lowRates.get(month).doubleValue() + data.getLowRate());
        }
    }

    private void getHighLowRatesMonth(List<DataElectric> dataElectrics, Map<LocalDate, Number> highRates, Map<LocalDate, Number> lowRates) {
        System.out.println(dateForChart);
        for (LocalDate date : dateForChart.withDayOfMonth(1)
                .datesUntil(dateForChart.withDayOfMonth(1).plusMonths(1)).collect(Collectors.toList())) {
            highRates.put(date, 0.0);
            lowRates.put(date, 0.0);
        }

        for (DataElectric data : dataElectrics) {
            LocalDate day = data.getTime().toLocalDateTime().toLocalDate();
            highRates.replace(day, highRates.get(day) == null ? 0.0 : highRates.get(day).doubleValue() + data.getHighRate());
            lowRates.replace(day, lowRates.get(day) == null ? 0.0 : lowRates.get(day).doubleValue() + data.getLowRate());
        }
    }

    /**
     * Method that sets hardware data to top right panel and its components.
     */
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

        //signalPowerHwField.setText("Signal power: ");
        SensorsUtil.setSignalImage(signalStrength, signalPowerHwField, signalImage);
        signalImage.setClassName("statusImage");
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

    private void setConsumptionBar() {
        calculateConsumptionHighestValue();
        if (consumptionMaxValue == 0.0) {
            consumptionText = new StyledTextComponent("Current consumption: <b>" + sensor.getConsumptionActual() + " / " + 0.0 + "</b> [W/h]");
        } else {
            consumptionText = new StyledTextComponent("Current consumption: <b>" + sensor.getConsumptionActual() + " / " + consumptionMaxValue * 1000 + "</b> [W/h]");
        }
        consumptionDivText.setText("");
        consumptionDivText.addComponentAtIndex(0, consumptionText);
        consumptionProgressBar.setMin(0);
        consumptionProgressBar.setMax(consumptionMaxValue * 1000);
        setConsumptionProgressBar(sensor);
        consumptionProgressBar.setHeight("15px");
        consumptionProgressBar.setVisible(true);
    }

    private void calculateConsumptionHighestValue() {
        List<DataElectric> dataElectrics = dataElectricService.findAllBySensorIdAndDate(sensor.getId(), LocalDate.now().minusDays(30), LocalDate.now());
        dataElectrics.forEach(dataElectric -> {
            if (dataElectric.getLowRate() > consumptionMaxValue) {
                consumptionMaxValue = dataElectric.getLowRate();
            } else if (dataElectric.getHighRate() > consumptionMaxValue) {
                consumptionMaxValue = dataElectric.getHighRate();
            }
        });
        consumptionMaxValue *= 1.3;
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

    /**
     * Periodically refresh data for values that can change. Specifically: Current Consumption of a sensor
     * and hardware status and signal strength.
     */
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

    private void setConsumptionProgressBar(Sensor sens) {
        if (sens.getConsumptionActual() >= consumptionMaxValue*1000) {
            consumptionProgressBar.setValue(consumptionMaxValue*1000);
        } else {
            consumptionProgressBar.setValue(sens.getConsumptionActual());
        }
    }

    private void refreshConsumptionText(Sensor sensor) {
        if (consumptionMaxValue == 0.0) {
            consumptionText.setText("Current consumption: <b>" + sensor.getConsumptionActual() + " / " + 0.0 + "</b> [W/h]");
        } else {
            consumptionText.setText("Current consumption: <b>" + sensor.getConsumptionActual() + " / " + consumptionMaxValue * 1000 + "</b> [W/h]");
        }
    }
}
