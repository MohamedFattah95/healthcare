package com.gp.shifa.utils;

import com.gp.shifa.data.apis.NetworkService;
import com.gp.shifa.data.models.DataWrapperModel;
import com.gp.shifa.data.prefs.AppPreferencesHelper;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

import javax.inject.Inject;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class Token {
    private static final String TAG = "FCM Service";

    @Inject
    NetworkService networkService;

    public Token(NetworkService networkService) {
        this.networkService = networkService;
    }

    public void sendRefreshedToken(String refreshedToken) {
        if (AppPreferencesHelper.getInstance().isUserLogged()) {
            int userId = AppPreferencesHelper.getInstance().getCurrentUserId();
            HashMap<String, String> map = new HashMap<>();
            map.put("_method", "PUT");
            map.put("fcm_token", refreshedToken);
            map.put("mobile_type", "android");

            networkService.updateFCMTokenApiCall(userId, map)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new SingleObserver<DataWrapperModel<Void>>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onSuccess(@NotNull DataWrapperModel<Void> voidDataWrapperModel) {
                            Timber.e("Success Refreshed token: %s", voidDataWrapperModel.getMessage());
                        }

                        @Override
                        public void onError(Throwable e) {
                            Timber.e("Failed! Token Response");
                        }
                    });
        }
    }
}
