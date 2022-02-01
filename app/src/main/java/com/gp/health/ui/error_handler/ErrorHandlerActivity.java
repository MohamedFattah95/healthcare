package com.gp.health.ui.error_handler;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.gp.health.R;
import com.gp.health.databinding.ActivityErrorHandlerBinding;
import com.gp.health.di.component.ActivityComponent;
import com.gp.health.ui.base.BaseActivity;
import com.gp.health.ui.user.login.LoginActivity;
import com.gp.health.utils.AppConstants;
import com.gp.health.utils.ErrorHandlingUtils;

public class ErrorHandlerActivity extends BaseActivity<ErrorHandlerViewModel> implements ErrorHandlerNavigator {

    ActivityErrorHandlerBinding binding;

    public static Intent newIntent(Context context) {
        return new Intent(context, ErrorHandlerActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityErrorHandlerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mViewModel.setNavigator(this);

        setUp();

    }

    private void setUp() {

        subscribeViewModel();

        showLoading();
        mViewModel.checkUser(mViewModel.getDataManager().getCurrentUserId());

        binding.okBtn.setOnClickListener(v -> {
            mViewModel.getDataManager().setCurrentUserId(AppConstants.NULL_INDEX);
            startActivity(getIntentWithClearHistory(LoginActivity.class));
        });
    }

    private void subscribeViewModel() {
        mViewModel.getCheckUserLiveData().observe(this, response -> {
            hideLoading();
            showMessage(R.string.login_again);
            mViewModel.getDataManager().setCurrentUserId(AppConstants.NULL_INDEX);
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
    public void showUserDeletedMsg() {
        hideLoading();
        binding.text.setText(R.string.your_account_deleted);
        binding.okBtn.setVisibility(View.VISIBLE);
    }

    @Override
    public void showMyApiMessage(String message) {
        hideLoading();
        showErrorMessage(message);
    }

}
