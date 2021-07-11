package com.example.application.views.main;

import java.util.Optional;

import com.example.application.data.entity.User;
import com.example.application.data.service.AuthService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentUtil;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.tabs.TabsVariant;
import com.vaadin.flow.dom.ThemeList;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.server.PWA;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.PageTitle;
import com.example.application.views.main.MainView;
import com.example.application.views.dashboard.DashboardView;
import com.example.application.views.logs.LogsView;
import com.example.application.views.sensors.SensorsView;
import com.example.application.views.createsensor.CreatesensorView;
import com.example.application.views.hardwares.HardwaresView;
import com.example.application.views.createhardware.CreatehardwareView;
import com.example.application.views.adminpanel.AdminpanelView;
import com.vaadin.flow.theme.lumo.Lumo;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * The main view is a top-level placeholder for other views.
 */
@CssImport("./views/main/main-view.css")
@JsModule("./styles/shared-styles.js")
@JsModule("./js/theme-selector.js")
public class MainView extends AppLayout {

    private final Tabs menu;
    private H1 viewTitle;

    private final AuthService authService;

    public MainView(AuthService authService) {
        this.authService = authService;
        setPrimarySection(Section.DRAWER);
        addToNavbar(true, createHeaderContent());
        menu = createMenu();
        addToDrawer(createDrawerContent(menu));
    }

    private Component createHeaderContent() {
        HorizontalLayout layout = new HorizontalLayout();
        layout.setId("header");
        //layout.getThemeList().set("dark", true);
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
        Button btn = new Button("");
        if(themeList.contains(Lumo.DARK)) {
            btn.setIcon(VaadinIcon.MOON_O.create());
        } else {
            btn.setIcon(VaadinIcon.MOON.create());
        }

        btn.addClickListener(click -> {
            if (themeList.contains(Lumo.DARK)) {
                System.out.println("je tam dark");
                UI.getCurrent().getPage().executeJavaScript("document.documentElement.setAttribute(\"theme\",\"light\")");
                themeList.remove(Lumo.DARK);
                themeList.add(Lumo.LIGHT);
                btn.setIcon(VaadinIcon.MOON.create());
            } else {
                System.out.println("je tam light");
                UI.getCurrent().getPage().executeJavaScript("document.documentElement.setAttribute(\"theme\",\"dark\")");
                themeList.remove(Lumo.LIGHT);
                themeList.add(Lumo.DARK);
                btn.setIcon(VaadinIcon.MOON_O.create());
            }
        });
        return btn;
    }

    private Component createUserAvatar() {
        UserAvatar avatar = new UserAvatar();
        avatar.addClickListener(avatarClickEvent -> {
            UI.getCurrent().navigate("user");
        });
        return avatar;
    }

    private Component createDrawerContent(Tabs menu) {
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
        layout.add(logoLayout, menu);
        return layout;
    }

    private Tabs createMenu() {
        final Tabs tabs = new Tabs();
        tabs.setOrientation(Tabs.Orientation.VERTICAL);
        tabs.addThemeVariants(TabsVariant.LUMO_CENTERED);
        tabs.setId("tabs");
        tabs.add(createMenuItems());
        return tabs;
    }

    private Component[] createMenuItems() {
        // Využiju Vaadin session, získám usera a podle jeho role vytvořím jednotlivé taby, které může navštívit
        User user = VaadinSession.getCurrent().getAttribute(User.class);
        return authService.getAuthorizedRoutes(user.getAdmin()).stream().map(route ->
                createTab(route.name(), route.view(), route.icon())).toArray(Component[]::new);
    }

    private static Tab createTab(String text, Class<? extends Component> navigationTarget, VaadinIcon icon) {
        final Tab tab = new Tab();
        tab.add(new RouterLink(text, navigationTarget));
        Icon iconObj = new Icon(icon);
        iconObj.setSize("25px");
        tab.addComponentAtIndex(0, iconObj);
        ComponentUtil.setData(tab, Class.class, navigationTarget);
        return tab;
    }

    @Override
    protected void afterNavigation() {
        super.afterNavigation();
        getTabForComponent(getContent()).ifPresent(menu::setSelectedTab);
        viewTitle.setText(getCurrentPageTitle());
    }

    private Optional<Tab> getTabForComponent(Component component) {
        return menu.getChildren().filter(tab -> ComponentUtil.getData(tab, Class.class).equals(component.getClass()))
                .findFirst().map(Tab.class::cast);
    }

    private String getCurrentPageTitle() {
        PageTitle title = getContent().getClass().getAnnotation(PageTitle.class);
        return title == null ? "" : title.value();
    }
}
