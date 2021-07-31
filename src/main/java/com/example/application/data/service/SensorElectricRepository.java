package com.example.application.data.service;

import com.example.application.data.entity.SensorElectric;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface SensorElectricRepository extends JpaRepository<SensorElectric, Integer> {

    @Transactional
    @Modifying
    @Query("DELETE FROM SensorElectric where sensor_id=:sensorId")
    void deleteBySensorId(@Param("sensorId") int sensorId);
}
