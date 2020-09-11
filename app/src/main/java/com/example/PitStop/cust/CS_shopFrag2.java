package com.example.PitStop.cust;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.PitStop.R;
import com.example.PitStop.classes.BookCard;
import com.example.PitStop.classes.ShopListSmall;
import com.example.PitStop.classes.ShopNameAdapter;
import com.example.PitStop.classes.TimeCard;
import com.example.PitStop.classes.TimeCardRoundAdapter;
import com.example.PitStop.classes.UserFunc;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

public class CS_shopFrag2 extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";

    // TODO: Rename and change types of parameters
      private int shopType;
    private RecyclerView recyclerView;
    private ShopNameAdapter adapter;
    private TimeCardRoundAdapter adapter2;
    private RecyclerView.LayoutManager layoutManagerGrid;
    private RecyclerView.LayoutManager layoutManager;
      private ArrayList<ShopListSmall> shopListSmalls;
      private ArrayList<TimeCard> timeCardsRound;
      private FirebaseDatabase fd;
      private DatabaseReference dbRef;

    private ValueEventListener Vl;
    private ValueEventListener Vl2;

    private String name;
    private Long phone;
    private Long bookerPhone;
    private String bookerName;
    private int bookerBookings;
    private Long phoneSpec;
    private String shopName;
    private TextView tv2;
    private boolean bookCheck;
    Button locBt;

    Double latitude;
    Double longitude;

    private TextView nillTv;

    public CS_shopFrag2(int t) {
        shopType = t;
    }

    // TODO: Rename and change types and number of parameters
    public static CS_shopFrag2 newInstance(int type) {
        CS_shopFrag2 fragment = new CS_shopFrag2(type);
        Bundle args = new Bundle();
        args.putInt("param1", type);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            shopType = getArguments().getInt(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_c_s_shop_frag2, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setup();
        buildReView();
    }

    private void setup() {

        fd = FirebaseDatabase.getInstance();
        dbRef = fd.getReference().child("users").child(""+ shopType);

        shopListSmalls = new ArrayList<ShopListSmall>();
        timeCardsRound=new ArrayList<TimeCard>();

        tv2=getView().findViewById(R.id.textView2);
        nillTv=getView().findViewById(R.id.nill_tv);
        nillTv.setVisibility(View.INVISIBLE);

        recyclerView = getView().findViewById(R.id.CS_reyclerview);
        locBt=getView().findViewById(R.id.CS_loc);
        recyclerView.setHasFixedSize(true);
        layoutManagerGrid = new GridLayoutManager(getContext(), 2);
        layoutManager=new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        UserFunc u=new UserFunc();
        bookerPhone=u.getPhone(getContext());
        bookerName=u.getName(getContext());
        bookCheck=false;
    }
    private void buildReView() {
        setupVel();
        dbRef.addListenerForSingleValueEvent(Vl);
        dbRef.removeEventListener(Vl);
    }

    private void setupVel() {
        Vl= new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot a : snapshot.getChildren()) {
                            name = a.child("Name").getValue().toString();
                            phone=(Long)a.child("phone").getValue();
                            shopListSmalls.add(new ShopListSmall(name, shopType,phone));
                        }
                        tv2.setText(R.string.avaiShops);
                        locBt.setVisibility(View.INVISIBLE);
                        adapter = new ShopNameAdapter(shopListSmalls);
                        recyclerView.setAdapter(adapter);
                        if(adapter.getItemCount()==0){
                            nillTv.setText("None Available");
                            nillTv.setVisibility(View.VISIBLE);
                        }
                        recyclerView.setLayoutManager(layoutManager);
                        setListenerForRView();
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                };
    }

    private void setListenerForRView() {
        setupVel2();
        adapter.setOnItemClickListener(new ShopNameAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                phoneSpec=shopListSmalls.get(position).getPhone();

                //location
                dbRef=fd.getReference().child("users").child("" + shopType).child("" + phoneSpec);
                dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        latitude=snapshot.child("Lat").getValue(Double.class);
                        longitude=snapshot.child("Longi").getValue(Double.class);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });


                dbRef=fd.getReference().child("users").child("" + shopType).child("" + phoneSpec).child("Bookers");
                dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Log.i("Main", "onDataChange: "+bookerPhone);
                        for(DataSnapshot a:snapshot.getChildren()){
                            Log.i("Main", "onDataChange: "+a.getValue(Long.class));
                            if(bookerPhone.equals(a.getValue(Long.class))){
                                Toast.makeText(getContext(),"Booked Already",Toast.LENGTH_SHORT).show();
                                bookCheck=true;
                                return;
                            }
                        }
                        if(!bookCheck){
                            dbRef = fd.getReference().child("users").child("" + shopType).child("" + phoneSpec).child("time cards");
                            dbRef.addListenerForSingleValueEvent(Vl2);
                            dbRef.removeEventListener(Vl2);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
            }
        });
    }

    private void setupVel2() {
        Vl2=new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
               for(DataSnapshot a:snapshot.getChildren()){
                   if(!Boolean.parseBoolean(a.child("booked").getValue().toString())){
                       TimeCard b = new TimeCard(
                               a.child("time1").getValue().toString(), a.child("time2").getValue().toString(),
                               a.child("am1").getValue().toString(), a.child("am2").getValue().toString(),
                               Boolean.parseBoolean(a.child("booked").getValue().toString()), Long.valueOf(a.child("bookedBY").getValue().toString()));
                       timeCardsRound.add(b);
                   }
               }
               //add adapter and layoutmanager
                tv2.setText(R.string.avaiSlots);
               //location
               locBt.setVisibility(View.VISIBLE);
               locBt.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View view) {
                       Log.i("Main"," latitude :"+latitude);
                       Log.i("Main"," longitude :"+longitude);

                       Uri mapUri = Uri.parse("google.navigation:q="+latitude+","+longitude);
                       Intent mapIntent=new Intent(Intent.ACTION_VIEW,mapUri);
                       mapIntent.setPackage("com.google.android.apps.maps");
                       try{
                           if(mapIntent.resolveActivity(getActivity().getPackageManager())!=null){
                               startActivity(mapIntent);
                           }
                       }catch (NullPointerException e){
                           Log.e(TAG, "onClick: NullPointerException : couldnt open app "+e.getMessage() );
                           Toast.makeText(getContext(),"Couldnt open app",Toast.LENGTH_SHORT);
                       }
                   }
               });

                recyclerView.setLayoutManager(layoutManagerGrid);
                adapter2=new TimeCardRoundAdapter(timeCardsRound);
                recyclerView.setAdapter(adapter2);
                if(adapter2.getItemCount()==0){
                    nillTv.setText("None Available");
                    nillTv.setVisibility(View.VISIBLE);
                }
                adapter2.setOnItemClickListener(new TimeCardRoundAdapter.OnItemClickListener() {
                    @Override
                    public void onPlusClick(int position) {
                        //TODO: that shown only once
                        // TODO: 15-08-2020 edit such that can be booked only once
                        dbRef=fd.getReference().child("users").child(""+ shopType).child(""+phoneSpec).child("time cards").child(""+position);
                        dbRef.child("booked").setValue(true);
                        dbRef.child("bookedBY").setValue(bookerPhone);
                        dbRef.child("bookerName").setValue(bookerName);
                        //book for the user
                        TimeCard temp=timeCardsRound.get(position);
                        shopName=name;
                        BookCard b =new BookCard(temp,name,phoneSpec, shopType);
                        dbRef=fd.getReference().child("users").child("0").child(""+bookerPhone).child("bookings");
                        dbRef.push().setValue(b);

                        addBooker();
                        Toast.makeText(getContext(), "Booked Successfully!", Toast.LENGTH_SHORT).show();
                        getActivity().onBackPressed();
                    }
                });
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        };
    }

    private void addBooker() {
        dbRef=fd.getReference().child("users").child(""+ shopType).child(""+phoneSpec);
        dbRef.child("Bookers").push().setValue(bookerPhone);
    }
}