package com.instacloudhost.extremes.webviews;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.instacloudhost.extremes.R;

public class CustomerWebView extends AppCompatActivity {
    public static final String WEBSITE_ADDRESS = "website_address";
    public static final String CATEGORY = "CAT";
    public static final String TOKEN = "CATf";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String url  = getIntent().getStringExtra(WEBSITE_ADDRESS);
        String cat  = getIntent().getStringExtra(CATEGORY);
        String tok  = getIntent().getStringExtra(TOKEN);
        url = url+"?token="+tok+"&category="+cat;
        System.out.println(url);

        setContentView(R.layout.activity_customer_web_view);

        WebView webView = (WebView) findViewById(R.id.cwv);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl(url);
    }
}