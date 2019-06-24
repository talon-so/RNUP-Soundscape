package com.talons.RNUPSoundscape.calibrate;

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
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

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
import com.talons.RNUPSoundscape.record.CurrentDataFragment;
import com.talons.RNUPSoundscape.record.RecordFragment;
import com.talons.RNUPSoundscape.sessiontools.StorageModel;
import com.talons.RNUPSoundscape.viewdata.DataActivity;

import java.lang.reflect.Field;


public class CalibrationActivity extends AppCompatActivity implements RecordFragment.RecordingCompleteCallback, View.OnClickListener {

    Button viewCollectedData;
    private GoogleMap map;
    //private GoogleMapsClient mapsClient;
    private FusedLocationProviderClient fusedLocationClient;
    private CalibrationFragment calibrationFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.activity_calibrate);
        calibrationFragment = CalibrationFragment.newInstance();
        switchFragment(R.id.frame, calibrationFragment, "calibration", false);
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
            calibrationFragment.stopCalibration();
            this.finish();
        }else
            super.onBackPressed();
    }
    @Override
    public void onRecordComplete(double averageDecibels) {

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
