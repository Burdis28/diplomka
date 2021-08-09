package com.example.application.data.service;

import com.example.application.data.entity.NotificationLogHw;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.vaadin.artur.helpers.CrudService;

@Service
public class NotificationLogHwService extends CrudService<NotificationLogHw, Integer> {

    private NotificationLogHwRepository notificationLogHwRepository;

    public NotificationLogHwService(@Autowired NotificationLogHwRepository notificationLogHwRepository) {
        this.notificationLogHwRepository = notificationLogHwRepository;
    }

    public void deleteBySerialHW(String serialHw) {
        notificationLogHwRepository.deleteBySerialHw(serialHw);
    }

    @Override
    protected JpaRepository<NotificationLogHw, Integer> getRepository() {
        return notificationLogHwRepository;
    }
}
