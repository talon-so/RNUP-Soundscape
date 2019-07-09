package com.talons.RNUPSoundscape.storagetools;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.google.android.gms.auth.api.signin.internal.Storage;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CSVGenerator {

    public static void generate(Context context) throws Exception {
        if (isExternalStorageWritable()) {
            String csvFile = context.getExternalFilesDir( Environment.DIRECTORY_DOCUMENTS ).getAbsolutePath()+"RNUP_Data.csv";
            FileWriter writer = new FileWriter( csvFile );
            Serializer serializer = new Serializer();
            SessionManager sessionManager = new SessionManager( context );
            List<StorageModel> db = new ArrayList<>(  );
            try {
                if(sessionManager.getDatabase() != null){
                    String serializedDatabase = sessionManager.getDatabase();
                    db = (List<StorageModel>) serializer.deserialize(serializedDatabase);
                }
            } catch (IOException e) {
                Log.e("IOException", String.valueOf( e ) );
            } catch (ClassNotFoundException e) {
                Log.e("ClassNotFoundException", String.valueOf( e ) );
            }

                writeLine( writer, "Average Decibels", "Latitude", "Longitude", "Model", "API Level", "Timestamp" );
            for (StorageModel item : db){
                writeLine( writer ,Double.toString( item.getAverageDecibels() ),Double.toString( item.getLatitude() ), Double.toString( item.getLongitude() ), item.getPhoneModel(), item.getPhoneApiLevel(),item.getStandardTime());
            }
            writer.flush();
            writer.close();
        }
    }
    /* Checks if external storage is available for read and write */
    private static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    private static void writeLine(FileWriter writer, String decibelsStr, String latStr, String lngStr, String phoneModel, String phoneApiLevel, String time){
        try {
            writer.append( decibelsStr ).append( "," ).append( latStr ).append( "," ).append( lngStr ).append( "," ).append( phoneModel ).append( "," ).append( phoneApiLevel ).append( "," ).append( time ).append( "\n" );
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
