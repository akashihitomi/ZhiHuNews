package com.example.asus.zhihunews;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;

public class NewsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webview);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        Bundle bundle = this.getIntent().getExtras();
        String ID = bundle.getString("ID");
        Log.d("the ID is",ID);
        WebView webView = (WebView) findViewById(R.id.Webview);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        webView.loadUrl("http://daily.zhihu.com/story/" + ID);
    }
}
