package com.example.application.data.service;

import com.example.application.data.entity.Sensor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.vaadin.artur.helpers.CrudService;

import java.util.Arrays;
import java.util.List;

@Service
public class SensorService extends CrudService<Sensor, Integer> {

    private SensorRepository sensorRepository;

    public SensorService(@Autowired SensorRepository repository) {
        this.sensorRepository = repository;
    }

    @Override
    protected SensorRepository getRepository() {
        return sensorRepository;
    }

    @Override
    public void delete(Integer integer) {
        // TODO smazat ostatní záznamy z tabulek, na které tenhle sensor odkazuje
        super.delete(integer);
    }

    public List<Sensor> findAll() {
        return this.sensorRepository.findAll();
    }

    public List<Sensor> findAllByOwner(int ownerId) {
        return this.sensorRepository.findAllByOwner(ownerId);
    }

    public List<Sensor> findSensorByIdHw(String serial_hw) {
        return this.sensorRepository.findSensorByIdHw(serial_hw);
    }
}
