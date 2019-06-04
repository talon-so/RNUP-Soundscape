package com.talons.RNUPSoundscape;

import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;
import java.io.Serializable;
import java.net.StandardProtocolFamily;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CurrentDataFragment extends Fragment implements View.OnClickListener {
    private static final String ARG_PARAM1 = "decibel_average";

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param averageDecibels the average decibels of a 10 second recording from RecordFragment
     * @return A new instance of fragment CurrentDataFragment.
     */
    public static CurrentDataFragment newInstance(StorageModel unit) {
        CurrentDataFragment fragment = new CurrentDataFragment();
        Bundle args = new Bundle();
        args.putSerializable( ARG_PARAM1, unit );
        fragment.setArguments( args );
        return fragment;
    }

    private double averageDecibels;
    private Button save;
    private Serializer serializer;
    private SessionManager sessionManager;
    private StorageModel unit;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        if (getArguments() != null) {
            unit = (StorageModel) getArguments().getSerializable( ARG_PARAM1 );
        }
        serializer = new Serializer();
        sessionManager = new SessionManager( Objects.requireNonNull( getContext() ) );
    }

    private View view;
    private TextView averageText;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate( R.layout.fragment_data, container, false );

        averageText = view.findViewById( R.id.average_num );
        int roundedDecibels = (int) Math.round( unit.getAverageDecibels() );

        averageText.setText( Integer.toString( roundedDecibels ) );

        save = view.findViewById( R.id.save );
        save.setOnClickListener( this );


        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach( context );
    }

    public List<StorageModel> getDatabase() {
        try {
            if(sessionManager.getDatabase() != null){
            String serializedDatabase = sessionManager.getDatabase();
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

    public void updateDatabase(StorageModel unit) {
        try {
            List<StorageModel> database = getDatabase();
            database.add( unit );
            String serializedDatabase = serializer.serialize( (Serializable) database );
            sessionManager.setDatabase( serializedDatabase );
        } catch (IOException e) {
        }
    }
    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case(R.id.save):
                updateDatabase( unit );
                save.setText( "Saved!" );
        }

    }
}
