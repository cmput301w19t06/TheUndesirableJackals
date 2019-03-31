package com.cmput301.w19t06.theundesirablejackals.adapter;

import android.net.Uri;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.cmput301.w19t06.theundesirablejackals.activities.R;
import com.cmput301.w19t06.theundesirablejackals.classes.FriendRequest;
import com.cmput301.w19t06.theundesirablejackals.database.BooleanCallback;
import com.cmput301.w19t06.theundesirablejackals.database.DatabaseHelper;
import com.cmput301.w19t06.theundesirablejackals.database.UriCallback;
import com.cmput301.w19t06.theundesirablejackals.database.UserCallback;
import com.cmput301.w19t06.theundesirablejackals.database.UserListCallback;
import com.cmput301.w19t06.theundesirablejackals.user.User;
import com.cmput301.w19t06.theundesirablejackals.user.UserInformation;
import com.cmput301.w19t06.theundesirablejackals.user.UserList;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class FriendRequestRecyclerViewAdapter extends RecyclerView.Adapter<FriendRequestRecyclerViewAdapter.MyViewHolder> {

    private static final String TAG = "FriendRequestRVAdapter";

    private ArrayList<FriendRequest> dataSet;
    private RecyclerViewClickListener myListener;
    private DatabaseHelper databaseHelper = new DatabaseHelper();
    private InternalClickListener internalClickListener;



    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        // each data item is a MyHealthStats member
        public ConstraintLayout mainTextView;
        private InternalClickListener internalClickListener;
        private RecyclerViewClickListener myListener;
        private Button acceptButton;
        private Button declineButton;


        MyViewHolder(ConstraintLayout v, RecyclerViewClickListener listener, InternalClickListener internalClickListener) {
            super(v);
            this.myListener = listener;
            this.internalClickListener = internalClickListener;
            this.mainTextView = v;
            acceptButton = this.mainTextView.findViewById(R.id.buttonItemFriendRequestAcceptRequest);
            declineButton = this.mainTextView.findViewById(R.id.buttonItemFriendRequestDeclineRequest);
            acceptButton.setOnClickListener(this);
            declineButton.setOnClickListener(this);
            mainTextView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v){
            if(v.getId() == acceptButton.getId()){
                internalClickListener.onAcceptClick(getAdapterPosition());
            }else if(v.getId() == declineButton.getId()){
                internalClickListener.onDeclineClick(getAdapterPosition());
            }else {
                myListener.onClick(v, getAdapterPosition());
            }
        }

    }

//    // Provide a suitable constructor (depends on the kind of dataset)
//    public BooksRecyclerViewAdapter(RecyclerViewClickListener listener) {
//        myListener = listener;
//        dataSet = new BookToInformationMap();
//    }

    public FriendRequestRecyclerViewAdapter(){
        //get the data.... Unsure if this is it...
        dataSet = new ArrayList<>();
    }


    // Create new views (invoked by the layout manager)
    @Override
    public FriendRequestRecyclerViewAdapter.MyViewHolder onCreateViewHolder(final ViewGroup parent,
                                                                      int viewType) {
        // create a new view
        ConstraintLayout v = (ConstraintLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_friend_request, parent, false);
        MyViewHolder vh = new MyViewHolder(v, myListener, internalClickListener);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(FriendRequestRecyclerViewAdapter.MyViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        TextView usernameTextView = (TextView) holder.mainTextView.findViewById(R.id.textViewItemFriendRequestUserName);
        TextView emailTextView = (TextView) holder.mainTextView.findViewById(R.id.textViewItemFriendRequestEmail);
        ImageView profileImageView = (ImageView) holder.mainTextView.findViewById(R.id.imageViewItemFriendRequestPhoto);
        UserInformation sender = dataSet.get(position).getRequestSender();
        usernameTextView.setText(sender.getUserName());
        emailTextView.setText(sender.getEmail());
        Picasso.get()
                .load(R.drawable.ic_person_outline_grey_24dp)
                .into(profileImageView);


//        UserInformation i = (UserInformation) dataSet.getUser(position);



    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    @Override
    public void onViewAttachedToWindow(FriendRequestRecyclerViewAdapter.MyViewHolder holder){
//        super.onViewAttachedToWindow(holder);
//        ImageView bookThumbnail = (ImageView) holder.mainTextView.findViewById(R.id.imageViewItemFriendsPhoto);
        int position = holder.getAdapterPosition();
        final ImageView profileImageView = (ImageView) holder.mainTextView.findViewById(R.id.imageViewItemFriendRequestPhoto);
        UserInformation sender = dataSet.get(position).getRequestSender();

        if (sender.getUserPhoto() != null && !sender.getUserPhoto().isEmpty()) {
            databaseHelper.getProfilePictureUri(sender, new UriCallback() {
                @Override
                public void onCallback(Uri uri) {
                    if (uri != null) {
                        Picasso.get()
                                .load(uri)
                                .error(R.drawable.ic_person_outline_grey_24dp)
                                .placeholder(R.drawable.ic_loading_with_text)
                                .into(profileImageView);

                    }
                }

            });
        }
    }

    public void deleteItem(int position){
        dataSet.remove(position);
        this.notifyItemRemoved(position);    //notifies the RecyclerView Adapter that data in adapter has been removed at a particular position.
        this.notifyItemRangeChanged(position, this.getItemCount());
        updateItems();
    }

    public void addItem(FriendRequest friendRequest){
        dataSet.add(friendRequest);
        updateItems();
    }


    private void updateItems(){
        this.notifyDataSetChanged();
    }

    public FriendRequest getRequest(int position){
        return dataSet.get(position);
    }

    public void setMyListener(RecyclerViewClickListener listener){
        myListener = listener;
    }

    public void setInternalClickListener(InternalClickListener acceptListener){
        this.internalClickListener = acceptListener;
    }

    public void setDataSet(ArrayList<FriendRequest> data){
        dataSet = new ArrayList<FriendRequest>(data);
        updateItems();
    }



    public void swipeToDecline(int position){
        internalClickListener.onDeclineClick(position);
    }

}

