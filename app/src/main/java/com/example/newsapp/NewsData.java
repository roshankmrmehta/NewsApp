package com.example.newsapp;

public class NewsData {

    private String newsHeadline;
    private String newsAuthor;
    private String newsDate;
    private String newsType;
    private String newsUrl;

    public NewsData(String newsHeadline, String newsAuthor, String newsDate, String newsType, String newsUrl) {
        this.newsHeadline = newsHeadline;
        this.newsAuthor = newsAuthor;
        this.newsDate = newsDate;
        this.newsType = newsType;
        this.newsUrl = newsUrl;
    }

    public String getNewsHeadline() {
        return newsHeadline;
    }

    public String getNewsAuthor() {
        return newsAuthor;
    }

    public String getNewsDate() {
        return newsDate;
    }

    public String getNewsType() {
        return newsType;
    }

    public String getNewsUrl() {
        return newsUrl;
    }
}
