package com.example.lianqy.doubleduck_android.ui.Transfer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.lianqy.doubleduck_android.R;
import com.example.lianqy.doubleduck_android.model.Rtinfo;
import com.example.lianqy.doubleduck_android.service.LoginService;
import com.example.lianqy.doubleduck_android.ui.ManageDishes.ManageDishesActivity;
import com.example.lianqy.doubleduck_android.ui.Order.OrderListActivity;
import com.example.lianqy.doubleduck_android.ui.RetaurantDetail.RestaurantDetailActivity;
import com.example.lianqy.doubleduck_android.ui.Statistics.ResStatisticsActivity;
import com.example.lianqy.doubleduck_android.ui.Transfer.bus.ChangeSalerInfoBusEvent;
import com.example.lianqy.doubleduck_android.util.BitmapUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TransferActivity extends AppCompatActivity {


    private CircleImageView restaurantIcon;
    private TextView restaurantName, restaurantDes;
    private TextView todayValidOrder, todayBusinessVolume;
    private CardView businessStatisticsCV, dishManagementCV, restaurantBulletinCV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer);

        //注册订阅者
        EventBus.getDefault().register(this);

        getRtInfo(); //最初的时候获取饭店信息
        setViews();
        setClicks();
    }


    private void setClicks() {

        dishManagementCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(TransferActivity.this, ManageDishesActivity.class);
                startActivity(intent);
            }
        });

        todayValidOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转到订单列表的界面
                Intent intent = new Intent();
                intent.setClass(TransferActivity.this, OrderListActivity.class);
                startActivity(intent);
            }
        });

        restaurantBulletinCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转到商家详情界面
                Intent intent = new Intent();
                intent.setClass(TransferActivity.this, RestaurantDetailActivity.class);
                //此处intent要传入商家的信息参数
                startActivity(intent);
            }
        });

        businessStatisticsCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转到统计界面
                Intent intent = new Intent();
                intent.setClass(TransferActivity.this, ResStatisticsActivity.class);
                //此处intent要传入商家的信息参数
                startActivity(intent);
            }
        });
    }



    private void setViews() {
        restaurantIcon = findViewById(R.id.restaurantIcon);
        restaurantName = findViewById(R.id.restaurantName);
        restaurantDes = findViewById(R.id.restaurantDes);
        todayValidOrder = findViewById(R.id.todayValidOrder);
        todayBusinessVolume = findViewById(R.id.todayBusinessVolume);
        businessStatisticsCV = findViewById(R.id.businessStatisticsCV);
        dishManagementCV = findViewById(R.id.dishManagementCV);
        restaurantBulletinCV = findViewById(R.id.restaurantBulletinCV);

        //当有餐厅的信息时对上面的组件赋值
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void changeSalerInfoBusEvent(ChangeSalerInfoBusEvent event){
        //对 组件 进行更新
        restaurantIcon.setImageBitmap(BitmapUtil.byteArrayToBitmap(event.logo));
        restaurantDes.setText(event.des);
        restaurantName.setText(event.name);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //注销注册
        EventBus.getDefault().unregister(this);
    }

    private void getRtInfo() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://172.18.218.192:9090/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        LoginService service = retrofit.create(LoginService.class);
        Call<Rtinfo> getrtinfo = service.getRtInfo("RT1");
        getrtinfo.enqueue(new Callback<Rtinfo>() {
            @Override
            public void onResponse(Call<Rtinfo> call, Response<Rtinfo> response) {
                Rtinfo temp = response.body();
                Log.d("output", temp.getRtname());
                Log.d("output", temp.getRtloc());
            }

            @Override
            public void onFailure(Call<Rtinfo> call, Throwable t) {

            }
        });
    }

}
