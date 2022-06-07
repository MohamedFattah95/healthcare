
package com.gp.shifa.ui.about;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;

import com.bumptech.glide.Glide;
import com.gp.shifa.R;
import com.gp.shifa.databinding.ActivityAboutBinding;
import com.gp.shifa.di.component.ActivityComponent;
import com.gp.shifa.ui.base.BaseActivity;
import com.gp.shifa.utils.ErrorHandlingUtils;

public class AboutActivity extends BaseActivity<AboutViewModel> implements AboutNavigator {

    ActivityAboutBinding binding;

    public static Intent newIntent(Context context) {
        return new Intent(context, AboutActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAboutBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mViewModel.setNavigator(this);

        binding.toolbar.toolbarTitle.setText(R.string.about);
        binding.toolbar.backButton.setOnClickListener(v -> finish());

        if (mViewModel.getDataManager().getSettingsObject() != null) {

            if (mViewModel.getDataManager().getSettingsObject().getAboutUs() != null && mViewModel.getDataManager().getSettingsObject().getAboutUs().contains("</")) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    binding.tvAbout.setText(Html.fromHtml(mViewModel.getDataManager().getSettingsObject().getAboutUs(), Html.FROM_HTML_MODE_COMPACT));
                } else {
                    binding.tvAbout.setText(Html.fromHtml(mViewModel.getDataManager().getSettingsObject().getAboutUs()));
                }
            } else if (mViewModel.getDataManager().getSettingsObject().getAboutUs() != null) {
                binding.tvAbout.setText(mViewModel.getDataManager().getSettingsObject().getAboutUs());
            }

            Glide.with(this)
                    .load(mViewModel.getDataManager().getSettingsObject().getAboutUsImage())
                    .into(binding.ivAbout);

        }

        subscribeViewModel();

    }

    private void subscribeViewModel() {

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
