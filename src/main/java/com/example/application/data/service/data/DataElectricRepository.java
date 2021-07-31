package com.example.application.data.service.data;

import com.example.application.data.entity.data.DataElectric;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public interface DataElectricRepository extends JpaRepository<DataElectric, Integer> {

    List<DataElectric> findAllBySensorId(int sensorId);

    @Query("SELECT e FROM DataElectric e WHERE e.sensorId=:sensorId AND e.time between :dateFrom AND :dateTo")
    List<DataElectric> findAllBySensorIdAndTime(@Param("sensorId") int sensorId,
                                                @Param("dateFrom") Date dateFrom,
                                                @Param("dateTo") Date dateTo);

//    @Query("SELECT s from Sensor s where s.idHw in (SELECT hwid from UserHW where userid=:userId)")
//    List<DataElectric> findAllByOwner(@Param("userId") Integer userId);
}