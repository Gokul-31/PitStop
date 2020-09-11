package com.example.PitStop.cust;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.PitStop.R;
import com.example.PitStop.classes.BookCard;
import com.example.PitStop.classes.BookCardAdapter;
import com.example.PitStop.classes.TimeCard;
import com.example.PitStop.classes.UserFunc;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

public class CS_bookFrag extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    public CS_bookFrag() {
        // Required empty public constructor
    }

    private FirebaseDatabase fd;
    private DatabaseReference dbRef;
    private UserFunc u;
    private Long phone;

    String deleteNameCust;

    private ValueEventListener vel;
    private ArrayList<BookCard> bookCards;

    private RecyclerView recyclerView;
    private BookCardAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    ItemTouchHelper.SimpleCallback simpleCallback;

    // TODO: Rename and change types and number of parameters
    public static CS_bookFrag newInstance(String param1, String param2) {
        CS_bookFrag fragment = new CS_bookFrag();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_c_s_book, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setup();
        setupVel();
        dbRef.addListenerForSingleValueEvent(vel);
        dbRef.removeEventListener(vel);
    }

    private void setupVel() {
        vel= new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot a:snapshot.getChildren()){
                    BookCard b =a.getValue(BookCard.class);
                    bookCards.add(b);
                }
                adapter= new BookCardAdapter(bookCards);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        };
    }

    private void setup() {
        fd=FirebaseDatabase.getInstance();
        u=new UserFunc();
        phone=u.getPhone(getContext());
        dbRef=fd.getReference().child("users").child("0").child(""+phone).child("bookings");
        bookCards=new ArrayList<BookCard>();
        recyclerView=getView().findViewById(R.id.recyclerview);
        layoutManager=new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        simpleCallback= new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                //delete at database
                BookCard b =bookCards.get(viewHolder.getAdapterPosition());
                deleteBookings(b);
                //deleteItem
                bookCards.remove(viewHolder.getAdapterPosition());
                adapter.notifyDataSetChanged();
                Log.i(TAG, "adapterSize: "+adapter.getItemCount());
            }
        };
        new ItemTouchHelper(simpleCallback).attachToRecyclerView(recyclerView);
    }

    private void deleteBookings(final BookCard bGet) {
        dbRef=fd.getReference().child("users").child("0").child(""+phone).child("bookings");
        //remove at customer
        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot a:snapshot.getChildren()){
                    BookCard b =a.getValue(BookCard.class);
                    if(b.getTime1().equals(bGet.getTime2())){
                        deleteNameCust=a.getKey();
                        fd.getReference().child("0").child("bookings").child(deleteNameCust).removeValue();
                        dbRef.removeEventListener(this);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        dbRef=fd.getReference().child(""+bGet.getShopType()).child(""+bGet.getShopPhone()).child("time cards");
        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot a:snapshot.getChildren()){
                    TimeCard t= a.getValue(TimeCard.class);
                    if(t.getTime1().equals(bGet.getTime1())){
                        dbRef=fd.getReference().child(""+bGet.getShopType()).child(""+bGet.getShopPhone()).child("time cards").child(a.getKey());
                        dbRef.child("booked").setValue(false);
                        dbRef.child("bookedBY").setValue(0);
                        dbRef.child("bookerName").removeValue();
                    }
                }
                dbRef.removeEventListener(this);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
}