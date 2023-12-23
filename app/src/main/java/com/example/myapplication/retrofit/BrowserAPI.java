package com.example.myapplication.retrofit;

import com.example.myapplication.model.UserModel;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface BrowserAPI {
    @POST("login.php")
    @FormUrlEncoded
    Call<UserModel> getUser(@Field("phone") String phone);

    @POST("signup.php")
    @FormUrlEncoded
    Call<UserModel> insertUser(@Field("phone") String phone,@Field("name") String name,@Field("password") String password);
}
