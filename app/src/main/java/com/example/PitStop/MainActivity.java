package com.example.PitStop;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.example.PitStop.cust.CustomerBase;
import com.example.PitStop.loginAct.LoginMain;
import com.example.PitStop.shop.ShopBase;

public class MainActivity extends AppCompatActivity {

    private boolean cusBool = true;
    private SharedPreferences sharePref;

    private Long phone;
    private String pass;
    private int type;

    private Object obj;
    private String className;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //check login
        sharePref = getSharedPreferences("Data",Context.MODE_PRIVATE);
        phone=sharePref.getLong("phone", 0);
        type = sharePref.getInt("type", -1);

        Log.i("Main", "phone:" +phone);
        Log.i("Main", "type: "+type);

        if (phone == 0) {
            //send to login page
            Intent login = new Intent(this, LoginMain.class);
            startActivity(login);
        } else {
            if (type == 0) {
                Intent customer = new Intent(this, CustomerBase.class);
                startActivity(customer);
            } else {
                Intent shop = new Intent(this, ShopBase.class);
                startActivity(shop);
            }
        }

        // TODO: 10-08-2020 find if it is a shop account or a consumer account

    }
}