
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
    public Single<DataWrapperModel<UserModel>> doLoginApiCall(String email, String password) {
        return networkService.doLoginApiCall(email, password);
    }

    @Override
    public Single<DataWrapperModel<UserModel>> doRegistrationApiCall(MultipartBody body) {
        return networkService.doRegistrationApiCall(body);
    }

    @Override
    public Single<DataWrapperModel<UserModel>> getProfileApiCall(int userId) {
        return networkService.getProfileApiCall(userId);
    }

    @Override
    public Single<DataWrapperModel<UserModel.UserBean>> updateProfileApiCall(MultipartBody map) {
        return networkService.updateProfileApiCall(map);
    }

    @Override
    public Single<DataWrapperModel<Void>> updateFCMTokenApiCall(int userId, HashMap<String, String> map) {
        return networkService.updateFCMTokenApiCall(userId, map);
    }

    @Override
    public Single<DataWrapperModel<UserModel>> sendForgotPasswordCodeApiCall(String mobileNumber) {
        return networkService.sendForgotPasswordCodeApiCall(mobileNumber);
    }

    @Override
    public Single<DataWrapperModel<UserModel>> resetPasswordApiCall(String email, String password) {
        return networkService.resetPasswordApiCall(email, password);
    }

    @Override
    public Single<DataWrapperModel<CategoryDoctorsModel>> getCategoryDoctorsApiCall(int categoryId) {
        return networkService.getCategoryDoctorsApiCall(categoryId);
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
    public Single<DataWrapperModel<Void>> doLogout() {
        return networkService.doLogout();
    }

    @Override
    public Single<DataWrapperModel<List<DoctorModel>>> getFavoritesApiCall() {
        return networkService.getFavoritesApiCall();
    }

    @Override
    public Single<DataWrapperModel<List<CountriesAndAreasModel>>> getCountriesAndAreasApiCall() {
        return networkService.getCountriesAndAreasApiCall();
    }

    @Override
    public Single<DataWrapperModel<Void>> favoriteOrUnFavoriteApiCall(int itemId) {
        return networkService.favoriteOrUnFavoriteApiCall(itemId);
    }

    @Override
    public Single<DataWrapperModel<List<FAQsModel>>> getFAQsApiCall() {
        return networkService.getFAQsApiCall();
    }

    @Override
    public Single<DataWrapperModel<List<ChatsModel>>> getUserChats() {
        return networkService.getUserChats();
    }

    @Override
    public Single<DataWrapperModel<List<CategoriesModel>>> getCategoriesApiCall() {
        return networkService.getCategoriesApiCall();
    }

    @Override
    public Single<DataWrapperModel<List<DoctorModel>>> getDoctorsApiCall(int page) {
        return networkService.getDoctors(page);
    }

    @Override
    public Single<DataWrapperModel<DoctorDetailsModel>> getDoctorDetailsApiCall(int doctorId) {
        return networkService.getDoctorDetailsApiCall(doctorId);
    }
}
