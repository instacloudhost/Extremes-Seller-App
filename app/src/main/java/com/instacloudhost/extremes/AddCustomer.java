package com.instacloudhost.extremes;

import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.content.CursorLoader;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.instacloudhost.extremes.model.MStatus;
import com.instacloudhost.extremes.remote.APIService;
import com.instacloudhost.extremes.remote.RetrofitClient;

import java.io.File;

public class AddCustomer extends AppCompatActivity {

    private SharedPreferences token;
    private ProgressDialog progressDialog;
    private TextInputLayout c1, c2, c3, c4;
    private EditText customer_name, mobile, bn, upi, ac, crn;
    private String extremes = "extremeStorage", image = "photo", mLastLocation, iCategory, custom;
    private ImageView selfie_iamge,proof_image;
    private Bitmap currentImage;
    private Uri f_image,s_image;
    private LocationManager locationManager;
    private LocationListener locationListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        iCategory = intent.getStringExtra("category");
        setTitle(intent.getStringExtra("title"));

        setContentView(R.layout.activity_add_customer);

        token = getSharedPreferences(extremes,
                Context.MODE_PRIVATE);
        Log.d("Token Customer:", token.getString("token", ""));

        c1 = (TextInputLayout) findViewById(R.id.bn);
        c2 = (TextInputLayout) findViewById(R.id.upi_id);
        c3 = (TextInputLayout) findViewById(R.id.ac_no);
        c4 = (TextInputLayout) findViewById(R.id.crn_no);

        customer_name   = (EditText)findViewById(R.id.customerName);
        mobile   = (EditText)findViewById(R.id.mobile);
        bn   = (EditText)findViewById(R.id.bn_et);
        upi   = (EditText)findViewById(R.id.upi_id_et);
        ac   = (EditText)findViewById(R.id.ac_no_et);
        crn   = (EditText)findViewById(R.id.crn_no_et);

        Button submit = (Button)findViewById(R.id.submit);
        Button photo = (Button)findViewById(R.id.photo);
        Button proof = (Button)findViewById(R.id.proof);

        selfie_iamge = (ImageView) findViewById(R.id.selfie_image);
        proof_image = (ImageView) findViewById(R.id.proof_image);

