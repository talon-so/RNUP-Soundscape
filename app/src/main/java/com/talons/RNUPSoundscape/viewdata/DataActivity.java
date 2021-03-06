package com.talons.RNUPSoundscape.viewdata;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.talons.RNUPSoundscape.R;
import com.talons.RNUPSoundscape.R.layout;
import com.talons.RNUPSoundscape.storagetools.CSVGenerator;
import com.talons.RNUPSoundscape.storagetools.Serializer;
import com.talons.RNUPSoundscape.storagetools.SessionManager;
import com.talons.RNUPSoundscape.model.StorageModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class DataActivity extends AppCompatActivity implements View.OnClickListener {
    SessionManager sessionManager;
    Serializer serializer;
    Button exportBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.activity_data);
        exportBtn = findViewById( R.id.export );
        exportBtn.setOnClickListener( this );

        serializer = new Serializer();
        sessionManager = new SessionManager( this );
        renderRecycler();
    }

    private List<StorageModel> getDatabase() {
        try {
            if(sessionManager.getDatabase() != null){
                String serializedDatabase = sessionManager.getDatabase();
                return (List<StorageModel>) serializer.deserialize(serializedDatabase);
            }
        } catch (IOException e) {
            Log.e("IOException", String.valueOf( e ) );
        } catch (ClassNotFoundException e) {
            Log.e("ClassNotFoundException", String.valueOf( e ) );
        }
        return new ArrayList<StorageModel>();
    }

    RecyclerView recyclerView;
    public void renderRecycler(){
        recyclerView = findViewById( R.id.data_recycler );
        DataListAdapter adapter = new DataListAdapter( getDatabase());
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }


    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.export:
                try {
                    CSVGenerator.generate( this );
                } catch (Exception e) {
                    e.printStackTrace();
                }
        }
    }
}
