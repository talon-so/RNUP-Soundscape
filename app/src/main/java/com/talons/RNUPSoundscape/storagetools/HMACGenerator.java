package com.talons.RNUPSoundscape.storagetools;

import android.util.Log;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.codec.binary.Base64;

public class HMACGenerator {
    public static String convertToSHA256(StorageModel unit) {
        try {
            String secret = "secret";
            String message = "";
            Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
            SecretKeySpec secret_key = new SecretKeySpec(secret.getBytes(), "HmacSHA256");
            sha256_HMAC.init(secret_key);

            String hash = Base64.encodeBase64String(sha256_HMAC.doFinal(message.getBytes()));
            System.out.println(hash);
            return hash;
        }
        catch (Exception e){
           Log.e("HMAC error", String.valueOf( e ) );
        }
        return "error";
    }
}