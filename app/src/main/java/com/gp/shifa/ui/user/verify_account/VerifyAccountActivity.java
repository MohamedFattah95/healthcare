package com.gp.shifa.ui.user.verify_account;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.firebase.messaging.FirebaseMessaging;
import com.gp.shifa.R;
import com.gp.shifa.data.models.UserModel;
import com.gp.shifa.databinding.ActivityVerifyAccountBinding;
import com.gp.shifa.di.component.ActivityComponent;
import com.gp.shifa.ui.base.BaseActivity;
import com.gp.shifa.ui.user.complete_profile.CompleteProfileActivity;
import com.gp.shifa.utils.ErrorHandlingUtils;

import java.util.HashMap;

public class VerifyAccountActivity extends BaseActivity<VerifyAccountViewModel> implements VerifyAccountNavigator {

    int time = 30;

    UserModel userModel;

    private ActivityVerifyAccountBinding binding;

    public static Intent newIntent(Context context) {
        return new Intent(context, VerifyAccountActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityVerifyAccountBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        mViewModel.setNavigator(this);

        setUp();

    }

    @SuppressLint("SetTextI18n")
    private void setUp() {
        userModel = (UserModel) getIntent().getSerializableExtra("user_model");

        subscribeViewModel();
        setUpViewClicked();

        binding.tvMobileNumber.setText(userModel.getUser().getPhone());

        playTimers();
    }

    private void playTimers() {

        binding.tvResend.setClickable(false);
        binding.tvResend.setEnabled(false);

        new CountDownTimer(30000, 1000) {

            @SuppressLint("SetTextI18n")
            public void onTick(long millisUntilFinished) {

                binding.tvTimer.setText(getString(R.string.after) + " " + time + " " + getString(R.string.seconds));
                time--;
            }

            @SuppressLint("SetTextI18n")
            public void onFinish() {
                binding.tvResend.setClickable(true);
                binding.tvResend.setEnabled(true);
                binding.tvTimer.setText(getString(R.string.after) + " " + "0" + " " + getString(R.string.seconds));
                time = 30;
            }

        }.start();
    }

    private void subscribeViewModel() {

        mViewModel.getVerifyCodeLiveData().observe(this, response -> {
            hideLoading();
            showVerificationDoneDialog();
            getFirebaseInstanceId();
        });

        mViewModel.getResendCodeLiveData().observe(this, response -> {
            hideLoading();
            binding.etCode.setText(String.valueOf(response.getData()));
        });

    }

    @SuppressLint("LogNotTimber")
    private void getFirebaseInstanceId() {
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Log.e("device_token", "getInstanceId failed", task.getException());
                        return;
                    }

                    // Get new Instance ID token
                    String token = task.getResult();
                    Log.d("device_token", token);

                    HashMap<String, String> map = new HashMap<>();
                    map.put("_method", "PUT");
                    map.put("fcm_token", token);
                    map.put("mobile_type", "android");

                    mViewModel.updateFCMToken(mViewModel.getDataManager().getCurrentUserId(), map);

                });

    }

    @Override
    public void performDependencyInjection(ActivityComponent buildComponent) {
        buildComponent.inject(this);
    }

    public void setUpViewClicked() {

        binding.ivBack.setOnClickListener(v -> finish());

        binding.btnSubmit.setOnClickListener(v -> {

            if (binding.etCode.getText().toString().trim().isEmpty()) {
                binding.etCode.setError(getText(R.string.empty_code));
                binding.etCode.requestFocus();
                return;
            }
            binding.etCode.setError(null);

            showLoading();
            mViewModel.verifyCode(userModel, binding.etCode.getText().toString());

        });

        binding.tvResend.setOnClickListener(v -> {
            playTimers();
            mViewModel.resendCode(userModel.getUser().getId());
        });

    }

    @Override
    public void handleError(Throwable throwable) {
        hideLoading();
        ErrorHandlingUtils.handleErrors(throwable);
    }

    @Override
    public void showMyApiMessage(String message) {
        hideLoading();
        showErrorMessage(message);
    }

    private void showVerificationDoneDialog() {
        MaterialDialog dialog = new MaterialDialog.Builder(this)
                .customView(R.layout.dialog_verification_done, true)
                .cancelable(false).build();

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        dialog.findViewById(R.id.btn_ok).setOnClickListener(v -> {
            dialog.dismiss();
            startActivity(getIntentWithClearHistory(CompleteProfileActivity.class));
        });

        dialog.show();

    }

}
