package com.example.asus.zhihunews;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import static android.R.id.message;
import static android.content.ContentValues.TAG;
import static android.media.tv.TvContract.Programs.Genres.NEWS;
import static java.lang.System.in;

public class getNetNews {
    public static ArrayList<NEWS> getNews(Context context, String urlString) {    /*获取并解析数据*/
        ArrayList<NEWS> listNews = new ArrayList<NEWS>();
        HttpURLConnection connection = null;
        BufferedReader reader = null;
        try {
            URL Url = new URL(urlString);
            connection = (HttpURLConnection) Url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(10000);
            connection.setReadTimeout(10000);
            InputStream in = connection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            JSONObject root_json = new JSONObject(response.toString());
            JSONArray jsonArray = root_json.getJSONArray("stories");
            for (int i = 0; i < jsonArray.length(); i++) {
                NEWS News = new NEWS();
                JSONObject news_json = jsonArray.getJSONObject(i);
                News.setTitle(news_json.getString("title"));  //获取标题
                JSONArray imagesArray = news_json.getJSONArray("images");
                News.setIcon_URL(imagesArray.getString(0));  //获取图片url
                News.setID(news_json.getString("id")); //获取ID
                listNews.add(News);
               // Log.d("MainActivity", news_json.getString("title"));
                //Log.d("MainActivity", imagesArray.getString(0));
               // Log.d("MainActivity", news_json.getString("id"));
            }
            //db数据操作

            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listNews;
    }

    public static NEWS getCollNews( String urlString) {    /*解析单条新闻数据*/
    NEWS CollNews = new NEWS();
    HttpURLConnection connection = null;
    BufferedReader reader = null;
        try {
        URL Url = new URL(urlString);
        connection = (HttpURLConnection) Url.openConnection();
        connection.setRequestMethod("GET");
        connection.setConnectTimeout(8000);
        connection.setReadTimeout(8000);
        InputStream is = connection.getInputStream();  //有bug
        reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder response = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            response.append(line);
        }
        JSONObject root_json = new JSONObject(response.toString());
            CollNews.setTitle(root_json.getString("title"));  //获取标题
            JSONArray imagesArray = root_json.getJSONArray("images");
            CollNews.setIcon_URL(imagesArray.getString(0));  //获取图片url
            CollNews.setID(root_json.getString("id")); //获取ID
            Log.d("MainActivity", root_json.getString("title"));
            Log.d("MainActivity", imagesArray.getString(0));
            Log.d("MainActivity", root_json.getString("id"));
        is.close();
     } catch (Exception e) {
        e.printStackTrace();
     }
        return CollNews;
  }
}