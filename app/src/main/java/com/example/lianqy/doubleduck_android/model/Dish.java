package com.example.lianqy.doubleduck_android.model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;

import com.example.lianqy.doubleduck_android.R;
import com.example.lianqy.doubleduck_android.ui.ManageDishes.ManageDishesActivity;
import com.example.lianqy.doubleduck_android.util.BitmapUtil;

public class Dish {
    private byte[] src;
    private String name;
    private String price;
    private String des;
    private String type;

    public Dish(String name, String price, String type, String des, byte[] src){
        this.name = name;
        this.price = price;
        this.type = type;
        this.des = des;
        this.src = src;
    }

    public Dish(String name, String price, String type, byte[] src){
        this(name, price, type,"", src);
    }

    public Dish(String name, String type, String price, String des){
        this(name, price, type, des, null);
    }


    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getPrice() {
        return price;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public String getDes() {
        return des;
    }

    public void setSrc(byte[] src) {
        this.src = src;
    }

    public byte[] getSrc() {
        return src;
    }
}
