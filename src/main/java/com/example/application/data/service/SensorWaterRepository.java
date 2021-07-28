package com.example.application.data.service;

import com.example.application.data.entity.SensorWater;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SensorWaterRepository extends JpaRepository<SensorWater, Integer> {

}
