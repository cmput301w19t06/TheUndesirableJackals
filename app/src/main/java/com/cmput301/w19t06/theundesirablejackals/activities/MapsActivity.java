package com.cmput301.w19t06.theundesirablejackals.activities;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.cmput301.w19t06.theundesirablejackals.classes.Geolocation;
import com.cmput301.w19t06.theundesirablejackals.database.DatabaseHelper;
import com.cmput301.w19t06.theundesirablejackals.database.UserCallback;
import com.cmput301.w19t06.theundesirablejackals.user.User;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    private DatabaseHelper databaseHelper;

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        databaseHelper = new DatabaseHelper(this);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;

        databaseHelper.getUserFromDatabase(new UserCallback() {
            @Override
            public void onCallback(User user) {
                LatLng coord;

                if (user != null){
                    // CANNOT ACCESS ATTRIBUTE Geolocation
                    // get the instance Geolocation from user
                    Geolocation geolocation = user.getPickUpLocation();
//
                    if (geolocation == null){
                        // SEEMS TO ALWAYS BE NULL
                        coord = new LatLng(-34, 151); // set to some random location
                    } else {
                        // get the values
                        Double lt = geolocation.getLatitude();
                        Double ln = geolocation.getLongitude();

                        coord = new LatLng(lt.intValue(), ln.intValue()); // set to some random location
                    }
//
                } else {
                    coord = new LatLng(-34, 151); // set to some random location
                }

                // Add a marker in coord and move the camera
                mMap.addMarker(new MarkerOptions().position(coord).title("Marker"));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(coord));
            }
        });

    }
}