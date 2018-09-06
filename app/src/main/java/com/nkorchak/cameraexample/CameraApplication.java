package com.nkorchak.cameraexample;

import android.app.Application;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CameraApplication extends Application {

    private static CameraApi cameraApi;

    @Override
    public void onCreate() {
        super.onCreate();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://18.188.170.218:5000")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        cameraApi = retrofit.create(CameraApi.class);
    }

    public static CameraApi getApi() {
        return cameraApi;
    }
}
