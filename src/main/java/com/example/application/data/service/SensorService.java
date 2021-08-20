package com.example.application.data.service;

import com.example.application.data.entity.Sensor;
import com.example.application.data.entity.SensorTypes;
import com.example.application.data.entity.SensorWater;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.vaadin.artur.helpers.CrudService;

import java.util.Arrays;
import java.util.List;

@Service
public class SensorService extends CrudService<Sensor, Integer> {

    private SensorRepository sensorRepository;
    private SensorElectricRepository sensorElectricRepository;
    private SensorWaterRepository sensorWaterRepository;
    private SensorGasRepository sensorGasRepository;

    public SensorService(@Autowired SensorRepository repository, SensorElectricRepository sensorElectricRepository,
                         SensorWaterRepository sensorWaterRepository, SensorGasRepository sensorGasRepository) {
        this.sensorRepository = repository;
        this.sensorElectricRepository = sensorElectricRepository;
        this.sensorWaterRepository = sensorWaterRepository;
        this.sensorGasRepository = sensorGasRepository;
    }

    @Override
    protected SensorRepository getRepository() {
        return sensorRepository;
    }

    public void delete(Integer integer, SensorTypes type) {
        switch (type) {
            case e: sensorElectricRepository.deleteBySensorId(integer);
            case w: sensorWaterRepository.deleteBySensorId(integer);
            // not in this version
            //case g: sensorGasRepository.deleteBySensorId(integer);
        }
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

    public List<Sensor> findByType(String type) {
        return sensorRepository.findAllByType(type);
    }

    public List<SensorGridRepresentation> findAllForGrid() {
        return sensorRepository.findAllForGrid();
    }

    public List<SensorGridRepresentation> findAllByOwnerForGrid(int ownerId) {
        return this.sensorRepository.findAllByOwnerForGrid(ownerId);
    }
}
