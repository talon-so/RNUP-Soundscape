package com.talons.RNUPSoundscape.services;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;



public class UofTServiceGenerator {

    private static final String BASE_URL = "https://www-beta.utsc.utoronto.ca/webservices/soundscape/";

    private static Gson gson = new GsonBuilder()
            .setLenient()
            .create();

    private static Retrofit.Builder builder =
            new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory( ScalarsConverterFactory.create())
                    .addConverterFactory( GsonConverterFactory.create( gson ) );
    private static Retrofit retrofit = builder.build();

    private static HttpLoggingInterceptor logging = new HttpLoggingInterceptor()
            .setLevel(HttpLoggingInterceptor.Level.BODY);

    private static OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

    public static <S> S createService(Class<S> serviceClass) {
        return createService(serviceClass, new Interceptor[]{});
    }

    public static <S> S createService(Class<S> serviceClass, Interceptor[] interceptors) {
        if (!httpClient.interceptors().contains(logging)) {

            for (Interceptor interceptor : interceptors) {
                httpClient.addInterceptor(interceptor);
            }

            httpClient.addInterceptor(logging);

            builder = builder.client(httpClient.build());
            retrofit = builder.build();
        }

        return retrofit.create(serviceClass);
    }
}
