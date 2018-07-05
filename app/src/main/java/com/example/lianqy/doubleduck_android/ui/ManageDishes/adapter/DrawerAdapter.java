package com.example.lianqy.doubleduck_android.ui.ManageDishes.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.lianqy.doubleduck_android.R;
import com.example.lianqy.doubleduck_android.model.Dish;
import com.example.lianqy.doubleduck_android.model.Type;
import com.example.lianqy.doubleduck_android.ui.ManageDishes.ManageDishesActivity;
import com.example.lianqy.doubleduck_android.util.BitmapUtil;

import java.util.ArrayList;
import java.util.List;

public class DrawerAdapter extends BaseAdapter {


    public class TypeMenuItem{
        public String title;
        public TypeMenuItem(String title){
            this.title = title;
        }
    }

    private List<TypeMenuItem> mMenuItems = new ArrayList<>();
    private Context mContext;
    private List<Type> mTypeList = new ArrayList<>();

    public DrawerAdapter(Context context, List<Type> types){
        this.mContext = context;
        this.mTypeList = types;

        for(int i = 0; i < mTypeList.size(); i ++){
            mMenuItems.add(new TypeMenuItem(mTypeList.get(i).getType()));
        }
    }

    public Type getType(int position){
        return mTypeList.get(position);
    }


    @Override
    public int getCount() {
        return mMenuItems == null ? 0 : mMenuItems.size();
    }

    @Override
    public TypeMenuItem getItem(int position) {
        return mMenuItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if(view == null){
            view = LayoutInflater.from(mContext).inflate(R.layout.menu_drawer_item, parent, false);
            ((TextView) view).setText(getItem(position).title);
        }

        return view;
    }
}
