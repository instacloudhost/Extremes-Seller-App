package com.instacloudhost.extremes.sections;

import android.app.DatePickerDialog;

import java.util.ArrayList;
import java.util.Calendar;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import fr.ganfra.materialspinner.MaterialSpinner;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.google.gson.Gson;
import com.instacloudhost.extremes.R;
import com.instacloudhost.extremes.model.MStatus;
import com.instacloudhost.extremes.remote.APIService;
import com.instacloudhost.extremes.remote.RetrofitClient;

import static com.basgeekball.awesomevalidation.ValidationStyle.UNDERLABEL;

public class WindsForm extends AppCompatActivity {

    private ProgressDialog progressDialog;
    private SharedPreferences token;
    private DatePickerDialog datepicker;
    private String iCategory;
    private String extremes = "extremeStorage";
    EditText dob;
    private EditText t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17;
    private Spinner s1, s2, s3, s4;
    private AwesomeValidation mAwesomeValidation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        iCategory = intent.getStringExtra("category");
        setTitle(intent.getStringExtra("title"));
        token = getSharedPreferences(extremes,
                Context.MODE_PRIVATE);

        setContentView(R.layout.activity_winds_form);

        mAwesomeValidation = new AwesomeValidation(UNDERLABEL);
        mAwesomeValidation.setContext(this);
        mAwesomeValidation.addValidation(this, R.id.shopName, "[a-zA-Z\\s]+", R.string.err_name);
        mAwesomeValidation.addValidation(this, R.id.customerName, "[a-zA-Z\\s]+", R.string.err_customer_name);
        mAwesomeValidation.addValidation(this, R.id.pan, "^[a-zA-Z0-9]{10}$", R.string.err_pan);
        mAwesomeValidation.addValidation(this, R.id.mobile, "^[0-9]{10}$", R.string.err_mobile);
        mAwesomeValidation.addValidation(this, R.id.pin, "^[0-9]{6}$", R.string.err_pin);
        mAwesomeValidation.addValidation(this, R.id.email, "^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$", R.string.err_email);

        state();
        relation();

