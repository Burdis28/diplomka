package com.example.application.data.service;

import com.example.application.data.entity.Hardware;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HardwareRepository extends JpaRepository<Hardware, Integer> {

}