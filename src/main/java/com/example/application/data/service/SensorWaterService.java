package com.example.application.data.service;

import com.example.application.data.entity.SensorWater;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.vaadin.artur.helpers.CrudService;

@Service
public class SensorWaterService extends CrudService<SensorWater, Integer> {

    private SensorWaterRepository sensorWaterRepository;

    public SensorWaterService(@Autowired SensorWaterRepository sensorWaterRepository) {
        this.sensorWaterRepository = sensorWaterRepository;
    }

    @Override
    protected JpaRepository<SensorWater, Integer> getRepository() {
        return sensorWaterRepository;
    }
}
