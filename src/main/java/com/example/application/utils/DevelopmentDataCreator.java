package com.example.application.utils;

import com.example.application.data.entity.Sensor;
import com.example.application.data.entity.SensorTypes;
import com.example.application.data.entity.data.DataElectric;
import com.example.application.data.service.SensorService;
import com.example.application.data.service.data.DataElectricService;
import org.apache.commons.lang3.RandomUtils;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.Month;

public class DevelopmentDataCreator {

    private DataElectricService dataElectricService;
    private SensorService sensorService;

    public DevelopmentDataCreator(DataElectricService dataElectricService, SensorService sensorService) {
        this.dataElectricService = dataElectricService;
        this.sensorService = sensorService;

        createElectricData();
    }

    public void createElectricData() {
        dataElectricService.deleteAll();
        Sensor sensor = sensorService.findByType("e").get(0);
        for (int i = 1; i <= 12; i++) {
            for (int y = 1; y <= 28; y++) {
                for( int x = 0; x < 24; x++) {
                    DataElectric dataElectric = new DataElectric();
                    dataElectric.setSensorId(sensor.getId());
                    LocalDateTime time = LocalDateTime.of(2021, Month.of(i), y, x, 0);
                    dataElectric.setTime(Timestamp.valueOf(time));
                    dataElectric.setLowRate(RandomUtils.nextDouble(10, 200));
                    dataElectric.setHighRate(RandomUtils.nextDouble(200, 500));

                    dataElectricService.update(dataElectric);
                }
            }
        }
    }
}
