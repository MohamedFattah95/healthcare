package com.gp.health.ui.user.register;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.gp.health.R;
import com.gp.health.databinding.ActivityRegisterBinding;
import com.gp.health.di.component.ActivityComponent;
import com.gp.health.ui.base.BaseActivity;
import com.gp.health.ui.main.MainActivity;
import com.gp.health.ui.privacy_policy.PrivacyPolicyActivity;
import com.gp.health.ui.terms.TermsActivity;
import com.gp.health.ui.user.verify_account.VerifyAccountActivity;
import com.gp.health.utils.ErrorHandlingUtils;

public class RegisterActivity extends BaseActivity<RegisterViewModel> implements RegisterNavigator {

    private ActivityRegisterBinding binding;

    int memberType = 2;

    public static Intent newIntent(Context context) {
        return new Intent(context, RegisterActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        mViewModel.setNavigator(this);

        setUp();

    }

    private void setUp() {
        subscribeViewModel();
        setUpViewClicked();

        mViewModel.getMemberTypes();
    }

    private void subscribeViewModel() {

        mViewModel.getMemberTypesLiveData().observe(this, response -> {
            for (int i = 0; i < response.getData().size(); i++) {
                if (response.getData().get(i).getIsDefault() == 1) {
                    memberType = response.getData().get(i).getId();
                    break;
                }
            }
        });

        mViewModel.getRegistrationLiveData().observe(this, response -> {
            hideLoading();
            startActivity(VerifyAccountActivity.newIntent(RegisterActivity.this)
                    .putExtra("user_model", response.getData()));
            finish();
        });
    }

    @Override
    public void performDependencyInjection(ActivityComponent buildComponent) {
        buildComponent.inject(this);
    }

    public void setUpViewClicked() {

        binding.ivBack.setOnClickListener(v -> finish());

        binding.btnSignUp.setOnClickListener(v -> {

            if (binding.etName.getText().toString().trim().isEmpty()) {
                binding.etName.setError(getText(R.string.empty_full_name));
                binding.etName.requestFocus();
                return;
            }
            binding.etName.setError(null);

            if (binding.etMobile.getText().toString().trim().isEmpty()) {
                binding.etMobile.setError(getText(R.string.empty_mobile));
                binding.etMobile.requestFocus();
                return;
            }

            if (!binding.etMobile.getText().toString().trim().startsWith("05")) {
                binding.etMobile.setError(getText(R.string.invalid_mobile_prefix));
                binding.etMobile.requestFocus();
                return;
            }

            if (binding.etMobile.getText().toString().trim().length() < 10) {
                binding.etMobile.setError(getText(R.string.invalid_mobile));
                binding.etMobile.setEnabled(true);
                binding.etMobile.requestFocus();
                return;
            }
            binding.etMobile.setError(null);

            if (binding.etPassword.getText().toString().trim().isEmpty()) {
                binding.etPassword.setError(getText(R.string.empty_password));
                binding.etPassword.requestFocus();
                return;
            }

            if (binding.etPassword.getText().toString().trim().length() < 6) {
                binding.etPassword.setError(getText(R.string.invalid_password));
                binding.etPassword.requestFocus();
                return;
            }

            if (!binding.etPassword.getText().toString().trim().equals(binding.etConfirmPassword.getText().toString().trim())) {
                binding.etPassword.setError(getText(R.string.passwords_not_matched));
                binding.etConfirmPassword.setError(getText(R.string.passwords_not_matched));
                binding.etPassword.requestFocus();
                return;
            }
            binding.etPassword.setError(null);
            binding.etConfirmPassword.setError(null);

            showLoading();
            mViewModel.doRegistration(binding.etName.getText().toString().trim(),
                    binding.etMobile.getText().toString().trim(), binding.etPassword.getText().toString().trim(), memberType);

        });

        binding.tvSkip.setOnClickListener(v -> startActivity(getIntentWithClearHistory(MainActivity.class)));

        binding.tvPrivacy.setOnClickListener(v -> startActivity(PrivacyPolicyActivity.newIntent(this)));

        binding.tvTerms.setOnClickListener(v -> startActivity(TermsActivity.newIntent(this)));

        binding.tvLogin.setOnClickListener(v -> finish());

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
