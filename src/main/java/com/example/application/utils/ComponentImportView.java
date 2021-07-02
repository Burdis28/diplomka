package com.example.application.utils;

import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

@Route(registerAtStartup = false)
public class ComponentImportView extends VerticalLayout {
    private LoginForm loginForm;
}
