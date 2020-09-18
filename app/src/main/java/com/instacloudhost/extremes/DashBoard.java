package com.instacloudhost.extremes;

import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import info.androidhive.fontawesome.FontDrawable;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.instacloudhost.extremes.foreground.Tracking;
import com.instacloudhost.extremes.model.Mgraph;
import com.instacloudhost.extremes.remote.APIService;
import com.instacloudhost.extremes.remote.RetrofitClient;
import com.instacloudhost.extremes.sections.WindsForm;

public class DashBoard extends AppCompatActivity {

    private SharedPreferences token;
    private String extremes = "extremeStorage";
    public Tracking gpsService;
    DrawerLayout drawerLayout;
    Toolbar toolbar;
    private String tokenid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dash_board);

        token = getSharedPreferences(extremes,
                Context.MODE_PRIVATE);
        tokenid = token.getString("token", "");
        Log.d("uid: ", tokenid);
        menu();

        checkAllState();

        final Intent intent = new Intent(this.getApplication(), Tracking.class);
        this.getApplication().startService(intent);
        this.getApplication().bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);

    }

    private void checkAllState() {
        ConnectivityManager conMgr = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = conMgr.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        if(isConnected) {
            getGraph(tokenid);
//            Toast.makeText(getApplicationContext(),"Internet Connected",Toast.LENGTH_LONG).show();
        }else {
//            Toast.makeText(getApplicationContext(),"Internet Not Connected",Toast.LENGTH_LONG).show();
        }

        final LocationManager manager = (LocationManager) getSystemService( Context.LOCATION_SERVICE );

        if ( !manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
            Toast.makeText(getApplicationContext(),"Internet Not Connected",Toast.LENGTH_LONG).show();
        }
    }

    public void menu() {

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView navigationView = (NavigationView) findViewById(R.id.drawerNavigation);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int id = menuItem.getItemId();

                switch (menuItem.getItemId()) {
                    case R.id.viewCustomer:
                        Intent main = new Intent(getBaseContext(), ViewCustomer.class);
                        startActivity(main);
                        break;
                    case R.id.notice:
                        Intent nt = new Intent(getBaseContext(), Notification.class);
                        startActivity(nt);
                        break;
                    case R.id.feedback:
                        Intent feed = new Intent(getBaseContext(), FeedBack.class);
                        startActivity(feed);
                        break;
                    case R.id.about:
                        Intent ab = new Intent(getBaseContext(), AboutUs.class);
                        startActivity(ab);
                        break;
                    case R.id.support:
                        Intent support = new Intent(getBaseContext(), Support.class);
                        startActivity(support);
                        break;
                    case R.id.share:
                        Intent sr = new Intent(Intent.ACTION_SEND);
                        sr.setType("text/plain")
                                .putExtra(Intent.EXTRA_TEXT, getString(R.string.share_title))
                                .putExtra(Intent.EXTRA_SUBJECT, "Download Extreme Sales Service App");
                        startActivity(Intent.createChooser(sr, "Share Using"));
                        break;
                    case R.id.logout:
                        SharedPreferences.Editor editor = token.edit();
                        editor.remove("token");
                        editor.commit();
                        Intent ma = new Intent(getBaseContext(), MainActivity.class);
                        startActivity(ma);
                        finish();
                        break;
                }
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });

        int[] icons = {
                 R.string.fa_user_solid
        };
        int[] icons_2 = {
                R.string.fa_bell_solid, R.string.fa_comment_solid, R.string.fa_address_card_solid,
                R.string.fa_life_ring_solid, R.string.fa_share_alt_solid, R.string.fa_sign_out_alt_solid
        };
        renderMenuIcons(navigationView.getMenu(), icons, true, false);
        renderMenuIcons(navigationView.getMenu().getItem(1).getSubMenu(), icons_2, true, false);

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.setDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.startService:
                        startService();
                        break;
                    case R.id.stopService:
                        stopService();
                        break;
                }
                return true;
            }
        });
    }

    private void renderMenuIcons(Menu menu, int[] icons, boolean isSolid, boolean isBrand) {
        for (int i = 0; i < menu.size(); i++) {
            MenuItem menuItem = menu.getItem(i);
            if (!menuItem.hasSubMenu()) {
                FontDrawable drawable = new FontDrawable(this, icons[i], isSolid, isBrand);
                drawable.setTextColor(ContextCompat.getColor(this, R.color.icon_nav_drawer));
                drawable.setTextSize(22);
                menu.getItem(i).setIcon(drawable);
            }
        }
    }

    @Override
    public void onBackPressed() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(DashBoard.this);
        builder.setMessage("Are you sure want Exit?")
                .setTitle("Extremes")
                .setIcon(R.mipmap.ic_launcher)
                .setCancelable(true)
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                }).show();
    }


    private void getGraph(String token) {
        Retrofit retrofit = RetrofitClient.getRetrofitClient();
        APIService apiservice = retrofit.create(APIService.class);
        Call call = apiservice.graph(token);
        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                if (response.body() != null) {
                    Mgraph mgraph = (Mgraph) response.body();
                    if(mgraph.getStatus().equals("true")) {
                        TextView tdc = (TextView) findViewById(R.id.todayCount);
                        tdc.setText(mgraph.getData().getToday());
                        TextView tc = (TextView) findViewById(R.id.totalCount);
                        tc.setText(mgraph.getData().getTotal());
                        TextView dt = (TextView) findViewById(R.id.drawerTitle);
                        dt.setText(mgraph.getData().getAgentName());
                        TextView rt = (TextView) findViewById(R.id.rejectCount);
                        rt.setText(mgraph.getData().getReject());
                    }else{
                        Toast.makeText(getApplicationContext(),"No Data Found",Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                Toast.makeText(getApplicationContext(),t.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
    }


    // Start the service
    public void startService() {
        gpsService.startTracking(tokenid);
    }

    // Stop the service
    public void stopService() {
        gpsService.stopTracking();
    }

    private ServiceConnection serviceConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder service) {
            String name = className.getClassName();
            if (name.endsWith("Tracking")) {
                gpsService = ((Tracking.LocationServiceBinder) service).getService();
            }
        }

        public void onServiceDisconnected(ComponentName className) {
            if (className.getClassName().equals("Tracking")) {
                gpsService = null;
            }
        }
    };

    // Services Activities Start Here

    public void amazonKyc(View view) {
        Intent main = new Intent(getBaseContext(), AddCustomer.class);
        main.putExtra("category", "1");
        main.putExtra("title", "Amazon Pay KYC");
        startActivity(main);
    }

    public void amazonMerchant(View view) {
        Intent main = new Intent(getBaseContext(), AddCustomer.class);
        main.putExtra("category", "2");
        main.putExtra("title", "Amazon Pay Merchant");
        startActivity(main);
    }

    public void khataBook(View view) {
        Intent main = new Intent(getBaseContext(), AddCustomer.class);
        main.putExtra("category", "3");
        main.putExtra("title", "KhataBook");
        startActivity(main);
    }

    public void qrupiya(View view) {
        Intent main = new Intent(getBaseContext(), AddCustomer.class);
        main.putExtra("category", "4");
        main.putExtra("title", "Qrupia");
        startActivity(main);
    }

    public void airtelMerchant(View view) {
        Intent main = new Intent(getBaseContext(), AddCustomer.class);
        main.putExtra("category", "5");
        main.putExtra("title", "Airtel Merchant");
        startActivity(main);
    }

    public void kotakBank(View view) {
        Intent main = new Intent(getBaseContext(), AddCustomer.class);
        main.putExtra("category", "6");
        main.putExtra("title", "Kotak Bank");
        startActivity(main);
    }

    public void dcbBank(View view) {
        Intent main = new Intent(getBaseContext(), AddCustomer.class);
        main.putExtra("category", "7");
        main.putExtra("title", "DCB Bank");
        startActivity(main);
    }

    public void rblBank(View view) {
        Intent main = new Intent(getBaseContext(), AddCustomer.class);
        main.putExtra("category", "8");
        main.putExtra("title", "RBL Bank");
        startActivity(main);
    }

    public void fmcKyc(View view) {
        Intent main = new Intent(getBaseContext(), AddCustomer.class);
        main.putExtra("category", "9");
        main.putExtra("title", "FMC Kyc");
        startActivity(main);
    }

    public void irctc(View view) {
        Intent main = new Intent(getBaseContext(), AddCustomer.class);
        main.putExtra("category", "10");
        main.putExtra("title", "IRCTC I Mundra KYC");
        startActivity(main);
    }

    public void winds(View view) {
        Intent main = new Intent(getBaseContext(), WindsForm.class);
        main.putExtra("category", "11");
        main.putExtra("title", "WINDS");
        startActivity(main);
    }

    public void pineLabs(View view) {
        Intent main = new Intent(getBaseContext(), AddCustomer.class);
        main.putExtra("category", "12");
        main.putExtra("title", "Pinelabs");
        startActivity(main);
    }

    public void smartLife(View view) {
        Intent main = new Intent(getBaseContext(), AddCustomer.class);
        main.putExtra("category", "13");
        main.putExtra("title", "SmartLife");
        startActivity(main);
    }

    public void phonePe(View view) {
        Intent main = new Intent(getBaseContext(), AddCustomer.class);
        main.putExtra("category", "14");
        main.putExtra("title", "PhonePe");
        startActivity(main);
    }

    // Service Activities End Here
}
