package com.cmput301.w19t06.theundesirablejackals.classes;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

public class ToastMessage {

    public static void show(Context context, String message){
        Toast mToast = Toast.makeText(context, message, Toast.LENGTH_LONG);
        mToast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        mToast.show();
    }
}
