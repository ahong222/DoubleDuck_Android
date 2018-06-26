package com.example.lianqy.doubleduck_android.ui.ManageDishes;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.lianqy.doubleduck_android.R;
import com.example.lianqy.doubleduck_android.model.Type;

import java.util.List;

public class ManageTypeAdapter extends RecyclerView.Adapter<ManageTypeAdapter.ViewHolder> implements View.OnClickListener {

    private ManageTypeAdapter.OnItemClickListener mOnItemClickListener;
    private List<Type> mTypes;
    private ManageTypeAdapter.MyItemClickListener mMyItemClickListener;

    public interface MyItemClickListener {
        public void clickListener(View v);
    }

    public ManageTypeAdapter(List<Type> typeList, MyItemClickListener listener){
        mTypes = typeList;
        mMyItemClickListener = listener;
    }

    public interface OnItemClickListener{
        void onClick(int position);
        void onLongClick(int position);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_manage_type, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Type type = mTypes.get(position);

        holder.typeName.setText(type.getType());

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
        return mTypes == null ? 0 : mTypes.size();
    }


    @Override
    public void onClick(View v) {
        mMyItemClickListener.clickListener(v);
    }

    public void setOnItemClickListener(ManageTypeAdapter.OnItemClickListener onItemClickListener){
        this.mOnItemClickListener = onItemClickListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView typeName;

        public ViewHolder(View itemView) {
            super(itemView);
            typeName = itemView.findViewById(R.id.type);
        }
    }
}
