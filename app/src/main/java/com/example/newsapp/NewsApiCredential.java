package com.example.newsapp;

import android.content.Context;
import android.net.Uri;

public class NewsApiCredential {

    private static final String newsApiUrl = "place your url here";
    private static final String api = "API_KEYS";

    static Uri.Builder UriPath(Context context) {
        Uri baseUri = Uri.parse(newsApiUrl);

        Uri.Builder uriBuilder = baseUri.buildUpon();
        uriBuilder.appendQueryParameter(api, "API");
        return baseUri.buildUpon();
    }
}
