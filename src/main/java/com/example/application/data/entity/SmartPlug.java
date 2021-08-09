package com.example.application.data.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.sql.Timestamp;

@Entity
public class SmartPlug {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO, generator="native")
    @GenericGenerator(name = "native", strategy = "native")
    private int id;
    private String serialHw;
    private String serialPlug;
    private String name;
    private String activationCode;
    private Timestamp activeTill;
    private int activationCount;

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

    public String getSerialPlug() {
        return serialPlug;
    }

    public void setSerialPlug(String serialPlug) {
        this.serialPlug = serialPlug;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getActivationCode() {
        return activationCode;
    }

    public void setActivationCode(String activationCode) {
        this.activationCode = activationCode;
    }

    public Timestamp getActiveTill() {
        return activeTill;
    }

    public void setActiveTill(Timestamp activeTill) {
        this.activeTill = activeTill;
    }

    public int getActivationCount() {
        return activationCount;
    }

    public void setActivationCount(int activationCount) {
        this.activationCount = activationCount;
    }
}
