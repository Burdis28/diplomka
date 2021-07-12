package com.example.application.data.service;

import com.example.application.data.entity.User;
import com.example.application.data.exceptions.AuthException;
import com.example.application.views.createsensor.CreatesensorView;
import com.example.application.views.dashboard.DashboardView;
import com.example.application.views.login.LogoutView;
import com.example.application.views.logs.LogsView;
import com.example.application.views.main.MainView;
import com.example.application.views.sensors.ElectricSensorView;
import com.example.application.views.sensors.SensorsView;
import com.example.application.views.sensors.WaterSensorView;
import com.example.application.views.user.UserView;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.router.RouteConfiguration;
import com.vaadin.flow.server.VaadinSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AuthService {

    private final UserRepository userRepository;

    public AuthService(@Autowired UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void authenticate(String username, String password) throws AuthException {
        User user = userRepository.getByLogin(username);

        if (user != null && user.checkPassword(password)) {
            VaadinSession.getCurrent().setAttribute(User.class, user);
            createRoutes(user.getAdmin());
        } else {
            throw new AuthException();
        }
    }

    private void createRoutes(boolean admin) {
        // Získám si všechny routy povolené pro určitého uživatele a nastavím je skrze RouteConfiguration pro aktuální
        // session. Tudíž se uživatel bude schopen dostat jenom na jemu povolené views.
        getAuthorizedRoutes(admin)
                .forEach(route -> {
                    RouteConfiguration.forSessionScope().setRoute(route.route, route.view, MainView.class);
                });
        RouteConfiguration.forSessionScope().setRoute("sensor-el-detail", ElectricSensorView.class, MainView.class);
        RouteConfiguration.forSessionScope().setRoute("sensor-wat-detail", WaterSensorView.class, MainView.class);

    }

    public List<AuthorizedRoute> getAuthorizedRoutes(boolean admin) {
        List<AuthorizedRoute> routes = new ArrayList<>();

        if (admin) {
            routes.add(new AuthorizedRoute("sensors", "Sensors", SensorsView.class, VaadinIcon.LIST));
            routes.add(new AuthorizedRoute("sensorcreate", "Sensor Create", CreatesensorView.class, VaadinIcon.FORM));
            routes.add(new AuthorizedRoute("dashboard", "Dashboard", DashboardView.class, VaadinIcon.BAR_CHART_H));
            routes.add(new AuthorizedRoute("logs", "Logs", LogsView.class, VaadinIcon.CLIPBOARD_TEXT));
            routes.add(new AuthorizedRoute("user", "My profile", UserView.class, VaadinIcon.USER));
            routes.add(new AuthorizedRoute("logout", "Logout", LogoutView.class, VaadinIcon.CLOSE_CIRCLE));
        } else {
            routes.add(new AuthorizedRoute("dashboard", "Dashboard", DashboardView.class, VaadinIcon.BAR_CHART_H));
            routes.add(new AuthorizedRoute("user", "My profile", UserView.class, VaadinIcon.USER));
            routes.add(new AuthorizedRoute("logout", "Logout", LogoutView.class, VaadinIcon.CLOSE_CIRCLE));
        }
        return routes;
    }

    public Map<String, AuthorizedRoute> getAuthorizedRoutesMap(boolean admin) {
        Map<String, AuthorizedRoute> routes = new HashMap<>();

        if (admin) {
            routes.put("sensors", new AuthorizedRoute("sensors", "Sensors", SensorsView.class, VaadinIcon.LIST));
            routes.put("sensorcreate", new AuthorizedRoute("sensorcreate", "Sensor Create", CreatesensorView.class, VaadinIcon.FORM));
            routes.put("dashboard", new AuthorizedRoute("dashboard", "Dashboard", DashboardView.class, VaadinIcon.BAR_CHART_H));
            routes.put("logs", new AuthorizedRoute("logs", "Logs", LogsView.class, VaadinIcon.CLIPBOARD_TEXT));
            routes.put("user", new AuthorizedRoute("user", "My profile", UserView.class, VaadinIcon.USER));
            routes.put("logout", new AuthorizedRoute("logout", "Logout", LogoutView.class, VaadinIcon.CLOSE_CIRCLE));
        } else {
            routes.put("dashboard", new AuthorizedRoute("dashboard", "Dashboard", DashboardView.class, VaadinIcon.BAR_CHART_H));
            routes.put("put", new AuthorizedRoute("user", "My profile", UserView.class, VaadinIcon.USER));
            routes.put("logout", new AuthorizedRoute("logout", "Logout", LogoutView.class, VaadinIcon.CLOSE_CIRCLE));
        }
        return routes;
    }

    public record AuthorizedRoute(String route, String name, Class<? extends Component> view, VaadinIcon icon) {
        // record - novinka v java 15
    }
}
