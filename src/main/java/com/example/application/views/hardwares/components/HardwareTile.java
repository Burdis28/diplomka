package com.example.application.views.hardwares.components;

import com.example.application.data.entity.Hardware;
import com.example.application.data.entity.HardwareLive;
import com.example.application.data.entity.Sensor;
import com.example.application.data.service.HardwareLiveService;
import com.example.application.data.service.HardwareService;
import com.example.application.utils.Colors;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.page.Page;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.component.html.H2;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

//@EnableScheduling
public class HardwareTile extends VerticalLayout {

    private final HorizontalLayout topVerticalLayout = new HorizontalLayout();
    private final HorizontalLayout bottomVerticalLayout = new HorizontalLayout();

    private final H3 idTitle = new H3();
    private final TextField serialHwField = new TextField();
    private final TextField nameField = new TextField();
    private final TextField ownerField = new TextField();
    private Icon signalIcon;

    private final DateTimePicker dateTimePicker = new DateTimePicker();
    private final TextField versionField = new TextField();
    private final Select<Sensor> sensorsList = new Select<>();

    private final Button editButton = new Button("Edit");
    private final Button deleteButton = new Button("Delete");
    private final HardwareService hardwareService;
    private final HardwareLiveService hardwareLiveService;
    private HardwareLive hardwareLive;

    public HardwareTile(Hardware hardware, HardwareLive hardwareLive, List<Sensor> attachedSensors, String ownerName,
                        HardwareService hardwareService, HardwareLiveService hardwareLiveService) {
        this.hardwareService = hardwareService;
        this.hardwareLiveService = hardwareLiveService;
        this.hardwareLive = hardwareLive;

        idTitle.setText("Hardware [ " + hardware.getId_HW() + " ]");
        idTitle.setId("idTitle");

        serialHwField.setLabel("Serial HW code");
        serialHwField.setValue(hardware.getSerial_HW());
        serialHwField.setReadOnly(true);

        nameField.setLabel("HW name");
        nameField.setValue(hardware.getName());
        nameField.setReadOnly(true);

        ownerField.setLabel("Owner");
        ownerField.setValue(ownerName);
        ownerField.setReadOnly(true);

        dateTimePicker.setLabel("Time of signal check");
        dateTimePicker.setValue(hardwareLive.getDateTime().toLocalDateTime());
        dateTimePicker.setReadOnly(true);

        versionField.setReadOnly(true);
        versionField.setLabel("Version");
        versionField.setValue(hardwareLive.getVersion() == null ? "" : hardwareLive.getVersion());

        sensorsList.setLabel("List of attached sensors");
        sensorsList.setItems(attachedSensors);
        sensorsList.setTextRenderer(item -> "[" + item.getIdHw() + "] " + item.getName());
        sensorsList.setPlaceholder("Sensors ...");
        sensorsList.setValue(null);
        sensorsList.addValueChangeListener(event -> navigateToSensorDetail(event.getValue()));
        sensorsList.addAttachListener(event -> {
        });

        editButton.setClassName("button");
        editButton.setThemeName("primary");
        editButton.addClickListener(event -> {
            createHardwareEditDialog(hardware);
        });

        deleteButton.setClassName("button");
        deleteButton.setThemeName("secondary");
        deleteButton.addClickListener(event -> {
            createHardwareDeleteDialog(attachedSensors.isEmpty(), hardware);
        });

        topVerticalLayout.setAlignItems(Alignment.START);
        topVerticalLayout.add(idTitle);
        topVerticalLayout.add(serialHwField);
        topVerticalLayout.add(nameField);
        topVerticalLayout.add(ownerField);
        topVerticalLayout.setId("topVerticalLayout");
        topVerticalLayout.add(editButton);
        topVerticalLayout.add(deleteButton);

        bottomVerticalLayout.setId("bottomVerticalLayout");
        bottomVerticalLayout.add(getSignalComponent(hardwareLive.getSignal_strength()));
        bottomVerticalLayout.add(dateTimePicker);
        bottomVerticalLayout.add(versionField);
        bottomVerticalLayout.add(sensorsList);

        add(topVerticalLayout);
        add(bottomVerticalLayout);
    }

    private void createHardwareDeleteDialog(boolean empty, Hardware hardware) {
        ConfirmDialog confirmDialog = new ConfirmDialog();
        confirmDialog.setConfirmButtonTheme("error primary");
        confirmDialog.setCancelText("Cancel");
        confirmDialog.setText("Are you sure you want to delete this hardware?");
        confirmDialog.setCancelable(true);
        confirmDialog.setCancelButtonTheme("primary");
        H2 title = new H2("Delete " + hardware.getName());

        confirmDialog.addConfirmListener(event -> {
            if (empty) {
                try {
                    this.hardwareService.delete(hardware.getId_HW(), hardware.getSerial_HW());
                    //UI.getCurrent().navigate("hardwares");
                    UI.getCurrent().getPage().reload();
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    openCannotDeleteDialog(confirmDialog);
                }
            } else {
                openCannotDeleteDialog(confirmDialog);
            }
        });
        confirmDialog.addComponentAsFirst(title);
        confirmDialog.open();
    }

    private void openCannotDeleteDialog(ConfirmDialog confirmDialog) {
        confirmDialog.close();
        Dialog dialog = new Dialog();
        dialog.add("You can't delete this hardware.");
        dialog.setModal(true);
        dialog.open();
    }

    private void createHardwareEditDialog(Hardware hardware) {
        ConfirmDialog confirmDialog = new ConfirmDialog();
        H2 title = new H2("Edit " + hardware.getName());
        confirmDialog.setCancelable(true);
        TextField name = new TextField();
        name.setLabel("HW name");
        name.setValue(hardware.getName());

        confirmDialog.addConfirmListener(event -> {
            hardware.setName(name.getValue());
            this.hardwareService.update(hardware);
            getUI().ifPresent(ui -> ui.access(() -> {
                nameField.setValue(hardware.getName());
                ui.push();
            }));
        });
        confirmDialog.add(title);
        confirmDialog.add(name);
        confirmDialog.open();
    }

    private Component getSignalComponent(int signal) {
        VerticalLayout div = new VerticalLayout();
        div.setClassName("signalDiv");

        signalIcon = VaadinIcon.SIGNAL.create();
        signalIcon.setClassName("signalIcon");
        signalIcon.setSize("50px");
        if (signal == 0) {
            div.add(new Text("No signal !"));

        } else {
            div.add(new Text("Signal " + signal + "%"));
        }

        div.add(new Hr());
        colorIcon(signalIcon, signal);
        div.add(signalIcon);
        return div;
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

    private void navigateToSensorDetail(Sensor sensor) {
        VaadinSession.getCurrent().setAttribute("sensorId", sensor.getId());
        switch (sensor.getType()) {
            case "e":
                UI.getCurrent().navigate("sensor-el-detail");
                return;
            case "w":
                UI.getCurrent().navigate("sensor-wat-detail");
                return;
            case "g":
                UI.getCurrent().navigate("sensor-gas-detail");
                return;
            default:
        }
    }

//    @Scheduled(fixedDelay=20000)
//    public void refreshSensorConsumption() {
//        HardwareLive refreshedHwLive = hardwareLiveService.findByHardwareId(hardwareLive.getHwId());
//        getUI().ifPresent(ui -> ui.access(() -> {
//            colorIcon(signalIcon, refreshedHwLive.getSignal_strength());
//            ui.push();
//        }));
//    }
}
