package com.metauniversity.behindthebusiness.Models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public class YelpBusiness {
    public static final double mileInMeter = 0.000621371;
    private String name;
    @SerializedName("image_url")
    private String imageUrl;
    private double rating;
    @SerializedName("distance")
    private double distanceAway;
    @SerializedName("location")
    private YelpLocation location;
    private String id;
    @SerializedName("url")
    private String businessURL;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public double getDistanceAway() {
        return distanceAway * mileInMeter;
    }

    public void setDistanceAway(double distanceAway) {
        this.distanceAway = distanceAway * mileInMeter;
    }

    public YelpLocation getLocation() {
        return location;
    }

    public void setLocation(YelpLocation location) {
        this.location = location;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBusinessURL() { return businessURL; }
    public void setBusinessURL(String businessURL) { this.businessURL = businessURL; }
}
