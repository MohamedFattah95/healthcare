
package com.gp.shifa.data.apis;

import com.gp.shifa.data.models.AdAndOrderModel;
import com.gp.shifa.data.models.BanksModel;
import com.gp.shifa.data.models.BlockedModel;
import com.gp.shifa.data.models.CategoriesModel;
import com.gp.shifa.data.models.ChatsModel;
import com.gp.shifa.data.models.CityModel;
import com.gp.shifa.data.models.CityServiceModel;
import com.gp.shifa.data.models.CommentModel;
import com.gp.shifa.data.models.CommercialsModel;
import com.gp.shifa.data.models.CommissionCalculatorModel;
import com.gp.shifa.data.models.ContactUsMessageTypesModel;
import com.gp.shifa.data.models.CountriesAndAreasModel;
import com.gp.shifa.data.models.DataWrapperModel;
import com.gp.shifa.data.models.FAQsModel;
import com.gp.shifa.data.models.FollowerModel;
import com.gp.shifa.data.models.GoogleCategoriesModel;
import com.gp.shifa.data.models.GoogleShopWrapperModel;
import com.gp.shifa.data.models.IntroModel;
import com.gp.shifa.data.models.LikeModel;
import com.gp.shifa.data.models.NotificationsModel;
import com.gp.shifa.data.models.PagDataWrapperModel;
import com.gp.shifa.data.models.SettingsModel;
import com.gp.shifa.data.models.SliderModel;
import com.gp.shifa.data.models.SpecOptionModel;
import com.gp.shifa.data.models.SubscriptionPackagesModel;
import com.gp.shifa.data.models.UserModel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Single;
import okhttp3.MultipartBody;

@Singleton
public class AppApiHelper implements ApiHelper {

    private final NetworkService networkService;

    @Inject
    public AppApiHelper(NetworkService networkService) {
        this.networkService = networkService;
    }


    @Override
    public Single<DataWrapperModel<SettingsModel>> getSettings() {
        return networkService.getSettings();
    }

    @Override
    public Single<DataWrapperModel<UserModel>> doLoginApiCall(String email, String password) {
        return networkService.doLoginApiCall(email, password);
    }

    @Override
    public Single<DataWrapperModel<UserModel>> doRegistrationApiCall(MultipartBody body) {
        return networkService.doRegistrationApiCall(body);
    }

    @Override
    public Single<DataWrapperModel<Void>> verifyCodeApiCall(String userId, String code) {
        return networkService.verifyCodeApiCall(userId, code);
    }

    @Override
    public Single<DataWrapperModel<Void>> verifyCodePasswordApiCall(String mobile, String code) {
        return networkService.verifyCodePasswordApiCall(mobile, code);
    }

    @Override
    public Single<DataWrapperModel<Integer>> resendCodeApiCall(int userId) {
        return networkService.resendCodeApiCall(userId);
    }

    @Override
    public Single<DataWrapperModel<UserModel>> getProfileApiCall(int userId) {
        return networkService.getProfileApiCall(userId);
    }

    @Override
    public Single<DataWrapperModel<UserModel>> updateProfileApiCall(MultipartBody map, int userId) {
        return networkService.updateProfileApiCall(map, userId);
    }

    @Override
    public Single<DataWrapperModel<Void>> updateFCMTokenApiCall(int userId, HashMap<String, String> map) {
        return networkService.updateFCMTokenApiCall(userId, map);
    }

    @Override
    public Single<DataWrapperModel<List<ContactUsMessageTypesModel>>> getContactUsMessageTypesApiCall() {
        return networkService.getContactUsMessageTypesApiCall();
    }

    @Override
    public Single<DataWrapperModel<Void>> sendContactUsMessageApiCall(HashMap<String, String> map) {
        return networkService.sendContactUsMessageApiCall(map);
    }

    @Override
    public Single<DataWrapperModel<UserModel>> sendForgotPasswordCodeApiCall(String mobileNumber) {
        return networkService.sendForgotPasswordCodeApiCall(mobileNumber);
    }

    @Override
    public Single<DataWrapperModel<Void>> resetPasswordApiCall(String code, String phone, String password) {
        return networkService.resetPasswordApiCall(code, phone, password);
    }

    @Override
    public Single<PagDataWrapperModel<List<NotificationsModel>>> getNotificationsApiCall(int userId, int page) {
        return networkService.getNotificationsApiCall(userId, page);
    }

