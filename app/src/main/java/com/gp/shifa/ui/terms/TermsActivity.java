
package com.gp.shifa.ui.terms;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;

import com.gp.shifa.R;
import com.gp.shifa.databinding.ActivityTermsBinding;
import com.gp.shifa.di.component.ActivityComponent;
import com.gp.shifa.ui.base.BaseActivity;
import com.gp.shifa.utils.ErrorHandlingUtils;

public class TermsActivity extends BaseActivity<TermsViewModel> implements TermsNavigator {

    ActivityTermsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTermsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mViewModel.setNavigator(this);

        binding.toolbar.toolbarTitle.setText(R.string.terms);
        binding.toolbar.backButton.setOnClickListener(v -> finish());
        subscribeViewModel();

        if (mViewModel.getDataManager().getSettingsObject() != null) {
            if (mViewModel.getDataManager().getSettingsObject().getTerms() != null && mViewModel.getDataManager().getSettingsObject().getTerms().contains("</")) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    binding.tvTerms.setText(Html.fromHtml(mViewModel.getDataManager().getSettingsObject().getTerms(), Html.FROM_HTML_MODE_COMPACT));
                } else {
                    binding.tvTerms.setText(Html.fromHtml(mViewModel.getDataManager().getSettingsObject().getTerms()));
                }
            } else if (mViewModel.getDataManager().getSettingsObject().getTerms() != null) {
                binding.tvTerms.setText(mViewModel.getDataManager().getSettingsObject().getTerms());
            }
        } else {
            showLoading();
            mViewModel.getSettings();
        }

    }

    private void subscribeViewModel() {
        mViewModel.getSettingsLiveData().observe(this, response -> {
            hideLoading();
            if (response.getData() != null && response.getData().getTerms() != null && response.getData().getTerms().contains("</")) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    binding.tvTerms.setText(Html.fromHtml(response.getData().getTerms(), Html.FROM_HTML_MODE_COMPACT));
                } else {
                    binding.tvTerms.setText(Html.fromHtml(response.getData().getTerms()));
                }
            } else if (response.getData() != null && response.getData().getTerms() != null) {
                binding.tvTerms.setText(response.getData().getTerms());
            }
        });
    }

    public static Intent newIntent(Context context) {
        return new Intent(context, TermsActivity.class);
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
