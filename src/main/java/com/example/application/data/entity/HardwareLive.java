package com.example.application.data.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;

@Entity
@Table(name = "live")
public class HardwareLive {

    @Id
    @Column(name = "hwid")
    private String hwId;
    private int signal_strength;
    @Column(name = "datetime")
    private Timestamp dateTime;
    private String version;

    public String getHwId() {
        return hwId;
    }

    public void setHwId(String hwId) {
        this.hwId = hwId;
    }

    public int getSignal_strength() {
        return signal_strength;
    }

    public void setSignal_strength(int signal_strength) {
        this.signal_strength = signal_strength;
    }

    public Timestamp getDateTime() {
        return dateTime;
    }

    public void setDateTime(Timestamp dateTime) {
        this.dateTime = dateTime;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
