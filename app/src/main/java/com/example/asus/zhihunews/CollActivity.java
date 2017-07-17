package com.example.asus.zhihunews;

import android.content.Intent;
import android.os.Message;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import static android.R.id.message;
import static com.example.asus.zhihunews.MainActivity.NAME_KEY;

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
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.menu);
        }

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

        colllistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(CollActivity.this,NewsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString(NAME_KEY,listCollNews.get(position).getID());
                intent.putExtras(bundle);
                MyApplication.getContext().startActivity(intent);
            }
        });

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(CollActivity.this,MainActivity.class);
                MyApplication.getContext().startActivity(intent);
                break;
            default:
        }
        return true;
    }

}
