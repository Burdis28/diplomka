package com.example.application.data.service;

import com.example.application.data.entity.Hardware;
import com.example.application.data.entity.Sensor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.vaadin.artur.helpers.CrudService;

import java.util.List;
import java.util.Optional;

@Service
public class HardwareService extends CrudService<Hardware, Integer> {

    private HardwareRepository hardwareRepository;

    public HardwareService(@Autowired HardwareRepository repository) {
        this.hardwareRepository = repository;
    }

    @Override
    protected HardwareRepository getRepository() {
        return hardwareRepository;
    }

    @Override
    public void delete(Integer integer) {
        // TODO smazat ostatní záznamy z tabulek, které se týkají tohohle HW
        super.delete(integer);
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
