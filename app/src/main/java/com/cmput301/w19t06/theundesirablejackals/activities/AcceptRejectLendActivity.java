package com.cmput301.w19t06.theundesirablejackals.activities;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cmput301.w19t06.theundesirablejackals.book.Book;
import com.cmput301.w19t06.theundesirablejackals.book.BookInformation;
import com.cmput301.w19t06.theundesirablejackals.book.BookRequest;
import com.cmput301.w19t06.theundesirablejackals.book.BookRequestList;
import com.cmput301.w19t06.theundesirablejackals.book.BookRequestStatus;
import com.cmput301.w19t06.theundesirablejackals.book.BookStatus;
import com.cmput301.w19t06.theundesirablejackals.classes.Geolocation;
import com.cmput301.w19t06.theundesirablejackals.classes.ToastMessage;
import com.cmput301.w19t06.theundesirablejackals.database.BookCallback;
import com.cmput301.w19t06.theundesirablejackals.database.BookRequestListCallback;
import com.cmput301.w19t06.theundesirablejackals.database.BooleanCallback;
import com.cmput301.w19t06.theundesirablejackals.database.DatabaseHelper;
import com.cmput301.w19t06.theundesirablejackals.database.UserCallback;
import com.cmput301.w19t06.theundesirablejackals.user.User;

/**
 * Allows use to accept of reject lend requests
 * Author: Kaya Thiessen
 * @see LentListActivity
 */
public class AcceptRejectLendActivity extends AppCompatActivity {
    private DatabaseHelper databaseHelper;
    private User currentUser;
    private boolean doneDatabaseFetch = false;
    private BookRequest request;
    private Geolocation geolocation;

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

        Toolbar toolbar = findViewById(R.id.tool_barAcceptReject);
        toolbar.setNavigationIcon(R.drawable.ic_action_back);
        toolbar.setTitle("Lend Requests");
        setSupportActionBar(toolbar);

        databaseHelper = new DatabaseHelper();
        databaseHelper.getCurrentUserFromDatabase(new UserCallback() {
            @Override
            public void onCallback(User user) {
                doneDatabaseFetch = true;
                if(user != null) {
                    currentUser = user;
                    geolocation = user.getPickUpLocation();
                }
            }
        });


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

        //Setting values to Visible depending on the status
        Button accept = (Button) findViewById(R.id.buttonAcceptRejectActivityAcceptRequest);
        Button reject = (Button) findViewById(R.id.buttonAcceptRejectActivityRejectRequest);
        LinearLayout map = (LinearLayout) findViewById(R.id.linearLayoutAcceptRejectLend);
        EditText isbn = (EditText) findViewById(R.id.editTextAcceptRejectLendISBN);
        Button isbnScan = (Button) findViewById(R.id.buttonAcceptRejectLendScanISBN);
        Button isbnConfirm = (Button) findViewById(R.id.buttonAcceptRejectLentConfirmISBN);

        BookRequestStatus status = request.getCurrentStatus();
        if (status == BookRequestStatus.PENDING){
            accept.setVisibility(View.VISIBLE);
            reject.setVisibility(View.VISIBLE);
            map.setVisibility(View.INVISIBLE);
            isbn.setVisibility(View.INVISIBLE);
            isbnScan.setVisibility(View.INVISIBLE);
            isbnConfirm.setVisibility(View.INVISIBLE);
        }
        else if(status == BookRequestStatus.ACCEPTED){
            accept.setVisibility(View.INVISIBLE);
            reject.setVisibility(View.INVISIBLE);
            map.setVisibility(View.VISIBLE);
            isbn.setVisibility(View.VISIBLE);
            isbnScan.setVisibility(View.VISIBLE);
            isbnConfirm.setVisibility(View.VISIBLE);
        }

