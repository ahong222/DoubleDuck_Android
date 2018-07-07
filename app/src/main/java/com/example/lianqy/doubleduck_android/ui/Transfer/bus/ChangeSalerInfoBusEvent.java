package com.example.lianqy.doubleduck_android.ui.Transfer.bus;


public class ChangeSalerInfoBusEvent {
    public String name;
    public String des;
    public String loc;
    public String phone;
    public byte[] logo;

    public ChangeSalerInfoBusEvent(String name, String des, String loc, String phone, byte[] logo){
        this.name = name;
        this.des = des;
        this.loc = loc;
        this.phone = phone;
        this.logo = logo;
    }
}
