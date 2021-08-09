package com.example.application.data.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.sql.Timestamp;

@Entity
public class NotificationLogHw {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO, generator="native")
    @GenericGenerator(name = "native", strategy = "native")
    private int id;
    private String serialHw;
    private String notificationType;
    private Timestamp lastSent;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSerialHw() {
        return serialHw;
    }

    public void setSerialHw(String serialHw) {
        this.serialHw = serialHw;
    }

    public String getNotificationType() {
        return notificationType;
    }

    public void setNotificationType(String notificationType) {
        this.notificationType = notificationType;
    }

    public Timestamp getLastSent() {
        return lastSent;
    }

    public void setLastSent(Timestamp lastSent) {
        this.lastSent = lastSent;
    }
}
