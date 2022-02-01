package com.gp.health.ui.user.forgot_password;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.gp.health.R;
import com.gp.health.databinding.ActivityForgotPasswordBinding;
import com.gp.health.di.component.ActivityComponent;
import com.gp.health.ui.base.BaseActivity;
import com.gp.health.ui.user.verify_code.VerifyCodeActivity;
import com.gp.health.utils.ErrorHandlingUtils;

@SuppressLint("NonConstantResourceId")
public class ForgotPasswordActivity extends BaseActivity<ForgotPasswordViewModel> implements ForgotPasswordNavigator {

    ActivityForgotPasswordBinding binding;

    public static Intent newIntent(Context context) {
        return new Intent(context, ForgotPasswordActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityForgotPasswordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mViewModel.setNavigator(this);

        setUp();

    }

    private void setUp() {

        setupOnViewClicked();
        subscribeViewModel();

    }

    private void subscribeViewModel() {
        mViewModel.getForgotPasswordCodeLiveData().observe(this, response -> {
            hideLoading();
            startActivity(VerifyCodeActivity.newIntent(ForgotPasswordActivity.this)
                    .putExtra("user_model", response.getData()));
        });
    }

    @Override
    public void performDependencyInjection(ActivityComponent buildComponent) {
        buildComponent.inject(this);
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


    public void setupOnViewClicked() {

        binding.arrowBack.setOnClickListener(v -> finish());

        binding.sendButton.setOnClickListener(v -> {

            if (binding.etMobile.getText().toString().trim().isEmpty()) {
                binding.etMobile.setError(getText(R.string.empty_mobile));
                binding.etMobile.requestFocus();
                return;
            }

            if (binding.etMobile.getText().toString().trim().length() < 10) {
                binding.etMobile.setError(getText(R.string.invalid_mobile));
                binding.etMobile.setEnabled(true);
                binding.etMobile.requestFocus();
                return;
            }

            if (!binding.etMobile.getText().toString().trim().startsWith("05")) {
                binding.etMobile.setError(getText(R.string.invalid_mobile_prefix));
                binding.etMobile.requestFocus();
                return;
            }
            binding.etMobile.setError(null);

            showLoading();
            mViewModel.sendForgotPasswordCode(binding.etMobile.getText().toString().trim());
        });

    }
}
