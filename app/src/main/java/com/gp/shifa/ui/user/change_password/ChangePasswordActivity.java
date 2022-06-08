package com.gp.shifa.ui.user.change_password;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.gp.shifa.R;
import com.gp.shifa.databinding.ActivityChangePasswordBinding;
import com.gp.shifa.di.component.ActivityComponent;
import com.gp.shifa.ui.base.BaseActivity;
import com.gp.shifa.utils.ErrorHandlingUtils;

public class ChangePasswordActivity extends BaseActivity<ChangePasswordViewModel> implements ChangePasswordNavigator {

    ActivityChangePasswordBinding binding;

    public static Intent newIntent(Context context) {
        return new Intent(context, ChangePasswordActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChangePasswordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mViewModel.setNavigator(this);

        setUp();

    }

    private void setUp() {

        subscribeViewModel();
        setupOnViewClicked();

    }

    private void subscribeViewModel() {
        mViewModel.getChangePasswordLiveData().observe(this, response -> {
            hideLoading();
            showSuccessMessage(response.getMessage());
            finish();
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


            if (binding.etOldPassword.getText().toString().trim().isEmpty()) {
                binding.etOldPassword.setError(getText(R.string.empty_password));
                binding.etOldPassword.requestFocus();
                return;
            }

            if (binding.etOldPassword.getText().toString().trim().length() < 8) {
                binding.etOldPassword.setError(getText(R.string.invalid_password));
                binding.etOldPassword.requestFocus();
                return;
            }
            binding.etOldPassword.setError(null);


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
            mViewModel.changePassword(binding.etOldPassword.getText().toString().trim(),
                    binding.etNewPassword.getText().toString().trim());

        });


    }
}
