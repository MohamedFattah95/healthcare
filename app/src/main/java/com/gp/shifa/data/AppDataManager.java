package com.gp.shifa.data;

import android.content.Context;

import com.google.gson.Gson;
import com.gp.shifa.data.apis.ApiHelper;
import com.gp.shifa.data.models.CategoriesModel;
import com.gp.shifa.data.models.CategoryDoctorsModel;
import com.gp.shifa.data.models.ChatsModel;
import com.gp.shifa.data.models.CountriesAndAreasModel;
import com.gp.shifa.data.models.DataWrapperModel;
import com.gp.shifa.data.models.DoctorDetailsModel;
import com.gp.shifa.data.models.DoctorModel;
import com.gp.shifa.data.models.FAQsModel;
import com.gp.shifa.data.models.IntroModel;
import com.gp.shifa.data.models.SliderModel;
import com.gp.shifa.data.models.UserModel;
import com.gp.shifa.data.prefs.PreferencesHelper;
import com.gp.shifa.utils.AppConstants;

import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Single;
import okhttp3.MultipartBody;

@Singleton
public class AppDataManager implements DataManager {

    private static final String TAG = "AppDataManager";

    private final ApiHelper mApiHelper;

    private final Context mContext;

    private final Gson mGson;

    private final PreferencesHelper mPreferencesHelper;

    @Inject
    public AppDataManager(Context context, PreferencesHelper preferencesHelper, ApiHelper apiHelper, Gson gson) {
        mContext = context;
        mPreferencesHelper = preferencesHelper;
        mApiHelper = apiHelper;
        mGson = gson;
    }

    @Override
    public void setUserObject(UserModel userModel) {
        mPreferencesHelper.setUserObject(userModel);
    }

    @Override
    public Boolean isUserLogged() {
        return mPreferencesHelper.isUserLogged();
    }

    @Override
    public UserModel getUserObject() {
        return mPreferencesHelper.getUserObject();
    }

    @Override
    public void setFirstTimeLaunch(boolean isFirstTime) {
        mPreferencesHelper.setFirstTimeLaunch(isFirstTime);
    }

    @Override
    public boolean isFirstTimeLaunch() {
        return mPreferencesHelper.isFirstTimeLaunch();
    }

    @Override
    public String getAccessToken() {
        return mPreferencesHelper.getAccessToken();
    }

    @Override
    public void setAccessToken(String accessToken) {
        mPreferencesHelper.setAccessToken(accessToken);
    }

    @Override
    public String getCurrentUserEmail() {
        return mPreferencesHelper.getCurrentUserEmail();
    }

    @Override
    public void setCurrentUserEmail(String email) {
        mPreferencesHelper.setCurrentUserEmail(email);
    }

    @Override
    public int getCurrentUserId() {
        return mPreferencesHelper.getCurrentUserId();
    }

    @Override
    public void setCurrentUserId(int userId) {
        mPreferencesHelper.setCurrentUserId(userId);
    }

    @Override
    public int getNotificationsSound() {
        return mPreferencesHelper.getNotificationsSound();
    }

    @Override
    public void setNotificationsSound(int sound) {
        mPreferencesHelper.setNotificationsSound(sound);
    }

    @Override
    public int getCurrentUserLoggedInMode() {
        return mPreferencesHelper.getCurrentUserLoggedInMode();
    }

    @Override
    public void setCurrentUserLoggedInMode(LoggedInMode mode) {
        mPreferencesHelper.setCurrentUserLoggedInMode(mode);
    }

    @Override
    public String getCurrentUserName() {
        return mPreferencesHelper.getCurrentUserName();
    }

    @Override
    public void setCurrentUserName(String userName) {
        mPreferencesHelper.setCurrentUserName(userName);
    }

    @Override
    public String getCurrentUserProfilePicUrl() {
        return mPreferencesHelper.getCurrentUserProfilePicUrl();
    }

    @Override
    public void setCurrentUserProfilePicUrl(String profilePicUrl) {
        mPreferencesHelper.setCurrentUserProfilePicUrl(profilePicUrl);
    }

    @Override
    public void setUserAsLoggedOut() {
        updateUserInfo(null, DataManager.LoggedInMode.LOGGED_IN_MODE_LOGGED_OUT);
    }


