package com.talons.RNUPSoundscape.viewdata;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.talons.RNUPSoundscape.R;

public class DataViewHolder extends RecyclerView.ViewHolder {

    public TextView decibelAverage, phoneModel, apiLevel, latitude, longitude, standardTime;
    public DataViewHolder(View view) {
        super(view);
        decibelAverage = view.findViewById( R.id.decibels);
        phoneModel = view.findViewById(R.id.phone_model);
        apiLevel = view.findViewById(R.id.api_level);
        standardTime = view.findViewById(R.id.standard_time);
        latitude = view.findViewById(R.id.latitude);
        longitude = view.findViewById(R.id.longitude);
    }
}