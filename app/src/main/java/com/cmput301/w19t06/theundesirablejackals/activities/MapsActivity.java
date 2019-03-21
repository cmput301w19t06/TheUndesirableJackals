/**
 * Retrieves the user's pick up default location and displays it in a google map as a mark
 * @version March 8, 2019
 * @see PersonalProfileActivity
 */

package com.cmput301.w19t06.theundesirablejackals.activities;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.cmput301.w19t06.theundesirablejackals.classes.Geolocation;
import com.cmput301.w19t06.theundesirablejackals.database.BooleanCallback;
import com.cmput301.w19t06.theundesirablejackals.database.DatabaseHelper;
import com.cmput301.w19t06.theundesirablejackals.database.UserCallback;
import com.cmput301.w19t06.theundesirablejackals.user.User;
import com.cmput301.w19t06.theundesirablejackals.user.UserInformation;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    private DatabaseHelper databaseHelper;
    // contains new coordinates
    private Double newLat = null;
    private Double newLng = null;
    private Button submit;
    private Button cancel;

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        databaseHelper = new DatabaseHelper();

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // declare buttons and their actions
        submit = (Button) findViewById(R.id.buttonSubmitNewLocation);
        cancel = (Button) findViewById(R.id.buttonCancel);

        // submit action
        submit.setOnClickListener(new View.OnClickListener() {
            /**
             * Called when user presses the submit info option
             * Saves the location and sends the user back to "MainHomeView" activity
             * @param view Context passed as parameter for the intent
             */
            public void onClick(View view) {
                updatePickUpLocation();
            }
        });

        // cancel action
        cancel.setOnClickListener(new View.OnClickListener() {
            // goes back without saving
            public void onClick(View view) {
                startMainHomeView();
            }
        });
    }

    /**
     * Executes when the map is ready. It retrieves the user's pick up location lat and lon from
     * attribute Geolocation, mark it on the map and rotates the map to that location
     * @param googleMap Google map instance that will be displayed
     */
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

                    coord = new LatLng(lt, ln);

                } else {
                    coord = new LatLng(-34, 151); // set to some random location
                }

                // Add a marker in coord and move the camera
                mMap.addMarker(new MarkerOptions().position(coord).title("Current pick up location")).showInfoWindow();;
                mMap.moveCamera(CameraUpdateFactory.newLatLng(coord));
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
                mMap.addMarker(new MarkerOptions().position(point).title("New pick up location")).showInfoWindow();;
                mMap.moveCamera(CameraUpdateFactory.newLatLng(point));
            }
        });

    }

    /**
     * Given new lat and lng creates new object "Geolocation" and saves it for the new user
     * @version 1 march 21, 2019
     */
    public void updatePickUpLocation() {
        Geolocation newGeolocation;

        if (newLat != null && newLng != null) {
            newGeolocation = new Geolocation(newLat, newLng);

            // now saves the new object on Firebase
            databaseHelper.updatePickUpLocation(newGeolocation, new BooleanCallback() {
                /**
                 * Writes the data and goes back to main activity if successful
                 * @param bool Value representing the success of the operation
                 */
                @Override
                public void onCallback(boolean bool) {
                    if(bool){
                        startMainHomeView();
                    }
                }
            });

        }
    }

    private void newLocation(Double lat, Double lng) {
        newLat = lat;
        newLng = lng;
    }

    /**
     * Called to return to main activity
     */
    public void startMainHomeView(){
        Intent intent = new Intent(this, MainHomeViewActivity.class);
        startActivity(intent);
    }
}
