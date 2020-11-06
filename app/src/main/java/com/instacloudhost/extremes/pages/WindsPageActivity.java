package com.instacloudhost.extremes.pages;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import com.instacloudhost.extremes.R;
import com.instacloudhost.extremes.ViewCustomer;
import com.instacloudhost.extremes.adapter.CustomerButtonAdapter;
import com.instacloudhost.extremes.adapter.TestAdapter;
import com.instacloudhost.extremes.model.CustomerButtonModel;
import com.instacloudhost.extremes.model.Message;
import com.instacloudhost.extremes.model.TestModel;
import com.instacloudhost.extremes.remote.APIService;
import com.instacloudhost.extremes.remote.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class WindsPageActivity extends AppCompatActivity {
    //ArrayList<TestModel> mTestModel = new ArrayList<>();
    Intent intent = getIntent();
    private TestAdapter mTestAdapter;
    private RecyclerView mTestRecyclerView;
    private String tokenid, userType;
    private String tk, category;
    private SharedPreferences token;
    private String extremes = "extremeStorage";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        setContentView(R.layout.activity_winds_page);
        token = getSharedPreferences(extremes,
                Context.MODE_PRIVATE);
        tk = token.getString("token", "");
        category =intent.getStringExtra("category");

        mTestRecyclerView = (RecyclerView) findViewById(R.id.recycler_test);
        mTestRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        getDetailResponse();
    }

    private void getDetailResponse() {

        Retrofit retrofit = RetrofitClient.getRetrofitClient();
        APIService apiservice = retrofit.create(APIService.class);
        Call call = apiservice.Test(token.getString("token", ""), token
                .getString("user_type", ""), category);

        call.enqueue(new Callback<TestModel>() {

            @Override
            public void onResponse(Call<TestModel> call, Response<TestModel> response) {
                if(response.isSuccessful()){
                    if(response.body().getStatus().equals("true")) {
                        List<Message> message = response.body().getMessage();
                        for (int i = 0; i < message.size(); i++) {
                            mTestAdapter = new TestAdapter((ArrayList<Message>) message);
                            mTestRecyclerView.setAdapter(mTestAdapter);
                            mTestAdapter.notifyDataSetChanged();
                        }
                    }
                    else {
                        Toast.makeText(WindsPageActivity.this, "No Data Found", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<TestModel> call, Throwable t) {
                Toast.makeText(WindsPageActivity.this, "Failure " + t, Toast.LENGTH_SHORT).show();
            }
        });
    }
}