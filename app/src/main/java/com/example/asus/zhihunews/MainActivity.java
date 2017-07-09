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
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static android.R.attr.action;
import static android.R.attr.data;
import static android.R.attr.duration;
import static android.R.attr.reparent;
import static android.R.id.list;
import static android.R.id.title;
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
            newsAdapter = new NewsAdapter(MainActivity.this, listNewsBean);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = (ListView) findViewById(listview);  //今日新闻列表
        adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, menudata);  //左菜单
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
        listView.setAdapter(newsAdapter);
        new Thread(new Runnable() {
            @Override
            public void run() {
                ArrayList<NEWS> listNewsBean =getNetNews(MainActivity.this, API);
                Message message = Message.obtain();
                message.obj = listNewsBean;
                mHandler.sendMessage(message);
            }
        }).start();
    }


    public class NewsAdapter extends BaseAdapter {
        private LayoutInflater mLayoutInflater;
        private List<NEWS> NewsData;

        public NewsAdapter(Context context, List<NEWS> listNewsBean) {
            this.mLayoutInflater = LayoutInflater.from(context);
            this.NewsData = listNewsBean;
        }

        @Override
        public int getCount() {
            return NewsData.size();
        }

        @Override
        public Object getItem(int position) {
            return NewsData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder viewHolder;
            if (convertView == null) {
                convertView = mLayoutInflater.inflate(R.layout.home, null);
                viewHolder = new ViewHolder();
                viewHolder.img_icon = (ImageView) convertView.findViewById(R.id.icon);
                viewHolder.news_title = (TextView) convertView.findViewById(R.id.Title);
                convertView.setTag(viewHolder);
            } else {

                viewHolder = (ViewHolder) convertView.getTag();
            }
            String image_url = NewsData.get(position).getIcon_URL();
            Picasso.with(MainActivity.this).load(image_url).into(viewHolder.img_icon);
            NEWS news = NewsData.get(position);
            viewHolder.news_title.setText(news.getTitle());
            return convertView;
        }

        class ViewHolder {
            ImageView img_icon;
            TextView news_title;
        }
    }

    public boolean onCreateOptionMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar, menu);
        return true;
    }

    public boolean onOptionItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.login:
                Toast.makeText(this, "点击会跳转至知乎登陆界面", Toast.LENGTH_SHORT).show();
                break;
            case R.id.setting:
                Toast.makeText(this, "点击会跳转至设置界面", Toast.LENGTH_SHORT).show();
                break;
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                break;
            default:
        }
        return true;
    }

        public  String convertStream(InputStream is) {  /*转换成string字符流*/
            String result = "";
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] bt = new byte[1024];
            int len = 0;
            try {
                while ((len = is.read(bt)) != -1) {
                    bos.write(bt, 0, len);
                    bos.flush();
                }
                result = new String(bos.toByteArray(), "utf-8");
                result = bos.toString();
                bos.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return result;
        }

        public ArrayList<NEWS> getNetNews(Context context, String urlString) {    /*获取并解析数据*/
            NEWS news = new NEWS();
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
                String response = convertStream(in);
                JSONObject root_json = new JSONObject(response);
                JSONArray jsonArray = root_json.getJSONArray("stories");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject news_json = jsonArray.getJSONObject(i);
                    news.setTitle(news_json.getString("title"));
                    JSONArray imagesArray = news_json.getJSONArray("images");
                    news.setIcon_URL(imagesArray.getString(0));
                    listNews.add(news);
                }
                in.close();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (connection != null) {
                        connection.disconnect();
                    }
                }
            }
            return listNews;
        }

    public class NEWS {
        private String title;
        private String  icon_URL;

        public String getTitle() {
            return title;
        }
        public void setTitle(String title) {
            this.title = title;
        }
        public String getIcon_URL() {
            return icon_URL;
        }
        public void setIcon_URL(String  icon_URL) {
            this.icon_URL = icon_URL;
        }
    }

}