package com.example.lianqy.doubleduck_android.model;

public class Rtinfo {
    String rtname; //餐厅名
    String rtdes; //餐厅描述
    String rtlocation; //餐厅地址
    String rtphone; //餐厅电话
    String rtlogo; //餐厅logo的url

    public Rtinfo(String rtname_, String rtdes_, String rtloc_, String rtphone_, String rtlogo_) {
        rtname = rtname_;
        rtdes = rtdes_;
        rtlocation = rtloc_;
        rtphone = rtphone_;
        rtlogo = rtlogo_;
    }

    public String getRtdes() {
        return rtdes;
    }

    public String getRtloc() {
        return rtlocation;
    }

    public String getRtlogo() {
        return rtlogo;
    }

    public String getRtname() {
        return rtname;
    }

    public String getRtphone() {
        return rtphone;
    }
}
