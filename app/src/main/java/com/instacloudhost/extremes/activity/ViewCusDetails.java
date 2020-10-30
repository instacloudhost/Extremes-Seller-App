package com.instacloudhost.extremes.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.instacloudhost.extremes.R;
import com.instacloudhost.extremes.adapter.CustomerViewAdapter;
import com.instacloudhost.extremes.model.Mgraph;
import com.instacloudhost.extremes.remote.APIService;
import com.instacloudhost.extremes.remote.RetrofitClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ViewCusDetails extends AppCompatActivity {

    ArrayList<String> personNames = new ArrayList<>();
    ArrayList<String> emailIds = new ArrayList<>();
    ArrayList<String> mobileNumbers = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_cus_details);


        Retrofit retrofit = RetrofitClient.getRetrofitClient();
        APIService apiservice = retrofit.create(APIService.class);
        Call call = apiservice.viewCustomer();
        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                if (response.body() != null) {
                    try {
                        JSONObject obj = new JSONObject(response.body().toString());
                        JSONArray customers = obj.getJSONArray("data");
                        for (int i = 0; i < customers.length(); i++) {
                            JSONObject userDetail = customers.getJSONObject(i);
                            personNames.add(userDetail.getString("customer_name"));
                            emailIds.add(userDetail.getString("email"));
                            mobileNumbers.add(userDetail.getString("mobile"));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                Toast.makeText(getApplicationContext(),"Call Fail",Toast.LENGTH_LONG).show();
                System.out.println(t.getMessage());
            }
        });

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        // set a LinearLayoutManager with default vertical orientation
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        CustomerViewAdapter customAdapter = new CustomerViewAdapter(ViewCusDetails.this, personNames, emailIds, mobileNumbers);
        recyclerView.setAdapter(customAdapter); // set the Adapter to RecyclerView
    }
}