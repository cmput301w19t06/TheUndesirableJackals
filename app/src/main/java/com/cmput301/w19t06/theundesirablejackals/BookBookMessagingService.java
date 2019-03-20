package com.cmput301.w19t06.theundesirablejackals;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class BookBookMessagingService extends Service {
    public BookBookMessagingService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
