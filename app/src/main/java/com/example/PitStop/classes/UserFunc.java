package com.example.PitStop.classes;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserFunc {
    public long getPhone(Context con){
        SharedPreferences sharePref;
        sharePref = con.getSharedPreferences("Data", Context.MODE_PRIVATE);
        return sharePref.getLong("phone", 0);
    }
    public int getType(Context con){
        SharedPreferences sharePref;
        sharePref = con.getSharedPreferences("Data", Context.MODE_PRIVATE);
        return sharePref.getInt("type", -1);
    }
    public String getName(Context con){
        SharedPreferences sharePref;
        sharePref = con.getSharedPreferences("Data", Context.MODE_PRIVATE);
        return sharePref.getString("name","user");
    }

    public UserFunc() {
    }

    public void clearAll(Context con){
        SharedPreferences sharePref;
        SharedPreferences.Editor editor;
        sharePref = con.getSharedPreferences("Data", Context.MODE_PRIVATE);
        editor=sharePref.edit();
        editor.clear();
        editor.apply();
    }

    public Consumer getConsumer(Context con){
        final long phone=getPhone(con);
        Log.i("UserFunc", "getConsumer: "+phone);
        final Consumer[] c = new Consumer[1];
        FirebaseDatabase fd=FirebaseDatabase.getInstance();
        DatabaseReference userDr=fd.getReference().child("users").child("0");
        ValueEventListener v =new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.i("Main", "Came here");
                for (DataSnapshot a : snapshot.getChildren()) {
                    if(String.valueOf(phone).equals(a.child("phone").getValue().toString())) {
                        c[0] = (Consumer) a.getValue();
                        Log.i("Main", "onDataChange: " + a.getKey());
                        return;
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        };
         userDr.addListenerForSingleValueEvent(v);
         userDr.removeEventListener(v);
        Log.i("Main", "getConsumer: "+c[0]);
         return c[0];
    }
}
