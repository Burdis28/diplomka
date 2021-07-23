package com.example.application.views.hardwares;

import com.example.application.data.entity.Hardware;
import com.example.application.data.entity.HardwareLive;
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

    private HardwareRepository hardwareRepository;
    private HardwareLiveRepository hardwareLiveRepository;
    private SensorRepository sensorRepository;
    private UserService userService;
    @Id("verticalBaseLayout")
    private VerticalLayout verticalBaseLayout;

    public HardwaresView(@Autowired HardwareRepository hardwareRepository,@Autowired HardwareLiveRepository hardwareLiveRepository,
                         @Autowired SensorRepository sensorRepository,@Autowired UserService userService) {
        this.hardwareRepository = hardwareRepository;
        this.hardwareLiveRepository = hardwareLiveRepository;
        this.sensorRepository = sensorRepository;
        this.userService = userService;

        createTiles(hardwareRepository, hardwareLiveRepository, sensorRepository, userService);
    }

    private void createTiles(HardwareRepository hardwareRepository, HardwareLiveRepository hardwareLiveRepository,
                             SensorRepository sensorRepository, UserService userService) {

        List<Hardware> hardwareList = hardwareRepository.findAll();
        List<HardwareLive> hardwareLiveList = hardwareLiveRepository.findAll();
        List<String> attachedSensorsNames = new ArrayList<>();

        Map<String, HardwareLive> hardwareLiveMap = new HashMap<>();
        for (HardwareLive live : hardwareLiveList) {
            hardwareLiveMap.putIfAbsent(live.getHwId(), live);
        }

        for (Hardware hardware : hardwareList ) {
            attachedSensorsNames = sensorRepository.findSensorByIdHw(hardware.getSerial_HW()).stream().map(sensor ->
                    "[" + sensor.getId() + "] " + sensor.getName()).collect(Collectors.toList());
            //System.out.println(hardware.getSerial_HW());
            HardwareTile tile = new HardwareTile(hardware, hardwareLiveMap.get(hardware.getSerial_HW()), attachedSensorsNames,
                    userService.getHardwareOwner(hardware.getSerial_HW()).getFullName());
            tile.addClassName("tile");
            verticalBaseLayout.add(tile);
        }
    }
}
