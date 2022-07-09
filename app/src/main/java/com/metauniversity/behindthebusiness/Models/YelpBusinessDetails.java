package com.metauniversity.behindthebusiness.Models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class YelpBusinessDetails {
    private List<YelpBusinessHours> hours;

    public List<YelpBusinessHours> getHours() {
        return hours;
    }

    public void setHours(List<YelpBusinessHours> hours) {
        this.hours = hours;
    }
}