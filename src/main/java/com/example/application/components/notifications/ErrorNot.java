package com.example.application.components.notifications;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;

public class ErrorNot extends Div {

    public ErrorNot(String text) {
        Icon icon = new Icon(VaadinIcon.EXCLAMATION_CIRCLE);
        icon.setSize("30px");
        addComponentAtIndex(0, icon);

        Text errorText = new Text(text);
        addComponentAtIndex(1, errorText);
    }
}
