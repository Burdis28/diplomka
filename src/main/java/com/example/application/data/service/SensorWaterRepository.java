package com.example.application.data.service;

import com.example.application.data.entity.SensorWater;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface SensorWaterRepository extends JpaRepository<SensorWater, Integer> {

    @Transactional
    @Modifying
    @Query("DELETE FROM SensorWater where sensor_id=:sensorId")
    void deleteBySensorId(@Param("sensorId") int sensorId);
}
