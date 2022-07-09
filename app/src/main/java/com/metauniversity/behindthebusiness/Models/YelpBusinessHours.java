package com.metauniversity.behindthebusiness.Models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class YelpBusinessHours {
    @SerializedName("open")
    private List<YelpDailyHours> dailyHours;
    @SerializedName("is_open_now")
    private boolean isOpen;

    public List<YelpDailyHours> getDailyHours() {
        return dailyHours;
    }

    public void setDailyHours(List<YelpDailyHours> dailyHours) {
        this.dailyHours = dailyHours;
    }

    public boolean getOpen() {
        return isOpen;
    }

    public void setOpen(boolean open) {
        isOpen = open;
    }
}
