package com.example.application.data.service.data;

import com.example.application.data.entity.data.DataWater;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface DataWaterRepository extends JpaRepository<DataWater, Integer> {

    List<DataWater> findAllBySensorId(int sensorId);

    @Query("SELECT e FROM DataWater e WHERE e.sensorId=:sensorId AND e.time between :dateFrom AND :dateTo")
    List<DataWater> findAllBySensorIdAndTime(@Param("sensorId") int sensorId,
                                                @Param("dateFrom") Date dateFrom,
                                                @Param("dateTo") Date dateTo);

//    @Query("SELECT s from Sensor s where s.idHw in (SELECT hwid from UserHW where userid=:userId)")
//    List<DataElectric> findAllByOwner(@Param("userId") Integer userId);
}