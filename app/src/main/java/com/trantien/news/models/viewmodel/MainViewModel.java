package com.trantien.news.models.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.trantien.news.data.Repository;
import com.trantien.news.models.headline.News;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainViewModel extends AndroidViewModel {
    private Repository mRepository;
    private OnResult listener;

    public MainViewModel(@NonNull Application application) {
        super(application);

        mRepository = Repository.getInstance(application);
    }

    public void getNews(@NonNull String country, @NonNull String apiKey){
        Call<News> call = mRepository.getAllHeadLine(country, apiKey);
        call.enqueue(new Callback<News>() {
            @Override
            public void onResponse(Call<News> call, Response<News> response) {
                if(response.isSuccessful()){
                    News news = response.body();

                    listener.onSucess(news);
                }else{
                    listener.onFail("Not found country" + response.code());
                }
            }

            @Override
            public void onFailure(Call<News> call, Throwable t) {
                listener.onFail("Please check your wifi");
            }
        });
    }

    public void setListener(OnResult listener){
        this.listener = listener;
    }

    public interface OnResult {
        void onSucess(News news);
        void onFail(String message);
    }
}
