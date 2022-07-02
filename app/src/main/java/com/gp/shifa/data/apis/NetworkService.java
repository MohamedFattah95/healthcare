package com.gp.shifa.data.apis;


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

import java.util.HashMap;
import java.util.List;

import io.reactivex.Single;
import okhttp3.MultipartBody;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface NetworkService {

    @FormUrlEncoded
    @POST("api/v1/login")
    Single<DataWrapperModel<UserModel>> doLoginApiCall(@Field("email") String email,
                                                       @Field("password") String password);

    @POST("api/v1/register")
    Single<DataWrapperModel<UserModel>> doRegistrationApiCall(@Body MultipartBody body);

    @GET("users/{user_id}/profile")
    Single<DataWrapperModel<UserModel>> getProfileApiCall(@Path("user_id") int userId);

    @POST("api/v1/update-profile")
    Single<DataWrapperModel<UserModel.UserBean>> updateProfileApiCall(@Body MultipartBody body);


    @POST("users/{user_id}/fcm")
    Single<DataWrapperModel<Void>> updateFCMTokenApiCall(@Path("user_id") int userId,
                                                         @Body HashMap<String, String> map);

    @FormUrlEncoded
    @POST("forgot_password")
    Single<DataWrapperModel<UserModel>> sendForgotPasswordCodeApiCall(@Field("mobile") String mobileNumber);

    @FormUrlEncoded
    @POST("api/v1/reset-password")
    Single<DataWrapperModel<UserModel>> resetPasswordApiCall(@Field("email") String email,
                                                             @Field("newPassword") String password);

    @GET("api/v1/get-all-categories")
    Single<DataWrapperModel<List<CategoriesModel>>> getCategoriesApiCall();

    @GET("how_to_use")
    Single<DataWrapperModel<List<IntroModel>>> getHowToUseApiCall();

    @GET("slider/{id}")
    Single<DataWrapperModel<SliderModel>> getSlider(@Path("id") int id);

    @POST("api/v1/logout")
    Single<DataWrapperModel<Void>> doLogout();


    @POST("api/v1/get-doctors-favorite")
    Single<DataWrapperModel<List<DoctorModel>>> getFavoritesApiCall();


    @GET("api/v1/get-all-governorates-with-areas")
    Single<DataWrapperModel<List<CountriesAndAreasModel>>> getCountriesAndAreasApiCall();

    @FormUrlEncoded
    @POST("api/v1/toggle-favorite")
    Single<DataWrapperModel<Void>> favoriteOrUnFavoriteApiCall(@Field("doctor_id") int itemId);

    @GET("faqs")
    Single<DataWrapperModel<List<FAQsModel>>> getFAQsApiCall();

    @POST("api/v1/chats")
    Single<DataWrapperModel<List<ChatsModel>>> getUserChats();


    @GET("api/v1/get-single-category-with-doctors")
    Single<DataWrapperModel<CategoryDoctorsModel>> getCategoryDoctorsApiCall(@Query("category_id") int categoryId);

    @GET("api/v1/get-doctors")
    Single<DataWrapperModel<List<DoctorModel>>> getDoctors(@Query("page") int page);

    @GET("api/v1/get-single-doctor")
    Single<DataWrapperModel<DoctorDetailsModel>> getDoctorDetailsApiCall(@Query("doctor_id") int doctorId);
}
