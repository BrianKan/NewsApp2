package com.example.redfish.newsapp;

import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
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
}
