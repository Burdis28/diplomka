package com.example.application.views.hardwares.components;

import com.example.application.data.entity.Hardware;
import com.example.application.data.entity.HardwareLive;
import com.example.application.utils.Colors;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.html.H3;


import java.time.LocalDateTime;

public class HardwareTile extends VerticalLayout {

    private HorizontalLayout topVerticalLayout = new HorizontalLayout();
    private HorizontalLayout bottomVerticalLayout = new HorizontalLayout();

    private H3 idTitle = new H3();
    private TextField serialHwField = new TextField();
    private TextField nameField = new TextField();

    private DateTimePicker dateTimePicker = new DateTimePicker();

    public HardwareTile(Hardware hardware, HardwareLive hardwareLive) {
        idTitle.setText("Hardware [ " + hardware.getId_HW() + " ]");
        idTitle.setId("idTitle");

        serialHwField.setLabel("Serial HW code");
        serialHwField.setValue(hardware.getSerial_HW());
        serialHwField.setReadOnly(true);

        nameField.setLabel("HW name");
        nameField.setValue(hardware.getName());
        nameField.setReadOnly(true);

        dateTimePicker.setLabel("Time of signal check");
        dateTimePicker.setValue(LocalDateTime.now());
        dateTimePicker.setReadOnly(true);

        topVerticalLayout.setAlignItems(Alignment.START);
        topVerticalLayout.add(idTitle);
        topVerticalLayout.add(serialHwField);
        topVerticalLayout.add(nameField);
        topVerticalLayout.setId("topVerticalLayout");

        bottomVerticalLayout.setId("bottomVerticalLayout");
        bottomVerticalLayout.add(getSignalComponent(hardwareLive.getSignal_strength()));
        bottomVerticalLayout.add(dateTimePicker);

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
