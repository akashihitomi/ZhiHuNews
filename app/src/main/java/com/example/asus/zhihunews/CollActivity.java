package com.example.asus.zhihunews;

import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ListView;

import java.util.ArrayList;

import static android.R.id.message;

public class CollActivity extends AppCompatActivity {
    private ListView colllistview;
    private NewsAdapter newsAdapter;
    private  ArrayList<NEWS> listCollNews;
    private static final String NewsUrl = "https://news-at.zhihu.com/api/4/news/";
    private android.os.Handler nHandler = new android.os.Handler(){
        public void handleMessage(Message msg) {
            listCollNews = (ArrayList<NEWS>) msg.obj;
            newsAdapter = new NewsAdapter(MyApplication.getContext(), listCollNews);
            Log.d("CollActivity", "adapter success and list.size = " + listCollNews.size());
            colllistview.setAdapter(newsAdapter);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coll);
        Toolbar colltoolbar = (Toolbar) findViewById(R.id.COLLtoolbar);
        colltoolbar.setTitle ("收藏");
        setSupportActionBar(colltoolbar);
        colllistview = (ListView) findViewById(R.id.Colllistview);
        new Thread(new Runnable() {
            @Override
            public void run() {
                NewsDBUtils newsDBUtils = new NewsDBUtils(MyApplication.getContext());
                ArrayList<NEWS> listCollNewsBean = newsDBUtils.Checknews();
                Message message = Message.obtain();
                message.obj = listCollNewsBean;
                nHandler.sendMessage(message);
            }
        }).start();

    }
}
