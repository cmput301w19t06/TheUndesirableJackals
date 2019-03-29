package com.cmput301.w19t06.theundesirablejackals.adapter;


import android.graphics.Color;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cmput301.w19t06.theundesirablejackals.activities.R;
import com.cmput301.w19t06.theundesirablejackals.classes.MessageMetaData;
import com.cmput301.w19t06.theundesirablejackals.classes.Messaging;
import com.cmput301.w19t06.theundesirablejackals.database.BooleanCallback;
import com.cmput301.w19t06.theundesirablejackals.database.DatabaseHelper;
import com.cmput301.w19t06.theundesirablejackals.database.MessageListCallback;
import com.cmput301.w19t06.theundesirablejackals.database.UriCallback;
import com.cmput301.w19t06.theundesirablejackals.database.UserCallback;
import com.cmput301.w19t06.theundesirablejackals.database.UserInformationCallback;
import com.cmput301.w19t06.theundesirablejackals.user.User;
import com.cmput301.w19t06.theundesirablejackals.user.UserInformation;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

public class MessagesRecyclerViewAdapter extends RecyclerView.Adapter<MessagesRecyclerViewAdapter.MyViewHolder> implements SwipeRefreshLayout.OnRefreshListener {

    private static final String TAG = "MessagesRVA";

    private ArrayList<MessageMetaData> dataSet = new ArrayList<>();
    private User currentUser;
    private RecyclerViewClickListener myListener;
    private SwipeRefreshLayout swipeRefreshLayout;
    private DatabaseHelper databaseHelper;


    @Override
    public void onRefresh() {
        swipeRefreshLayout.setRefreshing(true);
        getMessages();
        swipeRefreshLayout.setRefreshing(false);
    }



    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        // create a new view
        ConstraintLayout v = (ConstraintLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.personal_message_item, parent, false);
        return new MyViewHolder(v, myListener);

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        TextView usernameView = holder.mainView.findViewById(R.id.textViewPersonalMessageItemUsername);
        TextView unseenView = holder.mainView.findViewById(R.id.textViewPersonalMessageItemUnseen);
        final ImageView profilePhoto = holder.mainView.findViewById(R.id.imageViewPersonalMessagePhoto);


        Integer unseen = dataSet.get(position).getUnseen();
        String username = dataSet.get(position).getUsername();

        databaseHelper.getUserInfoFromDatabase(username, new UserInformationCallback() {
            @Override
            public void onCallback(UserInformation userInformation) {
                if (userInformation.getUserPhoto()!=null && !userInformation.getUserPhoto().isEmpty()) {
                    databaseHelper.getProfilePictureUri(userInformation, new UriCallback() {
                        @Override
                        public void onCallback(Uri uri) {
                            if (uri != null) {
                                Picasso.get()
                                        .load(uri)
                                        .error(R.drawable.ic_person_outline_grey_24dp)
                                        .placeholder(R.drawable.ic_loading_with_text)
                                        .into(profilePhoto);
                            }
                        }

                    });
                }

            }
        });
        switch (unseen){
            case 0 :
                unseenView.setTextColor(Color.parseColor("#ff000000"));
                unseenView.setText(unseen + " New Messages");
                break;
            default :
                unseenView.setTextColor(Color.parseColor("#ffcc0000"));
                unseenView.setText(unseen + " New Messages");
                break;
        }
        usernameView.setText(username);
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        // each data item is a MyHealthStats member
        private ConstraintLayout mainView;
        private RecyclerViewClickListener myListener;

        MyViewHolder(ConstraintLayout v, RecyclerViewClickListener listener) {
            super(v);
            myListener = listener;
            mainView = v;
            mainView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v){
            myListener.onClick(v, getAdapterPosition());
        }

    }

    public MessagesRecyclerViewAdapter(RecyclerViewClickListener listener, SwipeRefreshLayout swipeRefreshLayout){
        this.myListener = listener;
        this.swipeRefreshLayout = swipeRefreshLayout;
        this.swipeRefreshLayout.setOnRefreshListener(this);
        databaseHelper = new DatabaseHelper();
        onRefresh();
    }

    public ArrayList<MessageMetaData> getDataSet() {
        return dataSet;
    }

    public void setDataSet(ArrayList<MessageMetaData> dataset) {
        this.dataSet = dataset;
        notifyDataSetChanged();
    }


    private void getMessages() {
        if(currentUser != null){
            retrieveMessagesFromDatabase();
        }else {
            databaseHelper.getCurrentUserFromDatabase(new UserCallback() {
                @Override
                public void onCallback(User user) {
                    if (user != null) {
                        currentUser = user;
                        retrieveMessagesFromDatabase();
                    } else {
                        Log.d(TAG, "Something went wrong getting the current user");
                    }
                }
            });
        }
    }

    void deleteItems(int position) {
        MessageMetaData metaData = dataSet.get(position);
        dataSet.remove(position);
        notifyItemRemoved(position);
        notifyDataSetChanged();
        for(Messaging message : metaData.getMessagings()){
            databaseHelper.deleteMessage(message, new BooleanCallback() {
                @Override
                public void onCallback(boolean bool) {
                    if(bool){
                        Log.d(TAG, "Delete succeeded");
                    }else{
                        Log.d(TAG, "Delete failed");
                    }
                }
            });
        }
    }

    private void retrieveMessagesFromDatabase(){
        databaseHelper.getMessages(currentUser.getUserInfo().getUserName(), new MessageListCallback() {
            @Override
            public void onCallback(ArrayList<Messaging> messagingArrayList) {
                if(messagingArrayList != null){
//                    messages = messagingArrayList;
                    HashMap<String, Integer> seen = new HashMap<>();
                    dataSet = new ArrayList<>();
                    for(Messaging m : messagingArrayList){
                        if(seen.containsKey(m.getFrom())){
                            MessageMetaData temp = dataSet.get(seen.get(m.getFrom()));
                            temp.getMessagings().add(m);
                            if(!m.getSeen()){
                                temp.addUnseen();
                            }
                        }else if(seen.containsKey(m.getTo())) {
                            MessageMetaData temp = dataSet.get(seen.get(m.getTo()));
                            temp.getMessagings().add(m);
                            if (!m.getSeen()) {
                                temp.addUnseen();
                            }
                        }else{
                            MessageMetaData temp = new MessageMetaData();
                            if(!currentUser.getUserInfo().getUserName().equals(m.getFrom())){
                                seen.put(m.getFrom(), dataSet.size());
                                temp.getMessagings().add(m);
                                if (!m.getSeen()) {
                                    temp.addUnseen();
                                }
                                temp.setUsername(m.getFrom());
                            }else{
                                seen.put(m.getTo(), dataSet.size());
                                temp.getMessagings().add(m);
                                if (!m.getSeen()) {
                                    temp.addUnseen();
                                }
                                temp.setUsername(m.getTo());
                            }
                            dataSet.add(temp);
                        }
                    }
                    notifyDataSetChanged();
                }else{
                    Log.d(TAG, "Something went wrong getting messages");
                }
            }
        });
    }
}
