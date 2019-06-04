package com.talons.RNUPSoundscape;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.talons.RNUPSoundscape.R.layout;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class DataActivity extends AppCompatActivity{
    SessionManager sessionManager;
    Serializer serializer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.activity_data);

        serializer = new Serializer();
        sessionManager = new SessionManager( this );
        dataList = getDatabase();
        renderRecycler();
    }

    public List<StorageModel> getDatabase() {
        try {
            if(sessionManager.getDatabase() != null){
                String serializedDatabase = sessionManager.getDatabase();
                Log.i("SerializedDatabase ", serializedDatabase);
                return (List<StorageModel>) serializer.deserialize(serializedDatabase);
            } else {
                return new ArrayList<StorageModel>();
            }
        } catch (IOException e) {
            Log.e("IOException", String.valueOf( e ) );
        } catch (ClassNotFoundException e) {
            Log.e("ClassNotFoundException", String.valueOf( e ) );
        }
        return null;
    }

    RecyclerView recyclerView;
    List<StorageModel> dataList;
    public void renderRecycler(){
        recyclerView = findViewById( R.id.data_recycler );
        DataListAdapter adapter = new DataListAdapter( dataList );
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
}
