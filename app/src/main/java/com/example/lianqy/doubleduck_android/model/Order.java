package com.example.lianqy.doubleduck_android.model;

import android.support.annotation.ArrayRes;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Order implements Serializable{

    private int orderNum;
    private int deskNum;
    //private Date date;
    private ArrayList<Dish> dishs;
    private int amount;
    private int state;

    public Order(int orderNum, int deskNumm, ArrayList<Dish> dishes, int amount, int state){
        this.orderNum = orderNum;
        this.deskNum = deskNumm;
        this.dishs = dishes;
        this.amount = amount;
        this.state = state;
    }


    public int getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(int orderNum) {
        this.orderNum = orderNum;
    }

    public int getDeskNum() {
        return deskNum;
    }

    public void setDeskNum(int deskNum) {
        this.deskNum = deskNum;
    }

    public List<Dish> getDishs() {
        return dishs;
    }

    public void setDishs(ArrayList<Dish> dishs) {
        this.dishs = dishs;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
}
