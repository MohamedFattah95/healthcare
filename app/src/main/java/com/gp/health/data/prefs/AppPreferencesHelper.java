
package com.gp.health.data.prefs;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.gp.health.BaseApp;
import com.gp.health.data.DataManager;
import com.gp.health.data.models.SettingsModel;
import com.gp.health.data.models.UserModel;
import com.gp.health.di.PreferenceInfo;
import com.gp.health.utils.AppConstants;

import javax.inject.Inject;

public class AppPreferencesHelper implements PreferencesHelper {

    private static final String PREF_KEY_ACCESS_TOKEN = "PREF_KEY_ACCESS_TOKEN";

    private static final String PREF_KEY_CURRENT_USER_EMAIL = "PREF_KEY_CURRENT_USER_EMAIL";

    private static final String PREF_KEY_CURRENT_USER_ID = "PREF_KEY_CURRENT_USER_ID";

    private static final String PREF_KEY_CURRENT_USER_NAME = "PREF_KEY_CURRENT_USER_NAME";

    private static final String PREF_KEY_CURRENT_USER_PROFILE_PIC_URL = "PREF_KEY_CURRENT_USER_PROFILE_PIC_URL";

    private static final String PREF_KEY_USER_LOGGED_IN_MODE = "PREF_KEY_USER_LOGGED_IN_MODE";
    private static final String USER_OBJECT = "PREF_USER_OBJECT";
    private static final String IS_FIRST_TIME_LAUNCH = "PREF_FIRST_TIME";
    private static final String SETTINGS_OBJECT = "SETTINGS_OBJECT";
    private static final String PREF_KEY_NOTIFICATIONS_SOUND = "PREF_KEY_NOTIFICATIONS_SOUND";

    private final SharedPreferences mPrefs;
    private static AppPreferencesHelper mSharedPrefs;


    public static AppPreferencesHelper getInstance() {
        if (mSharedPrefs == null) {
            mSharedPrefs = new AppPreferencesHelper(BaseApp.getContext(), AppConstants.PREF_NAME);
        }
        return mSharedPrefs;
    }

    @Inject
    public AppPreferencesHelper(Context context, @PreferenceInfo String prefFileName) {
        mPrefs = context.getSharedPreferences(prefFileName, Context.MODE_PRIVATE);
    }

    @Override
    public Boolean isUserLogged() {
        return mPrefs.getString(PREF_KEY_ACCESS_TOKEN, null) != null;
    }


    @Override
    public SettingsModel getSettingsObject() {
        String objStr = mPrefs.getString(SETTINGS_OBJECT, "");
        if (!objStr.matches("")) {
            return new Gson().fromJson(objStr, SettingsModel.class);
        } else return null;
    }

    @Override
    public void setSettingsObject(SettingsModel settingsModel) {
        mPrefs.edit().putString(SETTINGS_OBJECT, new Gson().toJson(settingsModel)).apply();
    }

    @Override
    public UserModel getUserObject() {
        String objStr = mPrefs.getString(USER_OBJECT, "");
        if (!objStr.matches("")) {
            return new Gson().fromJson(objStr, UserModel.class);
        } else return null;
    }

    @Override
    public void setUserObject(UserModel userModel) {
        mPrefs.edit().putString(USER_OBJECT, new Gson().toJson(userModel)).apply();
    }


    @Override
    public void setFirstTimeLaunch(boolean isFirstTime) {
        SharedPreferences.Editor editor = mPrefs.edit();
        editor.putBoolean(IS_FIRST_TIME_LAUNCH, isFirstTime);
        editor.apply();
    }

    @Override
    public boolean isFirstTimeLaunch() {
        return mPrefs.getBoolean(IS_FIRST_TIME_LAUNCH, true);
    }


    @Override
    public String getAccessToken() {
        return mPrefs.getString(PREF_KEY_ACCESS_TOKEN, null);
    }

    @Override
    public void setAccessToken(String accessToken) {
        mPrefs.edit().putString(PREF_KEY_ACCESS_TOKEN, accessToken).apply();
    }

    @Override
    public String getCurrentUserEmail() {
        return mPrefs.getString(PREF_KEY_CURRENT_USER_EMAIL, null);
    }

    @Override
    public void setCurrentUserEmail(String email) {
        mPrefs.edit().putString(PREF_KEY_CURRENT_USER_EMAIL, email).apply();
    }

    @Override
    public int getCurrentUserId() {
        return mPrefs.getInt(PREF_KEY_CURRENT_USER_ID, AppConstants.NULL_INDEX);
    }

    @Override
    public void setCurrentUserId(int userId) {
        int id = userId == -1 ? AppConstants.NULL_INDEX : userId;
        mPrefs.edit().putInt(PREF_KEY_CURRENT_USER_ID, id).apply();
    }

    @Override
    public int getNotificationsSound() {
        return mPrefs.getInt(PREF_KEY_NOTIFICATIONS_SOUND, 1);
    }

    @Override
    public void setNotificationsSound(int sound) {
        mPrefs.edit().putInt(PREF_KEY_NOTIFICATIONS_SOUND, sound).apply();
    }

    @Override
    public int getCurrentUserLoggedInMode() {
        return mPrefs.getInt(PREF_KEY_USER_LOGGED_IN_MODE,
                DataManager.LoggedInMode.LOGGED_IN_MODE_LOGGED_OUT.getType());
    }

    @Override
    public void setCurrentUserLoggedInMode(DataManager.LoggedInMode mode) {
        mPrefs.edit().putInt(PREF_KEY_USER_LOGGED_IN_MODE, mode.getType()).apply();
    }

    @Override
    public String getCurrentUserName() {
        return mPrefs.getString(PREF_KEY_CURRENT_USER_NAME, null);
    }

    @Override
    public void setCurrentUserName(String userName) {
        mPrefs.edit().putString(PREF_KEY_CURRENT_USER_NAME, userName).apply();
    }

    @Override
    public String getCurrentUserProfilePicUrl() {
        return mPrefs.getString(PREF_KEY_CURRENT_USER_PROFILE_PIC_URL, null);
    }

    @Override
    public void setCurrentUserProfilePicUrl(String profilePicUrl) {
        mPrefs.edit().putString(PREF_KEY_CURRENT_USER_PROFILE_PIC_URL, profilePicUrl).apply();
    }

}
