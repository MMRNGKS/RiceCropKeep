package com.example.ricecropkeeptwo;

public class DataModel {

    private String title;
    private String date;
    private String time;
    private String alarmID;

    public DataModel(String title, String date, String time, String alarmID) {
        this.title = title;
        this.date = date;
        this.time = time;
        this.alarmID = alarmID;
    }

    public String getTitle() {
        return title;
    }

    public String getDate() {
        return date;
    }

    public String getTime() { return time; }

    public String getAlarmID() { return alarmID; }

}

