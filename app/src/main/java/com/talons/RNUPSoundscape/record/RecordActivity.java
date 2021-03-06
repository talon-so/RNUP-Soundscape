package com.talons.RNUPSoundscape.record;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;


import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.jaredrummler.android.device.DeviceName;
import com.talons.RNUPSoundscape.R;
import com.talons.RNUPSoundscape.R.layout;
import com.talons.RNUPSoundscape.viewdata.DataActivity;
import com.talons.RNUPSoundscape.model.StorageModel;

import java.lang.reflect.Field;


public class RecordActivity extends AppCompatActivity implements OnMapReadyCallback, RecordFragment.RecordingCompleteCallback, View.OnClickListener {

    Button viewCollectedData;
    private GoogleMap map;
    //private GoogleMapsClient mapsClient;
    private FusedLocationProviderClient fusedLocationClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Show rationale and request permission.
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 0);
        //mapsClient = GoogleMapsServiceGenerator.createService(GoogleMapsClient.class);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        setContentView(layout.activity_main);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById( R.id.map);
        mapFragment.getMapAsync(this);
        viewCollectedData = findViewById( R.id.view_collected_data);
        viewCollectedData.setOnClickListener( this );
        switchFragment(R.id.frame, new RecordFragment(), "record", false);

    }

    /**
     * set the Google label to a specified height (usually above the panel)
     */
    public void setMapPadding(int left,int top , int right, int bottom ){
        try {
            map.setPadding(left, top, right, bottom);
        } catch (NullPointerException e) {
            Log.e("error", String.valueOf( e ) );
        }
    }
    private double lastLatitude = 0.0;
    private double lastLongitude = 0.0;
    private LatLng currLatLng;
    /**
     * centers map on location maintaining the current level of map zoom
     */
    public void centerOnLocation(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            map.setMyLocationEnabled(true);
            map.getUiSettings().setMyLocationButtonEnabled(true);
            fusedLocationClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    // Got last known location. In some rare situations this can be null.
                    if (location != null) {
                        // get zoom level to keep same level of zoom as maps current
                        float zoomLevel = map.getCameraPosition().zoom;
                        currLatLng = new LatLng(location.getLatitude(), location.getLongitude());
                        lastLatitude = location.getLatitude();
                        lastLongitude = location.getLongitude();
                        map.animateCamera(CameraUpdateFactory.newLatLngZoom(currLatLng, zoomLevel));
                    }
                }
            });
        }
    }

    /**
     * set map style by loading a json resource file into the map
     * @param googleMap is the fragments map to set style on
     */
    private void setMapStyling(GoogleMap googleMap){
        try {
            // Customize the styling of the base map using a JSON object defined
            // in a raw resource file.
            boolean success = googleMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            this, R.raw.style_json));
            if (!success) {
                Log.e("MapsActivityRaw: %s", "Style parsing failed.");
            }

            // Map padding
            int paddingLeft = 0;
            int paddingTop = 0;
            int paddingRight = 0;
            int paddingBottom = 0;
            googleMap.setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom);

        } catch (Resources.NotFoundException e) {
            Log.e("MapsActivityRaw: %s", "Can't find style.");
        }
    }

    public void switchFragment(int frameId, Fragment fragment, String tag, boolean animation) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        if (animation){
            ft.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
        }
        ft.replace(frameId, fragment, tag);
        ft.addToBackStack(tag);
        ft.commit();
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() == 1) {
            this.finish();
        }else
            super.onBackPressed();
    }

    public String getVersionName(){
        Field[] fields = Build.VERSION_CODES.class.getFields();
        String osName = fields[Build.VERSION.SDK_INT].getName();
        return osName;
    }
    @Override
    public void onRecordComplete(double averageDecibels) {
        centerOnLocation();
        String deviceName = DeviceName.getDeviceName();
        StorageModel unit = new StorageModel( averageDecibels, lastLatitude, lastLongitude, deviceName, Integer.toString(Build.VERSION.SDK_INT), System.currentTimeMillis()/1000 );
        switchFragment( R.id.frame, CurrentDataFragment.newInstance(unit), "data", false);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.map = googleMap;
        setMapStyling( map );
        setMapPadding( 0,0,0,Math.round(this
                .getResources().getDimension(R.dimen.map_padding_bottom)));
        centerOnLocation();
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case(R.id.view_collected_data):
                Intent intent = new Intent(this, DataActivity.class);
                startActivity(intent);
                break;
        }
    }
}
