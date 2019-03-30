package com.cmput301.w19t06.theundesirablejackals.classes;


import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

import com.cmput301.w19t06.theundesirablejackals.activities.BorrowedListActivity;
import com.cmput301.w19t06.theundesirablejackals.activities.ChatActivity;
import com.cmput301.w19t06.theundesirablejackals.activities.LentListActivity;
import com.cmput301.w19t06.theundesirablejackals.activities.MessagesActivity;


//https://stackoverflow.com/questions/43965306/how-to-find-out-the-current-top-activity-from-onmessagereceived-of-firebaseme
public class CurrentActivityReceiver extends BroadcastReceiver {
    private static final String TAG = CurrentActivityReceiver.class.getSimpleName();
    public static final String CURRENT_ACTIVITY_ACTION = "current.activity.action";
    public static final IntentFilter CURRENT_ACTIVITY_RECEIVER_FILTER = new IntentFilter(CURRENT_ACTIVITY_ACTION);

    private Activity receivingActivity;

    public CurrentActivityReceiver(Activity activity) {
        this.receivingActivity = activity;
    }

    @Override
    public void onReceive(Context sender, Intent intent) {
        Log.d(TAG, "onReceive: finishing:" + receivingActivity.getClass().getSimpleName());

        if (receivingActivity.getClass().equals(ChatActivity.class)) {
            ((ChatActivity)receivingActivity).updateMessages();
        }else if(receivingActivity.getClass().equals(MessagesActivity.class)){
            ((MessagesActivity)receivingActivity).update();
        }else if(receivingActivity.getClass().equals(BorrowedListActivity.class)){
            ((BorrowedListActivity)receivingActivity).onRefresh();
        }else if(receivingActivity.getClass().equals(LentListActivity.class)){
            ((LentListActivity)receivingActivity).onRefresh();
        }
    }
}
