package com.example.PitStop.loginAct;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.PitStop.MainActivity;
import com.example.PitStop.R;
import com.example.PitStop.classes.Encode;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class shopLogin extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private Spinner spinner;
    private TextView spinnerError;
    private Button loginBt;
    private TextView signBt;
    private ImageButton eyeBt;
    private EditText passEt;
    private EditText phoneEt;

    private int type;
    private Long phone;
    private String pass;
    String hashPass;

    private FirebaseDatabase fd;
    private DatabaseReference dbRef;

    private SharedPreferences sharedPref;
    private SharedPreferences.Editor editor;

    private ValueEventListener v;

    public shopLogin() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static shopLogin newInstance() {
        shopLogin fragment = new shopLogin();
        Bundle args = new Bundle();
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
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_shop_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        spinner=getView().findViewById(R.id.L2_spinner);
        spinnerError=(TextView) spinner.getSelectedView();
        loginBt=getView().findViewById(R.id.bt2_login);
        signBt=getView().findViewById(R.id.L2_signin);
        eyeBt=getView().findViewById(R.id.L2_eye);
        phoneEt = getView().findViewById(R.id.L2_Et_Phone);
        passEt = getView().findViewById(R.id.L2_Et_Password);
        fd=FirebaseDatabase.getInstance();

        sharedPref=getActivity().getSharedPreferences("Data", Context.MODE_PRIVATE);
        editor=sharedPref.edit();

        //spinner part
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.shopType, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setBackground(getResources().getDrawable(R.drawable.spinner_back));
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Log.i("signin2", "onItemSelected: "+i);
                type=i+1;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                spinnerError.setError("Must be selected");
                spinnerError.setTextColor(Color.RED);
            }
        });

        v=new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boolean f = true;
                for (DataSnapshot a : snapshot.getChildren()) {
                    if (String.valueOf(phone).equals(a.child("phone").getValue().toString())) {
                        f = false;
                        hashPass= Encode.hashPassword(pass);
                        if (hashPass.equals(a.child("password").getValue())) {
                            editor.putString("name",(String)a.child("Name").getValue());
                            editor.putLong("phone",(Long)a.child("phone").getValue());
                            editor.putInt("type",type);
                            editor.apply();
                            Intent goHome=new Intent(getContext(), MainActivity.class);
                            startActivity(goHome);
                            getActivity().finish();
                        } else {
                            passEt.setError("Password incorrect");
                        }
                    }
                }
                if (f) {
                    phoneEt.setError("Phone number not found");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        };

        loginBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                phone = Long.parseLong(phoneEt.getText().toString());
                pass = passEt.getText().toString();
                if (String.valueOf(phone).length() != 10) {
                    phoneEt.setError("Number must be 10 digits long");
                } else {
                    dbRef=fd.getReference().child("users").child(""+type);
                    dbRef.addListenerForSingleValueEvent(v);
                    dbRef.removeEventListener(v);
                                   }
            }
        });

        signBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signInShop=new Intent(getContext(),SignIn2.class);
                startActivity(signInShop);
            }
        });

        eyeBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(passEt.getTransformationMethod()== PasswordTransformationMethod.getInstance()){
                    passEt.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
                else{
                    passEt.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });
    }
}