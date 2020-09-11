package com.example.PitStop.shop;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Calendar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.PitStop.R;
import com.example.PitStop.classes.TimeCard;
import com.example.PitStop.classes.TimeCardAdapter;
import com.example.PitStop.classes.UserFunc;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class SP_settingsFrag extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    public SP_settingsFrag() {
    }

    // TODO: Rename and change types and number of parameters
    public static SP_settingsFrag newInstance(String param1, String param2) {
        SP_settingsFrag fragment = new SP_settingsFrag();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    private TextView intervalTV;
    private Spinner sp1;
    private Spinner sp2;
    private Spinner sp3;
    private Spinner sp4;
    private Button setBt;
    private Button finalsetBt;

    private int t1;
    private int t2;
    private boolean am1;
    private boolean am2;
    private int interval;

    private int type;
    private Long phone;

    private Calendar currentTime;
    private int cTime;

    private RecyclerView recyclerV;
    private TimeCardAdapter adapter;
    private RecyclerView.LayoutManager layoutM;
    private ArrayList<TimeCard> timeSets;
    private ArrayList<TimeCard> remTimeSets;
    private ArrayList<TimeCard> setRemTimeSets;

    private FirebaseDatabase fd;
    private DatabaseReference dbRef;

    private UserFunc u;

    private boolean enableSet;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_s_p_settings, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        setSpinners();
        timeSets=new ArrayList<>();
        remTimeSets=new ArrayList<>();
        setRemTimeSets=new ArrayList<>();
        setBt=getView().findViewById(R.id.st_set);
        finalsetBt=getView().findViewById(R.id.st_finalset);
        finalsetBt.setEnabled(false);
        recyclerV=getView().findViewById(R.id.st_recycle);
        recyclerV.setHasFixedSize(true);
        layoutM=new LinearLayoutManager(getContext());
        recyclerV.setLayoutManager(layoutM);
        u=new UserFunc();
        phone=u.getPhone(getContext());
        type=u.getType(getContext());
        setTimeEnable();

        fd=FirebaseDatabase.getInstance();
        dbRef=fd.getReference().child("users").child(""+type).child(String.valueOf(phone)).child("time cards");
        
        intervalTV=getView().findViewById(R.id.st_interval);
        u=new UserFunc();
        //kept interval

        switch (u.getType(getContext())){
            case 1:
            case 3:
            case 4:
                intervalTV.setText(R.string.min15);
                interval=1;
                break;
            case 2:
                intervalTV.setText(R.string.min30);
                interval=2;
        }
        setBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timeSets.clear();
                createList(t1,t2);
                adapter=new TimeCardAdapter(timeSets);
                recyclerV.setAdapter(adapter);
                finalsetBt.setEnabled(true);
            }
        });
        finalsetBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setRemTimeSets.clear();
                remTimeSets=adapter.getList();
                for(TimeCard a : remTimeSets){
                    setRemTimeSets.add(new TimeCard(a.getTime1(),a.getTime2(),a.getAm1(),a.getAm2(),false,0L));
                }
                dbRef.setValue(setRemTimeSets);
                Toast.makeText(getContext(),"Set Successfully!",Toast.LENGTH_SHORT).show();
                getActivity().onBackPressed();
            }
        });
    }

    private void setTimeEnable() {
        currentTime=Calendar.getInstance();
        cTime=currentTime.get(Calendar.HOUR_OF_DAY);
        // TODO: 15-08-2020 remove comments
//        if(cTime<20&&cTime>9){
//            Toast.makeText(getContext(),"Cant change settings between 9AM to 8PM",Toast.LENGTH_LONG).show();
//            finalsetBt.setEnabled(false);
//            setBt.setEnabled(false);
//        }
//        else{
            finalsetBt.setEnabled(true);
            setBt.setEnabled(true);
//        }
    }

    private void createList(int a,int b) {
        int i;
        if(!am1){
            if(a!=12)
            a+=12;
        }
        else{
            if(a==12){
                a=0;
            }
        }
        if(!am2){
            if(b!=12){
                b+=12;
            }
        }
        else{
            if(b==12){
                b=0;
            }
        }
        for(i =a;i+interval<=b;i+=interval){
            TimeCard t = new TimeCard(i,i+interval);
            timeSets.add(t);
        }
    }

    private void setSpinners() {
        sp1=getView().findViewById(R.id.st_start_time);
        sp2=getView().findViewById(R.id.st_end_time);
        sp3=getView().findViewById(R.id.st_start_apm);
        sp4=getView().findViewById(R.id.st_end_apm);

        {
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.time, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            sp1.setAdapter(adapter);
            sp1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    t1 = i + 1;
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {
                    Toast.makeText(getContext(), "time must be selected", Toast.LENGTH_SHORT).show();
                    enableSet = false;
                }
            });
        }//spinner 1
        {
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.time, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            sp2.setAdapter(adapter);
            sp2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    t2 = i + 1;
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {
                    Toast.makeText(getContext(), "time must be selected", Toast.LENGTH_SHORT).show();
                    enableSet = false;
                }
            });
        }//spinner 2
        {
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.apm, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            sp3.setAdapter(adapter);
            sp3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    am1= i % 2 == 0;
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {
                    Toast.makeText(getContext(), "time must be selected", Toast.LENGTH_SHORT).show();
                    enableSet = false;
                }
            });
        }//spinner 3
        {
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.apm, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            sp4.setAdapter(adapter);
            sp4.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    am2= i % 2 == 0;
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {
                    Toast.makeText(getContext(), "time must be selected", Toast.LENGTH_SHORT).show();
                    enableSet = false;
                }
            });
        }//spinner 4
    }

}