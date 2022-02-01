
package com.gp.health.ui.adding_ad.add_ad_terms;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;

import com.gp.health.R;
import com.gp.health.databinding.ActivityAddAdTermsBinding;
import com.gp.health.di.component.ActivityComponent;
import com.gp.health.ui.adding_ad.select_ad_category.SelectAdCategoryActivity;
import com.gp.health.ui.base.BaseActivity;
import com.gp.health.utils.ErrorHandlingUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class AddAdTermsActivity extends BaseActivity<AddAdTermsViewModel> implements AddAdTermsNavigator {

    ActivityAddAdTermsBinding binding;

    public static Intent newIntent(Context context) {
        return new Intent(context, AddAdTermsActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddAdTermsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mViewModel.setNavigator(this);
        EventBus.getDefault().register(this);

        binding.toolbar.toolbarTitle.setText(R.string.add_ad_terms);
        setUp();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void finishAd(String keyword) {
        if (keyword.equals("finish_ad"))
            finish();
    }

    private void setUp() {
        setUpOnViewClicked();
        subscribeViewModel();

        showLoading();
        mViewModel.getSettings();
    }

    private void subscribeViewModel() {

        mViewModel.getSettingsLiveData().observe(this, response -> {
            hideLoading();

            if (response.getData().getAddAdvCondition().contains("</")) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    binding.tvAddAdTerms.setText(Html.fromHtml(response.getData().getAddAdvCondition(), Html.FROM_HTML_MODE_COMPACT));
                } else {
                    binding.tvAddAdTerms.setText(Html.fromHtml(response.getData().getAddAdvCondition()));
                }
            } else if (response.getData().getAddAdvCondition() != null) {
                binding.tvAddAdTerms.setText(response.getData().getAddAdvCondition());
            }
        });

    }

    public void setUpOnViewClicked() {

        binding.toolbar.backButton.setOnClickListener(v -> finish());
        binding.btnAccept.setOnClickListener(v -> {
            startActivity(SelectAdCategoryActivity.newIntent(this));
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
