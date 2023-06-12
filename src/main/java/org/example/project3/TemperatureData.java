package org.example.project3;

public class TemperatureData {
    private String place;
    private double temperature;
    private String date;
    private String time;
    private String station;

    public TemperatureData(String place, double temperature, String date, String time, String station) {
        this.place = place;
        this.temperature = temperature;
        this.date = date;
        this.time = time;
        this.station = station;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getStation() {
        return station;
    }

    public void setStation(String station) {
        this.station = station;
    }
}

