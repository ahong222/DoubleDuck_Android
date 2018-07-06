package com.example.lianqy.doubleduck_android.model;

public class PostDish {
    String name;
    String des;
    String pic;
    float price;
    int sale;
    String cate;
    String rt;

    public PostDish(String name_, String des_, float price_, String pic_, int sale_, String cate_, String rt_) {
        name = name_;
        des = des_;
        price = price_;
        pic = pic_;
        sale = sale_;
        cate = cate_;
        rt = rt_;
    }
}
