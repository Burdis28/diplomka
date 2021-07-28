package com.example.application.data.service;

import com.example.application.data.entity.Sensor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SensorRepository extends JpaRepository<Sensor, Integer> {

    List<Sensor> findSensorByIdHw(String idHW);

    @Query("SELECT s from Sensor s where s.idHw in (SELECT hwid from UserHW where userid=:userId)")
    List<Sensor> findAllByOwner(@Param("userId") Integer userId);
}