package com.example.lianqy.doubleduck_android.ui.Order;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.example.lianqy.doubleduck_android.R;
import com.example.lianqy.doubleduck_android.model.Dish;
import com.example.lianqy.doubleduck_android.model.Order;
import com.example.lianqy.doubleduck_android.ui.Order.adapter.DishInOrderAdapter;
import com.example.lianqy.doubleduck_android.util.BitmapUtil;

import java.util.ArrayList;
import java.util.List;

public class OrderDetailActivity extends AppCompatActivity {

    private Order order;

    private List<Dish> mDishes = new ArrayList<>();
    private TextView orderNum, state, deskNum, amount;
    private RecyclerView dishListInOrderRV;
    private DishInOrderAdapter mDishInOrderAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_order_detail);

        initTestList();
        setViews();
        setDishListInOrder();
    }


    private void setDishListInOrder() {
        dishListInOrderRV.setLayoutManager(new LinearLayoutManager(this));
        mDishInOrderAdapter = new DishInOrderAdapter(mDishes, null);

        mDishInOrderAdapter.setOnItemClickListener(new DishInOrderAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {

            }

            @Override
            public void onLongClick(int position) {

            }
        });
        dishListInOrderRV.setAdapter(mDishInOrderAdapter);
    }

    private void setViews() {
        orderNum = findViewById(R.id.orderNum);
        deskNum = findViewById(R.id.deskNum);
        state = findViewById(R.id.state);
        amount = findViewById(R.id.amount);
        dishListInOrderRV = findViewById(R.id.orderDishListRV);

        orderNum.setText(String.valueOf(order.getOrderNum()));
        deskNum.setText(String.valueOf(order.getDeskNum()));
        state.setText(String.valueOf(order.getState()));
        ///....amount赋值
    }

    private void initTestList() {
         Intent intent = this.getIntent();
         order = (Order)intent.getSerializableExtra("order");

         /*
        ArrayList<Dish> dishes = new ArrayList<>();

        Bitmap bitmap = BitmapFactory.decodeResource(OrderDetailActivity.this.getResources(), R.drawable.ic_happy_64);
        byte[] array = BitmapUtil.bitmapToByteArray(bitmap);
        for (int i = 0; i < 5; i++) {
            Dish d = new Dish("t1 " + Integer.toString(i), "¥ " + Integer.toString(i * 2),
                    Integer.toString(i / 10 + 1), "i am dish " + Integer.toString(i) + "哈哈哈哈啊啊哈哈哈哈哈啊啊哈",
                    array, 5);

            dishes.add(d);
        }
        order = new Order(1,1, dishes, 58, 3);
        mDishes = order.getDishs();
        */

         mDishes = order.getDishs();
    }

    //点击返回键结束该activity
    @Override
    public void onBackPressed(){
        finish();
    }


}
