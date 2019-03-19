package com.talons.RNUPSoundscape;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.talons.RNUPSoundscape.R;
import java.io.IOException;
import java.util.Objects;

import static android.support.v4.content.PermissionChecker.PERMISSION_DENIED;
import static android.support.v4.content.PermissionChecker.checkSelfPermission;
import static java.lang.Math.log10;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RecordFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link RecordFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RecordFragment extends Fragment implements View.OnClickListener{


    private static final int RECORD_AUDIO = 3 ;
    private static final int FINE_LOCATION = 5;
    private OnFragmentInteractionListener mListener;

    public RecordFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment RecordFragment.
     */
    public static RecordFragment newInstance() {
        RecordFragment fragment = new RecordFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    Button button;
    MediaRecorder recorder;
    View view;
    TextView averageDecibels;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_record, container, false);
        button = view.findViewById(R.id.button);
        button.setOnClickListener(this);
        averageDecibels = view.findViewById(R.id.decibels);
        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    final static int PERMISSION_ALL = 1;
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case PERMISSION_ALL:
                startRecording();
                break;
                default:
                    break;
        }
        //if (!permissionToRecordAccepted ) finish();
    }

    public boolean checkPermissions( String[] permissions){
            if (permissions != null) {
                for (String permission : permissions) {
                    if (checkSelfPermission(getContext(), permission) != PackageManager.PERMISSION_GRANTED) {
                        return false;
                    }
                }
            }
            return true;
    }

    public double getAmplitude() {
        double db;
        if (recorder != null) {
            db = 20.0 * log10( (recorder.getMaxAmplitude()) / 32767.0 );
            return db;
        }
        else
            return 0;

    }

    /* Checks if external storage is available for read and write */
    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    public void onPause()
    {
        super.onPause();
    }

    private int seconds = 10;
    private void startRecording() {
        recorder = new MediaRecorder();
        //recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        AudioManager audioManager = (AudioManager)getActivity().getSystemService(Context.AUDIO_SERVICE);
        // get unprocessed mic data if applicable (API24 and above on some device)
        if(audioManager.getProperty(AudioManager.PROPERTY_SUPPORT_AUDIO_SOURCE_UNPROCESSED) !=null)
            recorder.setAudioSource(MediaRecorder.AudioSource.UNPROCESSED);
        else
            // use voice recognition, closest thing to clean audio
            recorder.setAudioSource(MediaRecorder.AudioSource.VOICE_RECOGNITION);

        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        Log.i("File Path: ", getActivity().getExternalCacheDir().getAbsolutePath());
        if (isExternalStorageWritable()){
        recorder.setOutputFile(getActivity().getExternalCacheDir().getAbsolutePath() + "/temp");
        }else
        {
            Log.e("File Path: ", "external cache not available");
        }
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            recorder.prepare();
        } catch (IOException e) {
            Log.e("prepare() failed", String.valueOf(e));
        }

        recorder.start();
        button.setText("recording ");
        // stop recording after 10 seconds
        final Handler handler = new Handler();
        handler.post( new Runnable() {
            @Override
            public void run() {
                if (seconds > 0){
                    button.setText( "recording " + seconds );
                    averageDecibels.setText( Double.toString( getAmplitude()) );
                    seconds--;
                    handler.postDelayed(this,1000 );
                } else {
                    button.setText( "calculating..." );
                    stopRecording();
                    // reset the recording seconds
                    seconds = 10;
                }

            }
        } );

    }

    private void stopRecording() {
        try {
            recorder.stop();
        } catch(RuntimeException e) {
            //mFile.delete();  //you must delete the outputfile when the recorder stop failed.
        } finally {
            recorder.release();
            recorder = null;
        }
    }

    // record time at 10 seconds
    private static final int RECORD_TIME = 10000;
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button:
                Log.i( "boop","record pressed" );
                String[] PERMISSIONS = {
                        Manifest.permission.RECORD_AUDIO,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.ACCESS_FINE_LOCATION
                };

                if(!checkPermissions(PERMISSIONS)){
                    requestPermissions(PERMISSIONS, PERMISSION_ALL);
                }else {
                    startRecording();
                }

        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
