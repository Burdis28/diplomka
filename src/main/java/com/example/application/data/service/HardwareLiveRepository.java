package com.example.application.data.service;

import com.example.application.data.entity.HardwareLive;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface HardwareLiveRepository extends JpaRepository<HardwareLive, Integer> {

    @Query("select h from HardwareLive h where h.hwId in (:hwList)")
    List<HardwareLive> findByHardwareIds(@Param("hwList") List<String> serialHwList);

    Optional<HardwareLive> findByHwId(String hwId);
}