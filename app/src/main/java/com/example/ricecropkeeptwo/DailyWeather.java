package com.example.ricecropkeeptwo;

public class DailyWeather {
    private String dateTime;
    private String imageUrl;
    private double temperature;
    private String mainWeather;

    public DailyWeather(String dateTime, String imageUrl, double temperature, String mainWeather) {
        this.dateTime = dateTime;
        this.imageUrl = imageUrl;
        this.temperature = temperature;
        this.mainWeather = mainWeather;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public String getMainWeather() {
        return mainWeather;
    }

    public void setMainWeather(String mainWeather) {
        this.mainWeather = mainWeather;
    }
}
