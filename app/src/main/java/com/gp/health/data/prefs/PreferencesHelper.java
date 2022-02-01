
package com.gp.health.data.prefs;

import com.gp.health.data.DataManager;
import com.gp.health.data.models.SettingsModel;
import com.gp.health.data.models.UserModel;

public interface PreferencesHelper {

    void setUserObject(UserModel userModel);

    Boolean isUserLogged();

    SettingsModel getSettingsObject();

    void setSettingsObject(SettingsModel settingsModel);

    UserModel getUserObject();

    void setFirstTimeLaunch(boolean isFirstTime);

    boolean isFirstTimeLaunch();

    String getAccessToken();

    void setAccessToken(String accessToken);

    String getCurrentUserEmail();

    void setCurrentUserEmail(String email);

    int getCurrentUserId();

    void setCurrentUserId(int userId);

    int getNotificationsSound();

    void setNotificationsSound(int sound);

    int getCurrentUserLoggedInMode();

    void setCurrentUserLoggedInMode(DataManager.LoggedInMode mode);

    String getCurrentUserName();

    void setCurrentUserName(String userName);

    String getCurrentUserProfilePicUrl();

    void setCurrentUserProfilePicUrl(String profilePicUrl);
}
