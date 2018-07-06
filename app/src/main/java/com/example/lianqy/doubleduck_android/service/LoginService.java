package com.example.lianqy.doubleduck_android.service;

import com.example.lianqy.doubleduck_android.model.Errorinfo;
import com.example.lianqy.doubleduck_android.model.LoginState;
import com.example.lianqy.doubleduck_android.model.PostDish;
import com.example.lianqy.doubleduck_android.model.Rtinfo;
import com.example.lianqy.doubleduck_android.model.Saler;
import com.example.lianqy.doubleduck_android.model.postcate;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface LoginService {
    @POST("v1/salers")
    Call<Errorinfo>getRegisterState(@Body Saler saler);

    @POST("v1/salers/login")
    Call<LoginState>getLoginState(@Body Saler saler);

    @GET("v1/rt/")
    Call<Rtinfo>getRtInfo(@Query("rtname") String rtname);

    @POST("v1/salers/cate")
    Call<LoginState>Postcate(@Body postcate cate);

    @POST("v1/salers/info")
    Call<LoginState>Postrt(@Body Rtinfo rtinfo);

    @POST("v1/salers/dish")
    Call<LoginState>Postdish(@Body PostDish postdish);
}
