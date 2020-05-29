package com.trantien.news.view;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.trantien.news.R;
import com.trantien.news.adapter.ArticleAdapter;
import com.trantien.news.callbacks.ILoadMore;
import com.trantien.news.constraints.Constraint;
import com.trantien.news.databinding.ActivityMainBinding;
import com.trantien.news.models.headline.Article;
import com.trantien.news.models.headline.News;
import com.trantien.news.models.viewmodel.MainViewModel;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity implements ArticleAdapter.OnItemClickListener, ILoadMore {
    private static final String COUNTRY = "us";
    private ActivityMainBinding binding;

    private MainViewModel mMainViewModel;
    private ArticleAdapter mAdapter;

    private List<Article> mArticleList;
    private List<Article> displayArrayList;

    private CompositeDisposable disposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // load process
        binding.process.setVisibility(View.VISIBLE);
        binding.process.setIndeterminate(true);
        binding.recyclerMain.setVisibility(View.GONE);
        binding.toolbar.setVisibility(View.GONE);

        mMainViewModel = new MainViewModel();

        disposable.add(
                mMainViewModel.getNews(COUNTRY, getString(R.string.news_key))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnNext(news -> {
                            if (news.getStatus() != null) {
                                onSucess(news);
                            }
                        })
                        .doOnError(throwable -> {
                            if (throwable != null) {
                                Log.d(TAG, "onCreate: " + throwable.toString());
                                onFail(throwable);
                            }
                        })
                        .subscribe()
        );

        mArticleList = new ArrayList<>();
        displayArrayList = new ArrayList<>();
        mAdapter = new ArticleAdapter(this, this);

        binding.recyclerMain.setAdapter(mAdapter);
        binding.recyclerMain.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerMain.setHasFixedSize(true);

        mAdapter.submitList(displayArrayList);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        disposable.clear();
    }

    @Override
    public void onItemClick(View view, int position) {
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra(Constraint.SEND_URL, mArticleList.get(position).getUrl());
        startActivity(intent);
    }

    private static final String TAG = "LOG_MainActivity";

    public void onSucess(News news) {
        Log.d(TAG, "onSucess: " + news.toString());
        mArticleList = news.getArticles();

        for (int i = 0; i < Math.min(mArticleList.size(), 10); i++) {
            displayArrayList.add(mArticleList.get(i));
        }
        displayArrayList.add(null);
        mAdapter.submitList(displayArrayList);

        // load process
        binding.process.setVisibility(View.GONE);
        binding.process.setIndeterminate(false);
        binding.recyclerMain.setVisibility(View.VISIBLE);
        binding.toolbar.setVisibility(View.VISIBLE);
    }

    public void onFail(Throwable throwable) {
        Toast.makeText(this, throwable.getMessage(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLoadMore() {
        binding.recyclerMain.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                loadMore();
            }
        });
    }

    private void loadMore() {

        Handler handler = new Handler();
        handler.postDelayed(() -> {
            displayArrayList.remove(displayArrayList.size() - 1);
            int scrollPosition = displayArrayList.size();

            mAdapter.submitList(displayArrayList);
            int currentSize = scrollPosition;
            int nextLimit = Math.min(currentSize + 10, mArticleList.size());

            while (currentSize < nextLimit) {
                displayArrayList.add(mArticleList.get(currentSize));
                currentSize++;
            }

            if (displayArrayList.size() < mArticleList.size()) {
                displayArrayList.add(null);
            }

            mAdapter.submitList(displayArrayList);
        }, 500);

    }
}
