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

    List<Sensor> findAllByType(String type);

    @Query("SELECT s.id as id, s.idHw as idHw, s.pinId as pinId, s.name as name, s.type as type from Sensor s")
    List<SensorGridRepresentation> findAllForGrid();

    @Query("SELECT s.id as id, s.idHw as idHw, s.pinId as pinId, s.name as name, s.type as type " +
            "from Sensor s where s.idHw in (SELECT hwid from UserHW where userid=:userId)")
    List<SensorGridRepresentation> findAllByOwnerForGrid(@Param("userId") Integer userId);
}