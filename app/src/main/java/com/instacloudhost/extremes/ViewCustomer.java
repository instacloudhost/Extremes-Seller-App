package com.instacloudhost.extremes;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.webkit.WebView;

import androidx.appcompat.app.AppCompatActivity;

public class ViewCustomer extends AppCompatActivity {

    private SharedPreferences token;
    private String extremes = "extremeStorage";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_customer);

        token = getSharedPreferences(extremes,
                Context.MODE_PRIVATE);
        String tk = token.getString("token", "");
        WebView webView = (WebView) findViewById(R.id.webview);
        webView.loadUrl("https://extremes.in/api/app_view_customer/"+tk);
    }

}
