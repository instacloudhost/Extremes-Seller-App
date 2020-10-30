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
    public static final String CAT = "CATC";
    public static final String USER_TYPE = "USER";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String url  = getIntent().getStringExtra(WEBSITE_ADDRESS);
        String cat  = getIntent().getStringExtra(CATEGORY);
        String tok  = getIntent().getStringExtra(TOKEN);
        String cat_cat  = getIntent().getStringExtra(CAT);
        String user_type  = getIntent().getStringExtra(USER_TYPE);
        url = url+"?token="+tok+"&category="+cat+"&cat="+cat_cat+"&user_type="+user_type;
        System.out.println(url);

        setContentView(R.layout.activity_customer_web_view);

        WebView webView = (WebView) findViewById(R.id.cwv);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl(url);
    }
}