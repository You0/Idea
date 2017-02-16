package com.duanqu.Idea.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;

import com.duanqu.Idea.R;

/**
 * Created by Me on 2017/1/26.
 */

public class AboutActivity extends AppCompatActivity {
    private WebView webView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about);
        webView = (WebView) findViewById(R.id.webView);
        webView.loadUrl("http://cn.bing.com");

    }
}
