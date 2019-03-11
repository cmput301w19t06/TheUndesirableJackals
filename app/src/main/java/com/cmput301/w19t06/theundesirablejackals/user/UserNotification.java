package com.cmput301.w19t06.theundesirablejackals.user;

import java.util.Date;

/**
 * This class handles user notifications of different types.
 * @author Art Limbaga
 * @see UserNotificationType
 * @see UserNotificationList
 */
public class UserNotification {
    private String notificationMessage;
    private User reciever;
    private UserNotificationType type;
    private Boolean seen;
    private Date date;

    public UserNotification() {

    }


    public UserNotification(UserNotificationType type, User reciever) {
        switch (type) {
            case FRIEND_REQUEST:
                notificationMessage = "New Friend Request";
                break;

            case NEW_MESSAGE:
                notificationMessage =  "New message";
                break;

            case NEW_BOOK_REQUEST:
                notificationMessage = "New Book Request";
                break;

            case BOOK_REQUEST_UPDATE:
                notificationMessage = "A book request has been updated";
                break;
        }
        this.type = type;
        this.reciever = reciever;
    }

    /**
     * Send the notification to the receiver
     */
    public void doNotify() {

    }

    /**
     *
     * @return type of this UserNotification object
     */
    public UserNotificationType getType() {return type;}


}
