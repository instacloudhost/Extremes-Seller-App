package com.instacloudhost.extremes;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.instacloudhost.extremes.adapter.CustomerButtonAdapter;
import com.instacloudhost.extremes.model.CustomerButtonModel;
import com.instacloudhost.extremes.pages.WindsPageActivity;
import com.instacloudhost.extremes.remote.APIService;
import com.instacloudhost.extremes.remote.RetrofitClient;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ViewCustomer extends AppCompatActivity {

    private SharedPreferences token;
    private String extremes = "extremeStorage";
    private String tokenid, userType;
    private String tk;


    // for buttons Recycler View
    ArrayList<CustomerButtonModel> mCustomerButtonModel = new ArrayList<>();
    private CustomerButtonAdapter mCustomerButtonAdapter;
    private RecyclerView mCustomerButtonRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_customer);
        token = getSharedPreferences(extremes,
                Context.MODE_PRIVATE);
        tk = token.getString("token", "");

        mCustomerButtonRecyclerView = (RecyclerView) findViewById(R.id.recycler_customer_button);
        mCustomerButtonRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        getDetailResponse();
    }

    private void getDetailResponse() {
        Retrofit retrofit = RetrofitClient.getRetrofitClient();
        APIService apiservice = retrofit.create(APIService.class);
        Call call = apiservice.ListCustomerButton(token.getString("token", ""), token
                .getString("agent_type", ""), token
                .getString("category", ""), token
                .getString("title", ""));

        call.enqueue(new Callback<List<CustomerButtonModel>>() {
            @Override
            public void onResponse(Call<List<CustomerButtonModel>> call, Response<List<CustomerButtonModel>> response) {
                mCustomerButtonModel = new ArrayList<>(response.body());
                mCustomerButtonAdapter = new CustomerButtonAdapter(ViewCustomer.this, mCustomerButtonModel);
                mCustomerButtonRecyclerView.setAdapter(mCustomerButtonAdapter);
            }

            @Override
            public void onFailure(Call<List<CustomerButtonModel>> call, Throwable t) {
                Toast.makeText(ViewCustomer.this, "Failure" + t, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void clk_Test(View view) {
        Intent intent = new Intent(ViewCustomer.this, WindsPageActivity.class);
        startActivity(intent);
        finish();
    }
}
