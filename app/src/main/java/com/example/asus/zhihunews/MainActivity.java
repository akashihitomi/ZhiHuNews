package com.example.asus.zhihunews;

import android.app.Activity;
import android.app.Application;
import android.app.DownloadManager;
import android.content.ContentValues;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Message;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.NodeList;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;


import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static android.R.attr.action;
import static android.R.attr.data;
import static android.R.attr.duration;
import static android.R.attr.reparent;
import static android.R.attr.restoreAnyVersion;
import static android.R.id.list;
import static android.R.id.message;
import static android.R.id.title;
import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;
import static android.media.MediaFormat.KEY_DURATION;


import static com.example.asus.zhihunews.R.id.action0;
import static com.example.asus.zhihunews.R.id.home;
import static com.example.asus.zhihunews.R.id.listview;
import static com.example.asus.zhihunews.R.id.toolbar;

public class MainActivity extends AppCompatActivity {
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ListView listView;  //新闻列表
    private  ArrayList<NEWS> listNewsBean; //新闻集合
    private NewsAdapter newsAdapter;  //主屏幕适配器
    private ArrayAdapter<String> adapter;  //左菜单适配器
    private String[] menudata = {"首页", "日常心理学", "用户推荐日报", "电影日报", "不许无聊", "设计日报",
            "大公司日报", "财经日报", "互联网安全", "开始游戏", "音乐日报", "动漫日报", "体育日报"};
    private static String API = "https://news-at.zhihu.com/api/4/news/latest";

    private android.os.Handler mHandler = new android.os.Handler(){
        public void handleMessage(Message msg) {
            listNewsBean = (ArrayList<NEWS>) msg.obj;
            newsAdapter = new NewsAdapter(MyApplication.getContext(), listNewsBean);
            Log.d("MainActivity", "adapter success and list.size = "+listNewsBean.size());
            listView.setAdapter(newsAdapter);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = (ListView) findViewById(listview);  //今日新闻列表
        adapter = new ArrayAdapter<String>(MyApplication.getContext(), android.R.layout.simple_list_item_1, menudata);  //左菜单
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.draw_layout);
        ActionBar actionBar = getSupportActionBar();  //菜单按钮
        mDrawerList = (ListView) findViewById(R.id.leftlist);  //左菜单
        mDrawerList.setAdapter(adapter);   //左菜单
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.menu);
        }
        System.out.println("MainActivity:"+newsAdapter);
        new Thread(new Runnable() {
            @Override
            public void run() {
                ArrayList<NEWS> listNewsBean = getNetNews.getNews(MyApplication.getContext(),API);
                Message message = Message.obtain();
                message.obj = listNewsBean;
                mHandler.sendMessage(message);
            }
        }).start();

    }

    public boolean onCreateOptionMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar, menu);
        return true;
    }

    public boolean onOptionItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.login:
                Toast.makeText(MyApplication.getContext(), "点击会跳转至知乎登陆界面", Toast.LENGTH_SHORT).show();
                break;
            case R.id.setting:
                Toast.makeText(MyApplication.getContext(), "点击会跳转至设置界面", Toast.LENGTH_SHORT).show();
                break;
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                break;
            default:
        }
        return true;
    }

}