package com.example.asus.zhihunews;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;

public class Collshow extends AppCompatActivity {
    private WebView Collshow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collshow);
        Intent intent = getIntent();
        Bundle bundle =intent.getExtras();  //MainActivity传递ID
        String ID = bundle.getString(MainActivity.NAME_KEY);
        Collshow = (WebView) findViewById(R.id.Collview);
        Collshow.loadUrl("http://daily.zhihu.com/story/" + ID);
    }
}
