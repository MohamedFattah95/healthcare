package com.gp.health.ui.faqs;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.gp.health.R;
import com.gp.health.databinding.ActivityFaqsBinding;
import com.gp.health.di.component.ActivityComponent;
import com.gp.health.ui.base.BaseActivity;
import com.gp.health.utils.ErrorHandlingUtils;

import javax.inject.Inject;

public class FAQsActivity extends BaseActivity<FAQsViewModel> implements FAQsNavigator, FAQsAdapter.Callback {

    @Inject
    LinearLayoutManager linearLayoutManager;
    @Inject
    FAQsAdapter faqsAdapter;

    ActivityFaqsBinding binding;

    public static Intent newIntent(Context context) {
        return new Intent(context, FAQsActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFaqsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mViewModel.setNavigator(this);
        faqsAdapter.setCallback(this);

        setUp();

    }

    private void setUp() {

        binding.toolbar.toolbarTitle.setText(R.string.faqs);
        binding.toolbar.backButton.setOnClickListener(v -> finish());

        subscribeViewModel();

        binding.rvFaq.setLayoutManager(linearLayoutManager);
        binding.rvFaq.setAdapter(faqsAdapter);

        binding.swipeFaq.setOnRefreshListener(() -> {
            showLoading();
            binding.swipeFaq.setRefreshing(true);
            mViewModel.getFAQs();
        });

        showLoading();
        binding.swipeFaq.setRefreshing(true);
        mViewModel.getFAQs();
    }

    private void subscribeViewModel() {
        mViewModel.getFaqsLiveData().observe(this, response -> {
            hideLoading();
            binding.swipeFaq.setRefreshing(false);
            faqsAdapter.addItems(response.getData());
        });
    }

    @Override
    public void performDependencyInjection(ActivityComponent buildComponent) {
        buildComponent.inject(this);
    }

    @Override
    public void handleError(Throwable throwable) {
        hideLoading();
        binding.swipeFaq.setRefreshing(false);
        ErrorHandlingUtils.handleErrors(throwable);
    }

    @Override
    public void showMyApiMessage(String message) {
        hideLoading();
        binding.swipeFaq.setRefreshing(false);
        showErrorMessage(message);
    }

}
