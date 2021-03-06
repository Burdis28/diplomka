package com.example.application.data.service;

import com.example.application.data.entity.Hardware;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface HardwareRepository extends JpaRepository<Hardware, Integer> {

    @Query("SELECT serial_HW from Hardware")
    List<String> listHardwareSerials();

    @Query("SELECT h from Hardware h where h.serial_HW in (SELECT hwid from UserHW where userid=:userId)")
    List<Hardware> findByOwner(@Param("userId") Integer userId);

    @Query("SELECT h from Hardware h where h.serial_HW=:serialHw")
    Optional<Hardware> getBySerialHW(@Param("serialHw") String serialHw);

    @Transactional
    @Modifying
    @Query("delete from UserHW where hwid=:hwId")
    void deleteBindingTableData(String hwId);

    @Transactional
    @Modifying
    @Query(value = "insert into userhw (userId, hwId) VALUES (:userId,:serial_hw)", nativeQuery = true)
    void createBindingTableEntry(String serial_hw, int userId);

    @Query("SELECT name from Hardware where serial_HW=:serialHw")
    String getHardwareName(@Param("serialHw") String serialHw);
}