        else if(status == BookRequestStatus.HANDED_OFF){
            accept.setVisibility(View.INVISIBLE);
            reject.setVisibility(View.INVISIBLE);
            map.setVisibility(View.VISIBLE);
            isbn.setVisibility(View.VISIBLE);
            isbnScan.setVisibility(View.VISIBLE);
            isbnConfirm.setVisibility(View.VISIBLE);
        }

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
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.change_location_popup_alert);
        dialog.setCanceledOnTouchOutside(true);
        TextView titleView = (TextView) dialog.findViewById(R.id.textViewAcceptRejectLendPopupTitle);
        titleView.setText("Use Default Pickup Location?");

        Button yes = (Button) dialog.findViewById(R.id.buttonAcceptRejectLendPopupChangeLocationYes);
        Button no = (Button) dialog.findViewById(R.id.buttonAcceptRejectLendPopupChangeLocationNo);

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //code the functionality when send button is clicked
                if(doneDatabaseFetch && currentUser != null) {
                    sendRequestUpdate(BookRequestStatus.ACCEPTED);
                }else if(doneDatabaseFetch){
                    showToast("Something went wrong communicating with database");
                }
                dialog.dismiss();
            }
        });

        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //code the functionality when NO button is clicked
                retrieveLocation();
                dialog.dismiss();
            }
        });

        dialog.show();

    }

    private void sendRequestUpdate(final BookRequestStatus status){
        request.setCurrentStatus(status);
        request.setPickuplocation(geolocation);
        databaseHelper.updateLendRequest(request, new BooleanCallback() {
            @Override
            public void onCallback(boolean bool) {
                if (bool) {
                    if(status == BookRequestStatus.DENIED){
                        doDeniedStuff();
                    }else {
                        doAcceptedStuff();
                    }
                } else {
                    showToast("Something went wrong updating the request");
                }
            }
        });
    }

    private void doAcceptedStuff() {
        databaseHelper.getSpecificBookLendRequests(request.getBookRequested().getOwner(),
                request.getBookRequested().getBookInformationKey(),
                new BookRequestListCallback() {
                    @Override
                    public void onCallback(BookRequestList bookRequestList) {
                        if(bookRequestList != null && bookRequestList.size() > 0){
                            for(BookRequest bookRequest : bookRequestList.getBookRequests()){
                                if(bookRequest.getBookRequestLendKey().equals(request.getBookRequestLendKey())){
                                    continue;
                                }
                                bookRequest.setCurrentStatus(BookRequestStatus.DENIED);
                                databaseHelper.updateLendRequest(bookRequest, new BooleanCallback() {
                                    @Override
                                    public void onCallback(boolean bool) {
                                        if(bool){
                                            showToast("Request updated successfully");
                                        }else{
                                            showToast("Something went wrong updating all other requests");
                                        }
                                    }
                                });
                            }
                        }else if(bookRequestList == null){
                            showToast("Something went wrong updating the book status");
                        }else{
                            showToast("You shouldn't ever see this message...");
                        }
                    }
                });
        BookInformation bookInformation = request.getBookRequested();
        bookInformation.setStatus(BookStatus.ACCEPTED);
        databaseHelper.updateBookInformation(bookInformation, new BooleanCallback() {
            @Override
            public void onCallback(boolean bool) {
                if(bool){
                    showToast("Book status updated successfully");
                }else{
                    showToast("Book status wasn't updated");
                }
            }
        });
    }

    private void doDeniedStuff(){
        databaseHelper.getSpecificBookLendRequests(request.getBookRequested().getOwner(),
                request.getBookRequested().getBookInformationKey(),
                new BookRequestListCallback() {
                    @Override
                    public void onCallback(BookRequestList bookRequestList) {
                        if(bookRequestList != null && bookRequestList.size() > 0){
                            boolean noOpenRequests = true;
                            for(BookRequest bookRequest : bookRequestList.getBookRequests()){
                                if(bookRequest.getCurrentStatus() != BookRequestStatus.DENIED){
                                    noOpenRequests = false;
                                }
                            }
                            if(noOpenRequests) {
                                BookInformation bookInformation = request.getBookRequested();
                                bookInformation.setStatus(BookStatus.AVAILABLE);
                                databaseHelper.updateBookInformation(bookInformation, new BooleanCallback() {
                                    @Override
                                    public void onCallback(boolean bool) {
                                        if (bool) {
                                            showToast("Request updated successfully");
                                        } else {
                                            showToast("Couldn't update book status to AVAILABLE");
                                        }
                                    }
                                });
                            }
                        }else if(bookRequestList == null){
                            showToast("Something went wrong updating the book status");
                        }else{
                            showToast("You shouldn't ever see this message...");
                        }
                    }
                });
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
                geolocation.setLatitude(Double.parseDouble(data.getStringExtra("lat")));
                geolocation.setLongitude(Double.parseDouble(data.getStringExtra("lng")));
                sendRequestUpdate(BookRequestStatus.ACCEPTED);
            }
        }

        // continue here
//        Intent intent = new Intent();
//        intent.putExtra("resultAD",true);
//        setResult(Activity.RESULT_OK,intent);
//        finish();
    }

    /**
     * If the reject button is pressed
     * @param view
     */
    public void reject(View view){
        //TODO
        //Return False, If False delete only this request
        sendRequestUpdate(BookRequestStatus.DENIED);
    }


    private void showToast(String message){
        ToastMessage.show(this, message);
    }
}
