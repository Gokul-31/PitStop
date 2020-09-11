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

public class TimeCardRoundAdapter extends RecyclerView.Adapter<TimeCardRoundAdapter.TimeCardRoundViewHolder> {
    private ArrayList<TimeCard> mtimeCards;
    private TimeCardRoundAdapter.OnItemClickListener mListener;

    public interface OnItemClickListener{
        void onPlusClick(int position);
    }
    public void setOnItemClickListener(TimeCardRoundAdapter.OnItemClickListener listener){
        mListener=listener;
    }

    public static class TimeCardRoundViewHolder extends RecyclerView.ViewHolder{
        public TextView time1V;
        public TextView APM1V;
        public TextView time2V;
        public TextView APM2V;
        public ImageButton plusIBt;

        public TimeCardRoundViewHolder(@NonNull View itemView,final OnItemClickListener listener) {
            super(itemView);
            time1V=itemView.findViewById(R.id.rcard_t1);
            time2V=itemView.findViewById(R.id.rcard_t2);
            APM1V=itemView.findViewById(R.id.rcard_am1);
            APM2V=itemView.findViewById(R.id.rcard_am2);
            plusIBt=itemView.findViewById(R.id.rcard_plus);
            plusIBt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(listener!=null){
                        int position=getAdapterPosition();
                        if(position!=RecyclerView.NO_POSITION){
                            plusIBt.setImageResource(R.drawable.ic_icons8_tick_box);
                            listener.onPlusClick(position);
                        }
                    }
                }
            });
        }
    }

    public TimeCardRoundAdapter(ArrayList<TimeCard> timeCards){
        mtimeCards=timeCards;
    }

    @NonNull
    @Override
    public TimeCardRoundViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.time_card_round,parent,false);
        TimeCardRoundViewHolder tvh=new TimeCardRoundViewHolder(v,mListener);
        return tvh;
    }

    @Override
    public void onBindViewHolder(@NonNull TimeCardRoundViewHolder holder, final int position) {
        TimeCard t= mtimeCards.get(position);
        holder.time1V.setText(t.getTime1());
        holder.time2V.setText(t.getTime2());
        holder.APM1V.setText(t.getAm1());
        holder.APM2V.setText(t.getAm2());
        holder.plusIBt.setImageResource(R.drawable.ic_plus);
    }

    @Override
    public int getItemCount() {
        return mtimeCards.size();
    }

    public ArrayList<TimeCard> getList(){
        return mtimeCards;
    }
}
