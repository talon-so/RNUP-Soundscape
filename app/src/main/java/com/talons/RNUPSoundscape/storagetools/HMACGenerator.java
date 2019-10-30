package com.talons.RNUPSoundscape.storagetools;

import android.util.Log;

import com.talons.RNUPSoundscape.model.Reading;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;


import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;

public class HMACGenerator {
    public static String convertToSHA256(Reading reading) {
        try {
            String secret = "ke7pui5ioh6AhJieS8dee8yohshaeSah";
            String message = "averageDecibel:" + reading.getAverageDecibel()
                    + ":deviceApiLevel:" + reading.getDeviceApiLevel()
                    + ":deviceModel:" + reading.getDeviceModel()
                    + ":gpsLatitude:" + reading.getGpsLatitude()
                    + ":gpsLongitude:" + reading.getGpsLongitude()
                    + ":reportingDate:" + reading.getReportingDate();
            Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
            SecretKeySpec secret_key = new SecretKeySpec(secret.getBytes(), "HmacSHA256");
            sha256_HMAC.init(secret_key);

            String hash = new String(Hex.encodeHex(sha256_HMAC.doFinal(message.getBytes())));
            System.out.println(hash);
            return hash;
        }
        catch (Exception e){
           Log.e("HMAC error", String.valueOf( e ) );
        }
        return "error";
    }
}