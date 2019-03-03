package com.cmput301.w19t06.theundesirablejackals;

import com.cmput301.w19t06.theundesirablejackals.User.User;
import com.cmput301.w19t06.theundesirablejackals.User.UserNotification;
import com.cmput301.w19t06.theundesirablejackals.User.UserNotificationType;

import org.junit.Test;

import static org.junit.Assert.*;

public class UserNotificationTest {
    private User user;
    private UserNotification notification;

    @Test
    public void doNotify_isCorrect() {
        user = new User("felipe", "pass",
                "email@hotmail.com", "333-333-3333");
        notification = new UserNotification(UserNotificationType.BOOK_REQUEST_UPDATE, user);
        notification.doNotify();

        assertTrue(user.getNotifications().contains(notification));
    }
}