package com.example.application.data.entity;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class StateValve {

    @Id
    private int id;
    private String state;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
