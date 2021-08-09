package com.example.application.data.service;

import com.example.application.data.entity.HardwareLive;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.vaadin.artur.helpers.CrudService;

import java.util.List;
import java.util.Optional;

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
        super.delete(integer);
    }

    public void deleteBySerialHw(String serialHw) {
        hardwareLiveRepository.deleteByHwId(serialHw);
    }

    public List<HardwareLive> findByHardwareId(List<String> serialHwList) {
        return this.hardwareLiveRepository.findByHardwareIds(serialHwList);
    }

    public List<HardwareLive> findAll() {
        return this.hardwareLiveRepository.findAll();
    }

    public HardwareLive findByHardwareId(String hwId) {
        if (hardwareLiveRepository.findByHwId(hwId).isPresent()) {
            return hardwareLiveRepository.findByHwId(hwId).get();
        } else {
            return null;
        }
    }
}
