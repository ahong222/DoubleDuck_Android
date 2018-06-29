package com.example.lianqy.doubleduck_android.model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;

import com.example.lianqy.doubleduck_android.R;
import com.example.lianqy.doubleduck_android.ui.ManageDishes.ManageDishesActivity;
import com.example.lianqy.doubleduck_android.util.BitmapUtil;

import java.io.Serializable;

public class Dish implements Serializable {
    private byte[] src;
    private String name;
    private String price;
    private String des;
    private String type;

    //order类的dish还要有一个份数
    private int copy;

    public Dish(String name, String price, String type, String des, byte[] src, int copy){
        this.name = name;
        this.price = price;
        this.type = type;
        this.des = des;
        this.src = src;
        this.copy = copy;
    }

    public Dish(String name, String price, String type, String des, byte[] src){
        this(name, price, type,des, src, 0);
    }

    public Dish(String name, String price, String type, byte[] src){
        this(name, price, type,"", src, 0);
    }

    public Dish(String name, String type, String price, String des){
        this(name, price, type, des, null, 0);
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

    public int getCopy() {
        return copy;
    }

    public void setCopy(int copy) {
        this.copy = copy;
    }
}
