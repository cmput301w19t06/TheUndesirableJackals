package com.cmput301.w19t06.theundesirablejackals.adapter;

import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.cmput301.w19t06.theundesirablejackals.activities.R;
import com.cmput301.w19t06.theundesirablejackals.user.UserInformation;

import java.util.ArrayList;

public class FriendsRecyclerViewAdapter extends RecyclerView.Adapter<FriendsRecyclerViewAdapter.MyViewHolder> {

    private ArrayList<UserInformation> dataSet;
    private RecyclerViewClickListener myListener;

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public LinearLayout mainTextView;
        private RecyclerViewClickListener myListener;


        MyViewHolder(LinearLayout v, RecyclerViewClickListener listener) {
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

    public FriendsRecyclerViewAdapter(){dataSet = new ArrayList<UserInformation>();}



    @Override
    public FriendsRecyclerViewAdapter.MyViewHolder onCreateViewHolder(final ViewGroup parent,
                                                                    int viewType) {
        // create a new view
        LinearLayout v = (LinearLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.my_books_item, parent, false);

        FriendsRecyclerViewAdapter.MyViewHolder vh = new FriendsRecyclerViewAdapter.MyViewHolder(v, myListener);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(FriendsRecyclerViewAdapter.MyViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return dataSet.size();
    }
}
