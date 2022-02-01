package com.gp.health.data;

import android.content.Context;

import com.google.gson.Gson;
import com.gp.health.data.apis.ApiHelper;
import com.gp.health.data.models.AdAndOrderModel;
import com.gp.health.data.models.BanksModel;
import com.gp.health.data.models.BlockedModel;
import com.gp.health.data.models.CategoriesModel;
import com.gp.health.data.models.ChatsModel;
import com.gp.health.data.models.CityModel;
import com.gp.health.data.models.CityServiceModel;
import com.gp.health.data.models.CommentModel;
import com.gp.health.data.models.CommercialsModel;
import com.gp.health.data.models.CommissionCalculatorModel;
import com.gp.health.data.models.ContactUsMessageTypesModel;
import com.gp.health.data.models.DataWrapperModel;
import com.gp.health.data.models.FAQsModel;
import com.gp.health.data.models.FollowerModel;
import com.gp.health.data.models.GoogleCategoriesModel;
import com.gp.health.data.models.GoogleShopWrapperModel;
import com.gp.health.data.models.IntroModel;
import com.gp.health.data.models.LikeModel;
import com.gp.health.data.models.MemberTypeModel;
import com.gp.health.data.models.NotificationsModel;
import com.gp.health.data.models.PagDataWrapperModel;
import com.gp.health.data.models.SettingsModel;
import com.gp.health.data.models.SliderModel;
import com.gp.health.data.models.SpecOptionModel;
import com.gp.health.data.models.SubscriptionPackagesModel;
import com.gp.health.data.models.UserModel;
import com.gp.health.data.prefs.PreferencesHelper;
import com.gp.health.utils.AppConstants;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public SettingsModel getSettingsObject() {
        return mPreferencesHelper.getSettingsObject();
    }

    @Override
    public void setSettingsObject(SettingsModel settingsModel) {
        mPreferencesHelper.setSettingsObject(settingsModel);
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
            setCurrentUserId(user.getId());
            setCurrentUserName(user.getName());
            setCurrentUserEmail(user.getEmail());
            setCurrentUserProfilePicUrl(user.getImage());
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
    public Single<DataWrapperModel<SettingsModel>> getSettings() {
        return mApiHelper.getSettings();
    }

    @Override
    public Single<DataWrapperModel<UserModel>> doLoginApiCall(String mobile, String password) {
        return mApiHelper.doLoginApiCall(mobile, password);
    }

    @Override
    public Single<DataWrapperModel<UserModel>> doRegistrationApiCall(String name, String mobile, String password, int type) {
        return mApiHelper.doRegistrationApiCall(name, mobile, password, type);
    }

    @Override
    public Single<DataWrapperModel<Void>> verifyCodeApiCall(String userId, String code) {
        return mApiHelper.verifyCodeApiCall(userId, code);
    }

    @Override
    public Single<DataWrapperModel<Void>> verifyCodePasswordApiCall(String mobile, String code) {
        return mApiHelper.verifyCodePasswordApiCall(mobile, code);
    }

    @Override
    public Single<DataWrapperModel<Integer>> resendCodeApiCall(int userId) {
        return mApiHelper.resendCodeApiCall(userId);
    }

    @Override
    public Single<DataWrapperModel<UserModel>> getProfileApiCall(int userId) {
        return mApiHelper.getProfileApiCall(userId);
    }

    @Override
    public Single<DataWrapperModel<UserModel>> updateProfileApiCall(MultipartBody body, int userId) {
        return mApiHelper.updateProfileApiCall(body, userId);
    }

    @Override
    public Single<DataWrapperModel<Void>> updateFCMTokenApiCall(int userId, HashMap<String, String> map) {
        return mApiHelper.updateFCMTokenApiCall(userId, map);
    }

    @Override
    public Single<DataWrapperModel<List<ContactUsMessageTypesModel>>> getContactUsMessageTypesApiCall() {
        return mApiHelper.getContactUsMessageTypesApiCall();
    }

    @Override
    public Single<DataWrapperModel<Void>> sendContactUsMessageApiCall(HashMap<String, String> map) {
        return mApiHelper.sendContactUsMessageApiCall(map);
    }

    @Override
    public Single<DataWrapperModel<UserModel>> sendForgotPasswordCodeApiCall(String mobileNumber) {
        return mApiHelper.sendForgotPasswordCodeApiCall(mobileNumber);
    }

    @Override
    public Single<DataWrapperModel<Void>> resetPasswordApiCall(String code, String phone, String password) {
        return mApiHelper.resetPasswordApiCall(code, phone, password);
    }

    @Override
    public Single<PagDataWrapperModel<List<NotificationsModel>>> getNotificationsApiCall(int userId, int page) {
        return mApiHelper.getNotificationsApiCall(userId, page);
    }

    @Override
    public Single<DataWrapperModel<List<CategoriesModel>>> getCategoriesApiCall() {
        return mApiHelper.getCategoriesApiCall();
    }

    @Override
    public Single<GoogleShopWrapperModel> getSearchPlaces(Map<String, String> paramsMap) {
        return mApiHelper.getSearchPlaces(paramsMap);
    }

    @Override
    public Single<DataWrapperModel<Void>> rateUserApiCall(int id, String msg, int rating) {
        return mApiHelper.rateUserApiCall(id, msg, rating);
    }

    @Override
    public Single<DataWrapperModel<Void>> markNotificationAsSeenApiCall(int id, String method) {
        return mApiHelper.markNotificationAsSeenApiCall(id, method);
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
    public Single<DataWrapperModel<Void>> doLogout(int userId) {
        return mApiHelper.doLogout(userId);
    }

    @Override
    public Single<DataWrapperModel<Void>> markAllAsReadApiCall(int userId, String method) {
        return mApiHelper.markAllAsReadApiCall(userId, method);
    }

    @Override
    public Single<DataWrapperModel<Void>> checkUserApiCall(int currentUserId) {
        return mApiHelper.checkUserApiCall(currentUserId);
    }

    @Override
    public Single<DataWrapperModel<List<CityModel>>> getAreasAndCitiesApiCall(int id) {
        return mApiHelper.getAreasAndCitiesApiCall(id);
    }

    @Override
    public Single<DataWrapperModel<Void>> checkOldPasswordApiCall(int userId, String oldPassword) {
        return mApiHelper.checkOldPasswordApiCall(userId, oldPassword);
    }


    @Override
    public Single<PagDataWrapperModel<List<AdAndOrderModel>>> getFavoritesApiCall(int userId, int page) {
        return mApiHelper.getFavoritesApiCall(userId, page);
    }


    @Override
    public Single<DataWrapperModel<AdAndOrderModel>> submitAdOrOrderApiCall(MultipartBody builder) {
        return mApiHelper.submitAdOrOrderApiCall(builder);
    }

    @Override
    public Single<DataWrapperModel<List<BanksModel>>> getBanksApiCall() {
        return mApiHelper.getBanksApiCall();
    }

    @Override
    public Single<DataWrapperModel<Void>> submitCommissionApiCall(MultipartBody build) {
        return mApiHelper.submitCommissionApiCall(build);
    }

    @Override
    public Single<DataWrapperModel<Void>> sendChatNotif(int senderId, int receiverId) {
        return mApiHelper.sendChatNotif(senderId, receiverId);
    }

    @Override
    public Single<DataWrapperModel<List<MemberTypeModel>>> getMemberTypesApiCall() {
        return mApiHelper.getMemberTypesApiCall();
    }

    @Override
    public Single<PagDataWrapperModel<List<AdAndOrderModel>>> getUserAdsOrOrdersApiCall(int userId, int type, int page) {
        return mApiHelper.getUserAdsOrOrdersApiCall(userId, type, page);
    }

    @Override
    public Single<PagDataWrapperModel<List<CommentModel>>> getUserRatingsApiCall(int userId, int page) {
        return mApiHelper.getUserRatingsApiCall(userId, page);
    }

    @Override
    public Single<PagDataWrapperModel<List<FollowerModel>>> getUserFollowingsApiCall(int userId, int page) {
        return mApiHelper.getUserFollowingsApiCall(userId, page);
    }

    @Override
    public Single<PagDataWrapperModel<List<BlockedModel>>> getUserBlockedApiCall(int userId, int page) {
        return mApiHelper.getUserBlockedApiCall(userId, page);
    }

    @Override
    public Single<DataWrapperModel<Integer>> submitCommercialActivityApiCall(MultipartBody build) {
        return mApiHelper.submitCommercialActivityApiCall(build);
    }

    @Override
    public Single<DataWrapperModel<CommissionCalculatorModel>> calculateCommissionApiCall(String amount) {
        return mApiHelper.calculateCommissionApiCall(amount);
    }

    @Override
    public Single<DataWrapperModel<Void>> followOrUnFollowUserApiCall(int followingId) {
        return mApiHelper.followOrUnFollowUserApiCall(followingId);
    }

    @Override
    public Single<DataWrapperModel<List<SpecOptionModel>>> getSpecOptionsApiCall(String category) {
        return mApiHelper.getSpecOptionsApiCall(category);
    }

    @Override
    public Single<DataWrapperModel<List<CityModel>>> getRootCitiesApiCall() {
        return mApiHelper.getRootCitiesApiCall();
    }

    @Override
    public Single<DataWrapperModel<Void>> deleteAdOrOrderApiCall(HashMap<String, String> map) {
        return mApiHelper.deleteAdOrOrderApiCall(map);
    }

    @Override
    public Single<DataWrapperModel<AdAndOrderModel>> getAdOrOrderDetailsApiCall(int itemId) {
        return mApiHelper.getAdOrOrderDetailsApiCall(itemId);
    }

    @Override
    public Single<DataWrapperModel<Void>> deleteMediaApiCall(int mediaId) {
        return mApiHelper.deleteMediaApiCall(mediaId);
    }

    @Override
    public Single<DataWrapperModel<Void>> updateAdOrOrderApiCall(int id, MultipartBody build) {
        return mApiHelper.updateAdOrOrderApiCall(id, build);
    }

    @Override
    public Single<DataWrapperModel<List<SubscriptionPackagesModel>>> getSubscriptionPackagesApiCall() {
        return mApiHelper.getSubscriptionPackagesApiCall();
    }

    @Override
    public Single<PagDataWrapperModel<List<AdAndOrderModel>>> searchItemsApiCall(HashMap<String, String> filterMap, int page) {
        return mApiHelper.searchItemsApiCall(filterMap, page);
    }

    @Override
    public Single<DataWrapperModel<List<CommercialsModel>>> searchCommercialsApiCall(String ids) {
        return mApiHelper.searchCommercialsApiCall(ids);
    }

    @Override
    public Single<DataWrapperModel<Void>> blockOrUnblockUserApiCall(int userId) {
        return mApiHelper.blockOrUnblockUserApiCall(userId);
    }

    @Override
    public Single<PagDataWrapperModel<List<UserModel>>> searchMembersByMobileApiCall(String mobile) {
        return mApiHelper.searchMembersByMobileApiCall(mobile);
    }

    @Override
    public Single<DataWrapperModel<LikeModel>> favoriteOrUnFavoriteApiCall(int itemId) {
        return mApiHelper.favoriteOrUnFavoriteApiCall(itemId);
    }

    @Override
    public Single<PagDataWrapperModel<List<AdAndOrderModel>>> getSimilarItemsApiCall(int categoryId, int itemId) {
        return mApiHelper.getSimilarItemsApiCall(categoryId, itemId);
    }

    @Override
    public Single<DataWrapperModel<Void>> reportItemApiCall(int itemId, String commentText) {
        return mApiHelper.reportItemApiCall(itemId, commentText);
    }

    @Override
    public Single<DataWrapperModel<List<FAQsModel>>> getFAQsApiCall() {
        return mApiHelper.getFAQsApiCall();
    }

    @Override
    public Single<DataWrapperModel<List<ChatsModel>>> getUserChats(int userId) {
        return mApiHelper.getUserChats(userId);
    }

    @Override
    public Single<DataWrapperModel<CityModel>> getCityServicesReviewsApiCall(String arCurrentAreaAsId) {
        return mApiHelper.getCityServicesReviewsApiCall(arCurrentAreaAsId);
    }

    @Override
    public Single<DataWrapperModel<List<CityServiceModel>>> getCityServicesApiCall() {
        return mApiHelper.getCityServicesApiCall();
    }

    @Override
    public Single<DataWrapperModel<Void>> addCommentToCityApiCall(String city, String comment, int rating) {
        return mApiHelper.addCommentToCityApiCall(city, comment, rating);
    }

    @Override
    public Single<DataWrapperModel<Void>> addCityServiceRateApiCall(String city, int cityServiceId, int rate) {
        return mApiHelper.addCityServiceRateApiCall(city, cityServiceId, rate);
    }

    @Override
    public Single<DataWrapperModel<List<GoogleCategoriesModel>>> getGoogleServicesTypesApiCall() {
        return mApiHelper.getGoogleServicesTypesApiCall();
    }

    @Override
    public Single<GoogleShopWrapperModel> getGoogleShops(Map<String, String> paramsMap) {
        return mApiHelper.getGoogleShops(paramsMap);
    }

    @Override
    public Single<DataWrapperModel<CityModel>> isCityActiveApiCall(String arCurrentAreaAsId) {
        return mApiHelper.isCityActiveApiCall(arCurrentAreaAsId);
    }

    @Override
    public Single<DataWrapperModel<String>> getBadWordsApiCall() {
        return mApiHelper.getBadWordsApiCall();
    }
}
