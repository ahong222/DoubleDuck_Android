package com.example.lianqy.doubleduck_android.ui.Order.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.lianqy.doubleduck_android.R;
import com.example.lianqy.doubleduck_android.model.Order;

import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder> implements View.OnClickListener {

    private OrderAdapter.OnItemClickListener mOnItemClickListener;
    private List<Order> mOrders;
    private OrderAdapter.MyItemClickListener mMyItemClickListener;

    public interface MyItemClickListener {
        public void clickListener(View v);
    }

    public OrderAdapter(List<Order> orderList, MyItemClickListener listener){
        mOrders = orderList;
        mMyItemClickListener = listener;
    }

    public interface OnItemClickListener{
        void onClick(int position);
        void onLongClick(int position);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_order, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(OrderAdapter.ViewHolder holder, final int position) {
        Order order = mOrders.get(position);

        holder.orderNum.setText(String.valueOf(order.getOrderNum()));
        holder.orderAmount.setText(String.valueOf(order.getAmount()));
        holder.orderState.setText(String.valueOf(order.getState()));

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
        return mOrders == null ? 0 : mOrders.size();
    }


    @Override
    public void onClick(View v) {
        mMyItemClickListener.clickListener(v);
    }

    public void setOnItemClickListener(OrderAdapter.OnItemClickListener onItemClickListener){
        this.mOnItemClickListener = onItemClickListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView orderNum, orderAmount, orderState;

        public ViewHolder(View itemView) {
            super(itemView);

            orderNum = itemView.findViewById(R.id.orderNum);
            orderAmount = itemView.findViewById(R.id.total);
            orderState = itemView.findViewById(R.id.state);
        }
    }
}
