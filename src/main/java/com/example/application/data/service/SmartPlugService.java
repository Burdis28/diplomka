package com.example.application.data.service;

import com.example.application.data.entity.SmartPlug;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.vaadin.artur.helpers.CrudService;

@Service
public class SmartPlugService extends CrudService<SmartPlug, Integer> {

    private SmartPlugRepository smartPlugRepository;

    public SmartPlugService(@Autowired SmartPlugRepository smartPlugRepository) {
        this.smartPlugRepository = smartPlugRepository;
    }

    public void deleteBySerialHW(String serialHw) {
        smartPlugRepository.deleteBySerialHw(serialHw);
    }

    @Override
    protected JpaRepository<SmartPlug, Integer> getRepository() {
        return smartPlugRepository;
    }
}