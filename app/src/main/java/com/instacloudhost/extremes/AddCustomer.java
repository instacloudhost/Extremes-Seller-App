package com.instacloudhost.extremes;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.loader.content.CursorLoader;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.textfield.TextInputLayout;
import com.instacloudhost.extremes.activity.FrontCameraActivity;
import com.instacloudhost.extremes.adapter.DetailAdapter;
import com.instacloudhost.extremes.model.MStatus;
import com.instacloudhost.extremes.remote.APIService;
import com.instacloudhost.extremes.remote.RetrofitClient;

import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Pattern;

public class AddCustomer extends AppCompatActivity {

    private SharedPreferences token;
    private ProgressDialog progressDialog;
    private TextInputLayout c1, c2, c3, c4;
    private EditText customer_name, mobile, bn, upi, ac, crn;
    private TextView t1;
    private String extremes = "extremeStorage", image = "photo", mLastLocation, iCategory, custom, layout;
    private ImageView selfie_iamge, proof_image;
    private Bitmap currentImage;
    private String cfu1 = "false", cfu2 = "false";
    private Uri f_image, s_image;
    private File photoFile, file1, file2;
    private LocationCallback locationCallback;
    private FusedLocationProviderClient client;
    private String btn_n1, btn_n2;
    private String btn_n1_d, btn_n2_d;

    static final int CAPTURE_IMAGE_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        iCategory = intent.getStringExtra("category");
        layout = intent.getStringExtra("layout");
        setTitle(intent.getStringExtra("title"));
        btn_n1 = intent.getStringExtra("btn1");
        btn_n2 = intent.getStringExtra("btn2");
        btn_n1_d = intent.getStringExtra("btn1_d");
        btn_n2_d = intent.getStringExtra("btn2_d");


        setContentView(R.layout.activity_add_customer);

        token = getSharedPreferences(extremes,
                Context.MODE_PRIVATE);
        Log.d("Layout:", layout);

        t1 = (TextView) findViewById(R.id.errorUpdate);

        c1 = (TextInputLayout) findViewById(R.id.bn);
        c2 = (TextInputLayout) findViewById(R.id.upi_id);
        c3 = (TextInputLayout) findViewById(R.id.ac_no);
        c4 = (TextInputLayout) findViewById(R.id.crn_no);

        customer_name = (EditText) findViewById(R.id.customerName);
        mobile = (EditText) findViewById(R.id.mobile);
        bn = (EditText) findViewById(R.id.bn_et);
        upi = (EditText) findViewById(R.id.upi_id_et);
        ac = (EditText) findViewById(R.id.ac_no_et);
        crn = (EditText) findViewById(R.id.crn_no_et);

        Button submit = (Button) findViewById(R.id.submit);
        Button photo = (Button) findViewById(R.id.btn_n1);
        Button proof = (Button) findViewById(R.id.btn_n2);
        TextView txtn1 = (TextView) findViewById(R.id.txt_n1);
        TextView txtn2 = (TextView) findViewById(R.id.txt_n2);

        txtn1.setText(btn_n1_d);
        txtn2.setText(btn_n2_d);
        photo.setText(btn_n1);
        proof.setText(btn_n2);

        selfie_iamge = (ImageView) findViewById(R.id.selfie_image);
        proof_image = (ImageView) findViewById(R.id.proof_image);

