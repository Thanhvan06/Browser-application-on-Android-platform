package com.example.myapplication.repositories;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.myapplication.model.UserModel;
import com.example.myapplication.retrofit.BrowserAPI;
import com.example.myapplication.retrofit.RetrofitInstance;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserRepository {
    private BrowserAPI browserAPI;
    public UserRepository(){
        browserAPI = RetrofitInstance.getRetrofit().create(BrowserAPI.class);
    }
    public MutableLiveData<UserModel> getUser(String phone){
        MutableLiveData<UserModel> data = new MutableLiveData<>();
        browserAPI.getUser(phone).enqueue(new Callback<UserModel>() {
            @Override
            public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                data.setValue(response.body());
            }

            @Override
            public void onFailure(Call<UserModel> call, Throwable t) {
                Log.d("logg",t.getMessage());
                data.setValue(null);
            }
        });
        return data;
    }
    public MutableLiveData<UserModel> insertUser(String phone,String name,String password){
        MutableLiveData<UserModel> data = new MutableLiveData<>();
        browserAPI.insertUser(phone,name,password).enqueue(new Callback<UserModel>() {
            @Override
            public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                data.setValue(response.body());
            }

            @Override
            public void onFailure(Call<UserModel> call, Throwable t) {
                Log.d("logg",t.getMessage());
                data.setValue(null);
            }
        });
        return data;
    }
}
