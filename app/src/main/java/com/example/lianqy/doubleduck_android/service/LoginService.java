package com.example.lianqy.doubleduck_android.service;

import com.example.lianqy.doubleduck_android.model.LoginState;
import com.example.lianqy.doubleduck_android.model.Saler;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface LoginService {
    @POST("v1/salers")
    Call<LoginState>getRegisterState(@Body Saler saler);
}
