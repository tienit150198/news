package com.trantien.news.models.viewmodel;

import android.accounts.NetworkErrorException;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;

import com.trantien.news.data.Repository;
import com.trantien.news.models.headline.News;

import java.util.Collections;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import io.reactivex.rxjava3.subjects.BehaviorSubject;

public class MainViewModel extends ViewModel {
    private Repository mRepository;
    private BehaviorSubject<News> newsBehaviorSubject = BehaviorSubject.createDefault(new News());

    private CompositeDisposable disposable = new CompositeDisposable();

    public MainViewModel() {
        mRepository = Repository.getInstance();
    }

    private static final String TAG = "LOG_MainViewModel";

    public BehaviorSubject<News> getNews(@NonNull String country, @NonNull String apiKey) {
        if (newsBehaviorSubject.getValue().getStatus() == null || newsBehaviorSubject.getValue().getArticles() == null || newsBehaviorSubject.getValue().getArticles().size() == 0) {
            queryRetrofit(country, apiKey);
        }
        return newsBehaviorSubject;
    }

    @Override
    protected void onCleared() {
        disposable.clear();
    }

    private void queryRetrofit(@NonNull String country, @NonNull String apiKey) {
        Single<News> newsSingle = mRepository.getAllHeadLine(country, apiKey);

        disposable.add(
                newsSingle.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe((news, throwable) -> {
                            if (throwable != null) {
                                newsBehaviorSubject.onError(new NetworkErrorException("Please check your wifi"));
                            }else{
                                newsBehaviorSubject.onNext(news);
                            }
                        })
        );

//        Call<News> call = (Call<News>) mRepository.getAllHeadLine(country, apiKey);
//        call.enqueue(new Callback<News>() {
//            @Override
//            public void onResponse(Call<News> call, Response<News> response) {
//                if(response.isSuccessful()){
//                    News news = response.body();
//
//                    listener.onSucess(news);
//                }else{
//                    listener.onFail("Not found country" + response.code());
//                }
//            }
//
//            @Override
//            public void onFailure(Call<News> call, Throwable t) {
//                listener.onFail("Please check your wifi");
//            }
//        });
    }
}
