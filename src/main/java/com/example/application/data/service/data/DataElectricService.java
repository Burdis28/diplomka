package com.example.application.data.service.data;

import com.example.application.data.entity.data.DataElectric;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.vaadin.artur.helpers.CrudService;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

/**
 * Service class for Electricity Data.
 */
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

    /**
     * Method that finds and returns list of data for a specific sensor and range of dates.
     * @param sensorId ID of sensor
     * @param dateFrom start of range date, included
     * @param dateTo end of range date, excluded
     * @return list of DataElectric objects
     */
    public List<DataElectric> findAllBySensorIdAndDate(int sensorId, LocalDate dateFrom, LocalDate dateTo) {
        // Hledám podle UTC-2
        Calendar cal = Calendar.getInstance();

        Timestamp from = Timestamp.valueOf(dateFrom.atStartOfDay());
        Timestamp to = Timestamp.valueOf(dateTo.atTime(LocalTime.MAX));

        cal.setTimeInMillis(from.getTime());
        cal.add(Calendar.HOUR, -2);
        from = new Timestamp(cal.getTime().getTime());

        cal.setTimeInMillis(to.getTime());
        cal.add(Calendar.HOUR, -2);
        to = new Timestamp(cal.getTime().getTime());

        List<DataElectric> list = dataElectricRepository.findAllBySensorIdAndTime(sensorId,
                from,
                to);

        for (DataElectric dataElectric : list) {
            cal.setTimeInMillis(dataElectric.getTime().getTime());
            cal.add(Calendar.HOUR, 2);
            dataElectric.setTime(new Timestamp(cal.getTime().getTime()));
        }

        return list;
    }

    public void deleteAll() {
        dataElectricRepository.deleteAll();
    }
}
