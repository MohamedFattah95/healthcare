

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

public interface ApiHelper {

    Single<DataWrapperModel<UserModel>> doLoginApiCall(String email, String password);

    Single<DataWrapperModel<UserModel>> doRegistrationApiCall(MultipartBody body);

    Single<DataWrapperModel<UserModel>> getProfileApiCall(int userId);

    Single<DataWrapperModel<UserModel.UserBean>> updateProfileApiCall(MultipartBody body);

    Single<DataWrapperModel<Void>> updateFCMTokenApiCall(int userId, HashMap<String, String> map);

    Single<DataWrapperModel<UserModel>> sendForgotPasswordCodeApiCall(String mobileNumber);

    Single<DataWrapperModel<UserModel>> resetPasswordApiCall(String email, String password);

    Single<DataWrapperModel<CategoryDoctorsModel>> getCategoryDoctorsApiCall(int categoryId);

    Single<DataWrapperModel<List<IntroModel>>> getHowToUseApiCall();

    Single<DataWrapperModel<SliderModel>> getSlider(int id);

    Single<DataWrapperModel<Void>> doLogout();

    Single<DataWrapperModel<List<DoctorModel>>> getFavoritesApiCall();

    Single<DataWrapperModel<List<CountriesAndAreasModel>>> getCountriesAndAreasApiCall();

    Single<DataWrapperModel<Void>> favoriteOrUnFavoriteApiCall(int itemId);

    Single<DataWrapperModel<List<FAQsModel>>> getFAQsApiCall();

    Single<DataWrapperModel<List<ChatsModel>>> getUserChats();

    Single<DataWrapperModel<List<CategoriesModel>>> getCategoriesApiCall();

    Single<DataWrapperModel<List<DoctorModel>>> getDoctorsApiCall(int page);

    Single<DataWrapperModel<DoctorDetailsModel>> getDoctorDetailsApiCall(int doctorId);
}
