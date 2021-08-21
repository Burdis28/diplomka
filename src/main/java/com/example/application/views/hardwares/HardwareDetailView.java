package com.example.application.views.hardwares;

import com.example.application.data.entity.*;
import com.example.application.data.service.*;
import com.example.application.utils.Colors;
import com.example.application.views.sensors.components.SensorsUtil;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout.FormItem;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.littemplate.LitTemplate;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.template.Id;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.server.VaadinSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.ArrayList;
import java.util.List;

/**
 * A Designer generated component for the hardware-detail template.
 *
 * Designer will add and remove fields with @Id mappings but
 * does not overwrite or otherwise change this file.
 */
@Tag("hardware-detail")
@JsModule("./views/hardwares/hardware-detail.ts")
@CssImport("./views/hardwares/hardware-detail.css")
@EnableScheduling
public class HardwareDetailView extends LitTemplate {

    @Id("nameField")
    private TextField nameField;
    @Id("serialCodeField")
    private TextField serialCodeField;
    @Id("versionField")
    private TextField versionField;
    @Id("attachedSensorsSelect")
    private Select<Sensor> attachedSensorsSelect;
    @Id("vaadinFormItem")
    private FormItem vaadinFormItem;
    @Id("editButton")
    private Button editButton;
    @Id("saveButton")
    private Button saveButton;
    @Id("cancelButton")
    private Button cancelButton;
    @Id("ownersSelect")
    private Select<User> ownersSelect;
    @Id("signalImageDiv")
    private Span signalImageDiv;

    private UserService userService;
    private HardwareService hardwareService;
    private HardwareLiveService hardwareLiveService;
    private NotificationLogHwService notificationLogHwService;
    private SensorService sensorService;
    private Hardware hardware;
    Binder<Hardware> hardwareBinder = new Binder<>();
    @Id("onlineStatusDiv")
    private Span onlineStatusDiv;

    private Image signalImage;
    private Icon onlineIcon;
    private List<Sensor> attachedSensors;
    private List<User> owners;
    @Id("ownersFormItem")
    private FormItem ownersFormItem;
    @Id("attachedSensorsFormItem")
    private FormItem attachedSensorsFormItem;

    /**
     * Creates a new HardwareDetail.
     */
    public HardwareDetailView(@Autowired UserService userService, HardwareService hardwareService,
                              HardwareLiveService hardwareLiveService,
                              NotificationLogHwService notificationLogHwService, SensorService sensorService) {
        this.hardwareService = hardwareService;
        this.userService = userService;
        this.hardwareLiveService = hardwareLiveService;
        this.notificationLogHwService = notificationLogHwService;
        this.sensorService = sensorService;

        String hardwareId = VaadinSession.getCurrent().getAttribute("hardwareSerialCode").toString();
        hardware = hardwareService.getBySerialHW(hardwareId).get();

        cancelButton.setIcon(new Icon(VaadinIcon.CLOSE_CIRCLE_O));
        saveButton.setIcon(new Icon(VaadinIcon.CHECK_CIRCLE));

        setHardwareFields(hardware);

        setButton(saveButton,false);
        setButton(cancelButton, false);

        if (!VaadinSession.getCurrent().getAttribute(User.class).getAdmin()) {
            attachedSensorsFormItem.setVisible(false);
            ownersFormItem.setVisible(false);
        }

        editButton.addClickListener(buttonClickEvent -> {
            setReadOnlyFields(false);

            setButton(saveButton,true);
            setButton(editButton,false);
            setButton(cancelButton, true);
        });

        saveButton.addClickListener(buttonClickEvent -> {
            try {
                hardwareBinder.writeBean(hardware);
                hardwareService.update(hardware);

                setButton(saveButton, false);
                setButton(cancelButton, false);
                setButton(editButton, true);

                setReadOnlyFields(true);
            } catch (Exception e) {
                Dialog dialog = new Dialog();
                dialog.setModal(true);
                dialog.add("Saving has failed. Try again.");
                dialog.open();
                //do nothin
            }
        });

        cancelButton.addClickListener(buttonClickEvent -> {
            // do nothing, maybe reload forms
            setButton(saveButton,false);
            setButton(editButton,true);
            setButton(cancelButton, false);

            setReadOnlyFields(true);

            hardwareBinder.readBean(hardware);
        });
    }

