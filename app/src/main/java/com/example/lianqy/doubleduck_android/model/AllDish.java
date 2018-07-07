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

class Disheslist{
    String dishname;
    String dishpict;
    int dishsale;
    float dishprice;
    String dishdis;

    public float getDishprice() {
        return dishprice;
    }

    public int getDishsale() {
        return dishsale;
    }

    public String getDishdis() {
        return dishdis;
    }

    public String getDishname() {
        return dishname;
    }

    public String getDishpict() {
        return dishpict;
    }
}
