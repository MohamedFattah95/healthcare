package com.gp.health.ui.user.verify_code;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;

import com.gp.health.R;
import com.gp.health.data.models.UserModel;
import com.gp.health.databinding.ActivityVerifyCodeBinding;
import com.gp.health.di.component.ActivityComponent;
import com.gp.health.ui.base.BaseActivity;
import com.gp.health.ui.user.reset_password.ResetPasswordActivity;
import com.gp.health.utils.ErrorHandlingUtils;

public class VerifyCodeActivity extends BaseActivity<VerifyCodeViewModel> implements VerifyCodeNavigator {

    ActivityVerifyCodeBinding binding;

    int time = 30;

    UserModel userModel;

    public static Intent newIntent(Context context) {
        return new Intent(context, VerifyCodeActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityVerifyCodeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mViewModel.setNavigator(this);

        setUp();

    }

    @SuppressLint("SetTextI18n")
    private void setUp() {

        userModel = (UserModel) getIntent().getSerializableExtra("user_model");

        setupOnViewClicked();
        subscribeViewModel();

        binding.etCode.setText("" + userModel.getVerificationCode());

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

        mViewModel.getVerifyCodePasswordLiveData().observe(this, response -> {
            hideLoading();
            startActivity(ResetPasswordActivity.newIntent(VerifyCodeActivity.this)
                    .putExtra("code", binding.etCode.getText().toString().trim())
                    .putExtra("phone", userModel.getMobile()));
        });

        mViewModel.getResendCodeLiveData().observe(this, response -> {
            hideLoading();
            binding.etCode.setText(String.valueOf(response.getData()));
        });

    }

    @Override
    public void performDependencyInjection(ActivityComponent buildComponent) {
        buildComponent.inject(this);
    }

    public void setupOnViewClicked() {

        binding.arrowBack.setOnClickListener(v -> finish());

        binding.submitBtn.setOnClickListener(v -> {

            if (binding.etCode.getText().toString().trim().isEmpty()) {
                binding.etCode.setError(getText(R.string.empty_code));
                binding.etCode.requestFocus();
                return;
            }

            showLoading();
            mViewModel.verifyCodePassword(userModel.getMobile(), binding.etCode.getText().toString());
        });

        binding.tvResend.setOnClickListener(v -> {
            playTimers();
            mViewModel.resendCode(userModel.getId());
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

}
