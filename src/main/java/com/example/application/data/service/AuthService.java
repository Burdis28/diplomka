package com.example.application.data.service;

import com.example.application.data.entity.User;
import com.example.application.data.exceptions.AuthException;
import com.example.application.views.dashboard.SensorElectricDashboard;
import com.example.application.views.dashboard.SensorGasDashboard;
import com.example.application.views.dashboard.SensorWaterDashboard;
import com.example.application.views.hardwares.HardwareDetailView;
import com.example.application.views.login.LoginView;
import com.example.application.views.sensors.CreatesensorView;
import com.example.application.views.hardwares.CreateHardwareView;
import com.example.application.views.hardwares.HardwaresView;
import com.example.application.views.login.LogoutView;
import com.example.application.views.main.MainLayout;
import com.example.application.views.sensors.ElectricSensorView;
import com.example.application.views.sensors.GasSensorView;
import com.example.application.views.sensors.SensorsView;
import com.example.application.views.sensors.WaterSensorView;
import com.example.application.views.user.UserManagementView;
import com.example.application.views.user.UserView;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.router.RouteConfiguration;
import com.vaadin.flow.server.VaadinSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    public AuthService(@Autowired UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    public void authenticate(String username, String password) throws AuthException {
        User user = userRepository.getByLogin(username);

        if (user != null && passwordEncoder.matches(password, user.getPasswordHash())) {
            VaadinSession.getCurrent().setAttribute(User.class, user);
            if (RouteConfiguration.forSessionScope().getAvailableRoutes().size() < 3) {
                createRoutes(user.getAdmin());
            }
        } else {
            throw new AuthException();
        }
    }

    private void createRoutes(boolean admin) {
        // Získám si všechny routy povolené pro určitého uživatele a nastavím je skrze RouteConfiguration pro aktuální
        // session. Tudíž se uživatel bude schopen dostat jenom na jemu povolené views.
        getAuthorizedRoutes(admin)
                .forEach(route -> {
                    RouteConfiguration.forSessionScope().setRoute(route.getRoute(), route.getView(), MainLayout.class);
                });
        RouteConfiguration.forSessionScope().setRoute("sensor-el-detail", ElectricSensorView.class, MainLayout.class);
        RouteConfiguration.forSessionScope().setRoute("sensor-wat-detail", WaterSensorView.class, MainLayout.class);
        RouteConfiguration.forSessionScope().setRoute("sensor-gas-detail", GasSensorView.class, MainLayout.class);
        RouteConfiguration.forSessionScope().setRoute("hardware-detail", HardwareDetailView.class, MainLayout.class);
        RouteConfiguration.forSessionScope().setRoute("sensor-el-dashboard", SensorElectricDashboard.class, MainLayout.class);
        RouteConfiguration.forSessionScope().setRoute("sensor-wat-dashboard", SensorWaterDashboard.class, MainLayout.class);
        RouteConfiguration.forSessionScope().setRoute("sensor-gas-dashboard", SensorGasDashboard.class, MainLayout.class);
        RouteConfiguration.forSessionScope().setRoute("login", LoginView.class);

    }

    public static List<AuthorizedRouteData> getAuthorizedRoutes(boolean admin) {
        List<AuthorizedRouteData> routes = new ArrayList<>();

        if (admin) {
            //routes.add(new AuthorizedRoute("dashboard", "Dashboard", DashboardView.class, VaadinIcon.BAR_CHART_H));
            routes.add(new AuthorizedRouteData("sensors", "Sensors management", SensorsView.class, VaadinIcon.LIST));
            routes.add(new AuthorizedRouteData("create-sensor", "Create sensor", CreatesensorView.class, VaadinIcon.FORM));
            routes.add(new AuthorizedRouteData("hardwares", "Hardware management", HardwaresView.class, VaadinIcon.SERVER));
            routes.add(new AuthorizedRouteData("create-hardware", "Create hardware", CreateHardwareView.class, VaadinIcon.HARDDRIVE_O));
            //routes.add(new AuthorizedRoute("logs", "Logs", LogsView.class, VaadinIcon.CLIPBOARD_TEXT));
            routes.add(new AuthorizedRouteData("user", "My profile", UserView.class, VaadinIcon.USER));
            routes.add(new AuthorizedRouteData("user-management", "Users management", UserManagementView.class, VaadinIcon.USERS));
            routes.add(new AuthorizedRouteData("logout", "Logout", LogoutView.class, VaadinIcon.OUT));
        } else {
            //routes.add(new AuthorizedRoute("dashboard", "Dashboard", DashboardView.class, VaadinIcon.BAR_CHART_H));
            routes.add(new AuthorizedRouteData("sensors", "Sensors management", SensorsView.class, VaadinIcon.LIST));
            routes.add(new AuthorizedRouteData("create-sensor", "Create sensor", CreatesensorView.class, VaadinIcon.FORM));
            routes.add(new AuthorizedRouteData("hardwares", "Hardware management", HardwaresView.class, VaadinIcon.SERVER));
            routes.add(new AuthorizedRouteData("create-hardware", "Create hardware", CreateHardwareView.class, VaadinIcon.HARDDRIVE_O));
            routes.add(new AuthorizedRouteData("user", "My profile", UserView.class, VaadinIcon.USER));
            routes.add(new AuthorizedRouteData("logout", "Logout", LogoutView.class, VaadinIcon.OUT));
        }
        return routes;
    }

//    public Map<String, AuthorizedRoute> getAuthorizedRoutesMap(boolean admin) {
//        Map<String, AuthorizedRoute> routes = new HashMap<>();
//
//        if (admin) {
//            routes.put("dashboard", new AuthorizedRoute("dashboard", "Dashboard", DashboardView.class, VaadinIcon.BAR_CHART_H));
//            routes.put("sensors", new AuthorizedRoute("sensors", "Sensors", SensorsView.class, VaadinIcon.LIST));
//            routes.put("sensorcreate", new AuthorizedRoute("sensorcreate", "Sensor Create", CreatesensorView.class, VaadinIcon.FORM));
//            routes.put("logs", new AuthorizedRoute("logs", "Logs", LogsView.class, VaadinIcon.CLIPBOARD_TEXT));
//            routes.put("user", new AuthorizedRoute("user", "My profile", UserView.class, VaadinIcon.USER));
//            routes.put("logout", new AuthorizedRoute("logout", "Logout", LogoutView.class, VaadinIcon.CLOSE_CIRCLE));
//        } else {
//            routes.put("dashboard", new AuthorizedRoute("dashboard", "Dashboard", DashboardView.class, VaadinIcon.BAR_CHART_H));
//            routes.put("put", new AuthorizedRoute("user", "My profile", UserView.class, VaadinIcon.USER));
//            routes.put("logout", new AuthorizedRoute("logout", "Logout", LogoutView.class, VaadinIcon.CLOSE_CIRCLE));
//        }
//        return routes;
//    }
//    public record AuthorizedRoute(String route, String name, Class<? extends Component> view, VaadinIcon icon) {
//        // record - novinka v java 15
//    }
}
