package com.example.application.views.sensors;

import com.example.application.data.entity.*;
import com.example.application.data.service.*;
import com.example.application.utils.PatternStringUtils;
import com.example.application.views.main.MainView;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.littemplate.LitTemplate;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.template.Id;
import com.vaadin.flow.component.textfield.BigDecimalField;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.timepicker.TimePicker;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.validator.EmailValidator;
import com.vaadin.flow.dom.Element;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.ParentLayout;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * A Designer generated component for the person-form-view template.
 * <p>
 * Designer will add and remove fields with @Id mappings but does not overwrite
 * or otherwise change this file.
 */
@JsModule(value="./views/sensors/createsensor-view.ts")
@CssImport(value="./views/sensors/createsensor-view.css")
@Tag(value="createsensor-view")
@ParentLayout(value=MainView.class)
@PageTitle(value="Create sensor")
public class CreatesensorView extends LitTemplate {

    private final SensorService sensorService;
    private final SensorElectricService sensorElectricService;
    private final SensorWaterService sensorWaterService;
    private final StateValveService stateValveService;
    private final HardwareService hardwareService;

    @Id("nameField")
    private TextField nameField;
    @Id("div")
    private Div div;
    @Id("basicSensorAttributesForm")
    private FormLayout basicSensorAttributesForm;
    @Id("currencyField")
    private TextField currencyField;
    @Id("typeSelect")
    private Select<String> typeSelect;
    @Id("limitMonthField")
    private TextField limitMonthField;
    @Id("limitDayField")
    private TextField limitDayField;
    @Id("pinIdField")
    private TextField pinIdField;
    @Id("attachToHardware")
    private Select<String> attachToHardwareSelect;

    @Id("save")
    private Button save;
    @Id("cancel")
    private Button cancel;

    //Electric components
    @Id("electricLayout")
    private FormLayout electricLayout;
    @Id("highRateCheckBox")
    private Checkbox highRateCheckBox;
    @Id("pricePerKwHighField")
    private TextField pricePerKwHighField;

    //Water components
    @Id("waterLayout")
    private FormLayout waterLayout;
    @Id("pricePerM3Field")
    private TextField pricePerM3Field;
    @Id("waterStateSelect")
    private Select<StateValve> waterStateSelect;
    @Id("implPerLitField")
    private NumberField implPerLitField;
    @Id("pricePerKwLowField")
    private TextField pricePerKwLowField;
    @Id("fixedPriceField")
    private TextField fixedPriceField;
    @Id("servicePriceField")
    private TextField servicePriceField;
    @Id("implPerKwField")
    private TextField implPerKwField;
    @Id("overallLayout")
    private Element overallLayout;
    @Id("nightEndField")
    private TextField nightEndField;
    @Id("nightStartField")
    private TextField nightStartField;

    public CreatesensorView(SensorService sensorService, HardwareService hardwareService,
                            SensorElectricService sensorElectricService, SensorWaterService sensorWaterService,
                            StateValveService stateValveService) {
        this.sensorService = sensorService;
        this.hardwareService = hardwareService;
        this.sensorElectricService = sensorElectricService;
        this.sensorWaterService = sensorWaterService;
        this.stateValveService = stateValveService;
        electricLayout.setVisible(false);
        waterLayout.setVisible(false);

        setFormFieldsPatterns();
        setSelectFields();
        setRequiredFields();
        clearForm();

        typeSelect.addValueChangeListener(event -> {
            electricLayout.setVisible(typeSelect.getValue().equals(SensorTypes.e.toString()));
            waterLayout.setVisible(typeSelect.getValue().equals(SensorTypes.w.toString()));
            //gasLayout.setVisible(typeSelect.getValue().equals(SensorTypes.g.toString()));
            div.setHeight("1500px");
        });

        cancel.addClickListener(e -> clearForm());
        save.addClickListener(e -> {
            if (validateFields()) {
                try {
                    Sensor sensor = setNewSensorFromFields();
                    Sensor createdSensor = sensorService.update(sensor);
                    if (typeSelect.getValue().equals(SensorTypes.e.toString())) {
                        SensorElectric electric = setNewElectricSensorFromFields(createdSensor);
                        sensorElectricService.update(electric);
                    } else if (typeSelect.getValue().equals(SensorTypes.w.toString())) {
                        SensorWater water = setNewWaterSensorFromFields(createdSensor);
                        sensorWaterService.update(water);
                    } else {
                        // Gas sensor is not supported yet
                    }
                    Dialog dialog = new Dialog();
                    dialog.add("Sensor successfully created.");
                    dialog.open();
                    clearForm();
                } catch (Exception exception) {
                    createErrorDialog("Error: Sensor creation has failed, try again.");
                }
            }
        });
    }

