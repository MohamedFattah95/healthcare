

package com.gp.shifa.ui.base;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.gp.shifa.BaseApp;
import com.gp.shifa.R;
import com.gp.shifa.di.component.ActivityComponent;
import com.gp.shifa.di.component.DaggerActivityComponent;
import com.gp.shifa.di.module.ActivityModule;
import com.gp.shifa.ui.user.login.LoginActivity;
import com.gp.shifa.utils.CommonUtils;
import com.gp.shifa.utils.LanguageHelper;
import com.gp.shifa.utils.NetworkUtils;

import java.util.List;

import javax.inject.Inject;

import es.dmoral.toasty.Toasty;

public abstract class BaseActivity<V extends BaseViewModel> extends AppCompatActivity implements BaseFragment.Callback {

    private ProgressDialog mProgressDialog;

    @Inject
    protected V mViewModel;

    @Override
    public void onFragmentAttached() {

    }

    @Override
    public void onFragmentDetached(String tag) {

    }

    @Override
    protected void attachBaseContext(Context newBase) {
        CommonUtils.getLanguageAwareContext(newBase);
        super.attachBaseContext(LanguageHelper.setLanguage(newBase, LanguageHelper.getLanguage(newBase)));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        CommonUtils.getLanguageAwareContext(this);
        LanguageHelper.setLanguage(this, LanguageHelper.getLanguage(this));
        performDependencyInjection(getBuildComponent());
        super.onCreate(savedInstanceState);
    }

    @TargetApi(Build.VERSION_CODES.M)
    public boolean hasPermission(String permission) {
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.M ||
                checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED;
    }

    public void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }
    }

    public void showKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
            }
        }
    }

    public void hideLoading() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.cancel();
        }
    }

    public boolean isNetworkConnected() {
        return NetworkUtils.isNetworkConnected(getApplicationContext());
    }

    public void openActivityOnTokenExpire() {
        startActivity(LoginActivity.newIntent(this));
        finish();
    }

    private ActivityComponent getBuildComponent() {
        return DaggerActivityComponent.builder()
                .appComponent(((BaseApp) getApplication()).appComponent)
                .activityModule(new ActivityModule(this))
                .build();
    }

    public abstract void performDependencyInjection(ActivityComponent buildComponent);

    @TargetApi(Build.VERSION_CODES.M)
    public void requestPermissionsSafely(String[] permissions, int requestCode) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(permissions, requestCode);
        }
    }

    public void showLoading() {
        hideLoading();
        mProgressDialog = CommonUtils.showLoadingDialog(this);
    }

    public Intent getIntentWithClearHistory(Class<?> c) {
        Intent intent = new Intent(this, c);
        intent.setFlags(intent.getFlags() |
                Intent.FLAG_ACTIVITY_NEW_TASK |
                Intent.FLAG_ACTIVITY_CLEAR_TASK |
                Intent.FLAG_ACTIVITY_CLEAR_TOP);
        return intent;
    }

    protected void showApiMessage(String m) {
        Toast toast = Toast.makeText(this, m, Toast.LENGTH_LONG);

        try {
            ViewGroup group = (ViewGroup) toast.getView();
            TextView messageTextView = (TextView) group.getChildAt(0);
            messageTextView.setTextSize(12f);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            toast.show();
        }

    }

    protected void showMessage(int id) {
        Toast toast = Toast.makeText(this, id, Toast.LENGTH_LONG);

        try {
            ViewGroup group = (ViewGroup) toast.getView();
            TextView messageTextView = (TextView) group.getChildAt(0);
            messageTextView.setTextSize(12f);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            toast.show();
        }

    }

    protected void showMessage(String m) {
        Toast toast = Toast.makeText(this, m, Toast.LENGTH_LONG);

        try {
            ViewGroup group = (ViewGroup) toast.getView();
            TextView messageTextView = (TextView) group.getChildAt(0);
            messageTextView.setTextSize(12f);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            toast.show();
        }
    }

    protected void showErrorMessage(String m) {
        Toasty.error(this, m, Toast.LENGTH_LONG, false).show();
    }

    protected void showSuccessMessage(String m) {
        Toasty.success(this, m, Toast.LENGTH_LONG, true).show();
    }

    protected void showNoteMessage(String m) {
        Toasty.info(this, m, Toast.LENGTH_LONG, true).show();
    }

    public void showNoteDialog(String title, String note, Context context) {
        LayoutInflater factory = LayoutInflater.from(context);
        View dialogView = factory.inflate(R.layout.dialog_note, null);
        AlertDialog mAlertDialog = new AlertDialog.Builder(context).create();
        mAlertDialog.setCanceledOnTouchOutside(false);
        mAlertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mAlertDialog.setView(dialogView);

        Button btnOk = dialogView.findViewById(R.id.okBtn);
        TextView titleTV = dialogView.findViewById(R.id.title);
        TextView subTitleTV = dialogView.findViewById(R.id.subTitle);

        titleTV.setText(title);
        subTitleTV.setText(note);

        btnOk.setOnClickListener(view -> {
            mAlertDialog.dismiss();
        });

        mAlertDialog.show();
    }

    public boolean isLastActivity(Activity activity) {
        ActivityManager mngr = (ActivityManager) getSystemService(ACTIVITY_SERVICE);

        List<ActivityManager.RunningTaskInfo> taskList = mngr.getRunningTasks(10);

        return taskList.get(0).numActivities == 1 &&
                taskList.get(0).topActivity.getClassName().equals(activity.getClass().getName());
    }

}

