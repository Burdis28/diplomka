package com.example.application.views.hardwares;

import com.example.application.components.notifications.ErrorNotification;
import com.example.application.data.entity.*;
import com.example.application.data.service.HardwareLiveService;
import com.example.application.data.service.HardwareService;
import com.example.application.data.service.NotificationLogHwService;
import com.example.application.data.service.SmartPlugService;
import com.example.application.views.main.MainLayout;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.littemplate.LitTemplate;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.template.Id;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.ParentLayout;
import com.vaadin.flow.server.VaadinSession;

import java.sql.Timestamp;
import java.time.LocalDateTime;

/**
 * A Designer generated component for the create-hardware-view template.
 * <p>
 * Designer will add and remove fields with @Id mappings but
 * does not overwrite or otherwise change this file.
 */
@Tag("create-hardware-view")
@JsModule("./views/hardwares/create-hardware-view.ts")
@CssImport("./views/hardwares/create-hardware-view.css")
@ParentLayout(MainLayout.class)
@PageTitle("Create hardware")
public class CreateHardwareView extends LitTemplate {

    @Id("nameField")
    private TextField nameField;
    @Id("smartPlugSerialCodeField")
    private TextField smartPlugSerialCodeField;
    @Id("serialHwCodeField")
    private TextField serialHwCodeField;
    @Id("cancelButton")
    private Button cancelButton;
    @Id("createHwButton")
    private Button createHwButton;
    @Id("smartPlugNameField")
    private TextField smartPlugNameField;
    @Id("activationCodeField")
    private TextField activationCodeField;
    @Id("dateTimePickerField")
    private DateTimePicker dateTimePickerField;

    private HardwareService hardwareService;
    private HardwareLiveService hardwareLiveService;
    private SmartPlugService smartPlugService;
    private NotificationLogHwService notificationLogHwService;

    private Binder<Hardware> hardwareBinder;
    private Binder<SmartPlug> smartPlugBinder;

    private Hardware hardware;
    private HardwareLive hardwareLive;
    private SmartPlug smartPlug;
    private User loggedUser;

    /**
     * Creates a new CreateHardwareView.
     */
    public CreateHardwareView(HardwareService hardwareService,
                              HardwareLiveService hardwareLiveService,
                              NotificationLogHwService notificationLogHwService,
                              SmartPlugService smartPlugService) {
        this.hardwareService = hardwareService;
        this.hardwareLiveService = hardwareLiveService;
        this.smartPlugService = smartPlugService;
        this.notificationLogHwService = notificationLogHwService;
        this.hardwareBinder = new Binder<>();
        this.smartPlugBinder = new Binder<>();
        this.hardware = new Hardware();
        this.hardwareLive = new HardwareLive();
        this.smartPlug = new SmartPlug();
        loggedUser = VaadinSession.getCurrent().getAttribute(User.class);

        setBinderFields();

        Icon icon = new Icon(VaadinIcon.THUMBS_UP);
        createHwButton.setIcon(icon);
        Icon iconCancel = new Icon(VaadinIcon.CLOSE);
        createHwButton.setIcon(iconCancel);

        cancelButton.addClickListener(event -> clearForm());

        createHwButton.addClickListener(event -> {
            try {
                createHardwareRelatedObjects();
                clearForm();
                Notification notification = new Notification();
                notification.setPosition(Notification.Position.MIDDLE);
                notification.setText("Hardware created successfully.");
            } catch (Exception ex) {
                ErrorNotification error = new ErrorNotification();
                error.setErrorText("Hardware creation has failed. Please try again. " + ex.getMessage());
                ex.printStackTrace();
                error.open();
            }
        });
    }

    private void createHardwareRelatedObjects() throws Exception {
        hardwareBinder.writeBean(hardware);
        hardwareService.createHw(hardware, loggedUser.getId());

        Timestamp timeNow = Timestamp.valueOf(LocalDateTime.now());

        HardwareLive hwLive = new HardwareLive();
        hwLive.setHwId(hardware.getSerial_HW());
        hwLive.setDateTime(timeNow);
        hardwareLiveService.update(hwLive);

        smartPlugBinder.writeBean(smartPlug);
        smartPlug.setSerialHw(hardware.getSerial_HW());
        smartPlug.setActiveTill(Timestamp.valueOf(LocalDateTime.now()));
        smartPlugService.update(smartPlug);

        NotificationLogHw notificationLogHw = new NotificationLogHw();
        notificationLogHw.setSerialHw(hardware.getSerial_HW());
        notificationLogHw.setLastSent(timeNow);
        notificationLogHw.setNotificationType("");
        notificationLogHwService.update(notificationLogHw);
    }

    private void clearForm() {
        nameField.clear();
        serialHwCodeField.clear();
        smartPlugSerialCodeField.clear();
        smartPlugNameField.clear();
        activationCodeField.clear();
        dateTimePickerField.clear();
    }

    private void setBinderFields() {
        hardwareBinder.forField(nameField).asRequired("Required field.")
                .bind(Hardware::getName, Hardware::setName);

        hardwareBinder.forField(serialHwCodeField).asRequired("Required field.")
                .bind(Hardware::getSerial_HW, Hardware::setSerial_HW);

        smartPlugBinder.forField(smartPlugSerialCodeField).asRequired("Required field.")
                .bind(SmartPlug::getSerialPlug, SmartPlug::setSerialPlug);

        smartPlugBinder.forField(smartPlugNameField).asRequired("Required field.")
                .bind(SmartPlug::getName, SmartPlug::setName);

        smartPlugBinder.forField(activationCodeField).asRequired("Required field.")
                .bind(SmartPlug::getActivationCode, SmartPlug::setActivationCode);

//        smartPlugBinder.forField(dateTimePickerField).asRequired("Required field.")
//                .bind(smartPlug1 -> smartPlug1.getActiveTill() == null ? null :
//                                smartPlug1.getActiveTill().toLocalDateTime(),
//                        (smartPlug1, localDateTime) -> {
//                            if (localDateTime != null) {
//                                smartPlug1.setActiveTill(Timestamp.valueOf(localDateTime));
//                            }
//                        });
        hardwareBinder.readBean(hardware);
        smartPlugBinder.readBean(smartPlug);
    }
}
