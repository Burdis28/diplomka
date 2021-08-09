package com.example.application.data.service;

import com.example.application.data.entity.Hardware;
import com.example.application.data.entity.HardwareLive;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.vaadin.artur.helpers.CrudService;

@Service
public class HardwareLiveService extends CrudService<HardwareLive, Integer> {

    private HardwareLiveRepository hardwareLiveRepository;

    public HardwareLiveService(@Autowired HardwareLiveRepository repository) {
        this.hardwareLiveRepository = repository;
    }

    @Override
    protected HardwareLiveRepository getRepository() {
        return hardwareLiveRepository;
    }

    @Override
    public void delete(Integer integer) {
        // TODO smazat ostatní záznamy z tabulek, které se týkají tohohle HW
        super.delete(integer);
    }
}
