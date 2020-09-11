package com.example.PitStop.cust;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.PitStop.MainActivity;
import com.example.PitStop.R;
import com.example.PitStop.classes.UserFunc;
import com.google.android.material.navigation.NavigationView;

public class CustomerBase extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private TextView head_nameTV;
    private TextView head_userTV;
    private DrawerLayout dl;
    private NavigationView nv;
    private View headerLayout;
    private String s;

    private int shopSearchType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_base);

        Toolbar tb=findViewById(R.id.CN_toolbar);
//        tb.setBackground(getResources().getDrawable(R.drawable.grad));
        setSupportActionBar(tb);

        dl=findViewById(R.id.CN_drawer);
        nv=findViewById(R.id.CN_nav_view);
        headerLayout=nv.getHeaderView(0);
        head_nameTV=headerLayout.findViewById(R.id.head_name);
        head_userTV=headerLayout.findViewById(R.id.head_user);


        UserFunc u = new UserFunc();
        s=u.getName(getApplicationContext());
//        s=u.getConsumer(getApplicationContext()).getName();
        // TODO: 13-08-2020  
        head_nameTV.setText(s);
        head_userTV.setText(R.string.cust);

        nv.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle toggle=new ActionBarDrawerToggle(this,dl,tb,R.string.nav_drawer_open,R.string.nav_drawer_close);
        dl.addDrawerListener(toggle);
        toggle.syncState();

        if(savedInstanceState==null){
            getSupportFragmentManager().beginTransaction().replace(R.id.CN_fragment_container_view_tag,new CS_shopFrag()).commit();
            nv.setCheckedItem(R.id.CN_shops);
        }
    }

    @Override
    public void onBackPressed() {
        if(dl.isDrawerOpen(GravityCompat.START)) {
            dl.closeDrawer(GravityCompat.START);
        }
        else{
            getSupportFragmentManager().beginTransaction().replace(R.id.CN_fragment_container_view_tag,new CS_shopFrag()).commit();
            nv.setCheckedItem(R.id.CN_shops);
        }

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.CN_shops:
                getSupportFragmentManager().beginTransaction().replace(R.id.CN_fragment_container_view_tag,new CS_shopFrag()).commit();
                break;
            case R.id.CN_bookings:
                getSupportFragmentManager().beginTransaction().replace(R.id.CN_fragment_container_view_tag,new CS_bookFrag()).commit();
                break;
            case R.id.CN_signout:
                // TODO: 11-08-2020 signout
                UserFunc userFunc=new UserFunc();
                userFunc.clearAll(getApplicationContext());
                Intent goHome=new Intent(getApplicationContext(), MainActivity.class);
                startActivity(goHome);
                break;
        }
        dl.closeDrawer(GravityCompat.START);
        return true;
    }
}