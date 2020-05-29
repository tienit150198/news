package com.trantien.news.data;

import com.trantien.news.api.APIClient;
import com.trantien.news.api.RequestAPI;
import com.trantien.news.models.headline.News;

import io.reactivex.rxjava3.core.Single;

public class Repository {
    private static Repository mInstance;

    private RequestAPI mCallApi;

    private Repository() {
        mCallApi = APIClient.getClient().create(RequestAPI.class);
    }

    public static Repository getInstance() {
        if (mInstance == null) {
            synchronized (Repository.class) {
                mInstance = new Repository();
            }
        }
        return mInstance;
    }

    public Single<News> getAllHeadLine(String country, String apiKey) {
        return mCallApi.getAllHeadLine(country, apiKey);
    }

}
