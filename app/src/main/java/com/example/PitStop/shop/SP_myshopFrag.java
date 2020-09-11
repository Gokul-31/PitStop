package com.example.PitStop.shop;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.PitStop.R;
import com.example.PitStop.classes.BookShopCardAdapter;
import com.example.PitStop.classes.TimeCard;
import com.example.PitStop.classes.UserFunc;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SP_myshopFrag extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    public SP_myshopFrag() {
        // Required empty public constructor
    }

    private RecyclerView recyclerView;
    private BookShopCardAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    private FirebaseDatabase fd;
    private DatabaseReference dbRef;

    private UserFunc u;
    private int type;
    private Long phone;

    private ValueEventListener vel;

    private ArrayList<TimeCard> bookShopCards;

    private int count=0;
    private int totalCount;

    // TODO: Rename and change types and number of parameters
    public static SP_myshopFrag newInstance(String param1, String param2) {
        SP_myshopFrag fragment = new SP_myshopFrag();
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
        return inflater.inflate(R.layout.fragment_s_p_myshop, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setup();
        setTotalCount();
    }

    private void setTotalCount() {
        dbRef=fd.getReference().child("users").child(""+type).child(""+phone).child("time cards");
        vel=new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                totalCount=Integer.parseInt(""+snapshot.getChildrenCount());
                Log.i("Main", "totalCount: "+totalCount);
                buildRView();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        dbRef.addListenerForSingleValueEvent(vel);
        dbRef.removeEventListener(vel);
    }

    private void buildRView() {
        dbRef=fd.getReference().child("users").child(""+type).child(""+phone).child("time cards");
        readData();
    }

    public void readData(){
        dbRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                TimeCard a=snapshot.getValue(TimeCard.class);
                count++;
                Log.i("TAG", "count: "+count);
                if(a.isBooked()) {
                    bookShopCards.add(a);
                }
                if(count>=totalCount){
                    adapter=new BookShopCardAdapter(bookShopCards);
                    recyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void setup() {
        fd=FirebaseDatabase.getInstance();
        recyclerView=getView().findViewById(R.id.MS_recycle);
        layoutManager=new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        u=new UserFunc();
        type=u.getType(getContext());
        phone=u.getPhone(getContext());
        bookShopCards=new ArrayList<TimeCard>();
    }
}