    private SensorWater setNewWaterSensorFromFields(Sensor createdSensor) {
        SensorWater water = new SensorWater();
        water.setSensor_id(createdSensor.getId());
        water.setPrice_per_m3(Double.parseDouble(pricePerM3Field.getValue()));
        water.setState(waterStateSelect.getValue().getId());
        water.setImplPerLit(implPerLitField.getValue().intValue());

        setNightTimeFields(water);
        return water;
    }

    private void setNightTimeFields(SensorWater water) {
        String hoursStart = nightStartField.getValue();
        int indexOfDecimal = hoursStart.indexOf(":");
        String hour = hoursStart.substring(0, indexOfDecimal);
        String minute = hoursStart.substring(indexOfDecimal);
        minute = minute.substring(1);

        String hoursEnd = nightEndField.getValue();
        int indexOfDecimal2 = hoursEnd.indexOf(":");
        String hour2 = hoursEnd.substring(0, indexOfDecimal2);
        String minute2 = hoursEnd.substring(indexOfDecimal2);
        minute2 = minute2.substring(1);

        water.setNightStartHour(Integer.parseInt(hour));
        water.setNightStartMinute(Integer.parseInt(minute));
        water.setNightEndHour(Integer.parseInt(hour2));
        water.setNightEndMinute(Integer.parseInt(minute2));
    }

    private SensorElectric setNewElectricSensorFromFields(Sensor createdSensor) {
        SensorElectric electric = new SensorElectric();
        electric.setSensor_id(createdSensor.getId());
        electric.setHighRate(highRateCheckBox.getValue());
        electric.setImplPerKw(Integer.parseInt(implPerKwField.getValue()));
        electric.setPriceService(Double.parseDouble(servicePriceField.getValue()));
        electric.setPriceFix(Double.parseDouble(fixedPriceField.getValue()));
        electric.setPricePerKwLow(Double.parseDouble(pricePerKwLowField.getValue()));
        electric.setPricePerKwHigh(Double.parseDouble(pricePerKwHighField.getValue()));
        return electric;
    }

    private Sensor setNewSensorFromFields() {
        Sensor sensor = new Sensor();


        sensor.setName(nameField.getValue());
        sensor.setCurrencyString(currencyField.getValue());
        sensor.setIdHw(attachToHardwareSelect.getValue());
        sensor.setType(SensorTypes.fromCode(typeSelect.getValue()).name());
        sensor.setPinId(Integer.parseInt(pinIdField.getValue()));
        sensor.setLimit_day(Double.parseDouble(limitDayField.getValue()));
        sensor.setLimit_month(Double.parseDouble(limitMonthField.getValue()));
        sensor.setConsumptionActual(BigDecimal.ZERO.doubleValue());
        sensor.setConsumptionCorrelation(BigDecimal.ZERO.doubleValue());
        sensor.setCreatedDate(Timestamp.valueOf(LocalDateTime.now()));
        return sensor;
    }

    private boolean validateFields() {
        boolean valid = true;
        try {
            valid = validateRequiredFields();
            if (!valid) validateDialog("Validation error: Fill all required fields.");

            // logic validations of fields.
            valid = limitDayField.getValue().compareTo(limitMonthField.getValue()) <= 0;
            if (!valid) validateDialog("Validation error: Monthly limit must be bigger than daily limit.");
        } catch (Exception e) {
            valid = false;
        }
        return valid;
    }

    private boolean validateRequiredFields() {
        if (nameField.isEmpty() || nameField.isInvalid()) {
            return false;
        }
        if (limitDayField.isEmpty() || limitDayField.isInvalid()) {
            return false;
        }
        if (currencyField.isEmpty() || currencyField.isInvalid()) {
            return false;
        }
        if (limitMonthField.isEmpty() || limitMonthField.isInvalid()) {
            return false;
        }
        if (typeSelect.getValue() == null || typeSelect.getValue().equals("")) {
            return false;
        }
        if (pinIdField.isEmpty() || pinIdField.isInvalid()) {
            return false;
        }
        if (attachToHardwareSelect.getValue() == null || attachToHardwareSelect.getValue().equals("")) {
            return false;
        }
        return true;
    }

