package com.example.myapplication.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.myapplication.model.UserModel;
import com.example.myapplication.repositories.UserRepository;

public class UserViewModel extends ViewModel {
    private UserRepository userRepository;

    public UserViewModel() {
        this.userRepository = new UserRepository();
    }
    public MutableLiveData<UserModel> userModelMutableLiveDataLogin(String phone){
        return userRepository.getUser(phone);
    }
    public MutableLiveData<UserModel> userModelMutableLiveDataSignup(String phone,String name,String pass){
        return userRepository.insertUser(phone,name,pass);
    }
}
