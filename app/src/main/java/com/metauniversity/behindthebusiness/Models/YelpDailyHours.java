package com.metauniversity.behindthebusiness.Models;

import com.google.gson.annotations.SerializedName;

public class YelpDailyHours {
    @SerializedName("start")
    private String openingTime;
    @SerializedName("end")
    private String closingTime;
    private int day;

    public String getClosingTime() {
        return closingTime;
    }

    public void setClosingTime(String closingTime) {
        this.closingTime = closingTime;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public String getOpeningTime() {
        return openingTime;
    }

    public void setOpeningTime(String openingTime) {
        this.openingTime = openingTime;
    }
}

