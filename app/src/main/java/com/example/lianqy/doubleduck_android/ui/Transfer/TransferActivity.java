package com.example.lianqy.doubleduck_android.ui.Transfer;

import android.content.Intent;
import android.graphics.BitmapFactory;
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
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.lianqy.doubleduck_android.ui.Login.LoginActivity.RTNAME;

public class TransferActivity extends AppCompatActivity {


    private CircleImageView restaurantIcon;
    private TextView restaurantName, restaurantDes;
    private TextView todayValidOrder, todayBusinessVolume;
    private CardView businessStatisticsCV, dishManagementCV, restaurantBulletinCV;
    private String RtInfoRTname;
    private String RtInfoDes;

    public static String NAME_FROM_TRANSFER = "name";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer);

        //注册订阅者
        EventBus.getDefault().register(this);
        setViews();

        getRtInfo(); //最初的时候获取饭店信息
        setClicks();
    }


    private void setClicks() {

        dishManagementCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra(NAME_FROM_TRANSFER, RtInfoRTname);
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
                intent.putExtra(NAME_FROM_TRANSFER, RtInfoRTname);
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
                intent.putExtra(NAME_FROM_TRANSFER, RtInfoRTname);
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
                intent.putExtra(NAME_FROM_TRANSFER, RtInfoRTname);
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

    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void changeSalerInfoBusEvent(ChangeSalerInfoBusEvent event){
        //对 组件 进行更新
        restaurantIcon.setImageBitmap(BitmapUtil.byteArrayToBitmap(event.logo));
        restaurantDes.setText(event.des);
        restaurantName.setText(event.name);

        RtInfoRTname = event.name;
        //RtInfoDes = event.des;

        //post新的信息到服务器
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //注销注册
        EventBus.getDefault().unregister(this);
    }

    private void getRtInfo() {

        Intent i = this.getIntent();
        RtInfoRTname = i.getStringExtra(RTNAME);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://172.18.218.192:9090/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        LoginService service = retrofit.create(LoginService.class);
        Call<Rtinfo> getrtinfo = service.getRtInfo(RtInfoRTname);
        getrtinfo.enqueue(new Callback<Rtinfo>() {
            @Override
            public void onResponse(Call<Rtinfo> call, Response<Rtinfo> response) {
                Rtinfo temp = response.body();
                String slogo = temp.getRtlogo();
                RtInfoRTname = temp.getRtname();
                RtInfoDes = temp.getRtdes();
                Log.d("outputName", temp.getRtname());
                Log.d("outputDes", temp.getRtdes());

                String[] sub = slogo.split("/");

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("http://p8pbukobc.bkt.clouddn.com/")
                        .build();
                LoginService service = retrofit.create(LoginService.class);
                Call<ResponseBody>getimg = service.Getpic(sub[sub.length - 1]);
                getimg.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        Log.d("output", "success");
                        restaurantIcon.setImageBitmap(BitmapFactory.decodeStream(response.body().byteStream()));
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.d("output", "fail");
                    }
                });


                //当有餐厅的信息时对上面的组件赋值
                restaurantName.setText(RtInfoRTname);
                restaurantDes.setText(RtInfoDes);
            }

            @Override
            public void onFailure(Call<Rtinfo> call, Throwable t) {

            }
        });
    }

}
