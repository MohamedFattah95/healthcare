
package com.gp.health.ui.select_language;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.gp.health.databinding.ActivitySelectLanguageBinding;
import com.gp.health.di.component.ActivityComponent;
import com.gp.health.ui.base.BaseActivity;
import com.gp.health.ui.intro.IntroActivity;
import com.gp.health.utils.LanguageHelper;

public class SelectLanguageActivity extends BaseActivity<SelectLanguageViewModel> implements SelectLanguageNavigator {

    public static Intent newIntent(Context context) {
        return new Intent(context, SelectLanguageActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivitySelectLanguageBinding binding = ActivitySelectLanguageBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        mViewModel.setNavigator(this);

        binding.rbArabic.setOnClickListener(v -> {
            LanguageHelper.setLanguage(this, "ar");
            startActivity(IntroActivity.newIntent(SelectLanguageActivity.this));
        });

        binding.rbEnglish.setOnClickListener(v -> {
            LanguageHelper.setLanguage(this, "en");
            startActivity(IntroActivity.newIntent(SelectLanguageActivity.this));
        });

    }

    @Override
    public void performDependencyInjection(ActivityComponent buildComponent) {
        buildComponent.inject(this);
    }

}
