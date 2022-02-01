
package com.gp.health.data;

import com.gp.health.data.apis.ApiHelper;
import com.gp.health.data.models.UserModel;
import com.gp.health.data.prefs.PreferencesHelper;

public interface DataManager extends PreferencesHelper, ApiHelper {

    void setUserAsLoggedOut();

    void updateUserInfo(UserModel user, LoggedInMode loggedInMode);

    enum LoggedInMode {

        LOGGED_IN_MODE_LOGGED_OUT(0),
        LOGGED_IN_MODE_GOOGLE(1),
        LOGGED_IN_MODE_FB(2),
        LOGGED_IN_MODE_SERVER(3);

        private final int mType;

        LoggedInMode(int type) {
            mType = type;
        }

        public int getType() {
            return mType;
        }
    }
}
