package com.example.lianqy.doubleduck_android.ui.Order.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lianqy.doubleduck_android.R;
import com.example.lianqy.doubleduck_android.model.Dish;
import com.example.lianqy.doubleduck_android.util.BitmapUtil;

import java.util.List;

public class DishInOrderAdapter extends RecyclerView.Adapter<DishInOrderAdapter.ViewHolder> implements View.OnClickListener {

    private DishInOrderAdapter.OnItemClickListener mOnItemClickListener;
    private List<Dish> mDishes;
    private DishInOrderAdapter.MyItemClickListener mMyItemClickListener;

    public interface MyItemClickListener {
        public void clickListener(View v);
    }

    public DishInOrderAdapter(List<Dish> dishList, MyItemClickListener listener){
        mDishes = dishList;
        mMyItemClickListener = listener;
    }

    public interface OnItemClickListener{
        void onClick(int position);
        void onLongClick(int position);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_dish_in_order, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(DishInOrderAdapter.ViewHolder holder, final int position) {
        Dish dish = mDishes.get(position);

        holder.icon.setImageBitmap(BitmapUtil.byteArrayToBitmap(dish.getSrc()));
        holder.name.setText(dish.getName());
        holder.price.setText(dish.getPrice());
        holder.copy.setText(String.valueOf(dish.getCopy()));

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
        return mDishes == null ? 0 : mDishes.size();
    }


    @Override
    public void onClick(View v) {
        mMyItemClickListener.clickListener(v);
    }

    public void setOnItemClickListener(DishInOrderAdapter.OnItemClickListener onItemClickListener){
        this.mOnItemClickListener = onItemClickListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private ImageView icon;
        private TextView name, price, copy;

        public ViewHolder(View itemView) {
            super(itemView);

            icon = itemView.findViewById(R.id.img);
            name = itemView.findViewById(R.id.dishName);
            price = itemView.findViewById(R.id.price);
            copy = itemView.findViewById(R.id.copy);
        }
    }
}
