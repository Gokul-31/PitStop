package com.example.PitStop.classes;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.PitStop.R;

import java.util.ArrayList;

public class BookCardAdapter extends RecyclerView.Adapter<BookCardAdapter.BookCardViewHolder> {
        private ArrayList<BookCard> mbookCards;

        public static class BookCardViewHolder extends RecyclerView.ViewHolder {
            public TextView time1V;
            public TextView APM1V;
            public TextView time2V;
            public TextView APM2V;
            public TextView shopName;
            public TextView shopPhone;

            public BookCardViewHolder(@NonNull View itemView) {
                super(itemView);
                time1V = itemView.findViewById(R.id.bookcard_t1);
                time2V = itemView.findViewById(R.id.bookcard_t2);
                APM1V = itemView.findViewById(R.id.bookcard_am1);
                APM2V = itemView.findViewById(R.id.bookcard_am2);
                shopName=itemView.findViewById(R.id.bookcard_shopname);
                shopPhone=itemView.findViewById(R.id.bookcard_shopphone);
            }
        }

        public BookCardAdapter(ArrayList<BookCard> bookCards) {
            mbookCards = bookCards;
        }

        @NonNull
        @Override
        public BookCardAdapter.BookCardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.book_card, parent, false);
            BookCardAdapter.BookCardViewHolder tvh = new BookCardAdapter.BookCardViewHolder(v);
            return tvh;
        }

        @Override
        public void onBindViewHolder(@NonNull BookCardAdapter.BookCardViewHolder holder, final int position) {
            BookCard t = mbookCards.get(position);
            holder.time1V.setText(t.getTime1());
            holder.time2V.setText(t.getTime2());
            holder.APM1V.setText(t.getAm1());
            holder.APM2V.setText(t.getAm2());
            holder.shopName.setText(t.getShopName());
            holder.shopPhone.setText(t.getShopPhone().toString());
        }

        @Override
        public int getItemCount() {
            return mbookCards.size();
        }
    }
