package com.example.application.views.adminpanel;

import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.littemplate.LitTemplate;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.template.Id;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.ParentLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.PageTitle;
import com.example.application.views.main.MainView;

/**
 * A Designer generated component for the adminpanel-view template.
 *
 * Designer will add and remove fields with @Id mappings but does not overwrite
 * or otherwise change this file.
 */
@JsModule("./views/adminpanel/adminpanel-view.ts")
@CssImport("./views/adminpanel/adminpanel-view.css")
@Tag("adminpanel-view")
@ParentLayout(MainView.class)
@PageTitle("Admin panel")
public class AdminpanelView extends LitTemplate {

    @Id
    private TextField name;

    @Id
    private Button sayHello;

    public AdminpanelView() {
        sayHello.addClickListener(e -> {
            Notification.show("Hello " + name.getValue());
        });
    }
}