    private void validateDialog(String errorMessage) throws Exception{
        createErrorDialog(errorMessage);
        throw new Exception();
    }

    private void createErrorDialog(String errorMessage) {
        Dialog popUpDialog = new Dialog();
        popUpDialog.setCloseOnEsc(true);
        popUpDialog.setModal(true);
        Div content = new Div();
        content.addClassName("validation-error-dialog-style");
        content.setText(errorMessage);
        popUpDialog.add(content);
        popUpDialog.open();
    }

    private void setFormFieldsPatterns() {
        nameField.setMaxLength(45);
        nameField.setErrorMessage(PatternStringUtils.fieldIsRequired);
        currencyField.setMaxLength(45);
        currencyField.setErrorMessage(PatternStringUtils.fieldIsRequired);
        pinIdField.setPattern(PatternStringUtils.onlyNumbersRegex);
        pinIdField.setErrorMessage(PatternStringUtils.fieldIsRequired);
        nameField.setErrorMessage(PatternStringUtils.fieldIsRequired);
        limitDayField.setPattern(PatternStringUtils.doubleNumberRegex63);
        limitDayField.setErrorMessage(PatternStringUtils.fieldIsRequired);
        limitMonthField.setPattern(PatternStringUtils.doubleNumberRegex103);
        limitMonthField.setErrorMessage(PatternStringUtils.fieldIsRequired);
        pricePerKwLowField.setPattern(PatternStringUtils.doubleNumberRegex62);
        pricePerKwLowField.setErrorMessage(PatternStringUtils.fieldIsRequired);
        pricePerKwHighField.setPattern(PatternStringUtils.doubleNumberRegex62);
        pricePerKwHighField.setErrorMessage(PatternStringUtils.fieldIsRequired);
        servicePriceField.setPattern(PatternStringUtils.doubleNumberRegex62);
        servicePriceField.setErrorMessage(PatternStringUtils.fieldIsRequired);
        fixedPriceField.setPattern(PatternStringUtils.doubleNumberRegex62);
        fixedPriceField.setErrorMessage(PatternStringUtils.fieldIsRequired);
        implPerKwField.setPattern(PatternStringUtils.onlyNumbersRegex);
        implPerKwField.setErrorMessage(PatternStringUtils.fieldIsRequired);

        pricePerM3Field.setPattern(PatternStringUtils.doubleNumberRegex62);
        pricePerM3Field.setErrorMessage(PatternStringUtils.fieldIsRequired);
        nightStartField.setPattern(PatternStringUtils.nightHoursRegex);
        nightStartField.setErrorMessage(PatternStringUtils.fieldIsRequired);
        nightEndField.setPattern(PatternStringUtils.nightHoursRegex);
        nightEndField.setErrorMessage(PatternStringUtils.fieldIsRequired);
    }

    private void setRequiredFields() {
        nameField.setRequired(true);
        currencyField.setRequired(true);
        pinIdField.setRequired(true);
        limitDayField.setRequired(true);
    }

    private void setSelectFields() {
        attachToHardwareSelect.setItems(hardwareService.listHardwareSerials());
        typeSelect.removeAll();
        typeSelect.setItems(Arrays.stream(SensorTypes.values()).map(SensorTypes::toString).collect(Collectors.toList()));

        waterStateSelect.removeAll();
        waterStateSelect.setItems(stateValveService.listAll());
        waterStateSelect.setItemLabelGenerator(StateValve::getState);
    }

    private void clearForm() {
        nameField.clear();
        currencyField.clear();
        pinIdField.clear();
        limitDayField.clear();
        limitMonthField.clear();

        implPerKwField.clear();
        pricePerKwHighField.clear();
        pricePerKwLowField.clear();
        pricePerM3Field.clear();
        fixedPriceField.clear();
        servicePriceField.clear();
        highRateCheckBox.clear();

        waterStateSelect.clear();
        implPerLitField.clear();
        nightStartField.clear();
        nightEndField.clear();
    }
}
