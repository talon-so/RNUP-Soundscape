package com.talons.RNUPSoundscape.storagetools;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    private Context context;
    int PRIVATE_MODE = 0;

    private static final String PREF_NAME = "AndroidHivePref";

    private static final String KEY_DATABASE = "keepitasecret";
    private static final String KEY_AMPLITUDE_REF = "lilnuggets";

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


    public int getAmplitudeRef() {
        return pref.getInt(KEY_AMPLITUDE_REF, 0);
    }


    public void setAmplitudeRef(int amplitudeRef){
        editor.putInt(KEY_AMPLITUDE_REF, amplitudeRef);
        editor.commit();
    }



}
