package com.instacloudhost.extremes.pages;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import com.instacloudhost.extremes.R;
import com.instacloudhost.extremes.adapter.ViewAdapter;
import com.instacloudhost.extremes.model.Message;
import com.instacloudhost.extremes.model.ViewModel;
import com.instacloudhost.extremes.remote.APIService;
import com.instacloudhost.extremes.remote.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class CustomersViewPageActivity extends AppCompatActivity {

    Intent intent = getIntent();
    private ViewAdapter mViewAdapter;
    private RecyclerView mViewRecyclerView;
    private String tokenid, userType;
    private String tk, category;
    private SharedPreferences token;
    private String extremes = "extremeStorage";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        setContentView(R.layout.activity_customers_view_page);
        token = getSharedPreferences(extremes,
                Context.MODE_PRIVATE);
        tk = token.getString("token", "");
        category =intent.getStringExtra("category");

        mViewRecyclerView = (RecyclerView) findViewById(R.id.recycler_view_customers);
        mViewRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        getViewResponse();
    }

    private void getViewResponse() {

        Retrofit retrofit = RetrofitClient.getRetrofitClient();
        APIService apiservice = retrofit.create(APIService.class);
        Call call = apiservice.View(token.getString("token", ""), token
                .getString("user_type", ""), category);

        call.enqueue(new Callback<ViewModel>() {

            @Override
            public void onResponse(Call<ViewModel> call, Response<ViewModel> response) {
                if(response.isSuccessful()){
                    if(response.body().getStatus().equals("true")) {
                        List<Message> message = response.body().getMessage();
                        for (int i = 0; i < message.size(); i++) {
                            mViewAdapter = new ViewAdapter((ArrayList<Message>) message);
                            mViewRecyclerView.setAdapter(mViewAdapter);
                            mViewAdapter.notifyDataSetChanged();
                        }
                    }
                    else {
                        Toast.makeText(CustomersViewPageActivity.this, "No Data Found", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ViewModel> call, Throwable t) {
                Toast.makeText(CustomersViewPageActivity.this, "Failure " + t, Toast.LENGTH_SHORT).show();
            }
        });
    }
}