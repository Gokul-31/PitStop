package com.example.PitStop.loginAct;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.PitStop.R;
import com.example.PitStop.classes.Consumer;
import com.example.PitStop.classes.Encode;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.UUID;

public class SignIn extends AppCompatActivity {

    private EditText nameEt;
    private EditText phoneEt;
    private EditText passEt;
    private EditText rePassEt;
    private Button registerBt;
    private String name;
    private String pass;
    private String rePass;
    String hashPass;
    private long phone;
    private boolean locSelect;

    private FirebaseDatabase fd;
    private DatabaseReference userDataRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        //initialise
        nameEt=findViewById(R.id.S_ET_Name);
        phoneEt=findViewById(R.id.S_ET_phone);
        passEt=findViewById(R.id.S_ET_pass);
        rePassEt=findViewById(R.id.S_ET_repass);
        registerBt=findViewById(R.id.S_B_register);
        fd=FirebaseDatabase.getInstance();

        passEt.setTransformationMethod(PasswordTransformationMethod.getInstance());
        rePassEt.setTransformationMethod(PasswordTransformationMethod.getInstance());

        locSelect=true;

        registerBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userDataRef =fd.getReference().child("users").child("0");
                name =nameEt.getText().toString();
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
                        Toast.makeText(getApplicationContext(),"Phone number already linked to another account",Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                hashPass= Encode.hashPassword(pass);
                Consumer s = new Consumer(UUID.randomUUID().toString(), phone, name, hashPass);
                userDataRef.child(String.valueOf(phone)).setValue(s);
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
}