package com.cmput301.w19t06.theundesirablejackals.adapter;


import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cmput301.w19t06.theundesirablejackals.activities.R;
import com.cmput301.w19t06.theundesirablejackals.classes.MessageMetaData;
import com.cmput301.w19t06.theundesirablejackals.classes.Messaging;
import com.cmput301.w19t06.theundesirablejackals.database.DatabaseHelper;
import com.cmput301.w19t06.theundesirablejackals.database.MessageListCallback;
import com.cmput301.w19t06.theundesirablejackals.database.UserCallback;
import com.cmput301.w19t06.theundesirablejackals.user.User;
import com.cmput301.w19t06.theundesirablejackals.user.UserList;

import java.util.ArrayList;
import java.util.HashMap;

public class MessagesRecyclerViewAdapter extends RecyclerView.Adapter<MessagesRecyclerViewAdapter.MyViewHolder> implements SwipeRefreshLayout.OnRefreshListener {

    private static final String TAG = "MessagesRVA";

    private ArrayList<MessageMetaData> dataset = new ArrayList<MessageMetaData>();
    private ArrayList<MessageMetaData> cacheDataset = new ArrayList<MessageMetaData>();
    private User currentUser;
    private RecyclerViewClickListener myListener;
    private SwipeRefreshLayout swipeRefreshLayout;
    private DatabaseHelper databaseHelper;
    private ArrayList<Messaging> messages;


    @Override
    public void onRefresh() {
        swipeRefreshLayout.setRefreshing(true);
        getMessages();
        swipeRefreshLayout.setRefreshing(false);
    }



    @NonNull
    @Override
    public MessagesRecyclerViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        // create a new view
        ConstraintLayout v = (ConstraintLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.personal_message_item, parent, false);
        MyViewHolder vh = new MyViewHolder(v, myListener);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
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
    }

    public ArrayList<MessageMetaData> getDataset() {
        return dataset;
    }

    public void setDataset(ArrayList<MessageMetaData> dataset) {
        this.dataset = dataset;
    }

    public void setMyListener(RecyclerViewClickListener myListener) {
        this.myListener = myListener;
    }

    public void resetFilter(){
        if(currentUser != null && cacheDataset != null) {
            this.dataset = cacheDataset;
            notifyDataSetChanged();
        }else{
            getMessages();
        }
    }

    private void getMessages() {

        databaseHelper.getCurrentUserFromDatabase(new UserCallback() {
            @Override
            public void onCallback(User user) {
                if(user != null){
                    currentUser = user;

                    databaseHelper.getMessages(currentUser.getUserInfo().getUserName(), new MessageListCallback() {
                        @Override
                        public void onCallback(ArrayList<Messaging> messagingArrayList) {
                            if(messagingArrayList != null){
                                messages = messagingArrayList;
                                HashMap<String, Integer> seen = new HashMap<>();
                                for(Messaging m : messages){
                                    if(seen.containsKey(m.getFrom())){
                                        MessageMetaData temp = dataset.get(seen.get(m.getFrom()));
                                        temp.getMessagings().add(m);
                                        if(!m.getSeen()){
                                            temp.addUnseen();
                                        }
                                    }else if(seen.containsKey(m.getTo())) {
                                        MessageMetaData temp = dataset.get(seen.get(m.getTo()));
                                        temp.getMessagings().add(m);
                                        if (!m.getSeen()) {
                                            temp.addUnseen();
                                        }
                                    }else{
                                        MessageMetaData temp = new MessageMetaData();
                                        if(!currentUser.getUserInfo().getUserName().equals(m.getFrom())){
                                            seen.put(m.getFrom(), dataset.size());
                                            temp.getMessagings().add(m);
                                            if (!m.getSeen()) {
                                                temp.addUnseen();
                                            }
                                            temp.setUsername(m.getFrom());
                                        }else{
                                            seen.put(m.getTo(), dataset.size());
                                            temp.getMessagings().add(m);
                                            if (!m.getSeen()) {
                                                temp.addUnseen();
                                            }
                                            temp.setUsername(m.getTo());
                                        }
                                        dataset.add(temp);
                                    }
                                }
                                notifyDataSetChanged();
                            }else{
                                Log.d(TAG, "Something went wrong getting messages");
                            }
                        }
                    });
                }else{
                    Log.d(TAG, "Something went wrong getting the current user");
                }
            }
        });

    }
}