    @Override
    public Single<DataWrapperModel<List<CategoriesModel>>> getCategoriesApiCall() {
        return networkService.getCategoriesApiCall();
    }

    @Override
    public Single<GoogleShopWrapperModel> getSearchPlaces(Map<String, String> paramsMap) {
        return networkService.getSearchPlaces(paramsMap);
    }

    @Override
    public Single<DataWrapperModel<Void>> rateUserApiCall(int id, String msg, int rating) {
        return networkService.rateUserApiCall(id, msg, rating);
    }

    @Override
    public Single<DataWrapperModel<Void>> markNotificationAsSeenApiCall(int id, String method) {
        return networkService.markNotificationAsSeenApiCall(id, method);
    }

    @Override
    public Single<DataWrapperModel<List<IntroModel>>> getHowToUseApiCall() {
        return networkService.getHowToUseApiCall();
    }

    @Override
    public Single<DataWrapperModel<SliderModel>> getSlider(int id) {
        return networkService.getSlider(id);
    }

    @Override
    public Single<DataWrapperModel<Void>> doLogout(int userId) {
        return networkService.doLogout(userId);
    }

    @Override
    public Single<DataWrapperModel<Void>> markAllAsReadApiCall(int userId, String method) {
        return networkService.markAllAsReadApiCall(userId, method);
    }

    @Override
    public Single<DataWrapperModel<Void>> checkUserApiCall(int currentUserId) {
        return networkService.checkUserApiCall(currentUserId);
    }

    @Override
    public Single<DataWrapperModel<List<CityModel>>> getAreasAndCitiesApiCall(int id) {
        return networkService.getAreasAndCitiesApiCall(id);
    }

    @Override
    public Single<DataWrapperModel<Void>> checkOldPasswordApiCall(int userId, String oldPassword) {
        return networkService.checkOldPasswordApiCall(userId, oldPassword);
    }

    @Override
    public Single<PagDataWrapperModel<List<AdAndOrderModel>>> getFavoritesApiCall(int userId, int page) {
        return networkService.getFavoritesApiCall(userId, page);
    }

    @Override
    public Single<DataWrapperModel<AdAndOrderModel>> submitAdOrOrderApiCall(MultipartBody builder) {
        return networkService.submitAdOrOrderApiCall(builder);
    }

    @Override
    public Single<DataWrapperModel<List<BanksModel>>> getBanksApiCall() {
        return networkService.getBanksApiCall();
    }

    @Override
    public Single<DataWrapperModel<Void>> submitCommissionApiCall(MultipartBody build) {
        return networkService.submitCommissionApiCall(build);
    }

    @Override
    public Single<DataWrapperModel<Void>> sendChatNotif(int senderId, int receiverId) {
        return networkService.sendChatNotif(senderId, receiverId);
    }

    @Override
    public Single<DataWrapperModel<List<CountriesAndAreasModel>>> getCountriesAndAreasApiCall() {
        return networkService.getCountriesAndAreasApiCall();
    }

    @Override
    public Single<PagDataWrapperModel<List<AdAndOrderModel>>> getUserAdsOrOrdersApiCall(int userId, int type, int page) {
        return networkService.getUserAdsOrOrdersApiCall(userId, type, page);
    }

    @Override
    public Single<PagDataWrapperModel<List<CommentModel>>> getUserRatingsApiCall(int userId, int page) {
        return networkService.getUserRatingsApiCall(userId, page);
    }

    @Override
    public Single<PagDataWrapperModel<List<FollowerModel>>> getUserFollowingsApiCall(int userId, int page) {
        return networkService.getUserFollowingsApiCall(userId, page);
    }

    @Override
    public Single<PagDataWrapperModel<List<BlockedModel>>> getUserBlockedApiCall(int userId, int page) {
        return networkService.getUserBlockedApiCall(userId, page);
    }

    @Override
    public Single<DataWrapperModel<Integer>> submitCommercialActivityApiCall(MultipartBody build) {
        return networkService.submitCommercialActivityApiCall(build);
    }

    @Override
    public Single<DataWrapperModel<CommissionCalculatorModel>> calculateCommissionApiCall(String amount) {
        return networkService.calculateCommissionApiCall(amount);
    }

    @Override
    public Single<DataWrapperModel<Void>> followOrUnFollowUserApiCall(int followingId) {
        return networkService.followOrUnFollowUserApiCall(followingId);
    }

    @Override
    public Single<DataWrapperModel<List<SpecOptionModel>>> getSpecOptionsApiCall(String category) {
        return networkService.getSpecOptionsApiCall(category);
    }

