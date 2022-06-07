package com.gp.shifa.ui.intro;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.gp.shifa.R;
import com.gp.shifa.data.models.IntroModel;
import com.gp.shifa.databinding.ActivityIntroBinding;
import com.gp.shifa.di.component.ActivityComponent;
import com.gp.shifa.ui.base.BaseActivity;
import com.gp.shifa.ui.user.login.LoginActivity;

import java.util.ArrayList;
import java.util.List;

public class IntroActivity extends BaseActivity<IntroViewModel> implements IntroNavigator {

    public static List<IntroModel> introModels = new ArrayList<>();
    static IntroPagerAdapter introPagerAdapter;

    private ActivityIntroBinding binding;

    public static Intent newIntent(Context context) {
        return new Intent(context, IntroActivity.class);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityIntroBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        mViewModel.setNavigator(this);
        setUp();
    }

    @Override
    public void performDependencyInjection(ActivityComponent buildComponent) {
        buildComponent.inject(this);
    }

    private void setUp() {

        subscribeViewModel();
        showLoading();
        mViewModel.getHowToUse();
//        mViewModel.getSettings();

        if (mViewModel.getDataManager().isUserLogged()) {
            binding.tvSkip.setText(R.string.back);
        }

        binding.tvNext.setOnClickListener(v -> {
            if (!introModels.isEmpty() && binding.introViewPager.getCurrentItem() == introModels.size() - 1)
                onSkipClicked();
            else
                binding.introViewPager.setCurrentItem(binding.introViewPager.getCurrentItem() + 1, true);
        });

        binding.tvSkip.setOnClickListener(v -> onSkipClicked());

    }

    private void subscribeViewModel() {

        mViewModel.getIntroLiveData().observe(this, response -> {
            hideLoading();
            if (response.getData() != null && !response.getData().isEmpty()) {
                introModels = response.getData();
                introPagerAdapter = new IntroPagerAdapter(
                        this
                        , introModels);
                introPagerAdapter.notifyDataSetChanged();
                binding.introViewPager.setAdapter(introPagerAdapter);
                binding.pagerIndicator.setViewPager2(binding.introViewPager);
            } else {
                onSkipClicked();
            }
        });


    }

    public void onSkipClicked() {
        finish();
        if (!mViewModel.getDataManager().isUserLogged())
            startActivity(LoginActivity.newIntent(IntroActivity.this));

    }

    @Override
    public void handleError(Throwable throwable) {
        hideLoading();
        onSkipClicked();
//        ErrorHandlingUtils.handleErrors(throwable);
    }

    @Override
    public void showMyApiMessage(String message) {
        hideLoading();
        onSkipClicked();
//        showErrorMessage(message);
    }

}
