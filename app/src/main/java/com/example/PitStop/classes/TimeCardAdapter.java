package com.example.PitStop.classes;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.PitStop.R;

import java.util.ArrayList;

public class TimeCardAdapter extends RecyclerView.Adapter<TimeCardAdapter.TimeCardViewHolder> {
    private ArrayList<TimeCard> mtimeCards;
    public static class TimeCardViewHolder extends RecyclerView.ViewHolder{
        public TextView time1V;
        public TextView APM1V;
        public TextView time2V;
        public TextView APM2V;
        public ImageButton minusIBt;
        public TimeCardViewHolder(@NonNull View itemView) {
            super(itemView);
            time1V=itemView.findViewById(R.id.card_t1);
            time2V=itemView.findViewById(R.id.card_t2);
            APM1V=itemView.findViewById(R.id.card_am1);
            APM2V=itemView.findViewById(R.id.card_am2);
            minusIBt=itemView.findViewById(R.id.card_minus);
        }
    }

    public TimeCardAdapter(ArrayList<TimeCard> timeCards){
        mtimeCards=timeCards;
    }

    @NonNull
    @Override
    public TimeCardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.time_card,parent,false);
        TimeCardViewHolder tvh=new TimeCardViewHolder(v);
        return tvh;
    }

    @Override
    public void onBindViewHolder(@NonNull TimeCardViewHolder holder, final int position) {
        TimeCard t= mtimeCards.get(position);
        holder.time1V.setText(t.getTime1());
        holder.time2V.setText(t.getTime2());
        holder.APM1V.setText(t.getAm1());
        holder.APM2V.setText(t.getAm2());
        holder.minusIBt.setImageResource(R.drawable.ic_minus);
        holder.minusIBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeAt(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mtimeCards.size();
    }

    public void removeAt(int p){
        mtimeCards.remove(p);
        notifyItemRemoved(p);
        notifyItemRangeChanged(p,mtimeCards.size());
    }

    public ArrayList<TimeCard> getList(){
        return mtimeCards;
    }
}
