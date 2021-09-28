package com.example.application.data.service;

import com.example.application.data.entity.SensorElectric;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.vaadin.artur.helpers.CrudService;

/**
 * Service class for Electric Sensor details.
 */
@Service
public class SensorElectricService extends CrudService<SensorElectric, Integer> {

    private SensorElectricRepository sensorElectricRepository;

    public SensorElectricService(@Autowired SensorElectricRepository sensorElectricRepository) {
        this.sensorElectricRepository = sensorElectricRepository;
    }

    public boolean getHighRate(int id){
        return sensorElectricRepository.getHighRateOfSensor(id);
    }

    @Override
    protected JpaRepository<SensorElectric, Integer> getRepository() {
        return sensorElectricRepository;
    }
}
