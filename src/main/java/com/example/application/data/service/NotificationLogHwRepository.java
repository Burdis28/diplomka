package com.example.application.data.service;

import com.example.application.data.entity.NotificationLogHw;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface NotificationLogHwRepository extends JpaRepository<NotificationLogHw, Integer> {

    @Transactional
    @Modifying
    void deleteBySerialHw(String serialHw);

    NotificationLogHw findBySerialHw(String serialHw);
}
