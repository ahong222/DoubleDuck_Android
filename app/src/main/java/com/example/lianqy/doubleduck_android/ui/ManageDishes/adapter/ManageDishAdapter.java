package com.example.lianqy.doubleduck_android.ui.ManageDishes.adapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lianqy.doubleduck_android.R;
import com.example.lianqy.doubleduck_android.model.Dish;

import java.util.List;

public class ManageDishAdapter extends RecyclerView.Adapter<ManageDishAdapter.ViewHolder> implements View.OnClickListener {

    private ManageDishAdapter.OnItemClickListener mOnItemClickListener;
    private List<Dish> mDishes;
    private ManageDishAdapter.MyItemClickListener mMyItemClickListener;

    public interface MyItemClickListener {
        public void clickListener(View v);
    }

    public ManageDishAdapter(List<Dish> dishList, MyItemClickListener listener){
        mDishes = dishList;
        mMyItemClickListener = listener;
    }

    public interface OnItemClickListener{
        void onClick(int position);
        void onLongClick(int position);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_manage_dish, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Dish dish = mDishes.get(position);

        byte[] bitmapdata = dish.getSrc();
        Bitmap bitmap = BitmapFactory.decodeByteArray(bitmapdata, 0, bitmapdata.length);
        holder.icon.setImageBitmap(bitmap);
        holder.name.setText(dish.getName());
        holder.price.setText(dish.getPrice());
        holder.des.setText(dish.getDes());

        if(mOnItemClickListener != null){
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onClick(position);
                }
            });

            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    mOnItemClickListener.onLongClick(position);
                    return false;
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mDishes == null ? 0: mDishes.size();
    }

    @Override
    public void onClick(View v) {
        mMyItemClickListener.clickListener(v);
    }

    public void setOnItemClickListener(ManageDishAdapter.OnItemClickListener onItemClickListener){
        this.mOnItemClickListener = onItemClickListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView icon;
        private TextView name;
        private TextView price;
        private TextView des;

        public ViewHolder(View itemView) {
            super(itemView);
            icon = itemView.findViewById(R.id.icon);
            name = itemView.findViewById(R.id.name);
            price = itemView.findViewById(R.id.price);
            des = itemView.findViewById(R.id.des);
        }
    }


}
