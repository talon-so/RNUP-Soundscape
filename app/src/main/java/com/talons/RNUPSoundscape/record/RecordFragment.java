package com.talons.RNUPSoundscape.record;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.talons.RNUPSoundscape.R;
import com.talons.RNUPSoundscape.calibrate.CalibrationActivity;
import com.talons.RNUPSoundscape.storagetools.Serializer;
import com.talons.RNUPSoundscape.storagetools.SessionManager;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Objects;

import static androidx.core.content.PermissionChecker.checkSelfPermission;
import static java.lang.Math.log10;



public class RecordFragment extends Fragment implements View.OnClickListener{

    RecordingCompleteCallback callback;
    public interface RecordingCompleteCallback {
        void onRecordComplete(double averageDecibels) throws IOException;
    }

    private static final int RECORD_AUDIO = 3 ;
    private static final int FINE_LOCATION = 5;

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

    Serializer serializer;
    SessionManager sessionManager;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        serializer = new Serializer();
        sessionManager = new SessionManager( Objects.requireNonNull( getContext() ) );
    }

    ImageButton calibrateBtn;
    Button recordBtn;
    MediaRecorder recorder;
    View view;
    TextView textView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate( R.layout.fragment_record, container, false);
        setOnClickListeners();
        textView = view.findViewById(R.id.decibels);
        callback = (RecordingCompleteCallback) getContext();
        // Inflate the layout for this fragment
        return view;
    }

    public void setOnClickListeners(){
        recordBtn = view.findViewById(R.id.button);
        recordBtn.setOnClickListener(this);
        calibrateBtn = view.findViewById(R.id.calibration_button);
        calibrateBtn.setOnClickListener(this);
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

    private int seconds = 10;
    private int halfSeconds = 20;
    private int tenthofSeconds = 100;
    private int decibelAverage = 0;
    private double totalDecibels = 0.0;
    private double averageDecibels = 0.0;
    private void startRecording() {
        calibrateBtn.setOnClickListener( null );
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
        recorder.setOutputFile(getActivity().getExternalCacheDir().getAbsolutePath() + "/temp");
        } else {
            Log.e("File Path: ", "external cache not available");
        }
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            recorder.prepare();
        } catch (IOException e) {
            Log.e("prepare() failed", String.valueOf(e));
        }

        ((RecordActivity)getActivity()).viewCollectedData.setOnClickListener( null );
        recorder.start();
        // turn off recordBtn
        recordBtn.setEnabled( false );
        // stop recording after 10 seconds
        final Handler handler = new Handler();
        handler.post( new Runnable() {
            @Override
            public void run() {
                if (tenthofSeconds > 0){
                    if (tenthofSeconds < 99) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                // UI updation related code.
                                recordBtn.setText( "Recording " + tenthofSeconds/10 );
                            }
                        });
                    }
                    Double maxAmplitude = getAmplitude() + sessionManager.getAmplitudeRef();
                    DecimalFormat df = new DecimalFormat();
                    df.setMaximumFractionDigits(2);
                    df.setMinimumFractionDigits(2);
                    textView.setText( df.format( maxAmplitude ));
                    totalDecibels += maxAmplitude;
                    // subtract our counters
                    tenthofSeconds--;
                    handler.postDelayed(this,100 );
                } else {
                    stopRecording();
                    // reset the recording seconds
                    seconds = 10;
                    tenthofSeconds = 100;
                    recordBtn.setEnabled( true );
                    averageDecibels = totalDecibels/100;
                    // notify activity
                    try {
                        Log.i("decibels", "total: " + totalDecibels +  " average: " + averageDecibels);
                        callback.onRecordComplete( averageDecibels );
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    totalDecibels = 0;
                    averageDecibels = 0;
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
            recorder.reset();    // set state to idle
            recorder.release();  // release resources back to the system
            recorder = null;
            ((RecordActivity) getActivity()).viewCollectedData.setOnClickListener( (View.OnClickListener) getActivity() );
            calibrateBtn.setOnClickListener( this );
        }
       }

    private String[] PERMISSIONS = {
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button:
                    if (!checkPermissions( PERMISSIONS )) {
                        requestPermissions( PERMISSIONS, PERMISSION_ALL );
                    } else {
                        startRecording();
                    }
                break;
            case R.id.calibration_button:
                Intent intent = new Intent(getContext(), CalibrationActivity.class);
                startActivity(intent);
                break;
        }
    }
}
