
package com.gp.health.ui.privacy_policy;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;

import com.gp.health.R;
import com.gp.health.databinding.ActivityPrivacyPolicyBinding;
import com.gp.health.di.component.ActivityComponent;
import com.gp.health.ui.base.BaseActivity;
import com.gp.health.utils.ErrorHandlingUtils;

public class PrivacyPolicyActivity extends BaseActivity<PrivacyPolicyViewModel> implements PrivacyPolicyNavigator {

    ActivityPrivacyPolicyBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPrivacyPolicyBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mViewModel.setNavigator(this);

        setUp();

    }

    private void setUp() {

        binding.toolbar.toolbarTitle.setText(R.string.privacy_policy);
        binding.toolbar.backButton.setOnClickListener(v -> finish());
        subscribeViewModel();

        if (mViewModel.getDataManager().getSettingsObject() != null) {
            if (mViewModel.getDataManager().getSettingsObject().getPrivacy() != null && mViewModel.getDataManager().getSettingsObject().getPrivacy().contains("</")) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    binding.tvPrivacyPolicy.setText(Html.fromHtml(mViewModel.getDataManager().getSettingsObject().getPrivacy(), Html.FROM_HTML_MODE_COMPACT));
                } else {
                    binding.tvPrivacyPolicy.setText(Html.fromHtml(mViewModel.getDataManager().getSettingsObject().getPrivacy()));
                }
            } else if (mViewModel.getDataManager().getSettingsObject().getPrivacy() != null) {
                binding.tvPrivacyPolicy.setText(mViewModel.getDataManager().getSettingsObject().getPrivacy());
            }
        } else {
            showLoading();
            mViewModel.getSettings();
        }


    }

    private void subscribeViewModel() {
        mViewModel.getSettingsLiveData().observe(this, response -> {
            hideLoading();
            if (response.getData() != null && response.getData().getPrivacy() != null && response.getData().getPrivacy().contains("</")) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    binding.tvPrivacyPolicy.setText(Html.fromHtml(response.getData().getPrivacy(), Html.FROM_HTML_MODE_COMPACT));
                } else {
                    binding.tvPrivacyPolicy.setText(Html.fromHtml(response.getData().getPrivacy()));
                }
            } else if (response.getData() != null && response.getData().getPrivacy() != null) {
                binding.tvPrivacyPolicy.setText(response.getData().getPrivacy());
            }
        });
    }

    public static Intent newIntent(Context context) {
        return new Intent(context, PrivacyPolicyActivity.class);
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
