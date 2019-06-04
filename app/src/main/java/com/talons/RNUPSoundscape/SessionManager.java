package com.talons.RNUPSoundscape;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.List;

public class SessionManager {

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    private Context context;
    int PRIVATE_MODE = 0;

    private static final String PREF_NAME = "AndroidHivePref";

    private static final String KEY_DATABASE = "keepitasecret";

    // Constructor
    public SessionManager(Context context){
        this.context = context;
        pref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void setDatabase(String dataList){
        editor.putString(KEY_DATABASE, dataList);
        editor.commit();
    }

    public String getDatabase() {
        return pref.getString(KEY_DATABASE, null);
    }





}
