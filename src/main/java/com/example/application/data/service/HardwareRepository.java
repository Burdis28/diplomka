package com.example.application.data.service;

import com.example.application.data.entity.Hardware;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface HardwareRepository extends JpaRepository<Hardware, Integer> {

    @Query("SELECT serial_HW from Hardware")
    List<String> listHardwareSerials();

    @Query("SELECT h from Hardware h where h.serial_HW in (SELECT hwid from UserHW where userid=:userId)")
    List<Hardware> findByOwner(@Param("userId") Integer userId);
}