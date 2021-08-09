package com.example.application.views.createhardware;

import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.littemplate.LitTemplate;
import com.vaadin.flow.router.ParentLayout;
import com.vaadin.flow.router.PageTitle;
import com.example.application.views.main.MainView;

/**
 * A Designer generated component for the person-form-view template.
 *
 * Designer will add and remove fields with @Id mappings but does not overwrite
 * or otherwise change this file.
 */
@JsModule("./views/createhardware/createhardware-view.ts")
@CssImport("./views/createhardware/createhardware-view.css")
@Tag("createhardware-view")
@ParentLayout(MainView.class)
@PageTitle("Create hardware")
public class CreatehardwareView extends LitTemplate {

    public CreatehardwareView() {

    }
}
