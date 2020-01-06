package com.example.flow.services;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitBuild {
    private static RetrofitBuild retrofitBuild = null;
    private Retrofit retrofit;
    public ApiService apiService;

    private RetrofitBuild(){

        // Create/allocate Retrofit object
        retrofit = new Retrofit.Builder()
                .baseUrl("https://trekyourwalet-bdp.azurewebsites.net/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        // ApiService has the appending http calls.
        //      -> Needed to make Any ApiCall
        apiService = retrofit.create(ApiService.class);
    }

    // Gives an instance of RetrofitBuild
    public static RetrofitBuild getInstance()
    {
        if (retrofitBuild == null)
            retrofitBuild = new RetrofitBuild();

        return retrofitBuild;
    }


}
