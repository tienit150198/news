package com.trantien.news.data;

import android.app.Application;

import androidx.annotation.NonNull;

import com.trantien.news.api.APIClient;
import com.trantien.news.api.RequestAPI;
import com.trantien.news.models.headline.News;

import retrofit2.Call;

public class Repository {
    private static Repository mInstance;

    private RequestAPI mCallApi;

    private Repository(final Application application) {
        mCallApi = APIClient.getClient().create(RequestAPI.class);
    }

    public static Repository getInstance(@NonNull Application application) {
        if (mInstance == null) {
            synchronized (Repository.class) {
                mInstance = new Repository(application);
            }
        }
        return mInstance;
    }

    public Call<News> getAllHeadLine(String country, String apiKey) {
        return mCallApi.getAllHeadLine(country, apiKey);
    }

}
