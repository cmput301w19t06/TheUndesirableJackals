package com.cmput301.w19t06.theundesirablejackals.user;


import java.util.ArrayList;

/**
 * This class will handle lists of of notifications base on UserNotificationType. A feature of this
 * class is that it enables for sorting notifications by type and also delete the read notifications
 * @author Art Limbaga
 * @see UserNotification
 * @see UserNotificationType
 */
public class UserNotificationList {
    private ArrayList<UserNotification> notifications;

    /**
     * Instantiates an empty notificaition list
     */
    public  UserNotificationList() {
        notifications = new ArrayList<UserNotification>();
    }


    /**
     * Required for Firebase
     * @return
     */
    public ArrayList<UserNotification> getNotifications() {
        return notifications;
    }

    /**
     * Required for Firebase
     * @param notifications
     */
    public void setNotifications(ArrayList<UserNotification> notifications) {
        this.notifications = notifications;
    }

    /**
     *
     * @param notification to be added to the list
     */
    public void addNotification(UserNotification notification) {
        notifications.add(notification);
    }

    /**
     *
     * @param notification to be deleted from the list
     */
    public void deleteNotification(UserNotification notification) {
        notifications.remove(notification);
    }

    /**
     *
     * @param notification
     * @return false if the notification does not exist in the list, true otherwise
     */
    public boolean contains(UserNotification notification) {return notifications.contains(notification);}

    /**
     *
     * @return total number of notifications
     */
    public int count() {
        return notifications.size();
    }

    /**
     * @param index
     * @return notification and index
     */
    public UserNotification get(int index) {
        return notifications.get(index);
    }

}
