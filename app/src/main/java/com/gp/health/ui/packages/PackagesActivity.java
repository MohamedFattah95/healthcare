package com.gp.health.ui.packages;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.gp.health.R;
import com.gp.health.databinding.ActivityPackagesBinding;
import com.gp.health.di.component.ActivityComponent;
import com.gp.health.ui.base.BaseActivity;
import com.gp.health.utils.ErrorHandlingUtils;

import javax.inject.Inject;

public class PackagesActivity extends BaseActivity<PackagesViewModel> implements PackagesNavigator, PackagesAdapter.Callback {

    @Inject
    LinearLayoutManager linearLayoutManager;
    @Inject
    PackagesAdapter packagesAdapter;

    ActivityPackagesBinding binding;

    public static Intent newIntent(Context context) {
        return new Intent(context, PackagesActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPackagesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mViewModel.setNavigator(this);
        packagesAdapter.setCallback(this);

        setUp();

    }

    private void setUp() {

        binding.toolbar.toolbarTitle.setText(R.string.ad_commercial_fees);
        binding.toolbar.backButton.setOnClickListener(v -> finish());

        subscribeViewModel();

        binding.rvPackages.setLayoutManager(linearLayoutManager);
        binding.rvPackages.setAdapter(packagesAdapter);

        binding.swipeRefreshView.setOnRefreshListener(() -> {
            showLoading();
            binding.swipeRefreshView.setRefreshing(true);
            mViewModel.getPackages();
        });

        showLoading();
        binding.swipeRefreshView.setRefreshing(true);
        mViewModel.getPackages();
    }

    private void subscribeViewModel() {
        mViewModel.getPackagesLiveData().observe(this, response -> {
            hideLoading();
            binding.swipeRefreshView.setRefreshing(false);
            packagesAdapter.addItems(response.getData());
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

}
