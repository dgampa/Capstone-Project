package com.metauniversity.behindthebusiness.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Properties;

public class YelpSearchResult {
    @SerializedName("total")
    private int total;
    @SerializedName("businesses")
    private List<YelpBusiness> businesses;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<YelpBusiness> getBusinesses() {
        return businesses;
    }

    public void setBusinesses(List<YelpBusiness> businesses) {
        this.businesses = businesses;
    }
}