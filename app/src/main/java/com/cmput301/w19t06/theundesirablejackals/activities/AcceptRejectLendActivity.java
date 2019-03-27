package com.cmput301.w19t06.theundesirablejackals.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.cmput301.w19t06.theundesirablejackals.book.Book;
import com.cmput301.w19t06.theundesirablejackals.book.BookInformation;
import com.cmput301.w19t06.theundesirablejackals.book.BookRequest;
import com.cmput301.w19t06.theundesirablejackals.database.BookCallback;
import com.cmput301.w19t06.theundesirablejackals.database.DatabaseHelper;

/**
 * Allows use to accept of reject lend requests
 * Author: Kaya Thiessen
 * @see LentListActivity
 */
public class AcceptRejectLendActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private DatabaseHelper databaseHelper;

    private Double latitude;
    private Double longitude;
    private BookRequest request;

    /**
     * General creation
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accept_reject_lend);

        Intent intent = getIntent();
        request = (BookRequest) intent.getSerializableExtra("info");

        toolbar = findViewById(R.id.tool_barAcceptReject);
        toolbar.setNavigationIcon(R.drawable.ic_action_back);
        toolbar.setTitle("Lend Requests");
        setSupportActionBar(toolbar);

        databaseHelper = new DatabaseHelper();


        TextView username = (TextView) findViewById(R.id.textViewAcceptRejectActivityUserRequesting);
        TextView phone = (TextView) findViewById(R.id.textViewAcceptRejectLendPhone);
        TextView email = (TextView) findViewById(R.id.textViewAcceptRejectLendEmail);
        final TextView title = (TextView) findViewById(R.id.textViewAcceptRejectActivityBookTitle);

        //Set the values
        username.setText(request.getBorrower().getUserName());
        phone.setText(request.getBorrower().getPhoneNumber());
        email.setText(request.getBorrower().getEmail());
        BookInformation i = request.getBookRequested();
        title.setText("");
        databaseHelper.getBookFromDatabase(i.getIsbn(), new BookCallback() {
            @Override
            public void onCallback(Book book) {
                if(book != null){
                    title.setText(book.getTitle());
                }
            }
        });

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), com.cmput301.w19t06.theundesirablejackals.activities.LentListActivity.class));
                finish();
            }
        });

    }

    /**
     * If the accept button is pressed
     * @param view
     */
    public void accept(View view){
    //TODO
        //Return True, If True delete all other requests regarding this book, update Book status

        // ask the user to select a pick up location
        retrieveLocation();

//        Intent intent = new Intent();
//        intent.putExtra("resultAD",true);
//        setResult(Activity.RESULT_OK,intent);
//        finish();
    }

    /**
     * Calls "PersonalProfileActivity" to allow the user to select a location on the map
     */
    public void retrieveLocation() {
        Intent i = new Intent(this, SelectLocationActivity.class);
        startActivityForResult(i, 1);
    }

    /**
     * Triggers after the activity for result of "PersonalProfileActivity" is completed
     * Will save the returned result on "longitude" and "latitude" attributes
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            if(resultCode == Activity.RESULT_OK){
                // the coordinates to do whatever you need to do with them
                latitude = Double.parseDouble(data.getStringExtra("lat"));
                longitude = Double.parseDouble(data.getStringExtra("lng"));
            }
        }

        // continue here
        Intent intent = new Intent();
        intent.putExtra("resultAD",true);
        setResult(Activity.RESULT_OK,intent);
        finish();
    }

    /**
     * If the reject button is pressed
     * @param view
     */
    public void reject(View view){
        //TODO
        //Return False, If False delete only this request
        Intent intent = new Intent();
        intent.putExtra("resultAD",false);
        setResult(Activity.RESULT_OK,intent);
        finish();
    }
}
