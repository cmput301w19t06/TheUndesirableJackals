package com.cmput301.w19t06.theundesirablejackals.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cmput301.w19t06.theundesirablejackals.R;
import com.cmput301.w19t06.theundesirablejackals.User.UserInformation;

import java.util.List;

public class FriendsListAdapter extends RecyclerView.Adapter<FriendsListAdapter.FriendsListHolder>{

    private Context mCtx;
    private List<UserInformation> friendsList;

    public FriendsListAdapter(Context mCtx, List<UserInformation> friendsList) {
        this.mCtx = mCtx;
        this.friendsList = friendsList;
    }

    @NonNull
    @Override
    public FriendsListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.friendslist_layout, null);
        FriendsListHolder holder = new FriendsListHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull FriendsListHolder holder, int position) {
        UserInformation friend= friendsList.get(position);
        holder.friendUsername.setText(friend.getUserName());
    }

    @Override
    public int getItemCount() {
        return friendsList.size();
    }

    class FriendsListHolder extends RecyclerView.ViewHolder{
        //ImageView friendIcon;
        TextView friendUsername/*, friendRating*/;

        public FriendsListHolder(View itemView) {
            super(itemView);

            //friendIcon = itemView.findViewById(R.id.friendIcon_Id);
            friendUsername = itemView.findViewById(R.id.friendUsername_Id);
            //friendRating = itemView.findViewById(R.id.friendrating_Id);
        }
    }
}
