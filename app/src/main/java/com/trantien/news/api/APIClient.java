package com.trantien.news.api;

import com.google.gson.Gson;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class APIClient {
    private static final String BASE_URL =  "https://newsapi.org/v2/";
    private static Retrofit retrofit = null;

    public static Retrofit getClient(){
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .retryOnConnectionFailure(true) // reconnect while connect fail
                .build();

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(okHttpClient)   // add client
                .addConverterFactory(GsonConverterFactory.create()) // auto convert gson
                .build();

        return retrofit;
    }
}
