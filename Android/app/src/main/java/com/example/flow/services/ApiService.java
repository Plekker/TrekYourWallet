package com.example.flow.services;

import com.example.flow.classes.Expense;
import com.example.flow.classes.Person;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiService {

    @GET("api/Account")
    Call<Person> getPersonForApiKey(@Header("x-api-key")String ApiKey);

    @POST("api/Account/Login")
    Call<Person> getApiKey(@Header("Content-Type") String contentType, @Body Person person);

    @POST("api/Account/Register")
    Call<ResponseBody> register(@Header("Content-Type") String contentType, @Body Person person);

    @GET("api/Expenses/currentTrip")
    Call<List<Expense>> getExpensesForCurrentTrip(@Header("Content-Type") String contentType, @Header("x-api-key")String ApiKey);

    @POST("api/Account/changePassword")
    Call<ResponseBody> updatePassword(@Header("Content-Type") String contentType, @Header("x-api-key")String ApiKey, @Body ChangePasswordInfo info);

    @POST("api/Trips/AddExpense/{tripId}")
    Call<Expense> addExpense(@Header("Content-Type") String contentType, @Header("x-api-key")String ApiKey, @Path("tripId") int tripId, @Body Expense expense);
}
