package com.example.newsapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<NewsData>>{

    private static final String CLASS_TAG = MainActivity.class.getName();
    private static final int LOADER_TAG = 1;
    private ProgressBar progressBar;
    NewsAdapter newsAdapter;
    TextView networkMsg;
    Context context = this;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressBar = findViewById(R.id.circular_progress);
        networkMsg = findViewById(R.id.network_msg);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        listView = findViewById(R.id.list_view);
        listView.setEmptyView(networkMsg);

        newsAdapter = new NewsAdapter(this, new ArrayList<NewsData>());
        listView.setAdapter(newsAdapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                NewsData newsData = newsAdapter.getItem(position);
                assert newsData != null;
                String newsUrl = newsData.getNewsUrl();
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(newsUrl));
                startActivity(intent);
            }
        });
        NewsLoaderManager();
    }

    public void NewsLoaderManager() {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        assert connectivityManager != null;
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        if (isConnected) {
            LoaderManager newsLoaderManager = getSupportLoaderManager();
            newsLoaderManager.initLoader(LOADER_TAG, null, this);
        } else {
            progressBar.setVisibility(View.GONE);
            networkMsg.setVisibility(View.VISIBLE);
            listView.setVisibility(View.GONE);
            networkMsg.setText(R.string.networkMsg);
        }
    }

    @NonNull
    @Override
    public Loader<List<NewsData>> onCreateLoader(int id, @Nullable Bundle args) {
        Uri.Builder uriBuilder = NewsApiCredential.UriPath(MainActivity.this);
        Log.e(CLASS_TAG, uriBuilder.toString());
        return new NewsLoader(this, uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(@NonNull Loader<List<NewsData>> loader, List<NewsData> data) {
        progressBar.setVisibility(View.GONE);
        newsAdapter.clear();
        if (data != null) {
            newsAdapter.addAll(data);
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<List<NewsData>> loader) {
        newsAdapter.clear();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.refresh) {
            NewsLoaderManager();
            Toast.makeText(this, R.string.update_msg,
                    Toast.LENGTH_SHORT).show();

        } else if (id == R.id.exit) {
            finish();
        }
        return super.onOptionsItemSelected(item);

    }
}