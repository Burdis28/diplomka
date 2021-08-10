package com.example.application.views.main;

import java.util.List;
import java.util.Optional;

import com.example.application.data.entity.User;
import com.example.application.data.service.AuthService;
import com.example.application.data.service.AuthorizedRouteData;
import com.vaadin.componentfactory.ToggleButton;
import com.vaadin.flow.component.*;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.tabs.TabsVariant;
import com.vaadin.flow.dom.ThemeList;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.server.PWA;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * The main view is a top-level placeholder for other views.
 */
@CssImport("./views/main/main-view.css")
@JsModule("./styles/shared-styles.js")
@JsModule("./js/theme-selector.js")
public class MainLayout extends AppLayout {

    private final Tabs menu;
    private H1 viewTitle;

    public MainLayout() {
        setPrimarySection(Section.DRAWER);
        addToNavbar(true, createHeaderContent());
        menu = createMenu();
        addToDrawer(createDrawerContent(menu));
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

    private Component createDrawerContent(Tabs menu) {
        VerticalLayout layout = new VerticalLayout();
        layout.setSizeFull();
        layout.setPadding(false);
        layout.setSpacing(false);
        layout.getThemeList().set("spacing-s", true);
        layout.setAlignItems(FlexComponent.Alignment.STRETCH);
        HorizontalLayout logoLayout = new HorizontalLayout();
        logoLayout.setId("logo");
        logoLayout.add(new Image("images/smart_home.png", "Diplomka logo"));
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

    private Component createDropDownMenu(List<AuthorizedRouteData> routesList) {
        User user = VaadinSession.getCurrent().getAttribute(User.class);
        Component[] tabs = AuthService.getAuthorizedRoutes(user.getAdmin()).stream().map(route ->
                createTab(route.getName(), route.getView(), route.getIcon())).toArray(Component[]::new);
        Select<Tab> select = new Select<>();
        select.add(tabs);
        select.addComponentAsFirst(new Text("Management"));
        return select;
    };

    private Component[] createMenuItems() {
        // Využiju Vaadin session, získám usera a podle jeho role vytvořím jednotlivé taby, které může navštívit
        User user = VaadinSession.getCurrent().getAttribute(User.class);
        return AuthService.getAuthorizedRoutes(user.getAdmin()).stream().map(route ->
                createTab(route.getName(), route.getView(), route.getIcon())).toArray(Component[]::new);
    }

    private static Tab createTab(String text, Class<? extends Component> navigationTarget, VaadinIcon icon) {
        final Tab tab = new Tab();
        tab.add(new RouterLink(text, navigationTarget));
        Icon iconObj = new Icon(icon);
        iconObj.setSize("35px");
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
