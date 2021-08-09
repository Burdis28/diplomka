package com.example.application.data.service.data;

import com.example.application.data.entity.data.DataWater;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.vaadin.artur.helpers.CrudService;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@Service
public class DataWaterService extends CrudService<DataWater, Integer> {

    private DataWaterRepository dataWaterRepository;

    public DataWaterService(@Autowired DataWaterRepository repository) {
        this.dataWaterRepository = repository;
    }

    @Override
    protected DataWaterRepository getRepository() {
        return dataWaterRepository;
    }

    @Override
    public void delete(Integer integer) {
        super.delete(integer);
    }

    public List<DataWater> findAll() {
        return this.dataWaterRepository.findAll();
    }

    public List<DataWater> findAllBySensorId(int sensorId) {
        return this.dataWaterRepository.findAllBySensorId(sensorId);
    }

    public List<DataWater> findAllBySensorIdAndDate(int sensorId, LocalDate dateFrom, LocalDate dateTo) {
        ZoneId defaultZoneId = ZoneId.systemDefault();
        return this.dataWaterRepository.findAllBySensorIdAndTime(sensorId,
                Date.from(dateFrom.atStartOfDay(defaultZoneId).toInstant()),
                Date.from(dateTo.atTime(LocalTime.of(23,59,59)).atZone(defaultZoneId).toInstant()));
    }

    public void deleteAll() {
        dataWaterRepository.deleteAll();
    }
}
