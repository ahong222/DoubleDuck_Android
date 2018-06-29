package com.example.lianqy.doubleduck_android.ui.Transfer.bus;


public class ChangeSalerInfoBusEvent {
    public String name;
    public String des;
    public byte[] logo;

    public ChangeSalerInfoBusEvent(String name, String des, byte[] logo){
        this.name = name;
        this.des = des;
        this.logo = logo;
    }
}
