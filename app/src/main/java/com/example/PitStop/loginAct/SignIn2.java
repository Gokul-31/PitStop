package com.example.PitStop.loginAct;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.PitStop.MapActivity;
import com.example.PitStop.R;
import com.example.PitStop.classes.Encode;
import com.example.PitStop.classes.MyLocation;
import com.example.PitStop.classes.Shop;
import com.example.PitStop.shop.shop_loc;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.UUID;

public class SignIn2 extends AppCompatActivity {

    private static final int ERROR_DIALOG_REQUEST = 9004;
    private EditText nameEt;
    private EditText phoneEt;
    private EditText passEt;
    private EditText rePassEt;
    private Button locationBt;
    private Button registerBt;
    private String name;
    private String pass;
    private String rePass;
    private long phone;
    private boolean locSelect=false;
    private Spinner shopTypeSp;
    private TextView spinnerError;
    private int type;
    private String hashPass;

    private FirebaseDatabase fd;
    private DatabaseReference userDataRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in2);

        //initialise
        {
            nameEt = findViewById(R.id.S2_ET_Name);
            phoneEt = findViewById(R.id.S2_ET_phone);
            passEt = findViewById(R.id.S2_ET_pass);
            rePassEt = findViewById(R.id.S2_ET_repass);
            registerBt = findViewById(R.id.S2_B_register);
            locationBt = findViewById(R.id.Si2_loc);
            fd = FirebaseDatabase.getInstance();
        }

        shopTypeSp = (Spinner) findViewById(R.id.shopTypeSpinner);
        spinnerError=(TextView) shopTypeSp.getSelectedView();
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.shopType, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        shopTypeSp.setAdapter(adapter);
        shopTypeSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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

        passEt.setTransformationMethod(PasswordTransformationMethod.getInstance());
        rePassEt.setTransformationMethod(PasswordTransformationMethod.getInstance());

        //temp
        locationBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(MyLocation.getMyLat()==0D&&MyLocation.getMyLong()==0D) {
                    if (isServicesOk()) {
                        locSelect=true;
                        Intent map = new Intent(getApplicationContext(), MapActivity.class);
                        startActivity(map);
                    }
                }
            }
        });
        registerBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userDataRef=fd.getReference().child("users").child(""+type);
                name=nameEt.getText().toString();
                phone=Long.parseLong(phoneEt.getText().toString());
                pass=passEt.getText().toString();
                rePass=rePassEt.getText().toString();

                if(name.length()==0){
                    nameEt.setError("Cannot be blank");
                }
                //add for phone
                else if(String.valueOf(phone).length()!=10){
                    phoneEt.setError("Number must be 10 digits long");
                }
                else if(!pass.equals(rePass)){
                    rePassEt.setError("Password mismatch");
                }
//                else if(!locSelect){
//                    Toast.makeText(getApplicationContext(),"Should Select Location",Toast.LENGTH_SHORT).show();
//                }
                else{
                    checkAlreadyUse();
                }

            }
        });
    }

    private void checkAlreadyUse() {
        userDataRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot a:snapshot.getChildren()){
                    if(String.valueOf(phone).equals(a.child("phone").getValue().toString())){
                        //number link with another account
                        Toast.makeText(getApplicationContext(),"Phone number already linked to another account",Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                hashPass= Encode.hashPassword(pass);
                Shop s= new Shop(UUID.randomUUID().toString(),phone,name,hashPass,type,MyLocation.getMyLat(),MyLocation.getMyLong());
                userDataRef.child(String.valueOf(phone)).setValue(s);
                MyLocation.setMyLat(0D);
                MyLocation.setMyLong(0D);

                onBackPressed();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void eyeFunc1(View view) {
        if(passEt.getTransformationMethod()== PasswordTransformationMethod.getInstance()){
            passEt.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        }
        else{
            passEt.setTransformationMethod(PasswordTransformationMethod.getInstance());
        }
    }

    public void eyeFunc2(View view) {
        if(rePassEt.getTransformationMethod()== PasswordTransformationMethod.getInstance()){
            rePassEt.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        }
        else{
            rePassEt.setTransformationMethod(PasswordTransformationMethod.getInstance());
        }
    }

    public boolean isServicesOk(){
        int available= GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this);
        if(available== ConnectionResult.SUCCESS){
            Log.i("main", "Services are Ok: ");
            return true;
        }
        else if(GoogleApiAvailability.getInstance().isUserResolvableError(available)){
            Log.i("main", "isServicesOk: fixable error");
            Dialog dialog=GoogleApiAvailability.getInstance().getErrorDialog(this,available,ERROR_DIALOG_REQUEST);
            dialog.show();
        }
        else{
            Log.i("main", "isServicesOk: u cant use maps");
        }
        return false;
    }
}