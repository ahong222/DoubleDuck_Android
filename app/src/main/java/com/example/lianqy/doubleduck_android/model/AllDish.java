package com.example.lianqy.doubleduck_android.model;

import java.util.ArrayList;
import java.util.List;

public class AllDish {
    String category;
    int dishesnum;
    List<Disheslist> disheslist;

    public int getDishesnum() {
        return dishesnum;
    }

    public String getCategory() {
        return category;
    }

    public List<Disheslist> getDisheslist() {
        return disheslist;
    }
}

