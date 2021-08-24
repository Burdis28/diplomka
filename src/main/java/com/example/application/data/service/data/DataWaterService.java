package com.example.application.data.service.data;

import com.example.application.data.entity.data.DataElectric;
import com.example.application.data.entity.data.DataWater;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.vaadin.artur.helpers.CrudService;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Calendar;
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
        Calendar cal = Calendar.getInstance();

        Timestamp from = Timestamp.valueOf(dateFrom.atStartOfDay());
        Timestamp to = Timestamp.valueOf(dateTo.atTime(LocalTime.MAX));

        cal.setTimeInMillis(from.getTime());
        cal.add(Calendar.HOUR, -2);
        from = new Timestamp(cal.getTime().getTime());

        cal.setTimeInMillis(to.getTime());
        cal.add(Calendar.HOUR, -2);
        to = new Timestamp(cal.getTime().getTime());

        List<DataWater> list = dataWaterRepository.findAllBySensorIdAndTime(sensorId,
                from,
                to);

        for (DataWater dataWater : list) {
            cal.setTimeInMillis(dataWater.getTime().getTime());
            cal.add(Calendar.HOUR, +2);
            dataWater.setTime(new Timestamp(cal.getTime().getTime()));
        }

        return list;
    }

    public void deleteAll() {
        dataWaterRepository.deleteAll();
    }
}
