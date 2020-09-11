package com.example.PitStop.classes;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.PitStop.R;

import java.util.ArrayList;

public class BookShopCardAdapter extends RecyclerView.Adapter<BookShopCardAdapter.BookShopCardViewHolder> {
    private ArrayList<TimeCard> mbookShopCards;

    public static class BookShopCardViewHolder extends RecyclerView.ViewHolder {
        public TextView time1V;
        public TextView APM1V;
        public TextView time2V;
        public TextView APM2V;
        public TextView shopName;
        public TextView shopPhone;

        public BookShopCardViewHolder(@NonNull View itemView) {
            super(itemView);
            time1V = itemView.findViewById(R.id.bookcard_t1);
            time2V = itemView.findViewById(R.id.bookcard_t2);
            APM1V = itemView.findViewById(R.id.bookcard_am1);
            APM2V = itemView.findViewById(R.id.bookcard_am2);
            shopName = itemView.findViewById(R.id.bookcard_shopname);
            shopPhone = itemView.findViewById(R.id.bookcard_shopphone);
        }
    }

    public BookShopCardAdapter(ArrayList<TimeCard> bookShopCards) {
        mbookShopCards = bookShopCards;
    }

    @NonNull
    @Override
    public BookShopCardAdapter.BookShopCardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.book_card, parent, false);
        BookShopCardAdapter.BookShopCardViewHolder tvh = new BookShopCardAdapter.BookShopCardViewHolder(v);
        return tvh;
    }

    @Override
    public void onBindViewHolder(@NonNull BookShopCardAdapter.BookShopCardViewHolder holder, final int position) {
        TimeCard t = mbookShopCards.get(position);
        holder.time1V.setText(t.getTime1());
        holder.time2V.setText(t.getTime2());
        holder.APM1V.setText(t.getAm1());
        holder.APM2V.setText(t.getAm2());
        holder.shopName.setText(t.getBookerName());
        holder.shopPhone.setText(t.getBookedBY().toString());
    }

    @Override
    public int getItemCount() {
        return mbookShopCards.size();
    }
}