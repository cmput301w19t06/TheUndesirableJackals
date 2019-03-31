package com.cmput301.w19t06.theundesirablejackals.adapter;

import android.net.Uri;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cmput301.w19t06.theundesirablejackals.activities.R;
import com.cmput301.w19t06.theundesirablejackals.classes.ToastMessage;
import com.cmput301.w19t06.theundesirablejackals.database.BooleanCallback;
import com.cmput301.w19t06.theundesirablejackals.database.DatabaseHelper;
import com.cmput301.w19t06.theundesirablejackals.database.UriCallback;
import com.cmput301.w19t06.theundesirablejackals.database.UserCallback;
import com.cmput301.w19t06.theundesirablejackals.database.UserListCallback;
import com.cmput301.w19t06.theundesirablejackals.user.User;
import com.cmput301.w19t06.theundesirablejackals.user.UserInformation;
import com.cmput301.w19t06.theundesirablejackals.user.UserList;
import com.squareup.picasso.Picasso;



public class FriendsRecyclerViewAdapter extends RecyclerView.Adapter<FriendsRecyclerViewAdapter.MyViewHolder> {

    private static final String TAG = "FriendsRVAdapter";

    private UserList dataSet;
    private RecyclerViewClickListener myListener;
    private DatabaseHelper databaseHelper = new DatabaseHelper();




    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        // each data item is a MyHealthStats member
        public ConstraintLayout mainTextView;
        private RecyclerViewClickListener myListener;


        MyViewHolder(ConstraintLayout v, RecyclerViewClickListener listener) {
            super(v);
            myListener = listener;
            mainTextView = v;

            mainTextView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v){
            myListener.onClick(v, getAdapterPosition());
        }

    }


    public FriendsRecyclerViewAdapter(){
        //get the data.... Unsure if this is it...
        dataSet = new UserList();
    }


    // Create new views (invoked by the layout manager)
    @Override
    public FriendsRecyclerViewAdapter.MyViewHolder onCreateViewHolder(final ViewGroup parent,
                                                                      int viewType) {
        // create a new view
        ConstraintLayout v = (ConstraintLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_friends, parent, false);
        MyViewHolder vh = new MyViewHolder(v, myListener);
        return vh;
    }


    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(FriendsRecyclerViewAdapter.MyViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        TextView usernameTextView = (TextView) holder.mainTextView.findViewById(R.id.textViewItemFriendsUserName);
        TextView emailTextView = (TextView) holder.mainTextView.findViewById(R.id.textViewItemFriendsEmail);

        UserInformation userInformation = (UserInformation) dataSet.getUser(position);

        String username = userInformation.getUserName();
        String email = userInformation.getEmail();


        if(username != null) {
            usernameTextView.setText(username);
        }
        if(email != null) {
            emailTextView.setText(email);
        }

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    @Override
    public void onViewAttachedToWindow(FriendsRecyclerViewAdapter.MyViewHolder holder){
        int position = holder.getAdapterPosition();
        final ImageView profileImageView = (ImageView) holder.mainTextView.findViewById(R.id.imageViewItemFriendsPhoto);
        UserInformation userInformation = dataSet.getUser(position);
        // get URL of the thumbnail
        String profile = userInformation.getUserPhoto();
        if(profile != null && !profile.isEmpty()) {
            databaseHelper.getProfilePictureUri(userInformation, new UriCallback() {
                @Override
                public void onCallback(Uri uri) {
                    if(uri != null) {
                        Picasso.get()
                                .load(uri)
                                .error(R.drawable.book_icon)
                                .placeholder(R.drawable.book_icon)
                                .into(profileImageView);
                    }else{
                        Picasso.get()
                                .load(R.drawable.book_icon)
                                .into(profileImageView);
                    }
                }
            });

        }else{
            Picasso.get()
                    .load(R.drawable.book_icon)
                    .into(profileImageView);
        }
    }

    public void deleteItem(int position){
        dataSet.delete(dataSet.getUser(position));
        this.notifyItemRemoved(position);    //notifies the RecyclerView Adapter that data in adapter has been removed at a particular position.
        this.notifyItemRangeChanged(position, this.getItemCount());
        updateItems();
    }

    public void addItem(UserInformation u){
        dataSet.add(u);
        updateItems();
    }

    public UserInformation get(int position) {
        return dataSet.getUser(position);
    }


    private void updateItems(){
        this.notifyDataSetChanged();
    }


    public void setMyListener(RecyclerViewClickListener listener){
        myListener = listener;
    }

    public void setDataSet(UserList data){
        dataSet = data;
        updateItems();
    }

    public void doDeleteFriend(final int position) {
        final UserInformation userInformation = dataSet.getUser(position);
        databaseHelper.getCurrentUserFromDatabase(new UserCallback() {
            @Override
            public void onCallback(User user) {
                if (user != null) {
                    final UserInformation currentMe = user.getUserInfo();
                    databaseHelper.getFriendsList(userInformation.getUserName(), new UserListCallback() {
                        @Override
                        public void onCallback(UserList userList) {
                            if(userList != null){
                                userList.getUserlist().remove(currentMe);
                                databaseHelper.updateFriendsList(userInformation.getUserName(), userList, new BooleanCallback() {
                                    @Override
                                    public void onCallback(boolean bool) {
                                        if(bool){
                                            databaseHelper.getFriendsList(currentMe.getUserName(), new UserListCallback() {
                                                @Override
                                                public void onCallback(UserList userList) {
                                                    if (userList != null) {
                                                        userList.getUserlist().remove(userInformation);
                                                        databaseHelper.updateFriendsList(currentMe.getUserName(), userList, new BooleanCallback() {
                                                            @Override
                                                            public void onCallback(boolean bool) {
                                                                if (bool) {
                                                                    Log.d(TAG, "Friend removed");
                                                                    dataSet.getUserlist().remove(position);
                                                                    notifyItemRemoved(position);
                                                                    notifyDataSetChanged();
                                                                } else {
                                                                    Log.d(TAG, "Current User friendList was not updates");
                                                                    Log.e(TAG, "Current User friendlist update callback");
                                                                }
                                                            }
                                                        });
                                                    } else {
                                                        Log.d(TAG, "Current User's friend list came back null");
                                                        Log.e(TAG, "CurrentMe userlist callback");
                                                    }
                                                }
                                            });
                                        }else{
                                            Log.d(TAG, "Old Friend friendList was not updates");
                                            Log.e(TAG, "Old Friend friendlist update callback");
                                        }
                                    }
                                });
                            }else{
                                Log.d(TAG, "Old Friend's friend list came back null");
                                Log.e(TAG, "Old Friend userlist callback");
                            }
                        }
                    });

                }
            }
        });
    }




}
