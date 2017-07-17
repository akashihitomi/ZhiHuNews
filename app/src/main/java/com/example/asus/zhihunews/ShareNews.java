package com.example.asus.zhihunews;

import android.content.Intent;
import android.view.View;

public class ShareNews {
    public void shareText() {
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TEXT, "http://daily.zhihu.com/story/");
        shareIntent.setType("text/plain");
        MyApplication.getContext().startActivity(Intent.createChooser(shareIntent, "分享到"));
    }
}
