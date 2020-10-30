package com.instacloudhost.extremes.sections;

import androidx.appcompat.app.AppCompatActivity;
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
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.instacloudhost.extremes.DashBoard;
import com.instacloudhost.extremes.R;
import com.instacloudhost.extremes.model.MStatus;
import com.instacloudhost.extremes.remote.APIService;
import com.instacloudhost.extremes.remote.RetrofitClient;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class WindsUploads extends AppCompatActivity {

    private static final int PICK_IMAGES_CODE = 0;
    private ProgressDialog progressDialog;
    private String cid, sec;
    private File  file1 , file2, file3, file4, file5, file6;
    private ImageView img1, img2, img3, img4, img5, img6 = null;
    private Button btn1, btn2, btn3, btn4, btn5, btn6, submit;
    private Bitmap currentImage;
    private RequestBody rb1, rb2, rb3, rb4, rb5, rb6;
    private MultipartBody.Part mbp1, mbp2, mbp3, mbp4, mbp5, mbp6;
    static final int CAPTURE_IMAGE_REQUEST = 1;
    Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        cid = intent.getStringExtra("customer_id");
//        cid = "1234";
        setTitle("Winds Document Uploads");
        setContentView(R.layout.activity_winds_uploads);

        img1 = (ImageView) findViewById(R.id.file1);
        img2 = (ImageView) findViewById(R.id.file2);
        img3 = (ImageView) findViewById(R.id.file3);
        img4 = (ImageView) findViewById(R.id.file4);
        img5 = (ImageView) findViewById(R.id.file5);
        img6 = (ImageView) findViewById(R.id.file6);
        btn1 = (Button)findViewById(R.id.btn1);
        btn2 = (Button)findViewById(R.id.btn2);
        btn3 = (Button)findViewById(R.id.btn3);
        btn4 = (Button)findViewById(R.id.btn4);
        btn5 = (Button)findViewById(R.id.btn5);
        btn6 = (Button)findViewById(R.id.btn6);
        submit = (Button)findViewById(R.id.submitPics);
        btn1.setOnClickListener(btn1Clicked);
        btn2.setOnClickListener(btn2Clicked);
        btn3.setOnClickListener(btn3Clicked);
        btn4.setOnClickListener(btn4Clicked);
        btn5.setOnClickListener(btn5Clicked);
        btn6.setOnClickListener(btn6Clicked);
        submit.setOnClickListener(btnSubmit);

        permit();
    }

    private View.OnClickListener btnSubmit = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            progressBar();
            Retrofit retrofit = RetrofitClient.getRetrofitClient();
            APIService apiservice = retrofit.create(APIService.class);
            Call call = apiservice.windsFileCheck(cid);
            call.enqueue(new Callback() {
                @Override
                public void onResponse(Call call, Response response) {
                    if (response.body() != null) {
                        MStatus mstatus = (MStatus) response.body();
                        Log.d("Response: ", String.valueOf(mstatus.getMessage()));
                        progressDialog.dismiss();
                        if (mstatus.getStatus().equals("true")) {
                            Toast.makeText(getApplicationContext(), "Successfully Uploaded", Toast.LENGTH_LONG).show();
                            Intent main = new Intent(getBaseContext(), DashBoard.class);
                            startActivity(main);
                            finish();
                        } else {
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
    };

    private View.OnClickListener btn1Clicked = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            sec = "one";
            try {
                camptureImage();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    };

    private View.OnClickListener btn2Clicked = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            sec = "two";
            try {
                camptureImage();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    };

    private View.OnClickListener btn3Clicked = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            sec = "three";
            try {
                camptureImage();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    };

    private View.OnClickListener btn4Clicked = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            sec = "four";
            try {
                camptureImage();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    };

    private View.OnClickListener btn5Clicked = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            sec = "five";
            try {
                camptureImage();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    };

    private View.OnClickListener btn6Clicked = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            sec = "six";
            try {
                camptureImage();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    };

    private void uploadImage(MultipartBody.Part mbp, String fieldName) {
        progressBar();
        RequestBody custId = RequestBody.create(MediaType.parse("text/plain"), cid);
        RequestBody fN = RequestBody.create(MediaType.parse("text/plain"), fieldName);
        Retrofit retrofit = RetrofitClient.getRetrofitClient();
        APIService apiservice = retrofit.create(APIService.class);
        Call call = apiservice.windsUploadByField(custId, fN, mbp);
        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                if (response.body() != null) {
                    MStatus mstatus = (MStatus) response.body();
                    Log.d("Response: ", String.valueOf(mstatus.getMessage()));
                    progressDialog.dismiss();
                    if (mstatus.getStatus().equals("true")) {
                        Toast.makeText(getApplicationContext(), "Successfully Uploaded", Toast.LENGTH_LONG).show();
                    } else {
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
        String[] proj = {MediaStore.Images.Media._ID};
        CursorLoader loader = new CursorLoader(this, contentUri, proj, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID);
        cursor.moveToFirst();
        String result = cursor.getString(column_index);
        cursor.close();
        return result;
    }

    private void camptureImage() throws IOException {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(Intent.createChooser(intent, "Select Image(s)"), PICK_IMAGES_CODE);
    }

    private File convertBitmapToFile(String filename, Bitmap btmp) throws IOException {
        File f = new File(getCacheDir(), filename);
        f.createNewFile();

        //Convert bitmap to byte array
        Bitmap bitmap = btmp;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 60 /*ignored for PNG*/, bos);
        byte[] bitmapdata = bos.toByteArray();

        //write the bytes in file
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(f);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            fos.write(bitmapdata);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return f;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            uri = data.getData();
            switch (sec){
                case "one":
                    try {
                        currentImage = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                        img1.setImageBitmap(currentImage);
                        file1 = convertBitmapToFile("image1.jpg", currentImage);
                        rb1 = RequestBody.create(MediaType.parse("image/*"), file1);
                        mbp1 = MultipartBody.Part.createFormData("file1", "image1.jpg", rb1);
                        uploadImage(mbp1, "aadhar_front");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                case "two":
                    try {
                        currentImage = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                        img2.setImageBitmap(currentImage);
                        file2 = convertBitmapToFile("image2.jpg", currentImage);
                        rb2 = RequestBody.create(MediaType.parse(getContentResolver().getType(uri)), file2);
                        mbp2 = MultipartBody.Part.createFormData("file2", "image2.jpg", rb2);
                        uploadImage(mbp2, "aadhar_back");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                case "three":
                    try {
                        currentImage = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                        img3.setImageBitmap(currentImage);
                        file3 = convertBitmapToFile("image3.jpg", currentImage);
                        rb3 = RequestBody.create(MediaType.parse(getContentResolver().getType(uri)), file3);
                        mbp3 = MultipartBody.Part.createFormData("file3", "image3.jpg", rb3);
                        uploadImage(mbp3, "pan");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                case "four":
                    try {
                        currentImage = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                        img4.setImageBitmap(currentImage);
                        file4 = convertBitmapToFile("image4.jpg", currentImage);
                        rb4 = RequestBody.create(MediaType.parse(getContentResolver().getType(uri)), file4);
                        mbp4 = MultipartBody.Part.createFormData("file4", "image4.jpg", rb4);
                        uploadImage(mbp4, "shop_pic");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                case "five":
                    try {
                        currentImage = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                        img5.setImageBitmap(currentImage);
                        file5 = convertBitmapToFile("image5.jpg", currentImage);
                        rb5 = RequestBody.create(MediaType.parse(getContentResolver().getType(uri)), file5);
                        mbp5 = MultipartBody.Part.createFormData("file5", "image5.jpg", rb5);
                        uploadImage(mbp5, "passbook");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                case "six":
                    try {
                        currentImage = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                        img6.setImageBitmap(currentImage);
                        file6 = convertBitmapToFile("image6.jpg", currentImage);
                        rb6 = RequestBody.create(MediaType.parse(getContentResolver().getType(uri)), file6);
                        mbp6 = MultipartBody.Part.createFormData("file6", "image6.jpg", rb6);
                        uploadImage(mbp6, "selfie");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    }

    public void permit() {
        PermissionListener permissionListener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
//                Toast.makeText(MainActivity.this,"Permissions Granted", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onPermissionDenied(List<String> deniedPermissions) {
                Toast.makeText(WindsUploads.this,"Permissions Not Granted", Toast.LENGTH_LONG).show();
            }
        };
        TedPermission.with(WindsUploads.this)
                .setPermissionListener(permissionListener)
                .setPermissions(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                ).check();
    }

    private void progressBar() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Uploading Image");
        progressDialog.setMessage("Uploading...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMax(50);
        progressDialog.show();
        progressDialog.setCancelable(false);
        new Thread(new Runnable() {
            public void run() {
                try {
                    Thread.sleep(20000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                progressDialog.dismiss();
            }
        }).start();
    }
}