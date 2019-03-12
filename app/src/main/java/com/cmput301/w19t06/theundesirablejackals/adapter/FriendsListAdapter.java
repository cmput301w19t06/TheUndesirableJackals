package com.cmput301.w19t06.theundesirablejackals.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.cmput301.w19t06.theundesirablejackals.activities.R;

import com.cmput301.w19t06.theundesirablejackals.R;
import com.cmput301.w19t06.theundesirablejackals.user.UserInformation;

import com.cmput301.w19t06.theundesirablejackals.user.UserInformation;

import java.util.List;

/**
 * Friends list recycler view adapter
 * Author: Kaya Thiessen
 */
public class FriendsListAdapter extends RecyclerView.Adapter<FriendsListAdapter.FriendsListHolder>{

    private Context context;
    private List<UserInformation> friendsList;

    /**
     *A dapter builder
     * @param mCtx
     * @param friendsList
     */
    public FriendsListAdapter(Context mCtx, List<UserInformation> friendsList) {
        this.context = mCtx;
        this.friendsList = friendsList;
    }

    /**
     * On create
     * @param parent
     * @param viewType
     * @return
     */
    @NonNull
    @Override
    public FriendsListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.friendslist_layout, null);
        FriendsListHolder holder = new FriendsListHolder(view);
        return holder;
    }

    /**
     * onBindViewHolder, sets values
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(@NonNull FriendsListHolder holder, int position) {
        UserInformation friend= friendsList.get(position);
        holder.friendUsername.setText(friend.getUserName());
    }

    /**
     * getItemCount, gets the item count...
     * @return
     */
    @Override
    public int getItemCount() {
        return friendsList.size();
    }

    /**
     * FriendsListHolder, gets info from layout
     * Author: Kaya Thiessen
     */
    class FriendsListHolder extends RecyclerView.ViewHolder{
        //ImageView friendIcon;
        TextView friendUsername/*, friendRating*/;

        /**
         * FriendsListHolder, gets the view ids
         * @param itemView
         */
        public FriendsListHolder(View itemView) {
            super(itemView);

            //friendIcon = itemView.findViewById(R.id.friendIcon_Id);
            friendUsername = itemView.findViewById(R.id.friendUsername_Id);
            //friendRating = itemView.findViewById(R.id.friendrating_Id);
        }
    }
}
