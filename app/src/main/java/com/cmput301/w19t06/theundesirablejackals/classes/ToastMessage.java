package com.cmput301.w19t06.theundesirablejackals.classes;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

/**
 * This is used to show messages to users. It was created to avoid typing the same commands
 * to class Toast when something needs to be said to the user.
 * @author Art Limbaga
 */
public class ToastMessage {

    public static void show(Context context, String message){
        Toast mToast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
        mToast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        mToast.show();
    }
}
