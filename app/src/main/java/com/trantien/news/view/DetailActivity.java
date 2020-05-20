package com.trantien.news.view;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.trantien.news.constraints.Constraint;
import com.trantien.news.databinding.ActivityDetailBinding;

public class DetailActivity extends AppCompatActivity {
    private ActivityDetailBinding binding;
    private String mCurrentURL;

    boolean loadingFinished = true;
    boolean redirect = false;
    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // load process
        binding.process.setVisibility(View.VISIBLE);
        binding.process.setIndeterminate(true);
        binding.webview.setVisibility(View.GONE);

        getUrl();
        binding.webview.getSettings().setLoadsImagesAutomatically(true);
        binding.webview.getSettings().setJavaScriptEnabled(true);
        binding.webview.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        // set client
//        binding.webview.setWebChromeClient(new WebChromeClient());

        binding.webview.setWebViewClient(new WebViewClient(){
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                if(!loadingFinished){
                    redirect = true;
                }

                loadingFinished = false;
                view.loadUrl(request.getUrl().toString());
                return super.shouldOverrideUrlLoading(view, request);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                if(!redirect){
                    loadingFinished = true;

                    // load process
                    binding.process.setVisibility(View.GONE);
                    binding.process.setIndeterminate(false);
                    binding.webview.setVisibility(View.VISIBLE);
                }else{
                    redirect = false;
                }

                super.onPageFinished(view, url);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                loadingFinished = false;
                super.onPageStarted(view, url, favicon);
            }
        });
        binding.webview.loadUrl(mCurrentURL);
    }

    private void getUrl() {
        mCurrentURL = getIntent().getStringExtra(Constraint.SEND_URL);
    }

    public void finishActivity(View view){
        onBackPressed();
    }

}
