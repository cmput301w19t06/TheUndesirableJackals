package com.cmput301.w19t06.theundesirablejackals.activities;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;


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

public class SelectLocationActivity extends AppCompatActivity implements OnMapReadyCallback {
    private DatabaseHelper databaseHelper;
    // contains new coordinates
    private Double lat;
    private Double lng;
    private Button submit;
    Toolbar toolbar;
    private final float mZoomLevel = 17.0f;
    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_location);
        toolbar = findViewById(R.id.toolbarSelectPickupLocation);
        toolbar.setNavigationIcon(R.drawable.ic_action_back);
        toolbar.setTitle("Edit Pickup Location");
        setSupportActionBar(toolbar);


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // declare buttons and their actions
        submit = (Button) findViewById(R.id.submitLocation);

        // submit action
        submit.setOnClickListener(new View.OnClickListener() {
            /**
             * Called when user presses the submit info option
             * Return the lat and lng
             * @param view Context passed as parameter for the intent
             */
            public void onClick(View view) {
                returnLocation();
            }
        });
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    /**
     * Initializes the database helper which will be used to retrieve default pick up location
     * from current user
     */
    @Override
    public void onStart() {
        super.onStart();
        databaseHelper = new DatabaseHelper();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        databaseHelper.getCurrentUserFromDatabase(new UserCallback() {
            @Override
            public void onCallback(User user) {
                LatLng coord;

                if (user != null){
                    // get the instance Geolocation from user
                    Geolocation geolocation = user.getPickUpLocation();
                    // get the values
                    Double lt = geolocation.getLatitude();
                    Double ln = geolocation.getLongitude();
                    newLocation(lt, ln);
                    coord = new LatLng(lt, ln);

                } else {
                    newLocation(53.521331248, -113.521331248);
                    coord = new LatLng(53.521331248, -113.521331248); // set to the U of A
                }

                // Add a marker in coord and move the camera
                mMap.addMarker(new MarkerOptions().position(coord).title("Default pick up location")).showInfoWindow();
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(coord, mZoomLevel));
            }
        });

        // handles clicks on the map and modifications to the coordinates
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng point) {
                // clear the previous marker
                mMap.clear();

                // updates lat and lng to newly selected position
                newLocation(point.latitude, point.longitude);

                // add marker in new coord and move the camera
                mMap.addMarker(new MarkerOptions().position(point).title("New pick up location")).showInfoWindow();
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(point, mZoomLevel));
            }
        });
    }

    private void newLocation(Double latitude, Double longitude) {
        lat = latitude;
        lng = longitude;
    }

    /**
     * Returns the lat and lng selected by the user to the activity that called the
     * "startActivityForResult()"
     * @see AcceptRejectLendActivity
     */
    public void returnLocation() {
        Intent returnIntent = new Intent();
        returnIntent.putExtra("lat", Double.toString(lat));
        returnIntent.putExtra("lng", Double.toString(lng));
        setResult(Activity.RESULT_OK,returnIntent);
        finish();
    }
}
