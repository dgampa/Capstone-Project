package com.metauniversity.behindthebusiness.Models;

import com.google.gson.annotations.SerializedName;

public class YelpLocation {
    @SerializedName("address1")
    private String buildingNumberAndStreetName;
    private String city;
    private String state;
    @SerializedName("zip_code")
    private String zipcode;
    private String country;

    public String getBuildingNumberAndStreetName() {
        return buildingNumberAndStreetName;
    }

    public void setBuildingNumberAndStreetName(String buildingNumberAndStreetName) {
        this.buildingNumberAndStreetName = buildingNumberAndStreetName;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String formattedLocation() {
        return buildingNumberAndStreetName + ", " + city + ", " + state + " " + zipcode + "\n";
    }
}

