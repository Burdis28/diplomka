package com.example.application.data.service;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.icon.VaadinIcon;

public class AuthorizedRouteData {

    private final String route;
    private final String name;
    private final Class<? extends Component> view;
    private final VaadinIcon icon;

    public AuthorizedRouteData(String route, String name, Class<? extends Component> view, VaadinIcon icon) {
        this.route = route;
        this.name = name;
        this.view = view;
        this.icon = icon;
    }

    public String getRoute() {
        return route;
    }

    public String getName() {
        return name;
    }

    public Class<? extends Component> getView() {
        return view;
    }

    public VaadinIcon getIcon() {
        return icon;
    }
}
