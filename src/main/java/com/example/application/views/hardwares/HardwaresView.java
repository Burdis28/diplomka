package com.example.application.views.hardwares;

import com.example.application.data.entity.Hardware;
import com.example.application.data.entity.HardwareLive;
import com.example.application.data.entity.User;
import com.example.application.data.service.*;
import com.example.application.views.hardwares.components.HardwareTile;
import com.example.application.views.main.MainView;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.littemplate.LitTemplate;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.template.Id;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.ParentLayout;
import com.vaadin.flow.server.VaadinSession;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@JsModule("./views/hardwares/hardwares-view.ts")
@CssImport("./views/hardwares/hardwares-view.css")
@Tag("hardwares-view")
@ParentLayout(MainView.class)
@PageTitle("Hardware management")
public class HardwaresView extends LitTemplate {

    private HardwareService hardwareService;
    private HardwareLiveService hardwareLiveService;
    private SensorService sensorService;
    private UserService userService;
    @Id("verticalBaseLayout")
    private VerticalLayout verticalBaseLayout;
    private User loggedUser;

    public HardwaresView(@Autowired HardwareService hardwareService,@Autowired HardwareLiveService hardwareLiveService,
                         @Autowired SensorService sensorService,@Autowired UserService userService) {
        this.hardwareService = hardwareService;
        this.hardwareLiveService = hardwareLiveService;
        this.sensorService = sensorService;
        this.userService = userService;
        loggedUser = VaadinSession.getCurrent().getAttribute(User.class);

        createTiles(hardwareService, hardwareLiveService, sensorService, userService);
    }

    private void createTiles(HardwareService hardwareService, HardwareLiveService hardwareLiveService,
                             SensorService sensorService, UserService userService) {

        List<Hardware> hardwareList;
        List<HardwareLive> hardwareLiveList;
        if (loggedUser.getAdmin()) {
            hardwareList = hardwareService.findAll();
            hardwareLiveList = hardwareLiveService.findByHardwareId(hardwareList.stream()
                    .map(Hardware::getSerial_HW).collect(Collectors.toList()));
        } else {
            hardwareList = hardwareService.findByOwner(loggedUser.getId());
            hardwareLiveList = hardwareLiveService.findByHardwareId(hardwareList.stream()
                    .map(Hardware::getSerial_HW).collect(Collectors.toList()));
        }

        List<String> attachedSensorsNames = new ArrayList<>();

        Map<String, HardwareLive> hardwareLiveMap = new HashMap<>();
        for (HardwareLive live : hardwareLiveList) {
            hardwareLiveMap.putIfAbsent(live.getHwId(), live);
        }

        for (Hardware hardware : hardwareList ) {
            attachedSensorsNames = sensorService.findSensorByIdHw(hardware.getSerial_HW()).stream().map(sensor ->
                    "[" + sensor.getId() + "] " + sensor.getName()).collect(Collectors.toList());

            HardwareTile tile = new HardwareTile(hardware, hardwareLiveMap.get(hardware.getSerial_HW()), attachedSensorsNames,
                    userService.getHardwareOwner(hardware.getSerial_HW()).getFullName());
            tile.addClassName("tile");
            verticalBaseLayout.add(tile);
        }
    }
}
