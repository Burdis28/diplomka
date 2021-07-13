package com.example.application.views.main;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.example.application.data.entity.User;
import com.example.application.data.service.AuthService;
import com.vaadin.componentfactory.ToggleButton;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentUtil;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.tabs.TabsVariant;
import com.vaadin.flow.dom.ThemeList;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.theme.lumo.Lumo;

/**
 * The main view is a top-level placeholder for other views.
 */
@CssImport("./views/main/main-view.css")
@JsModule("./styles/shared-styles.js")
@JsModule("./js/theme-selector.js")
public class MainView extends AppLayout {

    private final VerticalMenu verticalMenu;
    private H1 viewTitle;

    private final AuthService authService;

    public MainView(AuthService authService) {
        this.authService = authService;
        setPrimarySection(Section.DRAWER);
        addToNavbar(true, createHeaderContent());
        verticalMenu = new VerticalMenu(this.authService);
        verticalMenu.setId("verticalMenu");
        addToDrawer(createDrawerContent(verticalMenu));
    }

    private Component createHeaderContent() {
        HorizontalLayout layout = new HorizontalLayout();
        layout.setId("header");
        layout.setWidthFull();
        layout.setSpacing(false);
        layout.setAlignItems(FlexComponent.Alignment.CENTER);
        layout.add(new DrawerToggle());
        viewTitle = new H1();
        layout.add(viewTitle);
        layout.add(createUserAvatar());
        layout.add(getThemeSwitchButton());
        return layout;
    }

    private Component getThemeSwitchButton() {
        ThemeList themeList = UI.getCurrent().getElement().getThemeList();
        System.out.println(themeList);
        Div div = new Div();
        div.setId("themeSwitcher");
        HorizontalLayout layout = new HorizontalLayout();
        //layout.setId("themeSwitcher");
        Component icon = new Icon(VaadinIcon.MOON);
        ToggleButton btn = new ToggleButton();
        btn.addValueChangeListener(click -> {
            if (themeList.contains(Lumo.DARK)) {
                System.out.println("je tam dark");
                UI.getCurrent().getPage().executeJavaScript("document.documentElement.setAttribute(\"theme\",\"light\")");
                themeList.remove(Lumo.DARK);
                themeList.add(Lumo.LIGHT);
            } else {
                System.out.println("je tam light");
                UI.getCurrent().getPage().executeJavaScript("document.documentElement.setAttribute(\"theme\",\"dark\")");
                themeList.remove(Lumo.LIGHT);
                themeList.add(Lumo.DARK);
            }
        });
        icon.setId("themeSwitcherIcon");
        btn.setId("themeSwitcherButton");
        layout.add(icon);
        layout.add(btn);
        div.add(layout);
        return div;
    }

    private Component createUserAvatar() {
        UserAvatar avatar = new UserAvatar();
        avatar.addClickListener(avatarClickEvent -> {
            UI.getCurrent().navigate("user");
        });
        return avatar;
    }

    private Component createDrawerContent(VerticalMenu menu) {
        VerticalLayout layout = new VerticalLayout();
        layout.setSizeFull();
        layout.setPadding(false);
        layout.setSpacing(false);
        layout.getThemeList().set("spacing-s", true);
        layout.setAlignItems(FlexComponent.Alignment.STRETCH);
        HorizontalLayout logoLayout = new HorizontalLayout();
        logoLayout.setId("logo");
        logoLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        logoLayout.add(new Image("images/logo.png", "Diplomka logo"));
        logoLayout.add(new H1("Diplomka"));
        layout.add(logoLayout);
        layout.add(menu);
        return layout;
    }

    @Override
    protected void afterNavigation() {
        super.afterNavigation();
        viewTitle.setText(getCurrentPageTitle());
    }

    private String getCurrentPageTitle() {
        PageTitle title = getContent().getClass().getAnnotation(PageTitle.class);
        return title == null ? "" : title.value();
    }
}
