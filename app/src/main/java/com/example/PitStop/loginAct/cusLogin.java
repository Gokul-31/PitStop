package com.example.PitStop.loginAct;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.PitStop.MainActivity;
import com.example.PitStop.R;
import com.example.PitStop.classes.Encode;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class cusLogin extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private FirebaseDatabase fd;
    private DatabaseReference userDr;

    private EditText phoneEt;
    private EditText passEt;
    private Button loginBt;
    private long phoneNo;
    private String pass;
    String hashPass;
    private Iterable<DataSnapshot> users = new ArrayList<>();
    private TextView signinTV;
    private ImageButton eyeBt;

    private ValueEventListener vEL;

    private SharedPreferences sharedPref;
    private SharedPreferences.Editor editor;

    public cusLogin() {
    }

    public static cusLogin newInstance() {
        cusLogin fragment = new cusLogin();
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
        return inflater.inflate(R.layout.fragment_cus_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        loginBt = getView().findViewById(R.id.Bt1_login);
        phoneEt = getView().findViewById(R.id.L1_Et_Phone);
        passEt = getView().findViewById(R.id.L1_Et_Password);
        signinTV=getView().findViewById(R.id.L1_signin);
        eyeBt=getView().findViewById(R.id.L1_eye);

        fd = FirebaseDatabase.getInstance();
        userDr = fd.getReference().child("users").child("0");

        sharedPref=getActivity().getSharedPreferences("Data",Context.MODE_PRIVATE);
        editor=sharedPref.edit();

        vEL=new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boolean f = true;
                for (DataSnapshot a : snapshot.getChildren()) {
                    if (String.valueOf(phoneNo).equals(a.child("phone").getValue().toString())) {
                        f = false;
                        hashPass= Encode.hashPassword(pass);
                        if (hashPass.equals(a.child("password").getValue())) {
                            editor.putString("name",(String)a.child("Name").getValue());
                            editor.putLong("phone",(Long)a.child("phone").getValue());
                            editor.putInt("type",0);
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
                phoneNo = Long.parseLong(phoneEt.getText().toString());
                pass = passEt.getText().toString();
                if (String.valueOf(phoneNo).length() != 10) {
                    phoneEt.setError("Number must be 10 digits long");
                } else {
                    userDr.addListenerForSingleValueEvent(vEL);
                    userDr.removeEventListener(vEL);
                }

            }
        });

        signinTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    Intent signInAct = new Intent(getContext(), SignIn.class);
                    startActivity(signInAct);
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