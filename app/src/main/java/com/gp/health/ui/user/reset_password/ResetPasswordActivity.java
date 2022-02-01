package com.gp.health.ui.user.reset_password;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.gp.health.R;
import com.gp.health.databinding.ActivityResetPasswordBinding;
import com.gp.health.di.component.ActivityComponent;
import com.gp.health.ui.base.BaseActivity;
import com.gp.health.ui.user.login.LoginActivity;
import com.gp.health.utils.ErrorHandlingUtils;

public class ResetPasswordActivity extends BaseActivity<ResetPasswordViewModel> implements ResetPasswordNavigator {

    ActivityResetPasswordBinding binding;

    public static Intent newIntent(Context context) {
        return new Intent(context, ResetPasswordActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityResetPasswordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mViewModel.setNavigator(this);

        setUp();

    }

    private void setUp() {

        subscribeViewModel();
        setupOnViewClicked();

    }

    private void subscribeViewModel() {
        mViewModel.getResetPasswordLiveData().observe(this, response -> {
            hideLoading();
            startActivity(getIntentWithClearHistory(LoginActivity.class));
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

        binding.saveButton.setOnClickListener(v -> {

            if (binding.etNewPassword.getText().toString().trim().isEmpty()) {
                binding.etNewPassword.setError(getText(R.string.empty_password));
                binding.etNewPassword.requestFocus();
                return;
            }

            if (binding.etNewPassword.getText().toString().trim().length() < 6) {
                binding.etNewPassword.setError(getText(R.string.invalid_password));
                binding.etNewPassword.requestFocus();
                return;
            }

            if (!binding.etNewPassword.getText().toString().trim().equals(binding.etConfirmNewPassword.getText().toString().trim())) {
                binding.etNewPassword.setError(getText(R.string.passwords_not_matched));
                binding.etConfirmNewPassword.setError(getText(R.string.passwords_not_matched));
                binding.etNewPassword.requestFocus();
                return;
            }
            binding.etNewPassword.setError(null);
            binding.etConfirmNewPassword.setError(null);

            showLoading();
            mViewModel.resetPassword(getIntent().getStringExtra("code"),
                    getIntent().getStringExtra("phone"),
                    binding.etNewPassword.getText().toString().trim());

        });


    }
}
