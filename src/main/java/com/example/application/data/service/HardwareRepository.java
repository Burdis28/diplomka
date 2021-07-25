package com.example.application.data.service;

import com.example.application.data.entity.Hardware;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface HardwareRepository extends JpaRepository<Hardware, Integer> {

    @Query("SELECT serial_HW from Hardware")
    List<String> listHardwareSerials();

}