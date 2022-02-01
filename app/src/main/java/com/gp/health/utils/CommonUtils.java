
package com.gp.health.utils;

import static com.gp.health.BaseApp.getContext;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.LayerDrawable;
import android.provider.Settings;
import android.util.Patterns;
import android.widget.Button;
import android.widget.RatingBar;

import androidx.core.graphics.drawable.DrawableCompat;

import com.gp.health.R;
import com.gp.health.ui.main.MainActivity;
import com.gp.health.ui.user.login.LoginActivity;

import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public final class CommonUtils {

    private CommonUtils() {
        // This utility class is not publicly instantiable
    }

    @SuppressLint("all")
    public static String getDeviceId(Context context) {
        return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    public static String getTimestamp() {
        return new SimpleDateFormat(AppConstants.TIMESTAMP_FORMAT, Locale.US).format(new Date());
    }

    public static boolean isEmailValid(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public static String loadJSONFromAsset(Context context, String jsonFileName) throws IOException {
        AssetManager manager = context.getAssets();
        InputStream is = manager.open(jsonFileName);

        int size = is.available();
        byte[] buffer = new byte[size];
        is.read(buffer);
        is.close();

        return new String(buffer, "UTF-8");
    }

    public static ProgressDialog showLoadingDialog(Context context) {
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.show();
        if (progressDialog.getWindow() != null) {
            progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        return progressDialog;
    }

    public static Context getLanguageAwareContext(Context context) {
        Configuration configuration = context.getResources().getConfiguration();
        Locale locale;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            locale = configuration.getLocales().get(0);
        } else {
            locale = configuration.locale;
        }
        configuration.setLocale(locale);
        configuration.setLayoutDirection(locale);
        return context.createConfigurationContext(configuration);
    }

    public static void handleNotAuthenticated(Context activity) {
        Dialog loginDialog = new Dialog(activity, R.style.FilterDialogTheme);
        loginDialog.setContentView(R.layout.dialog_login);
        loginDialog.setCancelable(true);

        Button button = loginDialog.getWindow().getDecorView().findViewById(R.id.goToLoginButton);
        Intent intent = new Intent(activity, LoginActivity.class);
        intent.setFlags(intent.getFlags() |
                Intent.FLAG_ACTIVITY_NEW_TASK |
                Intent.FLAG_ACTIVITY_CLEAR_TASK |
                Intent.FLAG_ACTIVITY_CLEAR_TOP);
        button.setOnClickListener(l -> activity.startActivity(intent));

        loginDialog.show();
    }


    public static void handleNotCompleted(Context activity) {
        Dialog dialog = new Dialog(activity, R.style.FilterDialogTheme);
        dialog.setContentView(R.layout.dialog_complete_profile);
        dialog.setCancelable(true);

        Button button = dialog.getWindow().getDecorView().findViewById(R.id.goToProfileButton);

        button.setOnClickListener(v -> {
            if (activity instanceof MainActivity) {
                ((MainActivity) activity).completeProfile();
            }
            dialog.dismiss();
        });

        dialog.show();
    }


    public static void setupRatingBar(RatingBar ratingBarDriver) {
        LayerDrawable layerDrawable = (LayerDrawable) ratingBarDriver.getProgressDrawable();
        DrawableCompat.setTint(DrawableCompat.wrap(layerDrawable.getDrawable(0)), Color.GRAY);  // Empty star
        DrawableCompat.setTint(DrawableCompat.wrap(layerDrawable.getDrawable(1)), getContext().getResources().getColor(R.color.colorGold)); // Partial star
        DrawableCompat.setTint(DrawableCompat.wrap(layerDrawable.getDrawable(2)), getContext().getResources().getColor(R.color.colorGold));
    }

    public static String setPriceCurrency(Context context, double price) {
        return formatCurrency(price) + " " + context.getString(R.string.sr);
    }

    public static String formatCurrency(double number) {
        String pattern = ",###.##"; //your pattern as per need
        Locale locale = new Locale("en", "US");
        DecimalFormat decimalFormat = (DecimalFormat) NumberFormat.getNumberInstance(locale);
        decimalFormat.applyPattern(pattern);
        return decimalFormat.format(number);
    }

}
