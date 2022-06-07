package com.gp.shifa.utils;

import static com.gp.shifa.BaseApp.getContext;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.gp.shifa.R;
import com.gp.shifa.data.DataManager;
import com.gp.shifa.data.prefs.AppPreferencesHelper;
import com.gp.shifa.ui.error_handler.ErrorHandlerActivity;
import com.gp.shifa.ui.user.login.LoginActivity;
import com.jakewharton.retrofit2.adapter.rxjava2.HttpException;

import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import es.dmoral.toasty.Toasty;

public class ErrorHandlingUtils {

    private static final AppPreferencesHelper appPreferencesHelper = AppPreferencesHelper.getInstance();

    public static void handleErrors(Throwable e) {
        Toasty.error(getContext(), getErrorMessage(e), Toast.LENGTH_LONG, false).show();
    }

    private static String getErrorMessage(Throwable e) {

        e.printStackTrace();
        if (e instanceof UnknownHostException) {
            return getContext().getResources().getString(R.string.check_internet_conn);
        } else if (e instanceof SocketTimeoutException) {
            return getContext().getResources().getString(R.string.weak_internet_conn);
        } else if (e instanceof HttpException && ((HttpException) e).code() == HttpURLConnection.HTTP_UNAUTHORIZED) {

            if (appPreferencesHelper.isUserLogged()) {

                appPreferencesHelper.setAccessToken(null);
//                appPreferencesHelper.setCurrentUserId(AppConstants.NULL_INDEX);
                appPreferencesHelper.setCurrentUserName(null);
                appPreferencesHelper.setCurrentUserEmail(null);
                appPreferencesHelper.setCurrentUserProfilePicUrl(null);
                appPreferencesHelper.setUserObject(null);
                appPreferencesHelper.setCurrentUserLoggedInMode(DataManager.LoggedInMode.LOGGED_IN_MODE_LOGGED_OUT);

                getContext().startActivity(ErrorHandlerActivity.newIntent(getContext())
                        .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                                Intent.FLAG_ACTIVITY_CLEAR_TASK |
                                Intent.FLAG_ACTIVITY_CLEAR_TOP));
            }

            return LanguageHelper.getLanguage(getContext()).equals("ar") ? "تم إيقاف الحساب" : "Account has been disabled";
        } else {
            return getContext().getResources().getString(R.string.some_error);
        }
    }


    private static void showLoginDialog() {
        MaterialDialog dialog = new MaterialDialog.Builder(getContext())
                .customView(R.layout.dialog_login_again, true)
                .cancelable(false).build();

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        dialog.findViewById(R.id.goToLoginButton).setOnClickListener(v -> {
            getContext().startActivity(LoginActivity.newIntent(getContext())
                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                            Intent.FLAG_ACTIVITY_CLEAR_TASK |
                            Intent.FLAG_ACTIVITY_CLEAR_TOP));
            dialog.dismiss();
        });

        dialog.show();
    }

}
