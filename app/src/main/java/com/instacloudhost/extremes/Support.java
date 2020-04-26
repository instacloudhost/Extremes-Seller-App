package com.instacloudhost.extremes;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebView;

public class Support extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_support);

        WebView webView = (WebView) findViewById(R.id.webview);
        webView.loadUrl("https://extremes.in/home/app_support");
    }
}