    private void setHardwareFields(Hardware hardware) {
        hardwareBinder.forField(nameField).asRequired("Required field.").bind(Hardware::getName, Hardware::setName);
        hardwareBinder.forField(serialCodeField).asRequired("Required field.").bind(Hardware::getSerial_HW, Hardware::setSerial_HW);

        setSignalImage();
        setOnlineStatusIcon();

        attachedSensors = new ArrayList<>(sensorService.findSensorByIdHw(hardware.getSerial_HW()));
        if (!attachedSensors.isEmpty()) {
            attachedSensorsSelect.setItems(attachedSensors);
            attachedSensorsSelect.setValue(attachedSensors.get(0));
            attachedSensorsSelect.setTextRenderer(item -> item.getName() + " | " + SensorTypes.valueOf(item.getType()) + " | serial: " + item.getIdHw() + "");
            attachedSensorsSelect.addAttachListener(event -> {
            });
            attachedSensorsSelect.addValueChangeListener(event -> SensorsUtil.navigateToSensorDetail(event.getValue()));
        }

        owners = new ArrayList<>(userService.getHardwareOwner(hardware.getSerial_HW()));
        if (!owners.isEmpty()) {
            ownersSelect.setItems(owners);
            ownersSelect.setValue(owners.get(0));
            ownersSelect.setTextRenderer(User::getFullName);
            ownersSelect.addAttachListener(event -> {
            });
//            ownersSelect.addValueChangeListener(event -> {
//                ownersSelect.setValue(event.getOldValue());
//            });
            //ownersSelect.setReadOnly(true);
        }

        hardwareBinder.readBean(hardware);
    }

    private void setSignalImage() {
        HardwareLive live = hardwareLiveService.findByHardwareId(hardware.getSerial_HW());
        Span span = new Span();
        signalImageDiv.addClassName("signalImage");
        signalImage = new Image();
        signalImage.setHeight("30px");
        signalImage.setWidth("30px");
        if (live != null) {
            versionField.setValue(live.getVersion() == null ? " " : live.getVersion());
        } else {
            versionField.setValue(" ");
        }
        SensorsUtil.updateSignalImage(live, signalImage);
        span.add(signalImage);
        signalImageDiv.add(span);
    }

    private void setOnlineStatusIcon() {
        NotificationLogHw logHw = notificationLogHwService.findBySerialHw(hardware.getSerial_HW());
        Span span = new Span();
        if (logHw == null) {
            onlineIcon = VaadinIcon.CIRCLE.create();
            onlineIcon.setColor(Colors.GREEN.getRgb());
            onlineIcon.setClassName("bottomMarginIcon");
            span.add(onlineIcon);
        } else {
            onlineIcon = VaadinIcon.CIRCLE.create();
            onlineIcon.setColor(Colors.RED.getRgb());
            onlineIcon.setClassName("bottomMarginIcon");
            span.add(onlineIcon);
        }
        onlineStatusDiv.add(span);
    }

    private void setButton(Button button, boolean b) {
        button.setEnabled(b);
        button.setVisible(b);
    }

    private void setReadOnlyFields(boolean b) {
        nameField.setReadOnly(b);
        //versionField.setReadOnly(b);
    }

    @Scheduled(fixedDelay = 10000)
    private void refreshStatusAndSignal() {
        getUI().ifPresent(ui -> ui.access(() -> {
            HardwareLive live = hardwareLiveService.findByHardwareId(hardware.getSerial_HW());
            if (live != null) {
                SensorsUtil.updateSignalImage(live, signalImage);
            }

            NotificationLogHw logHw = notificationLogHwService.findBySerialHw(hardware.getSerial_HW());
            if (logHw == null) {
                onlineIcon.setColor(Colors.GREEN.getRgb());
            } else {
                onlineIcon.setColor(Colors.RED.getRgb());
            }
        }));
    }
}
