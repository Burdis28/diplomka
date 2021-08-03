package com.example.application.utils;

import com.example.application.data.entity.Sensor;
import com.example.application.data.entity.SensorTypes;
import com.example.application.data.entity.data.DataElectric;
import com.example.application.data.entity.data.DataWater;
import com.example.application.data.service.SensorService;
import com.example.application.data.service.data.DataElectricService;
import com.example.application.data.service.data.DataWaterService;
import org.apache.commons.lang3.RandomUtils;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;

public class DevelopmentDataCreator {

    private DataElectricService dataElectricService;
    private SensorService sensorService;
    private DataWaterService dataWaterService;

    public DevelopmentDataCreator(DataElectricService dataElectricService, SensorService sensorService,
                                  DataWaterService dataWaterService) {
        this.dataElectricService = dataElectricService;
        this.sensorService = sensorService;
        this.dataWaterService = dataWaterService;
    }

    public void createElectricData() {
        dataElectricService.deleteAll();
        Sensor sensor = sensorService.findByType("e").get(0);
        LocalDate date;
        for (int i = 0; i <= 90; i++) {
            date = LocalDate.now().minusDays(i);
            for (int x = 0; x < 24; x++) {
                DataElectric dataElectric = new DataElectric();
                dataElectric.setSensorId(sensor.getId());
                LocalDateTime time = LocalDateTime.of(date.getYear(), date.getMonth(), date.getDayOfMonth(), x, 0);
                dataElectric.setTime(Timestamp.valueOf(time));
                dataElectric.setLowRate(RandomUtils.nextDouble(0.01, 0.99));
                dataElectric.setHighRate(RandomUtils.nextDouble(1.00, 3.00));

                dataElectricService.update(dataElectric);
            }
        }
    }

    public void createWaterData() {
        dataWaterService.deleteAll();
        Sensor sensor = sensorService.findByType("w").get(0);
        for (int i = 0; i <= 90; i++) {
            LocalDate date = LocalDate.now().minusDays(i);
            for (int x = 0; x < 24; x++) {
                DataWater dataWater = new DataWater();
                dataWater.setSensorId(sensor.getId());
                LocalDateTime time = LocalDateTime.of(date.getYear(), date.getMonth(), date.getDayOfMonth(), x, 0);
                dataWater.setTime(Timestamp.valueOf(time));
                dataWater.setM3(RandomUtils.nextDouble(10, 500));

                dataWaterService.update(dataWater);
            }
        }
    }
}
