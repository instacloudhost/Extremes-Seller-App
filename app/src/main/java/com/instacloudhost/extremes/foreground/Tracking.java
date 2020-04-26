package com.instacloudhost.extremes.foreground;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.instacloudhost.extremes.ViewCustomer;
import com.instacloudhost.extremes.model.MStatus;
import com.instacloudhost.extremes.remote.APIService;
import com.instacloudhost.extremes.remote.RetrofitClient;

import androidx.annotation.RequiresApi;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class Tracking extends Service {

    private final LocationServiceBinder binder = new LocationServiceBinder();
    private LocationManager locationManager;
    private LocationListener locationListener;
    private NotificationChannel channel1;
    private NotificationManager manager;
    private Location mLastLocation;

    private SharedPreferences token;
    private String extremes = "extremesStorage";

    // Notification Channel IDS
    private static final String CHANNEL_1_ID = "channel1";

    public Tracking() {

    }

    @SuppressLint("MissingPermission")
    @Override
    public void onCreate() {

        token = getSharedPreferences(extremes,
                Context.MODE_PRIVATE);

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                mLastLocation = location;
                sendData(Double.valueOf(location.getLatitude()).toString(), Double.valueOf(location.getLongitude()).toString(),token.getString("token",""));
                Log.d("Location Status:", Double.valueOf(location.getLatitude()).toString()+","+ Double.valueOf(location.getLongitude()).toString());
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
                Log.e("Location Status:", "onStatusChanged: " + status);
            }

            @Override
            public void onProviderEnabled(String provider) {
                Log.e("Location Status:", "onProviderEnabled: " + provider);
            }

            @Override
            public void onProviderDisabled(String provider) {
                Log.e("Location Status:", "onProviderDisabled: " + provider);
            }
        };
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (locationManager != null) {
            locationManager.removeUpdates(locationListener);
            locationManager = null;

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                manager.deleteNotificationChannel(CHANNEL_1_ID);
            }
            Toast.makeText(this,"Location Tracking Stopped",Toast.LENGTH_LONG).show();
        }
    }

    public void startTracking() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForeground(12345678, getNotification());
        }
        locationManager =  (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        try {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,60000,500,locationListener);
        } catch (java.lang.SecurityException ex) {
        } catch (IllegalArgumentException ex) {
        }
        Toast.makeText(this,"Location Tracking Started",Toast.LENGTH_LONG).show();
    }

    public void stopTracking() {
        this.onDestroy();
    }

    //  Notification
    @RequiresApi(api = Build.VERSION_CODES.O)
    private Notification getNotification() {

            channel1 = new NotificationChannel(
                    CHANNEL_1_ID,
                    "Chanel 1",
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel1.setDescription("Channel One Notification");
            manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel1);

            Notification.Builder builder = new Notification.Builder(getApplicationContext(), CHANNEL_1_ID);
            return builder.build();
    }

    //    Service Binder
    public class LocationServiceBinder extends Binder {
        public Tracking getService() {
            return Tracking.this;
        }
    }

    // Data send to server
    private void sendData(String lati, String longi, String agent) {
        Retrofit retrofit = RetrofitClient.getRetrofitClient();
        APIService apiservice = retrofit.create(APIService.class);
        Call call = apiservice.tracking(lati,longi,agent);
        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                if (response.body() != null) {
                    MStatus mstatus = (MStatus) response.body();
                    if(mstatus.getStatus().equals("true")) {
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
}