package com.yanlei.mooclike;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.yanlei.Utils.HttpUtil;

public class NewsActivity extends AppCompatActivity {
private WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        String newsUrl = getIntent().getStringExtra(HttpUtil.NEWS_URL);
        webView = (WebView) findViewById(R.id.webView);
        Log.i("=====",newsUrl);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl(newsUrl);
    }
}
