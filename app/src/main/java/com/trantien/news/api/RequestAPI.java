package com.trantien.news.api;

import com.trantien.news.models.headline.News;

import io.reactivex.rxjava3.core.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RequestAPI {
    @GET("top-headlines?country=us")
    Single<News> getAllHeadLine(@Query("country") String country, @Query("apiKey") String apiKey);
}
