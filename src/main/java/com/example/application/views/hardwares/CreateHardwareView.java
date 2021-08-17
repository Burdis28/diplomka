package com.example.application.views.hardwares;

import com.example.application.components.notifications.ErrorNotification;
import com.example.application.data.entity.*;
import com.example.application.data.service.HardwareLiveService;
import com.example.application.data.service.HardwareService;
import com.example.application.data.service.NotificationLogHwService;
import com.example.application.views.main.MainLayout;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.button.Button;
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
    @Id("serialHwCodeField")
    private TextField serialHwCodeField;
    @Id("cancelButton")
    private Button cancelButton;
    @Id("createHwButton")
    private Button createHwButton;

    private HardwareService hardwareService;
    private HardwareLiveService hardwareLiveService;
    private NotificationLogHwService notificationLogHwService;

    private Binder<Hardware> hardwareBinder;

    private Hardware hardware;
    private HardwareLive hardwareLive;
    private User loggedUser;

    /**
     * Creates a new CreateHardwareView.
     */
    public CreateHardwareView(HardwareService hardwareService,
                              HardwareLiveService hardwareLiveService,
                              NotificationLogHwService notificationLogHwService) {
        this.hardwareService = hardwareService;
        this.hardwareLiveService = hardwareLiveService;
        this.notificationLogHwService = notificationLogHwService;
        this.hardwareBinder = new Binder<>();
        this.hardware = new Hardware();
        this.hardwareLive = new HardwareLive();
        loggedUser = VaadinSession.getCurrent().getAttribute(User.class);

        setBinderFields();

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
                error.setErrorText("Hardware creation has failed. Please try again. Make sure Serial Code is unique and doesn't exist already." + ex.getMessage());
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

        NotificationLogHw notificationLogHw = new NotificationLogHw();
        notificationLogHw.setSerialHw(hardware.getSerial_HW());
        notificationLogHw.setLastSent(timeNow);
        notificationLogHw.setNotificationType("");
        notificationLogHwService.update(notificationLogHw);
    }

    private void clearForm() {
        nameField.clear();
        nameField.setInvalid(false);
        serialHwCodeField.clear();
        serialHwCodeField.setInvalid(false);
    }

    private void setBinderFields() {
        hardwareBinder.forField(nameField).asRequired("Required field.")
                .bind(Hardware::getName, Hardware::setName);

        hardwareBinder.forField(serialHwCodeField).asRequired("Required field.")
                .bind(Hardware::getSerial_HW, Hardware::setSerial_HW);

        hardwareBinder.readBean(hardware);
    }
}
