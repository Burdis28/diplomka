package com.example.application.data.service;

import com.example.application.data.entity.SensorElectric;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.vaadin.artur.helpers.CrudService;

@Service
public class SensorElectricService extends CrudService<SensorElectric, Integer> {

    private SensorElectricRepository sensorElectricRepository;

    public SensorElectricService(@Autowired SensorElectricRepository sensorElectricRepository) {
        this.sensorElectricRepository = sensorElectricRepository;
    }

    @Override
    protected JpaRepository<SensorElectric, Integer> getRepository() {
        return sensorElectricRepository;
    }
}
