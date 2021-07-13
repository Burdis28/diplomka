package com.example.application.data.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "hw")
public class Hardware {

    @Id
    private int id_HW;
    private String name;
    private String serial_HW;

    public int getId_HW() {
        return id_HW;
    }

    public void setId_HW(int id_HW) {
        this.id_HW = id_HW;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSerial_HW() {
        return serial_HW;
    }

    public void setSerial_HW(String serial_HW) {
        this.serial_HW = serial_HW;
    }
}
