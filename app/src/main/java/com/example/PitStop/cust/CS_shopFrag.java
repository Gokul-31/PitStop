package com.example.PitStop.cust;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.PitStop.R;

public class CS_shopFrag extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

private View.OnClickListener cardClick;
    private CardView c1;
    private CardView c2;
    private CardView c3;
    private CardView c4;

    public CS_shopFrag() {
        // Required empty public constructor
    }

    public static CS_shopFrag newInstance(String param1, String param2) {
        CS_shopFrag fragment = new CS_shopFrag();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_c_s_shop, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setup();
        setListeners();
    }

    private void setListeners() {
        c1.setOnClickListener(cardClick);
        c2.setOnClickListener(cardClick);
        c3.setOnClickListener(cardClick);
        c4.setOnClickListener(cardClick);
    }

    private void setup() {
        c1=getView().findViewById(R.id.card_grocery);
        c2=getView().findViewById(R.id.card_barber);
        c3=getView().findViewById(R.id.card_saloon);
        c4=getView().findViewById(R.id.card_pharamacy);
        cardClick=new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int type=0;
                switch (view.getId()){
                    case R.id.card_grocery:
                        type=1;
                        break;
                    case R.id.card_barber:
                        type=2;
                        break;
                    case R.id.card_saloon:
                        type=3;
                        break;
                    case R.id.card_pharamacy:
                        type=4;
                }
            getFragmentManager().beginTransaction().replace(R.id.CN_fragment_container_view_tag,new CS_shopFrag2(type)).commit();
                Log.i("****", "TYPE: "+type);
            }
        };

    }
}