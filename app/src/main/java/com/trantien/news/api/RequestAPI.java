package com.trantien.news.api;

import com.trantien.news.models.headline.News;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RequestAPI {
    @GET("top-headlines?country=us")
    Call<News> getAllHeadLine(@Query("country") String country, @Query("apiKey") String apiKey);
}
