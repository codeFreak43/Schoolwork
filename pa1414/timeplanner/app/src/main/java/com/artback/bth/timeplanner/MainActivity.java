package com.artback.bth.timeplanner;
import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.artback.bth.timeplanner.Geofence.GeofenceLocation;
import com.artback.bth.timeplanner.Geofence.GeofenceLocationProvider;
import com.artback.bth.timeplanner.Geofence.GeolocationService;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;


public class MainActivity extends Activity {
    private static final int REQUEST_STORAGE  =1;
    private static final int REQUEST_NETWORK =2;
    private static final int REQUEST_LOCATION =3;

    private RecyclerView locationView;
    private RecyclerView.Adapter locAdapter;
    private RecyclerView.LayoutManager locationLayoutManager;
    static public boolean geofencesAlreadyRegistered = false;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_page);
        init(this);
    }
    private int checkPermissons(){
        PermissionListener permissionlistener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
            }

            @Override
            public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                Toast.makeText(MainActivity.this, "Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
            }


        };


        return 0;
    }
    private void init(final Context context){
        locationView = (RecyclerView) findViewById(R.id.location_list);
        locationView.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        locationView.setLayoutManager(llm);

        locationLayoutManager= new LinearLayoutManager(this);
        locationView.setLayoutManager(locationLayoutManager);
        final List<GeofenceLocation> myGeofenceSet = new ArrayList<GeofenceLocation>
                (GeofenceLocationProvider.getInstance().getGeofencesLocations().values());
        locAdapter = new locationAdapter(myGeofenceSet);

        //start geolocation
        startService(new Intent(this, GeolocationService.class));

        locationView.addOnItemTouchListener(
                new RecyclerItemClickListener(context, locationView ,new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        Intent intent = new Intent(context, CalenderActivity.class);
                        intent.setAction(Intent.ACTION_SEND);
                        intent.putExtra(Intent.EXTRA_TEXT, myGeofenceSet.get(position).getId());
                        startActivity(intent);
                    }

                    @Override public void onLongItemClick(View view, int position) {
                        // do whatever
                    }
                })
        );
        locationView.setAdapter(locAdapter);
    }
    public void openaddpage(View view){
        Intent intent = new Intent(this, AddLocationActivity.class);
        startActivity(intent);
    }
    @Override
    protected void onStart() {
        super.onStart();
    }

}
