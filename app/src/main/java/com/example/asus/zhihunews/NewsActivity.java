package com.example.asus.zhihunews;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.Toast;

import static android.os.Build.ID;
import static com.example.asus.zhihunews.R.menu.news_toolbar;
import static com.example.asus.zhihunews.getNetNews.getCollNews;

public class NewsActivity extends AppCompatActivity {

    private static final String newsUrl = "https://news-at.zhihu.com/api/4/news/";
    private boolean ShareOrNot = false ;

    public String getIDl() {
        Intent intent = getIntent();
        Bundle bundle =intent.getExtras();  //MainActivity传递ID
        String ID = bundle.getString(MainActivity.NAME_KEY);
        Log.d("NewsActivity", "data from MainActivity is " + ID);
        return ID;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webview);
        Toolbar news_toolbar = (Toolbar) findViewById(R.id.News_toolbar); //新闻详细内容toolbar
        setSupportActionBar(news_toolbar);
        WebView webView = (WebView) findViewById(R.id.Webview);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        news_toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        webView.loadUrl("http://daily.zhihu.com/story/" + getIDl());
    }

     public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(news_toolbar,menu);
    return  true;
}

@Override
    public boolean onOptionsItemSelected(MenuItem item) {
            switch (item.getItemId()) {
                case R.id.collect:
                    if(item.getTitle().equals("收藏")) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                NewsDBUtils newsDBUtils = new NewsDBUtils(MyApplication.getContext());
                                String NewsUrl = newsUrl + getIDl();
                                Log.d("URL", "getTD =  " + getIDl());
                                Log.d("URL", "NewsUrl =  " + NewsUrl);
                                NEWS new1 = getNetNews.getCollNews(NewsUrl); //返回需要收藏的新闻对象
                                newsDBUtils.Insertnews(new1);  //存储数据至数据库
                                Log.d("collection", "save ID is  " + new1.getID());
                                Log.d("collection", "save title is  " + new1.getTitle());
                                Log.d("collection", "save imageUrl is  " + new1.getIcon_URL());
                            }
                        }).start();
                        item.setIcon(R.drawable.collected);
                        item.setTitle("取消收藏");
                        Toast.makeText(MyApplication.getContext(), "收藏成功了哟(*^__^*) ", Toast.LENGTH_SHORT).show();
                        break;
                    } else {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                               NewsDBUtils newsDBUtils = new NewsDBUtils(MyApplication.getContext());
                                Log.d("SQL", "the ID to delete is " + getIDl());
                                newsDBUtils.Deletenews(getIDl());
                            }
                        }).start();
                        item.setIcon(R.drawable.collect);
                        item.setTitle("收藏");
                        Toast.makeText(this,"竟然取消我？！...好吧收藏已取消", Toast.LENGTH_SHORT).show();
                        break;
                    }

                case R.id.share:
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            ShareNews sharenews = new ShareNews();
                            sharenews.shareText();
                        }
                    }).start();
                    Toast.makeText(MyApplication.getContext(),"系统分享",Toast.LENGTH_SHORT).show();
                    break;
                default:
            }
            return true;
        }

}
