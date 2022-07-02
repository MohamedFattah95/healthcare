
package com.gp.shifa.ui.user.login;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.firebase.messaging.FirebaseMessaging;
import com.gp.shifa.R;
import com.gp.shifa.databinding.ActivityLoginBinding;
import com.gp.shifa.di.component.ActivityComponent;
import com.gp.shifa.ui.base.BaseActivity;
import com.gp.shifa.ui.main.MainActivity;
import com.gp.shifa.ui.user.register.RegisterActivity;
import com.gp.shifa.ui.user.reset_password.ResetPasswordActivity;
import com.gp.shifa.utils.CommonUtils;
import com.gp.shifa.utils.ErrorHandlingUtils;

public class LoginActivity extends BaseActivity<LoginViewModel> implements LoginNavigator {

    private ActivityLoginBinding binding;

    public static Intent newIntent(Context context) {
        return new Intent(context, LoginActivity.class);
    }

    @Override
    public void openMainActivity() {
        startActivity(MainActivity.newIntent(LoginActivity.this));
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        mViewModel.setNavigator(this);

        setUp();

    }

    private void setUp() {

        subscribeViewModel();

        binding.ivBack.setOnClickListener(v -> finish());
        binding.tvSkip.setOnClickListener(v -> startActivity(getIntentWithClearHistory(MainActivity.class)));

        binding.btnLogin.setOnClickListener(v -> {

            if (binding.etEmail.getText().toString().trim().isEmpty()
                    || !CommonUtils.isEmailValid(binding.etEmail.getText().toString().trim())) {
                binding.etEmail.setError(getText(R.string.invalid_email));
                binding.etEmail.requestFocus();
                return;
            }
            binding.etEmail.setError(null);

            if (binding.etPassword.getText().toString().trim().isEmpty()) {
                binding.etPassword.setError(getText(R.string.empty_password));
                binding.etPassword.requestFocus();
                return;
            }

            if (binding.etPassword.getText().toString().trim().length() < 8) {
                binding.etPassword.setError(getText(R.string.invalid_password));
                binding.etPassword.requestFocus();
                return;
            }
            binding.etPassword.setError(null);

            showLoading();
            mViewModel.doLogin(binding.etEmail.getText().toString().trim(), binding.etPassword.getText().toString().trim());

        });

        binding.btnCreateNewAccount.setOnClickListener(v -> startActivity(RegisterActivity.newIntent(LoginActivity.this)));
        binding.tvRestorePassword.setOnClickListener(v -> startActivity(ResetPasswordActivity.newIntent(LoginActivity.this)));

    }

    private void subscribeViewModel() {
        mViewModel.getLoginLiveData().observe(this, response -> {
            getFirebaseInstanceId();
            hideLoading();
            startActivity(getIntentWithClearHistory(MainActivity.class));
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

//                    HashMap<String, String> map = new HashMap<>();
//                    map.put("_method", "PUT");
//                    map.put("fcm_token", token);
//                    map.put("mobile_type", "android");
//
//                    mViewModel.updateFCMToken(mViewModel.getDataManager().getCurrentUserId(), map);

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
}
