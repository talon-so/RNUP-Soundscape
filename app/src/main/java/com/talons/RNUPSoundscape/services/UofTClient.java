package com.talons.RNUPSoundscape.services;

import com.talons.RNUPSoundscape.model.Reading;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;


public interface UofTClient {

    @POST("/webservices/soundscape/submit/reading")
    Call<Reading> createReading(
            @Body Reading reading
    );



}
