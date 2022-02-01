
package com.gp.health.ui.adding_ad.select_ad_category;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.gp.health.R;
import com.gp.health.data.models.CategoriesModel;
import com.gp.health.databinding.ActivitySelectAdCategoryBinding;
import com.gp.health.di.component.ActivityComponent;
import com.gp.health.ui.adding_ad.ad_fees_info.AdFeesInfoActivity;
import com.gp.health.ui.base.BaseActivity;
import com.gp.health.utils.ErrorHandlingUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import javax.inject.Inject;

public class SelectAdCategoryActivity extends BaseActivity<SelectAdCategoryViewModel> implements SelectAdCategoryNavigator, AdCategoriesAdapter.Callback {

    @Inject
    LinearLayoutManager adCategoriesLinearLayoutManager;
    @Inject
    AdCategoriesAdapter categoriesAdapter;

    ActivitySelectAdCategoryBinding binding;

    public static Intent newIntent(Context context) {
        return new Intent(context, SelectAdCategoryActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySelectAdCategoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mViewModel.setNavigator(this);
        categoriesAdapter.setCallback(this);
        EventBus.getDefault().register(this);

        binding.toolbar.toolbarTitle.setText(R.string.select_category);
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
        setUpAdCategoryAdapter();

        showLoading();
        mViewModel.getCategories();
    }

    private void setUpAdCategoryAdapter() {
        binding.rvAdCategories.setLayoutManager(adCategoriesLinearLayoutManager);
        binding.rvAdCategories.setAdapter(categoriesAdapter);
    }

    private void subscribeViewModel() {

        mViewModel.getCategoriesLiveData().observe(this, response -> {
            hideLoading();
            binding.swipeRefreshView.setRefreshing(false);
            categoriesAdapter.addItems(response.getData());
        });

    }

    public void setUpOnViewClicked() {

        binding.toolbar.backButton.setOnClickListener(v -> finish());

        binding.swipeRefreshView.setOnRefreshListener(() -> {
            binding.swipeRefreshView.setRefreshing(true);
            mViewModel.getCategories();
        });

    }

    @Override
    public void performDependencyInjection(ActivityComponent buildComponent) {
        buildComponent.inject(this);
    }

    @Override
    public void handleError(Throwable throwable) {
        hideLoading();
        binding.swipeRefreshView.setRefreshing(false);
        ErrorHandlingUtils.handleErrors(throwable);
    }

    @Override
    public void showMyApiMessage(String message) {
        hideLoading();
        binding.swipeRefreshView.setRefreshing(false);
        showErrorMessage(message);
    }

    @Override
    public void selectCategory(CategoriesModel category) {
        startActivity(AdFeesInfoActivity.newIntent(this).putExtra("categoryId", category.getId()));
    }
}
