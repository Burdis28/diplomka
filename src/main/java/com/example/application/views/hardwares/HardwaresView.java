package com.example.application.views.hardwares;

import com.example.application.data.entity.Hardware;
import com.example.application.data.entity.HardwareLive;
import com.example.application.data.entity.Sensor;
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

@JsModule(value="./views/hardwares/hardwares-view.ts")
@CssImport(value="./views/hardwares/hardwares-view.css")
@Tag(value="hardwares-view")
@ParentLayout(value=MainView.class)
@PageTitle(value="Hardware management")
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

        List<Sensor> attachedSensors;

        Map<String, HardwareLive> hardwareLiveMap = new HashMap<>();
        for (HardwareLive live : hardwareLiveList) {
            hardwareLiveMap.putIfAbsent(live.getHwId(), live);
        }

        for (Hardware hardware : hardwareList ) {
            attachedSensors = new ArrayList<>(sensorService.findSensorByIdHw(hardware.getSerial_HW()));

            //try {
                HardwareTile tile = new HardwareTile(hardware, hardwareLiveMap.get(hardware.getSerial_HW()), attachedSensors,
                        userService.getHardwareOwner(hardware.getSerial_HW()).getFullName(), hardwareService, hardwareLiveService);
                tile.addClassName("tile");
                verticalBaseLayout.add(tile);
//            } catch (Exception e) {
//                System.out.println(e.getMessage());
//            }
        }
    }
}