        submit.setOnClickListener(addCustomer);
        photo.setOnClickListener(getPhoto);
        proof.setOnClickListener(getProof);
        check_category_form(layout);
        location();
    }

    private void check_category_form(String cat) {
        switch (cat) {
            case "layout_two":
                c3.setVisibility(View.VISIBLE);
                c4.setVisibility(View.VISIBLE);
                break;
            default:
        }
    }

    private void add_customer(String cn, String mb) {

        file1 = new File(getRealPathFromURI(f_image));
        file2 = new File(getRealPathFromURI(s_image));

        //creating request body for file
        RequestBody requestFile1 = RequestBody.create(MediaType.parse(getContentResolver().getType(f_image)), file1);
        MultipartBody.Part body = MultipartBody.Part.createFormData("selfie", "image1.jpg", requestFile1);
        RequestBody requestFile2 = RequestBody.create(MediaType.parse(getContentResolver().getType(s_image)), file2);
        MultipartBody.Part body2 = MultipartBody.Part.createFormData("proof", "image2.jpg", requestFile2);

        RequestBody tk = RequestBody.create(MediaType.parse("text/plain"), token.getString("token", ""));
        RequestBody utype = RequestBody.create(MediaType.parse("text/plain"), token.getString("user_type", ""));
        RequestBody customer_name = RequestBody.create(MediaType.parse("text/plain"), cn);
        RequestBody mobile = RequestBody.create(MediaType.parse("text/plain"), mb);
        RequestBody location = RequestBody.create(MediaType.parse("text/plain"), "" + mLastLocation);
        RequestBody category = RequestBody.create(MediaType.parse("text/plain"), iCategory);
        RequestBody customs = RequestBody.create(MediaType.parse("text/plain"), custom);

        Retrofit retrofit = RetrofitClient.getRetrofitClient();
        APIService apiservice = retrofit.create(APIService.class);
        Call call = apiservice.addCustomer(tk, utype, customer_name, mobile, location, category, customs, body, body2);
        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                if (response.body() != null) {
                    MStatus mstatus = (MStatus) response.body();
                    Log.d("Response: ", String.valueOf(mstatus.getMessage()));
                    if (mstatus.getStatus().equals("true")) {
                        client.removeLocationUpdates(locationCallback);
                        progressDialog.cancel();
                        Toast.makeText(getApplicationContext(), "Successfully Added", Toast.LENGTH_LONG).show();
                        Intent main = new Intent(getBaseContext(), DashBoard.class);
                        startActivity(main);
                        finish();
                    } else {
                        progressDialog.cancel();
                        Toast.makeText(getApplicationContext(), mstatus.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
                Log.d("Error: ", t.getMessage());
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

        LocationRequest request = new LocationRequest();
        request.setInterval(180 * 1000);
        request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        client = LocationServices.getFusedLocationProviderClient(this);
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                Location location = locationResult.getLastLocation();
                if (location != null) {
                    mLastLocation = Double.valueOf(location.getLatitude()).toString() + "," + Double.valueOf(location.getLongitude()).toString();
                    Log.d("loc: ", mLastLocation);
                }
            }
        };
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        client.requestLocationUpdates(request, locationCallback, null);
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
                if(checkAllState(mb)){
                    Retrofit retrofit = RetrofitClient.getRetrofitClient();
                    APIService apiservice = retrofit.create(APIService.class);
                    Call call = apiservice.customerMobile(mb);
                    call.enqueue(new Callback() {
                        @Override
                        public void onResponse(Call call, Response response) {
                            if (response.body().equals("true")) {
                                t1.setText("Mobile Number Already Exist");
                                progressDialog.cancel();
                            }else {
                                add_customer(cn,mb);
                            }
                        }

                        @Override
                        public void onFailure(Call call, Throwable t) {
                            Toast.makeText(getApplicationContext(),t.getMessage(),Toast.LENGTH_LONG).show();
                            Log.d("Error: ",t.getMessage());
                        }
                    });
                }else {
                    progressDialog.cancel();
                }
            }else{
                t1.setText("Please Enter Customer Name and Mobile");
                progressDialog.cancel();
            }
        }
    };

    private View.OnClickListener getPhoto = new View.OnClickListener() {
        public void onClick(View v) {
            image = "photo";
            try {
                camptureImage();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    };

    private View.OnClickListener getProof = new View.OnClickListener() {
        public void onClick(View v) {
            image = "proof";
            try {
                camptureImage();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    };

    private void camptureImage() throws IOException {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        photoFile = createImageNew();
        Uri photoURI = FileProvider.getUriForFile(this,
                "com.instacloudhost.extremes.fileprovider",
                photoFile);
        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
        startActivityForResult(takePictureIntent, CAPTURE_IMAGE_REQUEST);
    }

    private File createImageNew() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        // Save a file: path for use with ACTION_VIEW intents
//        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void processImage() {
        Bitmap image = BitmapFactory.decodeFile(photoFile.getAbsolutePath());
        int width = image.getWidth();
        int height = image.getHeight();
        float newWidth = ((float) 750/width);
        float newHeight = ((float) 450/height);
        Matrix matrix = new Matrix();
        matrix.postScale(newWidth, newHeight);
        matrix.postRotate(-90);
        currentImage = Bitmap.createBitmap(image, 0, 0, width, height, matrix, true);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        currentImage.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
//        Log.i("new width", currentImage.getWidth() + "");
//        Log.i("new height", currentImage.getHeight() + "");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
                processImage();
            switch (image) {
                case "photo":
                    selfie_iamge.setImageBitmap(currentImage);
                    String path = MediaStore.Images.Media.insertImage(getContentResolver(), currentImage, "Selfie", null);
                    f_image = Uri.parse(path);
                    cfu1 = "true";
                    break;
                case "proof":
                    proof_image.setImageBitmap(currentImage);
                    String path2 = MediaStore.Images.Media.insertImage(getContentResolver(), currentImage, "Proof", null);
                    s_image = Uri.parse(path2);
                    cfu2 = "true";
                    break;
            }
        }
    }

    private Boolean checkAllState(String mobile) {
        ConnectivityManager conMgr = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = conMgr.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        if(!isConnected) {
            t1.setText("Internet Not Connected");
            return false;
        }

        final LocationManager manager = (LocationManager) getSystemService( Context.LOCATION_SERVICE );

        if ( !manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
            t1.setText("GPS is Off");
            return false;
        }

        if(mobile.length() != 10 || !Pattern.matches("[0-9]+", mobile)) {
            t1.setText("Please Enter Valid Mobile Number");
            return false;
        }

        if(cfu1.equals("false")) {
            t1.setText("Please Click a Selfie");
            return false;
        }

        if(cfu2.equals("false")) {
            t1.setText("Please Choose a service proof");
            return false;
        }
        return true;
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
