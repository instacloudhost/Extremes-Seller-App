package com.instacloudhost.extremes.foreground;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
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
    private NotificationChannel channel1;
    private NotificationManager manager;
    private FusedLocationProviderClient client;
    private LocationRequest request;
    private LocationCallback locationCallback;

    private String token;

    // Notification Channel IDS
    private static final String CHANNEL_1_ID = "channel1";

    public Tracking() {

    }

    @Override
    public void onCreate() {

        request = new LocationRequest();
        request.setInterval((500*1000));
        request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        client = LocationServices.getFusedLocationProviderClient(this);
        locationCallback = new LocationCallback(){
            @Override
            public void onLocationResult(LocationResult locationResult) {
                Location location = locationResult.getLastLocation();
                if (location != null) {
                    sendData(String.valueOf(location.getLatitude()), String.valueOf(location.getLongitude()), token);
                }
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            manager.deleteNotificationChannel(CHANNEL_1_ID);
        }
        if(client != null) {
            client.removeLocationUpdates(locationCallback);
        }
        Toast.makeText(this,"Location Tracking Stopped",Toast.LENGTH_LONG).show();
    }

    public void startTracking(String tk) {
        token = tk;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForeground(12345678, getNotification());
        }
        client.requestLocationUpdates(request, locationCallback ,null);
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
    private void sendData(String lati, String longi, String agents) {
        Retrofit retrofit = RetrofitClient.getRetrofitClient();
        APIService apiservice = retrofit.create(APIService.class);
        Log.d("uid", agents);
        Call call = apiservice.tracking(lati,longi,agents);
        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                if (response.body() != null) {
                    MStatus mstatus = (MStatus) response.body();
                    if(mstatus.getStatus().equals("true")) {
                        Toast.makeText(getApplicationContext(),mstatus.getMessage(),Toast.LENGTH_LONG).show();
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