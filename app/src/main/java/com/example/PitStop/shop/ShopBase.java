package com.example.PitStop.shop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.PitStop.MainActivity;
import com.example.PitStop.R;
import com.example.PitStop.classes.UserFunc;
import com.google.android.material.navigation.NavigationView;

public class ShopBase extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout dl;
    private NavigationView nv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_base);

        Toolbar tb=findViewById(R.id.SP_toolbar);
        setSupportActionBar(tb);

        dl=findViewById(R.id.SP_drawer);
        nv=findViewById(R.id.SP_nav_view);

        nv.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle toggle=new ActionBarDrawerToggle(this,dl,tb,R.string.nav_drawer_open,R.string.nav_drawer_close);
        dl.addDrawerListener(toggle);
        toggle.syncState();

        if(savedInstanceState==null){
            getSupportFragmentManager().beginTransaction().replace(R.id.SP_fragment_container_view_tag,new SP_myshopFrag()).commit();
            nv.setCheckedItem(R.id.SP_myshop);
        }
    }

    @Override
    public void onBackPressed() {
        if(dl.isDrawerOpen(GravityCompat.START)) {
            dl.closeDrawer(GravityCompat.START);
        }
        else{
            getSupportFragmentManager().beginTransaction().replace(R.id.SP_fragment_container_view_tag,new SP_myshopFrag()).commit();
            nv.setCheckedItem(R.id.SP_myshop);
        }

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.SP_myshop:
                getSupportFragmentManager().beginTransaction().replace(R.id.SP_fragment_container_view_tag,new SP_myshopFrag()).commit();
                break;
//            case R.id.SP_account:
//                getSupportFragmentManager().beginTransaction().replace(R.id.SP_fragment_container_view_tag,new SP_AccountFrag()).commit();
//                break;
            case R.id.SP_settings:
                getSupportFragmentManager().beginTransaction().replace(R.id.SP_fragment_container_view_tag,new SP_settingsFrag()).commit();
                break;
            case R.id.SP_signout:
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