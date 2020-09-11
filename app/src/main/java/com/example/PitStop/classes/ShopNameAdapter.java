package com.example.PitStop.classes;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.PitStop.R;

import java.util.ArrayList;

public class ShopNameAdapter extends RecyclerView.Adapter<ShopNameAdapter.ShopNameViewHolder>{

    private ArrayList<ShopListSmall> mShopLittleCards;

    private ShopNameAdapter.OnItemClickListener mListener;

    public interface OnItemClickListener{
        void onItemClick(int position);
    }
    public void setOnItemClickListener(ShopNameAdapter.OnItemClickListener listener){
        mListener=listener;
    }

    public static class ShopNameViewHolder extends RecyclerView.ViewHolder{
        public TextView nTv;
        public TextView tTv;

        public ShopNameViewHolder(@NonNull View itemView,final OnItemClickListener listener) {
            super(itemView);
            nTv=itemView.findViewById(R.id.x_name);
            tTv=itemView.findViewById(R.id.x_type);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(listener!=null){
                        int position=getAdapterPosition();
                        if(position!=RecyclerView.NO_POSITION){
                            listener.onItemClick(position);
                        }
                }
            }});
        }
    }

    public ShopNameAdapter(ArrayList<ShopListSmall> t){
        mShopLittleCards =t;
    }

    @NonNull
    @Override
    public ShopNameViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.shop_card,parent,false);
        ShopNameViewHolder svh=new ShopNameViewHolder(v,mListener);
        return svh;
    }

    @Override
    public void onBindViewHolder(@NonNull ShopNameAdapter.ShopNameViewHolder holder, final int position) {
        ShopListSmall ss= mShopLittleCards.get(position);
        holder.nTv.setText(ss.getName());
        holder.tTv.setText(ss.getType());
    }

    @Override
    public int getItemCount() {
        return mShopLittleCards.size();
    }

}