        submit.setOnClickListener(addCustomer);
        photo.setOnClickListener(getPhoto);
        proof.setOnClickListener(getProof);
        check_category_form(iCategory);
        location();
    }

    private void check_category_form(String cat) {
        switch (cat){
            case "2":
            case "3":
            case "4":
            case "5":
                c1.setVisibility(View.VISIBLE);
                c2.setVisibility(View.VISIBLE);
                break;
            case "6":
            case "7":
            case "8":
                c3.setVisibility(View.VISIBLE);
                c4.setVisibility(View.VISIBLE);
                break;
            default:
                custom = "";
        }
    }

    private void add_customer(String cn, String mb, Uri f_image, Uri s_image) {
        File file1 = new File(getRealPathFromURI(f_image));
        File file2 = new File(getRealPathFromURI(s_image));

        //creating request body for file
        RequestBody requestFile1 = RequestBody.create(MediaType.parse(getContentResolver().getType(f_image)), file1);
        MultipartBody.Part body = MultipartBody.Part.createFormData("selfie", "image1.jpg", requestFile1);
        RequestBody requestFile2 = RequestBody.create(MediaType.parse(getContentResolver().getType(s_image)), file2);
        MultipartBody.Part body2 = MultipartBody.Part.createFormData("proof", "image2.jpg", requestFile2);

        RequestBody tk = RequestBody.create(MediaType.parse("text/plain"), token.getString("token",""));
        RequestBody customer_name = RequestBody.create(MediaType.parse("text/plain"), cn);
        RequestBody mobile = RequestBody.create(MediaType.parse("text/plain"), mb);
        RequestBody location = RequestBody.create(MediaType.parse("text/plain"), ""+mLastLocation);
        RequestBody category = RequestBody.create(MediaType.parse("text/plain"), iCategory);
        RequestBody customs = RequestBody.create(MediaType.parse("text/plain"), custom);

        Retrofit retrofit = RetrofitClient.getRetrofitClient();
        APIService apiservice = retrofit.create(APIService.class);
        Call call = apiservice.addCustomer(tk, customer_name, mobile, location, category, customs, body, body2);
        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                if (response.body() != null) {
                    MStatus mstatus = (MStatus) response.body();
                    Log.d("Response: ", String.valueOf(mstatus));
                    if(mstatus.getStatus().equals("true")) {
                        Intent main = new Intent(getBaseContext(), ViewCustomer.class);
                        startActivity(main);
                    }else{
                        Toast.makeText(getApplicationContext(),"No Data Found",Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                Toast.makeText(getApplicationContext(),t.getMessage(),Toast.LENGTH_LONG).show();
                Log.d("Error: ",t.getMessage());
            }
        });
    }

    private String getRealPathFromURI(Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        CursorLoader loader = new CursorLoader(this, contentUri, proj, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String result = cursor.getString(column_index);
        cursor.close();
        return result;
    }

    private void location() {
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                mLastLocation = Double.valueOf(location.getLatitude()).toString()+","+Double.valueOf(location.getLongitude()).toString();
                if (locationManager != null) {
                    locationManager.removeUpdates(locationListener);
                    locationManager = null;
                }
            }
            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {Log.d("Latitude","status"); }
            @Override
            public void onProviderEnabled(String provider) {Log.d("Latitude","enable"); }
            @Override
            public void onProviderDisabled(String provider) {Log.d("Latitude","disable"); }
        };
        locationManager =  (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        try {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListener);
        } catch (java.lang.SecurityException ex) {
            // Log.i(TAG, "fail to request location update, ignore", ex);
        } catch (IllegalArgumentException ex) {
            // Log.d(TAG, "gps provider does not exist " + ex.getMessage());
        }
    }

    private View.OnClickListener addCustomer = new View.OnClickListener() {
        public void onClick(View v) {
            progressBar();
            String cn = customer_name.getText().toString().trim();
            String mb = mobile.getText().toString().trim();
            if(iCategory.equals("2") || iCategory.equals("3") || iCategory.equals("4") || iCategory.equals("5")) {
                custom = "{'bn':'"+bn.getText().toString().trim()+"','upi':'"+upi.getText().toString().trim()+"'}";
            }else if(iCategory.equals("6") || iCategory.equals("7") || iCategory.equals("8")) {
                custom = "{'ac':'"+(String) ac.getText().toString().trim()+"','crn':'"+(String) crn.getText().toString().trim()+"'}";
            }else {
                custom = "";
            }
            if (!TextUtils.isEmpty(cn) && !TextUtils.isEmpty(mb)){
                add_customer(cn,mb,f_image,s_image);
            }else{
                Toast.makeText(getApplicationContext(),"Something Wrong",Toast.LENGTH_LONG).show();
                progressDialog.cancel();
            }
        }
    };

    private View.OnClickListener getPhoto = new View.OnClickListener() {
        public void onClick(View v) {
            image = "photo";
            Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
            photoPickerIntent.setType("image/*");
            startActivityForResult(photoPickerIntent, 1);
        }
    };

    private View.OnClickListener getProof = new View.OnClickListener() {
        public void onClick(View v) {
            image = "proof";
            Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
            photoPickerIntent.setType("image/*");
            startActivityForResult(photoPickerIntent, 1);
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            Uri photoUri = data.getData();
//            Log.d("Photo URL:",photoUri.getPath());
            if (photoUri != null) {
                try {
                    currentImage = MediaStore.Images.Media.getBitmap(this.getContentResolver(), photoUri);
                    switch (image) {
                        case "photo":
                            selfie_iamge.setImageBitmap(currentImage);
                            f_image = photoUri;
                            break;
                        case "proof":
                            proof_image.setImageBitmap(currentImage);
                            s_image = photoUri;
                            break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
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
}
