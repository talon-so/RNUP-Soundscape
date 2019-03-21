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
        switchFragment(R.id.frame, new RecordFragment(), "record");
    }

    public void switchFragment(int frameId, Fragment fragment, String tag) {
        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right)
                .replace(frameId, fragment, tag)
                .addToBackStack(tag)
                .commit();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
