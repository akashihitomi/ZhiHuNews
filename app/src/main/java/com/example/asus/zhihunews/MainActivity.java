package com.example.asus.zhihunews;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.os.Message;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;



public class MainActivity extends AppCompatActivity {
    public DrawerLayout mDrawerLayout;
    public NavigationView navigationView;
    private NewDBHelper newDBHelper;
    public static final String NAME_KEY = "ID";
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
            //Log.d("MainActivity", "adapter success and list.size = "+listNewsBean.size());
            listView.setAdapter(newsAdapter);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = (ListView) findViewById(R.id.listview);  //今日新闻列表
        adapter = new ArrayAdapter<String>(MyApplication.getContext(), android.R.layout.simple_list_item_1, menudata);  //左菜单
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.draw_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        /*if(navigationView.getHeaderCount() > 0) {
            View headerView = navigationView.getHeaderView(0);
            Log.d("head", "headerview is " + headerView.isShown());
            Button collection = (Button) headerView.findViewById(R.id.collection1);
            collection.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View headView) {
                    Intent intent = new Intent(MainActivity.this, CollActivity.class);
                    startActivity(intent);
                }
            });
        }*/

        ActionBar actionBar = getSupportActionBar();  //菜单按钮
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.menu);
        }
        navigationView.setCheckedItem(R.id.nav_first);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener(){
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                Intent intent = new Intent(MainActivity.this, CollActivity.class);
                startActivity(intent);
                return true;
            }
        });

        System.out.println("MainActivity:" + newsAdapter);
        new Thread(new Runnable() {
            @Override
            public void run() {
               MyApplication.getContext().deleteDatabase(newDBHelper.DBNAME);
                Log.d("delete", "the DB has been deleted");   //删除数据库
                ArrayList<NEWS> listNewsBean = getNetNews.getNews(MyApplication.getContext(), API);
                Message message = Message.obtain();
                message.obj = listNewsBean;
                mHandler.sendMessage(message);
            }
        }).start();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(MyApplication.getContext(), listNewsBean.get(position).getID(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MyApplication.getContext(),NewsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString(NAME_KEY,listNewsBean.get(position).getID());
                intent.putExtras(bundle);
                Log.d("MainActivity", "bundle data is " + bundle);
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
            case R.id.login:
                Toast.makeText(MyApplication.getContext(), "会跳转至消息界面...或者登陆界面", Toast.LENGTH_SHORT).show();
                break;
            case R.id.setting:
                Toast.makeText(MyApplication.getContext(), "会跳转至设置界面0-0但是我还没做出来", Toast.LENGTH_SHORT).show();
                break;
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                break;
            default:
        }
        return true;
    }

}