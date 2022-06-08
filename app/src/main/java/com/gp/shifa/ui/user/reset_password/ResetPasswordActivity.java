package com.gp.shifa.ui.user.reset_password;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.gp.shifa.R;
import com.gp.shifa.databinding.ActivityResetPasswordBinding;
import com.gp.shifa.di.component.ActivityComponent;
import com.gp.shifa.ui.base.BaseActivity;
import com.gp.shifa.ui.user.login.LoginActivity;
import com.gp.shifa.utils.CommonUtils;
import com.gp.shifa.utils.ErrorHandlingUtils;

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
            showSuccessMessage(response.getMessage());
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

            if (binding.etEmail.getText().toString().trim().isEmpty()
                    || !CommonUtils.isEmailValid(binding.etEmail.getText().toString().trim())) {
                binding.etEmail.setError(getText(R.string.invalid_email));
                binding.etEmail.requestFocus();
                return;
            }
            binding.etEmail.setError(null);

            if (binding.etNewPassword.getText().toString().trim().isEmpty()) {
                binding.etNewPassword.setError(getText(R.string.empty_password));
                binding.etNewPassword.requestFocus();
                return;
            }

            if (binding.etNewPassword.getText().toString().trim().length() < 8) {
                binding.etNewPassword.setError(getText(R.string.invalid_password));
                binding.etNewPassword.requestFocus();
                return;
            }
            binding.etNewPassword.setError(null);

            showLoading();
            mViewModel.resetPassword(binding.etEmail.getText().toString().trim(),
                    binding.etNewPassword.getText().toString().trim());

        });


    }
}
