package com.example.application.components.notifications;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import org.springframework.stereotype.Component;

@Component
public class ErrorNotification extends Notification {

    public ErrorNotification() {
        this.addThemeVariants(NotificationVariant.LUMO_ERROR);
        this.setPosition(Position.MIDDLE);
        this.setDuration(4000);
        this.addComponentAtIndex(0, getIcon());
    }

    private Icon getIcon() {
        Icon icon = new Icon(VaadinIcon.EXCLAMATION_CIRCLE);
        icon.setSize("30px");
        return icon;
    }

    public void setErrorText(String text) {
        this.addComponentAtIndex(1, new Text(" " + text));
    }
}