package com.instacloudhost.extremes.sections;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.loader.content.CursorLoader;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import com.instacloudhost.extremes.DashBoard;
import com.instacloudhost.extremes.R;
import com.instacloudhost.extremes.model.MStatus;
import com.instacloudhost.extremes.remote.APIService;
import com.instacloudhost.extremes.remote.RetrofitClient;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class WindsUploads extends AppCompatActivity {

    private String cid, sec;
    private File  file1 , file2, file3, file4, file5, file6 , sourceFile;
    private ImageView img1, img2, img3, img4, img5, img6 = null;
    private Button btn1, btn2, btn3, btn4, btn5, btn6, submit;
    private Bitmap currentImage;
    private RequestBody rb1, rb2, rb3, rb4, rb5, rb6;
    private MultipartBody.Part mbp1, mbp2, mbp3, mbp4, mbp5, mbp6;
    static final int CAPTURE_IMAGE_REQUEST = 1;
    String path;
    Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        cid = intent.getStringExtra("customer_id");
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
    }

    private View.OnClickListener btnSubmit = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if( file1 == null || file2 == null || file3 == null || file4 == null || file5 == null || file6 == null) {
                Toast.makeText(getApplicationContext(),"Please Upload All Image",Toast.LENGTH_LONG).show();
            }else {
                RequestBody custId = RequestBody.create(MediaType.parse("text/plain"), cid);
                Retrofit retrofit = RetrofitClient.getRetrofitClient();
                APIService apiservice = retrofit.create(APIService.class);
                Call call = apiservice.WindsUpload(custId, mbp1, mbp2, mbp3, mbp4, mbp5, mbp6);
                call.enqueue(new Callback() {
                    @Override
                    public void onResponse(Call call, Response response) {
                        if (response.body() != null) {
                            MStatus mstatus = (MStatus) response.body();
                            Log.d("Response: ", String.valueOf(mstatus.getMessage()));
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

    private void camptureImage() throws IOException {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        sourceFile = createImageNew();
        Uri photoURI = FileProvider.getUriForFile(this,
                "com.instacloudhost.extremes.fileprovider",
                sourceFile);
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
        Bitmap image = BitmapFactory.decodeFile(sourceFile.getAbsolutePath());
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
    }

    private void finishProcess(String tpo) {
        switch (tpo) {
            case "one":
                img1.setImageBitmap(currentImage);
                path = MediaStore.Images.Media.insertImage(getContentResolver(), currentImage, "image1", null);
                uri = Uri.parse(path);
                file1 = new File(getRealPathFromURI(uri));
                rb1 = RequestBody.create(MediaType.parse(getContentResolver().getType(uri)), file1);
                mbp1 = MultipartBody.Part.createFormData("file1", "image1.jpg", rb1);
                break;
            case "two":
                img2.setImageBitmap(currentImage);
                path = MediaStore.Images.Media.insertImage(getContentResolver(), currentImage, "image2", null);
                uri = Uri.parse(path);
                file2 = new File(getRealPathFromURI(uri));
                rb2 = RequestBody.create(MediaType.parse(getContentResolver().getType(uri)), file2);
                mbp2 = MultipartBody.Part.createFormData("file2", "image2.jpg", rb2);
                break;
            case "three":
                img3.setImageBitmap(currentImage);
                path = MediaStore.Images.Media.insertImage(getContentResolver(), currentImage, "image3", null);
                uri = Uri.parse(path);
                file3 = new File(getRealPathFromURI(uri));
                rb3 = RequestBody.create(MediaType.parse(getContentResolver().getType(uri)), file3);
                mbp3 = MultipartBody.Part.createFormData("file3", "image3.jpg", rb3);
                break;
            case "four":
                img4.setImageBitmap(currentImage);
                path = MediaStore.Images.Media.insertImage(getContentResolver(), currentImage, "image4", null);
                uri = Uri.parse(path);
                file4 = new File(getRealPathFromURI(uri));
                rb4 = RequestBody.create(MediaType.parse(getContentResolver().getType(uri)), file4);
                mbp4 = MultipartBody.Part.createFormData("file4", "image4.jpg", rb4);
                break;
            case "five":
                img5.setImageBitmap(currentImage);
                path = MediaStore.Images.Media.insertImage(getContentResolver(), currentImage, "image5", null);
                uri = Uri.parse(path);
                file5 = new File(getRealPathFromURI(uri));
                rb5 = RequestBody.create(MediaType.parse(getContentResolver().getType(uri)), file5);
                mbp5 = MultipartBody.Part.createFormData("file5", "image5.jpg", rb5);
                break;
            case "six":
                img6.setImageBitmap(currentImage);
                path = MediaStore.Images.Media.insertImage(getContentResolver(), currentImage, "image6", null);
                uri = Uri.parse(path);
                file6 = new File(getRealPathFromURI(uri));
                rb6 = RequestBody.create(MediaType.parse(getContentResolver().getType(uri)), file6);
                mbp6 = MultipartBody.Part.createFormData("file6", "image6.jpg", rb6);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (sec){
                case "one":
                    processImage();
                    break;
                case "two":
                    processImage();
                    break;
                case "three":
                    processImage();
                    break;
                case "four":
                    processImage();
                    break;
                case "five":
                    processImage();
                    break;
                case "six":
                    processImage();
                    break;
            }
            finishProcess(sec);
        }
    }
}