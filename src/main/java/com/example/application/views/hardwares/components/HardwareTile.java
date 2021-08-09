package com.example.application.views.hardwares.components;

import com.example.application.data.entity.Hardware;
import com.example.application.data.entity.HardwareLive;
import com.example.application.utils.Colors;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.details.Details;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.html.H3;


import java.time.LocalDateTime;
import java.util.List;

public class HardwareTile extends VerticalLayout {

    private final HorizontalLayout topVerticalLayout = new HorizontalLayout();
    private final HorizontalLayout bottomVerticalLayout = new HorizontalLayout();

    private final H3 idTitle = new H3();
    private final TextField serialHwField = new TextField();
    private final TextField nameField = new TextField();
    private final TextField ownerField = new TextField();

    private final DateTimePicker dateTimePicker = new DateTimePicker();
    private final TextField versionField = new TextField();
    private final Select<String> sensorsList = new Select();

    public HardwareTile(Hardware hardware, HardwareLive hardwareLive, List<String> attachedSensorsNames, String ownerName) {
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
        dateTimePicker.setValue(LocalDateTime.now());
        dateTimePicker.setReadOnly(true);

        versionField.setReadOnly(true);
        versionField.setLabel("Version");
        versionField.setValue(hardwareLive.getVersion());

        sensorsList.setLabel("List of attached sensors");
        sensorsList.setItems(attachedSensorsNames);
        sensorsList.setPlaceholder("Sensors ...");
        sensorsList.setValue(null);
        sensorsList.addValueChangeListener(event -> {
            sensorsList.setValue(null);

        });
        sensorsList.addAttachListener(event -> {});

        topVerticalLayout.setAlignItems(Alignment.START);
        topVerticalLayout.add(idTitle);
        topVerticalLayout.add(serialHwField);
        topVerticalLayout.add(nameField);
        topVerticalLayout.add(ownerField);
        topVerticalLayout.setId("topVerticalLayout");

        bottomVerticalLayout.setId("bottomVerticalLayout");
        bottomVerticalLayout.add(getSignalComponent(hardwareLive.getSignal_strength()));
        bottomVerticalLayout.add(dateTimePicker);
        bottomVerticalLayout.add(versionField);
        bottomVerticalLayout.add(sensorsList);

        add(topVerticalLayout);
        add(bottomVerticalLayout);
    }

    private Component getSignalComponent(int signal) {
        VerticalLayout div = new VerticalLayout();
        //Div div = new Div();
        div.setClassName("signalDiv");

        Icon signalIcon = VaadinIcon.SIGNAL.create();
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
}
