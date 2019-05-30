package com.talons.RNUPSoundscape;

import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class DataFragment extends Fragment {
    private static final String ARG_PARAM1 = "decibel_average";

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param averageDecibels the average decibels of a 10 second recording from RecordFragment
     * @return A new instance of fragment DataFragment.
     */
    public static DataFragment newInstance(double averageDecibels) {
        DataFragment fragment = new DataFragment();
        Bundle args = new Bundle();
        args.putDouble( ARG_PARAM1, averageDecibels );
        fragment.setArguments( args );
        return fragment;
    }

    private double averageDecibels;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        if (getArguments() != null) {
            averageDecibels = getArguments().getDouble( ARG_PARAM1 );
        }
    }

    View view;
    TextView averageText;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate( R.layout.fragment_data, container, false );

        averageText = view.findViewById( R.id.average_num );
        int roundedDecibels = (int) Math.round( averageDecibels );
        averageText.setText( Integer.toString( roundedDecibels ) );
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach( context );
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}
