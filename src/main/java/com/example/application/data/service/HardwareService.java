package com.example.application.data.service;

import com.example.application.data.entity.Hardware;
import com.example.application.data.entity.HardwareLive;
import com.example.application.data.entity.Sensor;
import com.example.application.data.entity.UserHW;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.vaadin.artur.helpers.CrudService;

import java.util.List;
import java.util.Optional;

@Service
public class HardwareService extends CrudService<Hardware, Integer> {

    private HardwareRepository hardwareRepository;
    private HardwareLiveService hardwareLiveService;
    private NotificationLogHwService notificationLogHwService;

    public HardwareService(@Autowired HardwareRepository repository, HardwareLiveService hardwareLiveService,
                           NotificationLogHwService notificationLogHwService) {
        this.hardwareRepository = repository;
        this.hardwareLiveService = hardwareLiveService;
        this.notificationLogHwService = notificationLogHwService;
    }

    public Hardware createHw(Hardware entity, int userId) {
        Hardware hardware = super.update(entity);
        hardwareRepository.createBindingTableEntry(entity.getSerial_HW(), userId);
        return hardware;
    }

    @Override
    protected HardwareRepository getRepository() {
        return hardwareRepository;
    }

    @Override
    public void delete(Integer integer) {
    }

    public void delete(Integer id, String serialHW) {
 //       hardwareRepository.deleteBindingTableData(serialHW);
 //       hardwareLiveService.deleteBySerialHw(serialHW);
 //       notificationLogHwService.deleteBySerialHW(serialHW);
        super.delete(id);
    }

    public List<String> listHardwareSerials() {
        return hardwareRepository.listHardwareSerials();
    }

    public List<Hardware> findByOwner(Integer id) {
        return this.hardwareRepository.findByOwner(id);
    }

    public List<Hardware> findAll() {
        return this.hardwareRepository.findAll();
    }

    public Optional<Hardware> getBySerialHW(String serialHw) {
        return hardwareRepository.getBySerialHW(serialHw);
    }
}
