package com.instacloudhost.extremes.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.SurfaceHolder.Callback;
import android.view.View;
import android.widget.Button;

import com.instacloudhost.extremes.R;

import androidx.annotation.Nullable;

public class FrontCameraActivity extends Activity implements Callback{

    private SurfaceView surfaceView;
    private SurfaceHolder surfaceHolder;
    private Camera camera;
    private Button capture;
    private int rotation;
    private Bitmap loadedImage = null;
    private Bitmap rotatedBitmap = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_front_camera);

        surfaceView = (SurfaceView) findViewById(R.id.surfaceView);
        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.addCallback((SurfaceHolder.Callback) this);

        capture=(Button)findViewById(R.id.captureImage);
        capture.setOnClickListener(imageCapture);
    }

    private View.OnClickListener imageCapture = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent=new Intent();
            intent.putExtra("data",rotatedBitmap);
            setResult(2,intent);
            finish();
        }
    };

    private boolean openCamera() {
        boolean result = false;
        int cameraId = CameraInfo.CAMERA_FACING_FRONT;
        releaseCamera();
        try {
            camera = Camera.open(cameraId);
            setUpCamera(camera);
            camera.setErrorCallback(new Camera.ErrorCallback() {
                @Override
                public void onError(int error, Camera camera) {
                }
            });
            camera.setPreviewDisplay(surfaceHolder);
            camera.startPreview();
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
            result = false;
            releaseCamera();
        }
        return result;
    }

    private void releaseCamera() {
        try {
            if (camera != null) {
                camera.setPreviewCallback(null);
                camera.setErrorCallback(null);
                camera.stopPreview();
                camera.release();
                camera = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("error", e.toString());
            camera = null;
        }
    }

    private void setUpCamera(Camera c) {
        Camera.CameraInfo info = new Camera.CameraInfo();
        Camera.getCameraInfo(CameraInfo.CAMERA_FACING_FRONT, info);
        rotation = getWindowManager().getDefaultDisplay().getRotation();
        int degree = 0;
        switch (rotation) {
            case Surface.ROTATION_0:
                degree = 0;
                break;
            case Surface.ROTATION_90:
                degree = 90;
                break;
            case Surface.ROTATION_180:
                degree = 180;
                break;
            case Surface.ROTATION_270:
                degree = 270;
                break;

            default:
                break;
        }

        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            // frontFacing
            rotation = (info.orientation + degree) % 330;
            rotation = (360 - rotation) % 360;
        } else {
            // Back-facing
            rotation = (info.orientation - degree + 360) % 360;
        }
        c.setDisplayOrientation(rotation);
        Camera.Parameters params = c.getParameters();
        params.setPictureFormat(ImageFormat.JPEG);
        params.setJpegQuality(100);
        params.setRotation(rotation);
    }

    private void takeImage() {
        camera.takePicture(null, null, new Camera.PictureCallback() {

            @Override
            public void onPictureTaken(byte[] data, Camera camera) {
                try {
                    loadedImage = BitmapFactory.decodeByteArray(data, 0,
                            data.length);

                    // rotate Image
                    Matrix rotateMatrix = new Matrix();
                    rotateMatrix.postRotate(rotation);
                    rotatedBitmap = Bitmap.createBitmap(loadedImage, 0, 0,
                            loadedImage.getWidth(), loadedImage.getHeight(),
                            rotateMatrix, false);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        openCamera();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }
}
