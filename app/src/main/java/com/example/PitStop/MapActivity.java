package com.example.PitStop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Dialog;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.PitStop.classes.MyLocation;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback,GoogleMap.OnMarkerDragListener{

    SupportMapFragment supportMapFragment;

    FusedLocationProviderClient client;

    String TAG="Map";
    Float DEF_ZOOM=15f;
    final String FINE_LOC=Manifest.permission.ACCESS_FINE_LOCATION;
    final String COARSE_LOC=Manifest.permission.ACCESS_COARSE_LOCATION;

    boolean mLocPermGranted;
    GoogleMap mMap;
    Geocoder geo;
    LatLng myLoc;
    MarkerOptions m;

    Button setBt;

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Toast.makeText(this,"Map Ready",Toast.LENGTH_SHORT).show();
        mMap=googleMap;
        getDeviceLocation();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        setBt=findViewById(R.id.map_set);
        getLocationPermission();
    }

    public void initMap(){
        supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.google_map);
        supportMapFragment.getMapAsync(MapActivity.this);
    }

    public void getDeviceLocation(){
        Log.i(TAG, "getDeviceLocation: getting");
        client=LocationServices.getFusedLocationProviderClient(this);
        try {
            if(mLocPermGranted){
                final Task location=client.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if(task.isSuccessful()){
                            Log.e(TAG, "onComplete: loc found");
                            Location currentLocation=(Location) task.getResult();

                            assert currentLocation != null;
                            myLoc= new LatLng(currentLocation.getLatitude(),currentLocation.getLongitude());
                            MoveCamera(myLoc,DEF_ZOOM);
                            m = new MarkerOptions().position(myLoc).draggable(true);
                            geoLocate(currentLocation.getLatitude(),currentLocation.getLongitude());
                            mMap.addMarker(m);

                            setBt.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    MyLocation.setMyLat(m.getPosition().latitude);
                                    MyLocation.setMyLong(m.getPosition().longitude);
//                                    Log.i("Map", "latitude : "+MyLocation.getMyLat());
//                                    Log.i("Map", "longitude : "+MyLocation.getMyLong());
                                    onBackPressed();
                                }
                            });
                        }
                        else{
                            Toast.makeText(getApplicationContext(),"Unable to get current location",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }
        catch (SecurityException e){
            Log.e(TAG, "getDeviceLocation: Security Exception "+e.getMessage() );
        }
    }

    private void geoLocate(Double x,Double y)  {
        geo=new Geocoder(MapActivity.this);
        List<Address> list=new ArrayList<>();
        try {
            geo.getFromLocation(x,y,1);
        }
        catch (IOException e){
            Log.e(TAG, "geoLocate: IOException "+ e.getMessage());
        }
        if(list.size()>0){
            Address address = list.get(0);
            String streename=address.getLocality();
            m.title(streename);
        }

    }

    private void MoveCamera(LatLng l,float zoom){
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(l,zoom));
    }

    @Override
    public void onMarkerDragStart(Marker marker) {
    }

    @Override
    public void onMarkerDrag(Marker marker) {
    }

    @Override
    public void onMarkerDragEnd(Marker marker) {
        LatLng l=marker.getPosition();
        m.position(l);
        MyLocation.setMyLat(l.latitude);
        MyLocation.setMyLong(l.longitude);
    }

    public void getLocationPermission(){
        String[] permissions= {FINE_LOC,COARSE_LOC};
        if(ContextCompat.checkSelfPermission(this.getApplicationContext(),FINE_LOC)==PackageManager.PERMISSION_GRANTED){
            if(ContextCompat.checkSelfPermission(this.getApplicationContext(),COARSE_LOC)==PackageManager.PERMISSION_GRANTED){
                mLocPermGranted=true;
                initMap();
            }else{
                ActivityCompat.requestPermissions(this,permissions,44);
            }
        }else{
            ActivityCompat.requestPermissions(this,permissions,44);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        mLocPermGranted=false;
        switch (requestCode){
            case 44:
                if(grantResults.length>0){
                    for(int i=0;i<grantResults.length;i++){
                        if(grantResults[i]!=PackageManager.PERMISSION_GRANTED){
                            mLocPermGranted=false;
                            return;
                        }
                    }
                    mLocPermGranted=true;
                    initMap();
                }
        }
    }

}