    @Override
    public Single<DataWrapperModel<List<CityModel>>> getRootCitiesApiCall() {
        return networkService.getRootCitiesApiCall();
    }

    @Override
    public Single<DataWrapperModel<Void>> deleteAdOrOrderApiCall(HashMap<String, String> map) {
        return networkService.deleteAdOrOrderApiCall(map);
    }

    @Override
    public Single<DataWrapperModel<AdAndOrderModel>> getAdOrOrderDetailsApiCall(int itemId) {
        return networkService.getAdOrOrderDetailsApiCall(itemId);
    }

    @Override
    public Single<DataWrapperModel<Void>> deleteMediaApiCall(int mediaId) {
        return networkService.deleteMediaApiCall(mediaId);
    }

    @Override
    public Single<DataWrapperModel<Void>> updateAdOrOrderApiCall(int id, MultipartBody build) {
        return networkService.updateAdOrOrderApiCall(id, build);
    }

    @Override
    public Single<DataWrapperModel<List<SubscriptionPackagesModel>>> getSubscriptionPackagesApiCall() {
        return networkService.getSubscriptionPackagesApiCall();
    }

    @Override
    public Single<PagDataWrapperModel<List<AdAndOrderModel>>> searchItemsApiCall(HashMap<String, String> filterMap, int page) {
        return networkService.searchItemsApiCall(filterMap, page);
    }

    @Override
    public Single<DataWrapperModel<List<CommercialsModel>>> searchCommercialsApiCall(String ids) {
        return networkService.searchCommercialsApiCall(ids);
    }

    @Override
    public Single<DataWrapperModel<Void>> blockOrUnblockUserApiCall(int userId) {
        return networkService.blockOrUnblockUserApiCall(userId);
    }

    @Override
    public Single<PagDataWrapperModel<List<UserModel>>> searchMembersByMobileApiCall(String mobile) {
        return networkService.searchMembersByMobileApiCall(mobile);
    }

    @Override
    public Single<DataWrapperModel<LikeModel>> favoriteOrUnFavoriteApiCall(int itemId) {
        return networkService.favoriteOrUnFavoriteApiCall(itemId);
    }

    @Override
    public Single<PagDataWrapperModel<List<AdAndOrderModel>>> getSimilarItemsApiCall(int categoryId, int itemId) {
        return networkService.getSimilarItemsApiCall(categoryId, itemId);
    }

    @Override
    public Single<DataWrapperModel<Void>> reportItemApiCall(int itemId, String commentText) {
        return networkService.reportItemApiCall(itemId, commentText);
    }

    @Override
    public Single<DataWrapperModel<List<FAQsModel>>> getFAQsApiCall() {
        return networkService.getFAQsApiCall();
    }

    @Override
    public Single<DataWrapperModel<List<ChatsModel>>> getUserChats(int userId) {
        return networkService.getUserChats(userId);
    }

    @Override
    public Single<DataWrapperModel<CityModel>> getCityServicesReviewsApiCall(String arCurrentAreaAsId) {
        return networkService.getCityServicesReviewsApiCall(arCurrentAreaAsId);
    }

    @Override
    public Single<DataWrapperModel<List<CityServiceModel>>> getCityServicesApiCall() {
        return networkService.getCityServicesApiCall();
    }

    @Override
    public Single<DataWrapperModel<Void>> addCommentToCityApiCall(String city, String comment, int rating) {
        return networkService.addCommentToCityApiCall(city, comment, rating);
    }

    @Override
    public Single<DataWrapperModel<Void>> addCityServiceRateApiCall(String city, int cityServiceId, int rate) {
        return networkService.addCityServiceRateApiCall(city, cityServiceId, rate);
    }

    @Override
    public Single<DataWrapperModel<List<GoogleCategoriesModel>>> getGoogleServicesTypesApiCall() {
        return networkService.getGoogleServicesTypesApiCall();
    }

    @Override
    public Single<GoogleShopWrapperModel> getGoogleShops(Map<String, String> paramsMap) {
        return networkService.getGoogleShops(paramsMap);
    }

    @Override
    public Single<DataWrapperModel<CityModel>> isCityActiveApiCall(String arCurrentAreaAsId) {
        return networkService.isCityActiveApiCall(arCurrentAreaAsId);
    }

    @Override
    public Single<DataWrapperModel<String>> getBadWordsApiCall() {
        return networkService.getBadWordsApiCall();
    }
}
