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
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public interface NetworkService {

    @GET("settings")
    Single<DataWrapperModel<SettingsModel>> getSettings();

    @FormUrlEncoded
    @POST("api/v1/login")
    Single<DataWrapperModel<UserModel>> doLoginApiCall(@Field("email") String email,
                                                       @Field("password") String password);

    @POST("api/v1/register")
    Single<DataWrapperModel<UserModel>> doRegistrationApiCall(@Body MultipartBody body);

    @FormUrlEncoded
    @POST("check_verification_code")
    Single<DataWrapperModel<Void>> verifyCodeApiCall(@Field("user_id") String userId,
                                                     @Field("code") String code);

    @FormUrlEncoded
    @POST("check_verification_code_password")
    Single<DataWrapperModel<Void>> verifyCodePasswordApiCall(@Field("mobile") String mobile,
                                                             @Field("code") String code);

    @FormUrlEncoded
    @POST("resend_code")
    Single<DataWrapperModel<Integer>> resendCodeApiCall(@Field("user_id") int userId);

    @GET("areas/{id}")
    Single<DataWrapperModel<List<CityModel>>> getAreasAndCitiesApiCall(@Path("id") int id);

    @GET("users/{user_id}/profile")
    Single<DataWrapperModel<UserModel>> getProfileApiCall(@Path("user_id") int userId);

    @POST("users/{user_id}")
    Single<DataWrapperModel<UserModel>> updateProfileApiCall(@Body MultipartBody body,
                                                             @Path("user_id") int userId);


    @POST("users/{user_id}/fcm")
    Single<DataWrapperModel<Void>> updateFCMTokenApiCall(@Path("user_id") int userId,
                                                         @Body HashMap<String, String> map);

    @GET("contactus_types")
    Single<DataWrapperModel<List<ContactUsMessageTypesModel>>> getContactUsMessageTypesApiCall();

    @POST("contactus")
    Single<DataWrapperModel<Void>> sendContactUsMessageApiCall(@Body HashMap<String, String> map);

    @FormUrlEncoded
    @POST("forgot_password")
    Single<DataWrapperModel<UserModel>> sendForgotPasswordCodeApiCall(@Field("mobile") String mobileNumber);

    @FormUrlEncoded
    @POST("change_password")
    Single<DataWrapperModel<Void>> resetPasswordApiCall(@Field("code") String code,
                                                        @Field("mobile") String phone,
                                                        @Field("password") String password);

    @GET("users/{user_id}/notifications")
    Single<PagDataWrapperModel<List<NotificationsModel>>> getNotificationsApiCall(@Path("user_id") int userId,
                                                                                  @Query("page") int page
    );

    @GET("categories")
    Single<DataWrapperModel<List<CategoriesModel>>> getCategoriesApiCall();

    @GET("https://maps.googleapis.com/maps/api/place/textsearch/json")
    Single<GoogleShopWrapperModel> getSearchPlaces(@QueryMap Map<String, String> paramsMap);

    @FormUrlEncoded
    @POST("users/rate_comments")
    Single<DataWrapperModel<Void>> rateUserApiCall(@Field("user_id") int id,
                                                   @Field("comment") String msg,
                                                   @Field("rate") int rating);


    @FormUrlEncoded
    @POST("notifications/{notification_id}/readed")
    Single<DataWrapperModel<Void>> markNotificationAsSeenApiCall(@Path("notification_id") int id,
                                                                 @Field("_method") String method);

    @GET("how_to_use")
    Single<DataWrapperModel<List<IntroModel>>> getHowToUseApiCall();

    @GET("slider/{id}")
    Single<DataWrapperModel<SliderModel>> getSlider(@Path("id") int id);

    @FormUrlEncoded
    @POST("logout")
    Single<DataWrapperModel<Void>> doLogout(@Field("user_id") int userId);

    @FormUrlEncoded
    @POST("notifications/{user_id}/user_readed_all")
    Single<DataWrapperModel<Void>> markAllAsReadApiCall(@Path("user_id") int userId,
                                                        @Field("_method") String method);

    @FormUrlEncoded
    @POST("check_user")
    Single<DataWrapperModel<Void>> checkUserApiCall(@Field("user_id") int currentUserId);

    @FormUrlEncoded
    @POST("users/check_old_password")
    Single<DataWrapperModel<Void>> checkOldPasswordApiCall(@Field("id") int userId,
                                                           @Field("password") String oldPassword);


    @GET("users/{user_id}/likes")
    Single<PagDataWrapperModel<List<AdAndOrderModel>>> getFavoritesApiCall(@Path("user_id") int userId,
                                                                           @Query("page") int page);


    @POST("items/store")
    Single<DataWrapperModel<AdAndOrderModel>> submitAdOrOrderApiCall(@Body MultipartBody builder);


    @GET("bank_accounts")
    Single<DataWrapperModel<List<BanksModel>>> getBanksApiCall();

    @POST("commissions/transfere")
    Single<DataWrapperModel<Void>> submitCommissionApiCall(@Body MultipartBody build);

    @FormUrlEncoded
    @POST("shat_notifications")
    Single<DataWrapperModel<Void>> sendChatNotif(@Field("user_id") int senderId,
                                                 @Field("user_reciever_id") int receiverId);

    @GET("api/v1/get-all-governorates-with-areas")
    Single<DataWrapperModel<List<CountriesAndAreasModel>>> getCountriesAndAreasApiCall();

    @GET("users/{user_id}/item_type/{type}")
    Single<PagDataWrapperModel<List<AdAndOrderModel>>> getUserAdsOrOrdersApiCall(@Path("user_id") int userId,
                                                                                 @Path("type") int type,
                                                                                 @Query("page") int page);

    @GET("users/{user_id}/commentes_by_others")
    Single<PagDataWrapperModel<List<CommentModel>>> getUserRatingsApiCall(@Path("user_id") int userId,
                                                                          @Query("page") int page);

    @GET("users/{user_id}/followings")
    Single<PagDataWrapperModel<List<FollowerModel>>> getUserFollowingsApiCall(@Path("user_id") int userId,
                                                                              @Query("page") int page);

    @GET("users/{user_id}/banned")
    Single<PagDataWrapperModel<List<BlockedModel>>> getUserBlockedApiCall(@Path("user_id") int userId,
                                                                          @Query("page") int page);

    @POST("commercial_activities")
    Single<DataWrapperModel<Integer>> submitCommercialActivityApiCall(@Body MultipartBody build);


    @GET("commissions/calculate")
    Single<DataWrapperModel<CommissionCalculatorModel>> calculateCommissionApiCall(@Query("amount") String amount);

    @FormUrlEncoded
    @POST("users/follow")
    Single<DataWrapperModel<Void>> followOrUnFollowUserApiCall(@Field("follow_user_id") int followingId);

    @GET("categories/{category_id}/options")
    Single<DataWrapperModel<List<SpecOptionModel>>> getSpecOptionsApiCall(@Path("category_id") String category);

    @GET("countries/root")
    Single<DataWrapperModel<List<CityModel>>> getRootCitiesApiCall();

    @POST("items/delete")
    Single<DataWrapperModel<Void>> deleteAdOrOrderApiCall(@Body HashMap<String, String> map);

    @GET("items/{item_id}")
    Single<DataWrapperModel<AdAndOrderModel>> getAdOrOrderDetailsApiCall(@Path("item_id") int itemId);

    @FormUrlEncoded
    @POST("files/delete")
    Single<DataWrapperModel<Void>> deleteMediaApiCall(@Field("id") int mediaId);

    @POST("items/{id}/update")
    Single<DataWrapperModel<Void>> updateAdOrOrderApiCall(@Path("id") int id,
                                                          @Body MultipartBody build);

    @GET("subscription_packages")
    Single<DataWrapperModel<List<SubscriptionPackagesModel>>> getSubscriptionPackagesApiCall();

    @POST("items/search")
    Single<PagDataWrapperModel<List<AdAndOrderModel>>> searchItemsApiCall(@Body HashMap<String, String> filterMap,
                                                                          @Query("page") int page);

    @FormUrlEncoded
    @POST("commercial_activities/search")
    Single<DataWrapperModel<List<CommercialsModel>>> searchCommercialsApiCall(@Field("ids") String ids);

    @FormUrlEncoded
    @POST("users/bann")
    Single<DataWrapperModel<Void>> blockOrUnblockUserApiCall(@Field("user_id") int userId);

    @FormUrlEncoded
    @POST("users/search_mobile")
    Single<PagDataWrapperModel<List<UserModel>>> searchMembersByMobileApiCall(@Field("mobile") String mobile);

    @FormUrlEncoded
    @POST("items/like")
    Single<DataWrapperModel<LikeModel>> favoriteOrUnFavoriteApiCall(@Field("item_id") int itemId);

    @GET("items/relations/{category_id}/{item_id}")
    Single<PagDataWrapperModel<List<AdAndOrderModel>>> getSimilarItemsApiCall(@Path("category_id") int categoryId,
                                                                              @Path("item_id") int itemId);

    @FormUrlEncoded
    @POST("items/bann")
    Single<DataWrapperModel<Void>> reportItemApiCall(@Field("item_id") int itemId,
                                                     @Field("reason") String commentText);

    @GET("faqs")
    Single<DataWrapperModel<List<FAQsModel>>> getFAQsApiCall();

    @GET("users/{user_id}/chats")
    Single<DataWrapperModel<List<ChatsModel>>> getUserChats(@Path("user_id") int userId);

    @GET("countries/{city}")
    Single<DataWrapperModel<CityModel>> getCityServicesReviewsApiCall(@Path("city") String arCurrentAreaAsId);

    @GET("country_services")
    Single<DataWrapperModel<List<CityServiceModel>>> getCityServicesApiCall();

    @FormUrlEncoded
    @POST("countries/rate_comments")
    Single<DataWrapperModel<Void>> addCommentToCityApiCall(@Field("country") String city,
                                                           @Field("comment") String comment,
                                                           @Field("rate") int rating);

    @FormUrlEncoded
    @POST("country_services/services_percentage")
    Single<DataWrapperModel<Void>> addCityServiceRateApiCall(@Field("country") String city,
                                                             @Field("country_service_id") int cityServiceId,
                                                             @Field("percentage") int rate);

    @GET("categories/google")
    Single<DataWrapperModel<List<GoogleCategoriesModel>>> getGoogleServicesTypesApiCall();

    //Google Shops
    @GET("https://maps.googleapis.com/maps/api/place/nearbysearch/json")
    Single<GoogleShopWrapperModel> getGoogleShops(@QueryMap Map<String, String> paramsMap);

    @GET("countries/check_is_acitve/{city}")
    Single<DataWrapperModel<CityModel>> isCityActiveApiCall(@Path("city") String arCurrentAreaAsId);

    @GET("bad_words")
    Single<DataWrapperModel<String>> getBadWordsApiCall();

}
