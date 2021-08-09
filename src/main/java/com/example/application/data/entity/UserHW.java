package com.example.application.data.entity;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class UserHW {

    @Id
    private int id;
    private int userid;
    private String hwid;

    public UserHW() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public String getHwid() {
        return hwid;
    }

    public void setHwid(String hwid) {
        this.hwid = hwid;
    }
}