        String[] ITEMS = {"Andhra Pradesh", "Arunachal Pradesh", "Assam", "Bihar", "Chhattisgarh", "Goa"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(WindsForm.this, android.R.layout.simple_spinner_item, ITEMS);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        MaterialSpinner spinner = (MaterialSpinner) findViewById(R.id.district);
        spinner.setAdapter(adapter);

        String[] IT = {"Male", "Female"};
        ArrayAdapter<String> ad = new ArrayAdapter<String>(WindsForm.this, android.R.layout.simple_spinner_item, IT);
        ad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        MaterialSpinner sp = (MaterialSpinner) findViewById(R.id.gender);
        sp.setAdapter(ad);

        t1 = (EditText) findViewById(R.id.shopName);
        t2 = (EditText) findViewById(R.id.aoro);
        t3 = (EditText) findViewById(R.id.pan);
        t4 = (EditText) findViewById(R.id.mobile);
        t5 = (EditText) findViewById(R.id.email);
        t6 = (EditText) findViewById(R.id.ahn);
        t7 = (EditText) findViewById(R.id.bankname);
        t8 = (EditText) findViewById(R.id.baddress);
        t9 = (EditText) findViewById(R.id.accountNo);
        t10 = (EditText) findViewById(R.id.ifsc);
        t11 = (EditText) findViewById(R.id.permanetAddress);
        t12 = (EditText) findViewById(R.id.dob);
        t13 = (EditText) findViewById(R.id.pin);
        t14 = (EditText) findViewById(R.id.fhname);
        t15 = (EditText) findViewById(R.id.vidano);
        t16 = (EditText) findViewById(R.id.nominee);
        t17 = (EditText) findViewById(R.id.customerName);
        s1 = (Spinner) findViewById(R.id.state);
        s2 = (Spinner) findViewById(R.id.district);
        s3 = (Spinner) findViewById(R.id.gender);
        s4 = (Spinner) findViewById(R.id.relation);


        dob=(EditText) findViewById(R.id.dob);
        dob.setInputType(InputType.TYPE_NULL);
        dob.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    final Calendar cldr = Calendar.getInstance();
                    int day = cldr.get(Calendar.DAY_OF_MONTH);
                    int month = cldr.get(Calendar.MONTH);
                    int year = cldr.get(Calendar.YEAR);
                    // date picker dialog
                    datepicker = new DatePickerDialog(WindsForm.this,
                            new DatePickerDialog.OnDateSetListener() {
                                @Override
                                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                    dob.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                                }
                            }, year, month, day);
                    datepicker.show();
                }
            }
        });
    }

    public void submitForm(View v) {
        mAwesomeValidation.clear();
        if(mAwesomeValidation.validate()) {
//            System.out.println(t1.getText().toString());
//            System.out.println(t2.getText().toString());
//            System.out.println(t3.getText().toString());
//            System.out.println(t4.getText().toString());
//            System.out.println(t5.getText().toString());
//            System.out.println(t6.getText().toString());
//            System.out.println(t7.getText().toString());
//            System.out.println(t8.getText().toString());
//            System.out.println(t9.getText().toString());
//            System.out.println(t10.getText().toString());
//            System.out.println(t11.getText().toString());
//            System.out.println(t12.getText().toString());
//            System.out.println(t13.getText().toString());
//            System.out.println(t14.getText().toString());
//            System.out.println(t15.getText().toString());
//            System.out.println(t16.getText().toString());
//            System.out.println(t17.getText().toString());
//            System.out.println(s1.getSelectedItem().toString());
//            System.out.println(s2.getSelectedItem().toString());
//            System.out.println(s3.getSelectedItem().toString());
//            System.out.println(s4.getSelectedItem().toString());
//            return;
            progressBar();
            Retrofit retrofit = RetrofitClient.getRetrofitClient();
            APIService apiservice = retrofit.create(APIService.class);
            Call call = apiservice.addWinds(t1.getText().toString(), t2.getText().toString(), t3.getText().toString(),
                    t4.getText().toString(), t5.getText().toString(), t6.getText().toString(), t7.getText().toString(),
                    t8.getText().toString(), t9.getText().toString(), t10.getText().toString(), t11.getText().toString(),
                    t12.getText().toString(), t13.getText().toString(), t14.getText().toString(), t15.getText().toString(),
                    t16.getText().toString(), t17.getText().toString(), s1.getSelectedItem().toString(), s2.getSelectedItem().toString(),
                    s3.getSelectedItem().toString(), s4.getSelectedItem().toString(), token.getString("token", ""));
            call.enqueue(new Callback() {
                @Override
                public void onResponse(Call call, Response response) {
                    if (response.body() != null) {
                        MStatus mwindform = (MStatus) response.body();
                        progressDialog.dismiss();
//                    System.out.println(mwindform);
                        if(mwindform.getStatus().equals("true")) {
                            Intent main = new Intent(getBaseContext(), WindsUploads.class);
                            main.putExtra("customer_id", mwindform.getMessage());
                            startActivity(main);
                            finish();
                        }else{
                            Toast.makeText(getApplicationContext(),mwindform.getMessage(),Toast.LENGTH_LONG).show();
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

    private void progressBar() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Adding Customer");
        progressDialog.setMessage("Uploading...");
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

    private void state() {
        String[] ITEMS = {"Andhra Pradesh", "Arunachal Pradesh", "Assam", "Bihar", "Chhattisgarh", "Goa",
                "Gujarat", "Haryana", "Himachal Pradesh", "Jammu and Kashmir", "Jharkhand", "Karnataka", "Kerala", "Madhya Pradesh",
                "Maharashtra", "Manipur", "Meghalaya", "Mizoram", "Nagaland", "Odisha", "Punjab", "Rajasthan", "Sikkim", "Tamil Nadu",
                "Telangana", "Tripura", "Uttarakhand", "Uttar Pradesh", "West Bengal", "Andaman and Nicobar Islands", "Chandigarh", "Dadra and Nagar Haveli",
                "Daman and Diu", "Delhi", "Lakshadweep", "Puducherry"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(WindsForm.this, android.R.layout.simple_spinner_item, ITEMS);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        MaterialSpinner spinner = (MaterialSpinner) findViewById(R.id.state);
        spinner.setAdapter(adapter);
    }

    private void relation() {
        String[] IT = {"Father", "Mother", "Son", "Daughter", "Brother", "Sister", "Husband", "Wife"};
        ArrayAdapter<String> ad = new ArrayAdapter<String>(WindsForm.this, android.R.layout.simple_spinner_item, IT);
        ad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        MaterialSpinner sp = (MaterialSpinner) findViewById(R.id.relation);
        sp.setAdapter(ad);
    }

}