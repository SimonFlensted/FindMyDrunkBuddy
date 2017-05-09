package com.example.simon.findmydrunkbuddy;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;

        Intent intent = getIntent();

        ArrayList<User> users = (ArrayList<User>) intent.getSerializableExtra("Users");
        for(User user : users) {
            float userLongtitude = user.getLongtitude();
            float userLattitude = user.getLattitude();
            String username = user.getName();

            LatLng member = new LatLng(userLattitude, userLongtitude);
            mMap.addMarker(new MarkerOptions().position(member).title(username)).showInfoWindow();
            mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(users.get(0).getLattitude(), users.get(0).getLongtitude())));
            mMap.moveCamera(CameraUpdateFactory.zoomTo(15));
        }

        mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(users.get(0).getLattitude(), users.get(0).getLongtitude())));
        mMap.moveCamera(CameraUpdateFactory.zoomTo(15));
    }
}
