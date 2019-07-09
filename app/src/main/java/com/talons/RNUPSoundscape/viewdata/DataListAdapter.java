package com.talons.RNUPSoundscape.viewdata;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.talons.RNUPSoundscape.R;
import com.talons.RNUPSoundscape.storagetools.StorageModel;

import java.util.List;

public class DataListAdapter extends RecyclerView.Adapter<DataViewHolder> {
    private static final int LIST_OBJ = R.layout.view_data;
    private List<StorageModel> data;

    DataListAdapter(List<StorageModel> data) {
        this.data = data;
    }

    @Override
    public int getItemViewType(int position) {
        return LIST_OBJ;
    }

    @Override
    public DataViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType != -1) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(viewType, parent, false);
            switch (viewType) {
                case LIST_OBJ:
                    return new DataViewHolder(view);
            }
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull DataViewHolder holder, int position) {
        StorageModel unit = data.get(position);
        String decibelAverage = "Decibel Average: " + String.format( "%f",unit.getAverageDecibels());
        holder.decibelAverage.setText(decibelAverage);
        String lat= "Latitude: " + String.format("%f",unit.getLatitude());
        holder.latitude.setText(lat);
        String lng= "Latitude: " + String.format("%f",unit.getLongitude());
        holder.longitude.setText(lng);
        String apiLevel = "Phone API Level: " + unit.getPhoneApiLevel();
        holder.apiLevel.setText( apiLevel );
        String phoneModel = "Phone Model: " + unit.getPhoneModel();
        holder.phoneModel.setText( phoneModel );
        String time = unit.getStandardTime();
        holder.standardTime.setText( time );
    }

    @Override
    public int getItemCount() {
        return data.size();
    }


}
