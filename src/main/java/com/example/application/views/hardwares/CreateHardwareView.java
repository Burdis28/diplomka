package com.example.application.views.hardwares;

import com.example.application.views.main.MainView;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.littemplate.LitTemplate;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.ParentLayout;

/**
 * A Designer generated component for the create-hardware-view template.
 *
 * Designer will add and remove fields with @Id mappings but
 * does not overwrite or otherwise change this file.
 */
@Tag("create-hardware-view")
@JsModule("./views/hardwares/create-hardware-view.ts")
@CssImport("./views/hardwares/create-hardware-view.css")
@ParentLayout(MainView.class)
@PageTitle("Create hardware")
public class CreateHardwareView extends LitTemplate {

    /**
     * Creates a new CreateHardwareView.
     */
    public CreateHardwareView() {
        // You can initialise any data required for the connected UI components here.
    }

}
