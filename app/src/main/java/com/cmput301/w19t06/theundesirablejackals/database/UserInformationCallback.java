package com.cmput301.w19t06.theundesirablejackals.database;

import com.cmput301.w19t06.theundesirablejackals.user.UserInformation;

public interface UserInformationCallback {
    void onCallback(UserInformation userInformation);
}
