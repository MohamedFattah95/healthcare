

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

import io.reactivex.Single;
import okhttp3.MultipartBody;

public interface ApiHelper {

    Single<DataWrapperModel<SettingsModel>> getSettings();

    Single<DataWrapperModel<UserModel>> doLoginApiCall(String email, String password);

    Single<DataWrapperModel<UserModel>> doRegistrationApiCall(MultipartBody body);

    Single<DataWrapperModel<Void>> verifyCodeApiCall(String userId, String code);

    Single<DataWrapperModel<Void>> verifyCodePasswordApiCall(String mobile, String code);

    Single<DataWrapperModel<Integer>> resendCodeApiCall(int userId);

    Single<DataWrapperModel<UserModel>> getProfileApiCall(int userId);

    Single<DataWrapperModel<UserModel>> updateProfileApiCall(MultipartBody body, int userId);

    Single<DataWrapperModel<Void>> updateFCMTokenApiCall(int userId, HashMap<String, String> map);

    Single<DataWrapperModel<List<ContactUsMessageTypesModel>>> getContactUsMessageTypesApiCall();

    Single<DataWrapperModel<Void>> sendContactUsMessageApiCall(HashMap<String, String> map);

    Single<DataWrapperModel<UserModel>> sendForgotPasswordCodeApiCall(String mobileNumber);

    Single<DataWrapperModel<Void>> resetPasswordApiCall(String code, String phone, String password);

    Single<PagDataWrapperModel<List<NotificationsModel>>> getNotificationsApiCall(int userId, int page);

    Single<DataWrapperModel<List<CategoriesModel>>> getCategoriesApiCall();

    Single<GoogleShopWrapperModel> getSearchPlaces(Map<String, String> paramsMap);

    Single<DataWrapperModel<Void>> rateUserApiCall(int id, String msg, int rating);

    Single<DataWrapperModel<Void>> markNotificationAsSeenApiCall(int id, String method);

    Single<DataWrapperModel<List<IntroModel>>> getHowToUseApiCall();

    Single<DataWrapperModel<SliderModel>> getSlider(int id);

    Single<DataWrapperModel<Void>> doLogout(int userId);

    Single<DataWrapperModel<Void>> markAllAsReadApiCall(int userId, String method);

    Single<DataWrapperModel<Void>> checkUserApiCall(int currentUserId);

    Single<DataWrapperModel<List<CityModel>>> getAreasAndCitiesApiCall(int id);

    Single<DataWrapperModel<Void>> checkOldPasswordApiCall(int userId, String oldPassword);

    Single<PagDataWrapperModel<List<AdAndOrderModel>>> getFavoritesApiCall(int userId, int page);

    Single<DataWrapperModel<AdAndOrderModel>> submitAdOrOrderApiCall(MultipartBody builder);

    Single<DataWrapperModel<List<BanksModel>>> getBanksApiCall();

    Single<DataWrapperModel<Void>> submitCommissionApiCall(MultipartBody build);

    Single<DataWrapperModel<Void>> sendChatNotif(int senderId, int receiverId);

    Single<DataWrapperModel<List<CountriesAndAreasModel>>> getCountriesAndAreasApiCall();

    Single<PagDataWrapperModel<List<AdAndOrderModel>>> getUserAdsOrOrdersApiCall(int userId, int type, int page);

    Single<PagDataWrapperModel<List<CommentModel>>> getUserRatingsApiCall(int userId, int page);

    Single<PagDataWrapperModel<List<FollowerModel>>> getUserFollowingsApiCall(int userId, int page);

    Single<PagDataWrapperModel<List<BlockedModel>>> getUserBlockedApiCall(int userId, int page);

    Single<DataWrapperModel<Integer>> submitCommercialActivityApiCall(MultipartBody build);

    Single<DataWrapperModel<CommissionCalculatorModel>> calculateCommissionApiCall(String amount);

    Single<DataWrapperModel<Void>> followOrUnFollowUserApiCall(int followingId);

    Single<DataWrapperModel<List<SpecOptionModel>>> getSpecOptionsApiCall(String category);

    Single<DataWrapperModel<List<CityModel>>> getRootCitiesApiCall();

    Single<DataWrapperModel<Void>> deleteAdOrOrderApiCall(HashMap<String, String> map);

    Single<DataWrapperModel<AdAndOrderModel>> getAdOrOrderDetailsApiCall(int itemId);

    Single<DataWrapperModel<Void>> deleteMediaApiCall(int mediaId);

    Single<DataWrapperModel<Void>> updateAdOrOrderApiCall(int id, MultipartBody build);

    Single<DataWrapperModel<List<SubscriptionPackagesModel>>> getSubscriptionPackagesApiCall();

    Single<PagDataWrapperModel<List<AdAndOrderModel>>> searchItemsApiCall(HashMap<String, String> filterMap, int page);

    Single<DataWrapperModel<List<CommercialsModel>>> searchCommercialsApiCall(String ids);

    Single<DataWrapperModel<Void>> blockOrUnblockUserApiCall(int userId);

    Single<PagDataWrapperModel<List<UserModel>>> searchMembersByMobileApiCall(String mobile);

    Single<DataWrapperModel<LikeModel>> favoriteOrUnFavoriteApiCall(int itemId);

    Single<PagDataWrapperModel<List<AdAndOrderModel>>> getSimilarItemsApiCall(int categoryId, int itemId);

    Single<DataWrapperModel<Void>> reportItemApiCall(int itemId, String commentText);

    Single<DataWrapperModel<List<FAQsModel>>> getFAQsApiCall();

    Single<DataWrapperModel<List<ChatsModel>>> getUserChats(int userId);

    Single<DataWrapperModel<CityModel>> getCityServicesReviewsApiCall(String arCurrentAreaAsId);

    Single<DataWrapperModel<List<CityServiceModel>>> getCityServicesApiCall();

    Single<DataWrapperModel<Void>> addCommentToCityApiCall(String city, String comment, int rating);

    Single<DataWrapperModel<Void>> addCityServiceRateApiCall(String city, int cityServiceId, int rate);

    Single<DataWrapperModel<List<GoogleCategoriesModel>>> getGoogleServicesTypesApiCall();

    Single<GoogleShopWrapperModel> getGoogleShops(Map<String, String> paramsMap);

    Single<DataWrapperModel<CityModel>> isCityActiveApiCall(String arCurrentAreaAsId);

    Single<DataWrapperModel<String>> getBadWordsApiCall();
}
