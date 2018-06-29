package com.example.lianqy.doubleduck_android.ui.Order;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.lianqy.doubleduck_android.R;
import com.example.lianqy.doubleduck_android.model.Dish;
import com.example.lianqy.doubleduck_android.model.Order;
import com.example.lianqy.doubleduck_android.ui.Order.adapter.OrderAdapter;
import com.example.lianqy.doubleduck_android.util.BitmapUtil;

import java.util.ArrayList;

public class OrderListActivity extends AppCompatActivity {

    private RecyclerView orderListRV;
    private OrderAdapter mOrderAdapter;
    private ArrayList<Order> mOrders = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_list);

        setViews();
        initTestOrderListData();
        setOrderList();
    }


    private void setOrderList() {
        orderListRV.setLayoutManager(new LinearLayoutManager(this));
        mOrderAdapter = new OrderAdapter(mOrders, null);

        mOrderAdapter.setOnItemClickListener(new OrderAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                //点击跳转到order ddetail fragent
                //并将该order传递过去
                Order or = mOrders.get(position);
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putSerializable("order", or);
                intent.putExtras(bundle);
                intent.setClass(OrderListActivity.this, OrderDetailActivity.class);
                startActivity(intent);
            }

            @Override
            public void onLongClick(int position) {

            }
        });

        orderListRV.setAdapter(mOrderAdapter);
    }

    private void setViews() {
        orderListRV = findViewById(R.id.orderListRV);
    }

    private void initTestOrderListData() {
        ArrayList<Dish> dishes = new ArrayList<>();

        Bitmap bitmap = BitmapFactory.decodeResource(OrderListActivity.this.getResources(), R.drawable.ic_happy_64);
        byte[] array = BitmapUtil.bitmapToByteArray(bitmap);
        for (int i = 0; i < 5; i++) {
            Dish d = new Dish("t1 " + Integer.toString(i), "¥ " + Integer.toString(i * 2),
                    Integer.toString(i / 10 + 1), "i am dish " + Integer.toString(i) + "哈哈哈哈啊啊哈哈哈哈哈啊啊哈",
                    array, 5);

            dishes.add(d);
        }
        Order o1 = new Order(1, 1, dishes, 50, 1);
        dishes.remove(dishes.size()-1);
        Order o2 = new Order(2, 1, dishes, 52, 2);
        dishes.remove(dishes.size()-1);
        Order o3 = new Order(3, 1, dishes, 54, 3);

        mOrders.add(o1);
        mOrders.add(o2);
        mOrders.add(o3);
    }

    //点击返回键结束该activity
    @Override
    public void onBackPressed(){
        finish();
    }

}
