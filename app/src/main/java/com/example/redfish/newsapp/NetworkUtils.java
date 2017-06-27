package com.example.redfish.newsapp;

import android.net.Uri;
import android.util.Log;

import com.example.redfish.newsapp.Models.NewsItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

import static android.content.ContentValues.TAG;

/**
 * Created by Redfish on 6/15/2017.
 */

public class NetworkUtils {

    final static String NEWSAPP_BASE_URL="newsapi.org";
    final static String SOURCE_PARAM="source";
    final static String SORT_BY_PARAM="sortBy";
    final static String API_KEY_PARAM="apiKey";

    public static URL makeURL() {
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("https").authority(NEWSAPP_BASE_URL)
                .appendPath("v1")
                .appendPath("articles")
                .appendQueryParameter(SOURCE_PARAM,"the-next-web")
                .appendQueryParameter(SORT_BY_PARAM,"latest")
                .appendQueryParameter(API_KEY_PARAM,"40ea54a985fd4321bd7bb7027a91a263");
        URL url =null;
        try{
            url=new URL(builder.build().toString());
            Log.d(TAG,"Url" +url);
        }catch(MalformedURLException e){
            e.printStackTrace();
        }
        return url;
    }

    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection)url.openConnection();
        try{
            InputStream in=urlConnection.getInputStream();

            Scanner scanner=new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput=scanner.hasNext();
            if(hasInput){
                return scanner.next();
            }
            else{
                return null;
            }
        }finally{
            urlConnection.disconnect();
        }
    }

    public static ArrayList<NewsItem> parseJSON(String json) throws JSONException {
        ArrayList<NewsItem> result = new ArrayList<>();
        JSONObject main = new JSONObject(json);
        JSONArray items = main.getJSONArray("articles");

        for(int i = 0; i < items.length(); i++){
            JSONObject item = items.getJSONObject(i);
            String author = item.getString("author");
            String title=item.getString("title");
            String description=item.getString("description");
            String url=item.getString("url");
            String urlToImage=item.getString("urlToImage");
            String publishedAt=item.getString("publishedAt");

            NewsItem repo = new NewsItem(author,title,description,url,urlToImage,publishedAt);
            result.add(repo);
        }
        return result;
    }
}
