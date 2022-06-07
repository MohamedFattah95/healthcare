
package com.gp.shifa.ui.select_language;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.gp.shifa.databinding.ActivitySelectLanguageBinding;
import com.gp.shifa.di.component.ActivityComponent;
import com.gp.shifa.ui.base.BaseActivity;
import com.gp.shifa.ui.intro.IntroActivity;
import com.gp.shifa.utils.LanguageHelper;

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
