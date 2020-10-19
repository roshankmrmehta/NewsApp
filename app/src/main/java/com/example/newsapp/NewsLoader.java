package com.example.newsapp;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;

import java.util.List;

public class NewsLoader extends AsyncTaskLoader<List<NewsData>> {
    private String mUrl;

    public NewsLoader(@NonNull Context context, String request) {
        super(context);
        mUrl = request;
    }

    @Nullable
    @Override
    public List<NewsData> loadInBackground() {
        if (mUrl == null) {
            return null;
        } else
            return QueryClass.fetchNews(mUrl);
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }
}