    @Override
    public void updateUserInfo(UserModel user, LoggedInMode loggedInMode) {
        setCurrentUserLoggedInMode(loggedInMode);

        if (user != null) {
            setAccessToken(user.getAccessToken());
            setCurrentUserId(user.getUser().getId());
            setCurrentUserName(user.getUser().getName());
            setCurrentUserEmail(user.getUser().getEmail());
            setCurrentUserProfilePicUrl(user.getUser().getImg());
            setUserObject(user);
        } else {
            setAccessToken(null);
            setCurrentUserId(AppConstants.NULL_INDEX);
            setCurrentUserName(null);
            setCurrentUserEmail(null);
            setCurrentUserProfilePicUrl(null);
            setUserObject(null);
        }
    }

    @Override
    public Single<DataWrapperModel<UserModel>> doLoginApiCall(String email, String password) {
        return mApiHelper.doLoginApiCall(email, password);
    }

    @Override
    public Single<DataWrapperModel<UserModel>> doRegistrationApiCall(MultipartBody body) {
        return mApiHelper.doRegistrationApiCall(body);
    }

    @Override
    public Single<DataWrapperModel<UserModel>> getProfileApiCall(int userId) {
        return mApiHelper.getProfileApiCall(userId);
    }

    @Override
    public Single<DataWrapperModel<UserModel.UserBean>> updateProfileApiCall(MultipartBody body) {
        return mApiHelper.updateProfileApiCall(body);
    }

    @Override
    public Single<DataWrapperModel<Void>> updateFCMTokenApiCall(int userId, HashMap<String, String> map) {
        return mApiHelper.updateFCMTokenApiCall(userId, map);
    }

    @Override
    public Single<DataWrapperModel<UserModel>> sendForgotPasswordCodeApiCall(String mobileNumber) {
        return mApiHelper.sendForgotPasswordCodeApiCall(mobileNumber);
    }

    @Override
    public Single<DataWrapperModel<UserModel>> resetPasswordApiCall(String email, String password) {
        return mApiHelper.resetPasswordApiCall(email, password);
    }

    @Override
    public Single<DataWrapperModel<CategoryDoctorsModel>> getCategoryDoctorsApiCall(int categoryId) {
        return mApiHelper.getCategoryDoctorsApiCall(categoryId);
    }

    @Override
    public Single<DataWrapperModel<List<IntroModel>>> getHowToUseApiCall() {
        return mApiHelper.getHowToUseApiCall();
    }

    @Override
    public Single<DataWrapperModel<SliderModel>> getSlider(int id) {
        return mApiHelper.getSlider(id);
    }

    @Override
    public Single<DataWrapperModel<Void>> doLogout() {
        return mApiHelper.doLogout();
    }

    @Override
    public Single<DataWrapperModel<List<DoctorModel>>> getFavoritesApiCall() {
        return mApiHelper.getFavoritesApiCall();
    }

    @Override
    public Single<DataWrapperModel<List<CountriesAndAreasModel>>> getCountriesAndAreasApiCall() {
        return mApiHelper.getCountriesAndAreasApiCall();
    }

    @Override
    public Single<DataWrapperModel<Void>> favoriteOrUnFavoriteApiCall(int itemId) {
        return mApiHelper.favoriteOrUnFavoriteApiCall(itemId);
    }

    @Override
    public Single<DataWrapperModel<List<FAQsModel>>> getFAQsApiCall() {
        return mApiHelper.getFAQsApiCall();
    }

    @Override
    public Single<DataWrapperModel<List<ChatsModel>>> getUserChats() {
        return mApiHelper.getUserChats();
    }

    @Override
    public Single<DataWrapperModel<List<CategoriesModel>>> getCategoriesApiCall() {
        return mApiHelper.getCategoriesApiCall();
    }

    @Override
    public Single<DataWrapperModel<List<DoctorModel>>> getDoctorsApiCall(int page) {
        return mApiHelper.getDoctorsApiCall(page);
    }

    @Override
    public Single<DataWrapperModel<DoctorDetailsModel>> getDoctorDetailsApiCall(int doctorId) {
        return mApiHelper.getDoctorDetailsApiCall(doctorId);
    }
}
