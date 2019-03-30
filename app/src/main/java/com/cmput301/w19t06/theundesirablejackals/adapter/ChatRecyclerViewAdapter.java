package com.cmput301.w19t06.theundesirablejackals.adapter;


import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cmput301.w19t06.theundesirablejackals.activities.R;
import com.cmput301.w19t06.theundesirablejackals.classes.MessageMetaData;
import com.cmput301.w19t06.theundesirablejackals.classes.Messaging;


public class ChatRecyclerViewAdapter extends RecyclerView.Adapter<ChatRecyclerViewAdapter.MyViewHolder> {

    MessageMetaData dataSet;


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ConstraintLayout v = (ConstraintLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_chat_layout, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        TextView theirMessageView = holder.mainView.findViewById(R.id.textViewChatActivityTheirMessages);
        TextView myMessageView = holder.mainView.findViewById(R.id.textViewChatActivityMyMessages);
        Messaging m = dataSet.getMessagings().get(position);
        if(m.getFrom().equals(dataSet.getUsername())){
            theirMessageView.setText(m.getMessage());
            theirMessageView.setVisibility(View.VISIBLE);
            myMessageView.setVisibility(View.INVISIBLE);
        }else{
            myMessageView.setText(m.getMessage());
            theirMessageView.setVisibility(View.INVISIBLE);
            myMessageView.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public int getItemCount() {
        return dataSet.getMessagings().size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        // each data item is a MyHealthStats member
        private ConstraintLayout mainView;


        MyViewHolder(ConstraintLayout v) {
            super(v);
            mainView = v;
        }


    }

    public ChatRecyclerViewAdapter(MessageMetaData messageMetaData){
        this.dataSet = messageMetaData;
        notifyDataSetChanged();
    }

    public MessageMetaData getDataSet(){
        return dataSet;
    }

    public void addMessage(Messaging m){
        this.dataSet.getMessagings().add(m);
        notifyDataSetChanged();

    }

    public void setDataSet(MessageMetaData messageMetaData){
        dataSet = messageMetaData;
        notifyDataSetChanged();
    }


}
