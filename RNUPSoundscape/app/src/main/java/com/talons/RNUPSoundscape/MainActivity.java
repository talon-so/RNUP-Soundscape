package com.talons.RNUPSoundscape;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.talons.RNUPSoundscape.R.layout;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.activity_main);
        switchFragment(R.id.frame, new RecordFragment(), "record", "record frag");
    }

    public void switchFragment(int frameId, Fragment fragment, String tag, String entryName) {
        getSupportFragmentManager().beginTransaction()
                .replace(frameId, fragment, tag)
                .addToBackStack(entryName)
                .commit();
    }
}
