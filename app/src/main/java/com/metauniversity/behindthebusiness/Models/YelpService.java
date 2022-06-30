package com.metauniversity.behindthebusiness.Models;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface YelpService {
    @GET("businesses/search")
    Call<YelpFusion> searchRestaurants(@Header("Authorization") String authHeader, @Query("term") String searchTerm, @Query("location") String location);
}
