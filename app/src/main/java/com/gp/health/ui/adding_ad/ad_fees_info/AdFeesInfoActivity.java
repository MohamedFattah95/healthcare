
package com.gp.health.ui.adding_ad.ad_fees_info;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;

import com.gp.health.R;
import com.gp.health.databinding.ActivityAdFeesInfoBinding;
import com.gp.health.di.component.ActivityComponent;
import com.gp.health.ui.adding_ad.add_ad_images.AddAdImagesActivity;
import com.gp.health.ui.base.BaseActivity;
import com.gp.health.utils.ErrorHandlingUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class AdFeesInfoActivity extends BaseActivity<AdFeesInfoViewModel> implements AdFeesInfoNavigator {

    ActivityAdFeesInfoBinding binding;

    int selectedCategoryId = -1;

    public static Intent newIntent(Context context) {
        return new Intent(context, AdFeesInfoActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAdFeesInfoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mViewModel.setNavigator(this);
        EventBus.getDefault().register(this);

        binding.toolbar.toolbarTitle.setText(R.string.ad_fees);
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

        selectedCategoryId = getIntent().getIntExtra("categoryId", -1);

        showLoading();
        mViewModel.getSettings();
    }

    private void subscribeViewModel() {
        mViewModel.getSettingsLiveData().observe(this, response -> {
            hideLoading();

            if (response.getData().getAdvCommission().contains("</")) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    binding.tvAdFeesTitle.setText(Html.fromHtml(response.getData().getAdvCommission(), Html.FROM_HTML_MODE_COMPACT));
                } else {
                    binding.tvAdFeesTitle.setText(Html.fromHtml(response.getData().getAdvCommission()));
                }
            } else if (response.getData().getAdvCommission() != null) {
                binding.tvAdFeesTitle.setText(response.getData().getAdvCommission());
            }

            if (response.getData().getCommissionDesc().contains("</")) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    binding.tvAdFeesDetails.setText(Html.fromHtml(response.getData().getCommissionDesc(), Html.FROM_HTML_MODE_COMPACT));
                } else {
                    binding.tvAdFeesDetails.setText(Html.fromHtml(response.getData().getCommissionDesc()));
                }
            } else if (response.getData().getCommissionDesc() != null) {
                binding.tvAdFeesDetails.setText(response.getData().getCommissionDesc());
            }
        });
    }

    public void setUpOnViewClicked() {

        binding.toolbar.backButton.setOnClickListener(v -> finish());

        binding.btnAccept.setOnClickListener(v -> {
            startActivity(AddAdImagesActivity.newIntent(this).putExtra("categoryId", selectedCategoryId));
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
