package com.example.application.data.service;

import com.example.application.data.entity.SensorGas;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.vaadin.artur.helpers.CrudService;

/**
 * Service class for Gas Sensor details.
 */
@Service
public class SensorGasService extends CrudService<SensorGas, Integer> {

    private SensorGasRepository sensorGasRepository;

    public SensorGasService(@Autowired SensorGasRepository sensorGasRepository) {
        this.sensorGasRepository = sensorGasRepository;
    }

    @Override
    protected JpaRepository<SensorGas, Integer> getRepository() {
        return sensorGasRepository;
    }
}
