package com.instacloudhost.extremes;


import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.instacloudhost.extremes.model.Mlogin;
import com.instacloudhost.extremes.remote.APIService;
import com.instacloudhost.extremes.remote.RetrofitClient;
import com.instacloudhost.extremes.sections.WindsForm;

import java.util.List;

import fr.ganfra.materialspinner.MaterialSpinner;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP;

public class MainActivity extends AppCompatActivity {

    private SharedPreferences token;
    private String extremes = "extremeStorage", type;
    private ProgressDialog progressDialog;
    private EditText username,password;
    private TextView mText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        token = getSharedPreferences(extremes,
                Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = token.edit();
//        editor.putString("token", "112379");
//        editor.commit();
        if (token.contains("token")) {
            Intent dashboard = new Intent(getBaseContext(), DashBoard.class);
            startActivity(dashboard);
            finish();
        }

        setContentView(R.layout.activity_main);

        username   = (EditText)findViewById(R.id.username);
        password   = (EditText)findViewById(R.id.password);
        mText = (TextView)findViewById(R.id.textView1);
        Button button = (Button)findViewById(R.id.login);

        String[] ITEMS = {"FSE", "Agent"};
        ArrayAdapter<String> distAdapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_spinner_item, ITEMS);
        distAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        MaterialSpinner spinner = (MaterialSpinner) findViewById(R.id.userType);
        spinner.setAdapter(distAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                type = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // Login Button
        button.setOnClickListener(logIn);
        permit();

    }

    private View.OnClickListener logIn = new View.OnClickListener() {
        public void onClick(View v) {
            progressBar();
            String user = username.getText().toString().trim();
            String pass = password.getText().toString().trim();
            if (!TextUtils.isEmpty(user) && !TextUtils.isEmpty(pass) && !TextUtils.isEmpty(type)){
                checkUser(user, pass, type);
            }else{
                mText.setText("All fields are required.");
                progressDialog.cancel();
            }
        }
    };

    private void checkUser(String user, String pass, String tps) {
        Retrofit retrofit = RetrofitClient.getRetrofitClient();
        APIService apiservice = retrofit.create(APIService.class);
        Call call = apiservice.checkUser(user, pass, tps);
        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                if (response.body() != null) {
                    Mlogin mlogin = (Mlogin) response.body();
                    if(mlogin.getStatus().equals("true")) {
                        SharedPreferences.Editor editor = token.edit();
                        editor.putString("token", mlogin.getToken());
                        editor.putString("user_type", type);
                        editor.commit();
                        progressDialog.cancel();
                        Intent dashboard = new Intent(getBaseContext(), DashBoard.class);
                        startActivity(dashboard);
//                        finish();
                    }else{
                        mText.setText("Username or Password is Wrong.");
                        progressDialog.cancel();
                    }
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                Toast.makeText(getApplicationContext(),t.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
    }

    private void progressBar() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Authenticating");
        progressDialog.setMessage("Loading...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMax(50);
        progressDialog.show();
        progressDialog.setCancelable(false);
        new Thread(new Runnable() {
            public void run() {
                try {
                    Thread.sleep(10000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                progressDialog.dismiss();
            }
        }).start();
    }

    public void permit() {
        PermissionListener permissionListener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
//                Toast.makeText(MainActivity.this,"Permissions Granted", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onPermissionDenied(List<String> deniedPermissions) {
                Toast.makeText(MainActivity.this,"Permissions Not Granted", Toast.LENGTH_LONG).show();
            }
        };
        TedPermission.with(MainActivity.this)
                .setPermissionListener(permissionListener)
                .setPermissions(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                ).check();
    }
}
