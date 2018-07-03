package com.example.lianqy.doubleduck_android.model;

import java.io.Serializable;
import java.util.List;

public class Type implements Serializable {
    private String type;
    private List<Dish> dishes;

    public Type(String type, List<Dish> dishes){
        this.type = type;
        this.dishes = dishes;
    }

    public Type(String type){
        this(type, null);
    }


    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setDishes(List<Dish> dishes) {
        this.dishes = dishes;
    }

    public List<Dish> getDishes() {
        return dishes;
    }
}
