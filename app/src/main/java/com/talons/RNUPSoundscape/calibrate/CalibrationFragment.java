package com.talons.RNUPSoundscape.calibrate;

import android.content.Context;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.talons.RNUPSoundscape.R;
import com.talons.RNUPSoundscape.storagetools.Serializer;
import com.talons.RNUPSoundscape.storagetools.SessionManager;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Objects;

import static androidx.core.content.PermissionChecker.checkSelfPermission;
import static java.lang.Math.log10;


public class CalibrationFragment extends Fragment implements View.OnClickListener{

    private static final int RECORD_AUDIO = 3 ;
    private static final int FINE_LOCATION = 5;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment RecordFragment.
     */
    public static CalibrationFragment newInstance() {
        CalibrationFragment fragment = new CalibrationFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    Serializer serializer;
    SessionManager sessionManager;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        serializer = new Serializer();
        sessionManager = new SessionManager( Objects.requireNonNull( getContext() ) );
    }

    MediaRecorder recorder;
    View view;
    TextView textView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate( R.layout.fragment_calibrate, container, false);
        setOnClickListeners();
        textView = view.findViewById(R.id.decibels);
        constantTextview = view.findViewById( R.id.constant_textview );
        startCalibration();
        // Inflate the layout for this fragment
        return view;
    }

    ImageButton  addBtn, subtractBtn;
    Button calibrateBtn;
    public void setOnClickListeners(){
        calibrateBtn = view.findViewById(R.id.finish_button);
        calibrateBtn.setOnClickListener( this );
        addBtn = view.findViewById( R.id.add );
        addBtn.setOnClickListener( this );
        subtractBtn = view.findViewById( R.id.subtract);
        subtractBtn.setOnClickListener( this );
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
                startCalibration();
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

    /**
     * @return db a double with the decibel value of the current intake of mic audio
     */
    public double getAmplitude() {
        double db;
        if (recorder != null) {
            db = 20.0 * log10( (recorder.getMaxAmplitude()));
            // if db result is infinity return 0
            if (db < 0) {
                return 0;
            }
            return (double)Math.round(db * 100d) / 100d;
        }
        else
            return 0;

    }

    /**
     *  Checks if external storage is available for read and write
     *  */
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

    private void startCalibration(){
        recorder = new MediaRecorder();
        //recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        AudioManager audioManager = (AudioManager)getActivity().getSystemService(Context.AUDIO_SERVICE);
        // get unprocessed mic data if applicable (API24 and above on some device)
        if(audioManager.getProperty(AudioManager.PROPERTY_SUPPORT_AUDIO_SOURCE_UNPROCESSED) !=null) {
            recorder.setAudioSource( MediaRecorder.AudioSource.UNPROCESSED );
            Log.i( "mic used", "unprocessed" );
        } else {
            // use voice recognition, closest thing to clean audio
            recorder.setAudioSource( MediaRecorder.AudioSource.VOICE_RECOGNITION);
            Log.i( "mic used", "voice recognition" );
        }
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        Log.i("File Path: ", getActivity().getExternalCacheDir().getAbsolutePath());
        if (isExternalStorageWritable()){
            recorder.setOutputFile(getActivity().getExternalCacheDir().getAbsolutePath() + "/temp/");
        } else {
            Log.e("File Path: ", "external cache not available");
        }
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            recorder.prepare();
        } catch (IOException e) {
            Log.e("prepare() failed", String.valueOf(e));
        }
        recorder.start();

        final Handler handler = new Handler();
        handler.post( new Runnable() {
            @Override
            public void run() {
                Double maxAmplitude = getAmplitude() + calibrationConstant ;
                DecimalFormat df = new DecimalFormat();
                df.setMaximumFractionDigits( 2 );
                df.setMinimumFractionDigits( 2 );
                textView.setText( df.format( maxAmplitude ) );
                handler.postDelayed( this, 100 );
            }
        });
    }

    public void onBackPressed() {
        stopCalibration();
        getActivity().finish();

    }

    public void updateAmplitudeReference(int calibrationConstant) {
            sessionManager.setAmplitudeRef( calibrationConstant );
    }

    public void stopCalibration() {
        try {
            recorder.stop();
        } catch(RuntimeException e) {
            //mFile.delete();  //you must delete the outputfile when the recorder stop failed.
        } finally {
            recorder.reset();    // set state to idle
            recorder.release();  // release resources back to the system
            recorder = null;
            updateAmplitudeReference( calibrationConstant );
        }

    }
    TextView constantTextview;
    public void updateConstantTextView(){
        constantTextview.setText( Integer.toString(calibrationConstant) );
    }
    int calibrationConstant = 0;
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.finish_button:
                   stopCalibration();
                   getActivity().finish();
               break;
            case R.id.add:
                calibrationConstant++;
                updateConstantTextView();
                break;
            case R.id.subtract:
                calibrationConstant--;
                updateConstantTextView();
                break;
        }
    }
}
