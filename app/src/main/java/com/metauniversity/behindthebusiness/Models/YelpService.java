package com.metauniversity.behindthebusiness.Models;

import retrofit2.Call;

import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface YelpService {
    @GET("businesses/search")
    Call<YelpSearchResult> searchBusinesses(@Header("Authorization") String authHeader, @Query("categories") String category, @Query("location") String location);

    @GET("businesses/{id}")
    Call<YelpBusinessDetails> searchForDetails(@Header("Authorization") String authHeader, @Path("id") String id);
}
