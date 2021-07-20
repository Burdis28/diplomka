package com.example.application.views.hardwares;

import com.example.application.data.entity.Hardware;
import com.example.application.data.entity.HardwareLive;
import com.example.application.data.service.HardwareLiveRepository;
import com.example.application.data.service.HardwareRepository;
import com.example.application.data.service.SensorRepository;
import com.example.application.data.service.UserRepository;
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
    private UserRepository userRepository;
    @Id("verticalBaseLayout")
    private VerticalLayout verticalBaseLayout;

    public HardwaresView(HardwareRepository hardwareRepository, HardwareLiveRepository hardwareLiveRepository,
                         SensorRepository sensorRepository, UserRepository userRepository) {
        this.hardwareRepository = hardwareRepository;
        this.hardwareLiveRepository = hardwareLiveRepository;
        this.sensorRepository = sensorRepository;
        this.userRepository = userRepository;

        createTiles(hardwareRepository, hardwareLiveRepository, sensorRepository, userRepository);
    }

    private void createTiles(HardwareRepository hardwareRepository, HardwareLiveRepository hardwareLiveRepository,
                             SensorRepository sensorRepository, UserRepository userRepository) {

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
            HardwareTile tile = new HardwareTile(hardware, hardwareLiveMap.get(hardware.getSerial_HW()), attachedSensorsNames,
                    userRepository.getHardwareOwner(hardware.getSerial_HW()).getFullName());
            tile.addClassName("tile");
            verticalBaseLayout.add(tile);
        }
    }
}
