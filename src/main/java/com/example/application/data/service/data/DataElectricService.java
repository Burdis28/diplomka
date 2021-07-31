package com.example.application.data.service.data;

import com.example.application.data.entity.data.DataElectric;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.vaadin.artur.helpers.CrudService;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@Service
public class DataElectricService extends CrudService<DataElectric, Integer> {

    private DataElectricRepository dataElectricRepository;

    public DataElectricService(@Autowired DataElectricRepository repository) {
        this.dataElectricRepository = repository;
    }

    @Override
    protected DataElectricRepository getRepository() {
        return dataElectricRepository;
    }

    @Override
    public void delete(Integer integer) {
        super.delete(integer);
    }

    public List<DataElectric> findAll() {
        return this.dataElectricRepository.findAll();
    }

    public List<DataElectric> findAllBySensorId(int sensorId) {
        return this.dataElectricRepository.findAllBySensorId(sensorId);
    }

    public List<DataElectric> findAllBySensorIdAndDate(int sensorId, LocalDate dateFrom, LocalDate dateTo) {
        ZoneId defaultZoneId = ZoneId.systemDefault();
        return this.dataElectricRepository.findAllBySensorIdAndTime(sensorId,
                Date.from(dateFrom.atStartOfDay(defaultZoneId).toInstant()),
                Date.from(dateTo.atTime(LocalTime.of(23,59,59)).atZone(defaultZoneId).toInstant()));
    }

    public void deleteAll() {
        dataElectricRepository.deleteAll();
    }
}
