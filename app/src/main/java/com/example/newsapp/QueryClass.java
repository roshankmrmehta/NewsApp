package com.example.newsapp;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class QueryClass {

    private static final String CLASS_TAG = QueryClass.class.getSimpleName();

    public QueryClass() {
    }

    static List<NewsData> fetchNews(String requestUrl) {
        URL url = fetchUrl(requestUrl);
        String response = null;
        try {
            response = HttpRequest(url);
        } catch (IOException e) {
            Log.e(CLASS_TAG, "Problem making HTTP request.", e);
        }

        return newsJsonParse(response);
    }

    private static URL fetchUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(CLASS_TAG, "Problem fetching the URL.", e);
        }
        return url;
    }

    //Parsing JSON from Guardian API
    private static List<NewsData> newsJsonParse(String response) {
        String newsAuthor = "";
        ArrayList<NewsData> newsDataArrayList = new ArrayList<>();
        try {
            JSONObject jsonResponse = new JSONObject(response);
            JSONObject apiResponse = jsonResponse.getJSONObject("response");
            JSONArray jsonResult = apiResponse.getJSONArray("results");

            for (int i = 0; i < jsonResult.length(); i++) {
                JSONObject jsonObject = jsonResult.getJSONObject(i);
                String newsHeadline = jsonObject.getString("webTitle");
                String newsUrl = jsonObject.getString("webUrl");
                String newsDate = jsonObject.getString("webPublicationDate");
                newsDate = formatDate(newsDate);
                String newsType = jsonObject.getString("sectionName");

                JSONArray jsonTag = jsonObject.getJSONArray("tags");
                if (jsonTag.length() == 0) {
                    newsAuthor = "Unknown";
                } else {
                    for (int j = 0; j < jsonTag.length(); j++) {
                        try {
                            JSONObject authorName = jsonTag.getJSONObject(j);
                            newsAuthor = authorName.getString("webTitle");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
                newsDataArrayList.add(new NewsData(newsHeadline, newsAuthor, newsType, newsDate, newsUrl));
            }
        } catch (JSONException e) {
            Log.e("newsJsonParse", "Error in parsing JSON, Please Check", e);
        }
        return newsDataArrayList;
    }

    //Making HTTP Request..
    private static String HttpRequest(URL url) throws IOException {
        String responseJSON = "";

        if (url == null) {
            return responseJSON;
        }
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;

        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.connect();
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                responseJSON = readFromStream(inputStream);
            } else {
                Log.e(CLASS_TAG, "Wrong response code" + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(CLASS_TAG, "Error in fetching HTTP request", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return responseJSON;
    }

    //Date Formatter
    private static String formatDate(String rawDate) {
        String jsonDatePattern = "yyyy-MM-dd'T'HH:mm:ss'Z'";
        SimpleDateFormat jsonFormatter = new SimpleDateFormat(jsonDatePattern, Locale.US);
        try {
            Date parsedJsonDate = jsonFormatter.parse(rawDate);
            String finalDatePattern = "MMM d, yyy";
            SimpleDateFormat finalDateFormatter = new SimpleDateFormat(finalDatePattern, Locale.US);
            assert parsedJsonDate != null;
            return finalDateFormatter.format(parsedJsonDate);
        } catch (ParseException e) {
            Log.e("QueryUtilClass", "Error in JSON date: ", e);
            return "";
        }
    }

    //Read From Stream
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

}
