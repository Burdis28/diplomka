package com.example.application.data.service;

import com.example.application.data.entity.StateValve;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.vaadin.artur.helpers.CrudService;

import java.util.List;

/**
 * Service class for representation of state of a sensor.
 */
@Service
public class StateValveService extends CrudService<StateValve, Integer> {

    private StateValveRepository stateValveRepository;

    public StateValveService(@Autowired StateValveRepository stateValveRepository) {
        this.stateValveRepository = stateValveRepository;
    }

    @Override
    protected JpaRepository<StateValve, Integer> getRepository() {
        return stateValveRepository;
    }

    public List<StateValve> listAll() {
        return stateValveRepository.findAll();
    }
}
