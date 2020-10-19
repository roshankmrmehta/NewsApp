package com.example.newsapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class NewsAdapter extends ArrayAdapter<NewsData> {

    public NewsAdapter(@NonNull Context context, @NonNull List<NewsData> newsData) {
        super(context, 0, newsData);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        if (listItem == null) {
            listItem = LayoutInflater.from(getContext()).inflate(R.layout.news_list, parent, false);
        }

        NewsData item = getItem(position);

        TextView newsHeadline = listItem.findViewById(R.id.newsHeadline);
        assert item != null;
        newsHeadline.setText(item.getNewsHeadline());

        TextView newsAuthor = listItem.findViewById(R.id.newsAuthor);
        newsAuthor.setText(item.getNewsAuthor());

        TextView newsDate = listItem.findViewById(R.id.newsDate);
        newsDate.setText(item.getNewsDate());

        TextView newsType = listItem.findViewById(R.id.newsType);
        newsType.setText(item.getNewsType());

        return listItem;
    }
}


