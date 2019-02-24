package com.cmput301.w19t06.theundesirablejackals;

import java.util.ArrayList;

public class UserNotificationList {
    private ArrayList<UserNotification> notifications;

    public  UserNotificationList() {
        notifications = new ArrayList<UserNotification>();
    }

    public void addNotification(UserNotification notification) {
        notifications.add(notification);
    }

    public void deleteNotification(UserNotification notification) {
        notifications.remove(notification);
    }

}
