
package com.orwa.gatherin.Models.NotificationsModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Datum {

    @SerializedName("hour")
    @Expose
    private int hour;
    @SerializedName("day")
    @Expose
    private int day;
    @SerializedName("month")
    @Expose
    private int month;
    @SerializedName("text")
    @Expose
    private String text;
    @SerializedName("minute")
    @Expose
    private int minute;
    @SerializedName("year")
    @Expose
    private int year;

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

}
