package com.example.DatamartAPI;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@RestController
public class TemperatureController {

    @GetMapping("/v1/places/temperature/max")
    public List<TemperatureData> getPlacesWithMaxTemperature(
            @RequestParam("from") String fromDate,
            @RequestParam("to") String toDate) throws SQLException {

        SQLiteDBHelper db = new SQLiteDBHelper();
        ResultSet rs = db.selectData("Tmax", "*", fromDate, toDate);
        List<TemperatureData> temperatureDataList = new ArrayList<>();

        while (rs.next()) {
            String place = rs.getString("place");
            double temperature = rs.getDouble("value");
            String date = rs.getString("date");
            String time = rs.getString("time");
            String station = rs.getString("station");

            TemperatureData temperatureData = new TemperatureData(place, temperature, date, time, station);
            temperatureDataList.add(temperatureData);
        }


        return temperatureDataList;
    }


    @GetMapping("/v1/places/temperature/min")
    public List<TemperatureData> getPlacesWithMinTemperature(
            @RequestParam("from") String fromDate,
            @RequestParam("to") String toDate) throws SQLException {
        SQLiteDBHelper db = new SQLiteDBHelper();
        List<TemperatureData> temperatureDataList;
        try (ResultSet rs = db.selectData("Tmin", "*", fromDate, toDate)) {
            temperatureDataList = new ArrayList<>();

            while (rs.next()) {
                String place = rs.getString("place");
                double temperature = rs.getDouble("value");
                String date = rs.getString("date");
                String time = rs.getString("time");
                String station = rs.getString("station");

                TemperatureData temperatureData = new TemperatureData(place, temperature, date, time, station);
                temperatureDataList.add(temperatureData);
            }
        }

        return temperatureDataList;
    }
}