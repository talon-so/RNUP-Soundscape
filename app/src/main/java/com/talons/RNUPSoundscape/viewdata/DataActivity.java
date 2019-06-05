package com.talons.RNUPSoundscape.viewdata;

import android.os.Bundle;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.talons.RNUPSoundscape.R;
import com.talons.RNUPSoundscape.R.layout;
import com.talons.RNUPSoundscape.sessiontools.Serializer;
import com.talons.RNUPSoundscape.sessiontools.SessionManager;
import com.talons.RNUPSoundscape.sessiontools.StorageModel;

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


}
