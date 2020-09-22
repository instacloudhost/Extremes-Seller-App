package com.instacloudhost.extremes;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.instacloudhost.extremes.webviews.CustomerWebView;

import androidx.appcompat.app.AppCompatActivity;

public class ViewCustomer extends AppCompatActivity {

    private SharedPreferences token;
    private String extremes = "extremeStorage";
    private GridView gridView;
    private TextView textView;
    private String tk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_customer);
        token = getSharedPreferences(extremes,
                Context.MODE_PRIVATE);
        tk = token.getString("token", "");

        String[] btnViews = {
                "SmartLife Customers",
                "Pinelabs Customers",
                "Winds Customers",
                "PhonePe Customers"
        };

        gridView = (GridView)findViewById(R.id.gridView);
        textView = (TextView)findViewById(R.id.btn);
        final ArrayAdapter adapter = new ArrayAdapter(this,
                R.layout.customer_grid_layout, R.id.btn, btnViews);

        gridView.setAdapter(adapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                // TODO Auto-generated method stub
                loadWebView(adapter.getItemId(position));
            }
        });
    }

    private void loadWebView(Long id) {
        Intent i = new Intent(ViewCustomer.this, CustomerWebView.class);
        switch (Long.toString(id)){
            case "0":
                i.putExtra(CustomerWebView.WEBSITE_ADDRESS, "https://extremes.in/api/view_customer/");
                i.putExtra(CustomerWebView.TOKEN, tk);
                i.putExtra(CustomerWebView.CATEGORY, "smartlife");
                startActivity(i);
                break;
            case "1":
                i.putExtra(CustomerWebView.WEBSITE_ADDRESS, "https://extremes.in/api/view_customer/");
                i.putExtra(CustomerWebView.TOKEN, tk);
                i.putExtra(CustomerWebView.CATEGORY, "pinelabs");
                startActivity(i);
                break;
            case "2":
                i.putExtra(CustomerWebView.WEBSITE_ADDRESS, "https://extremes.in/api/view_customer/");
                i.putExtra(CustomerWebView.TOKEN, tk);
                i.putExtra(CustomerWebView.CATEGORY, "winds");
                startActivity(i);
                break;
            case "3":
                i.putExtra(CustomerWebView.WEBSITE_ADDRESS, "https://extremes.in/api/view_customer/");
                i.putExtra(CustomerWebView.TOKEN, tk);
                i.putExtra(CustomerWebView.CATEGORY, "phonepe");
                startActivity(i);
                break;
        }
    }

}
