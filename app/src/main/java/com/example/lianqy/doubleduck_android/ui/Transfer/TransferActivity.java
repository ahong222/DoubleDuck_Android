package com.example.lianqy.doubleduck_android.ui.Transfer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.TextView;

import com.example.lianqy.doubleduck_android.R;
import com.example.lianqy.doubleduck_android.ui.ManageDishes.ManageDishesActivity;

import de.hdodenhof.circleimageview.CircleImageView;

public class TransferActivity extends AppCompatActivity {

    private CircleImageView restaurantIcon;
    private TextView restaurantName, restaurantDes;
    private TextView todayValidOrder, todayBusinessVolume;
    private CardView businessStatisticsCV, dishManagementCV, restaurantBulletinCV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer);

        setViews();
        setClicks();
    }


    private void setClicks() {
        //暂时只有菜品统计的界面跳转
        dishManagementCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(TransferActivity.this, ManageDishesActivity.class);
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

}
