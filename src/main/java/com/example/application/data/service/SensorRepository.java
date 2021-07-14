package com.example.application.data.service;

import com.example.application.data.entity.Sensor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SensorRepository extends JpaRepository<Sensor, Integer> {

    List<Sensor> findSensorByIdHw(String idHW);

}