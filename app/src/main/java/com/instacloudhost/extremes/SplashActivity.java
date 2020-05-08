package com.instacloudhost.extremes;

import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.instacloudhost.extremes.remote.APIService;
import com.instacloudhost.extremes.remote.RetrofitClient;

public class SplashActivity extends AppCompatActivity {

    private int SPLASH_TIME_OUT = 1000;
    private String currentAppVersionCode;
    private String checkUpdate = "false";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        PackageInfo pInfo = null;
        try {
            pInfo = this.getPackageManager().getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        currentAppVersionCode = String.valueOf(pInfo.versionCode);
        Log.d("version:", currentAppVersionCode);
        checkVersion();
    }

    private void checkVersion() {
        Retrofit retrofit = RetrofitClient.getRetrofitClient();
        APIService apiservice = retrofit.create(APIService.class);
        Call call = apiservice.check_version();
        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                if (response.body() != null) {
                    if(!currentAppVersionCode.equals(String.valueOf(response.body()))) {
                        Intent main = new Intent(getBaseContext(), UpdateChecker.class);
                        startActivity(main);
                        finish();
                    }else {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Intent main = new Intent(SplashActivity.this,MainActivity.class);
                                startActivity(main);
                                finish();
                            }
                        },SPLASH_TIME_OUT);
                    }
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                Toast.makeText(getApplicationContext(),t.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
    }
}
