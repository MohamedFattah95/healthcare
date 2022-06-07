package com.gp.shifa.ui.settings;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.afollestad.materialdialogs.MaterialDialog;
import com.gp.shifa.R;
import com.gp.shifa.databinding.DialogSelectLanguageBinding;
import com.gp.shifa.databinding.FragmentSettingsBinding;
import com.gp.shifa.di.component.FragmentComponent;
import com.gp.shifa.ui.about.AboutActivity;
import com.gp.shifa.ui.base.BaseFragment;
import com.gp.shifa.ui.contact_us.ContactUsActivity;
import com.gp.shifa.ui.faqs.FAQsActivity;
import com.gp.shifa.ui.intro.IntroActivity;
import com.gp.shifa.ui.main.MainActivity;
import com.gp.shifa.ui.privacy_policy.PrivacyPolicyActivity;
import com.gp.shifa.ui.terms.TermsActivity;
import com.gp.shifa.utils.AppUtils;
import com.gp.shifa.utils.LanguageHelper;


public class SettingsFragment extends BaseFragment<SettingsViewModel> implements SettingsNavigator {

    public static final String TAG = "SettingsFragment";

    FragmentSettingsBinding binding;

    public static SettingsFragment newInstance(int instance) {
        Bundle args = new Bundle();
        args.putInt(BaseFragment.ARGS_INSTANCE, instance);
        SettingsFragment fragment = new SettingsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel.setNavigator(this);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSettingsBinding.inflate(inflater, container, false);


        setUp();

        return binding.getRoot();
    }

    @SuppressLint("SetTextI18n")
    private void setUp() {

        binding.tvLang.setText(getString(R.string.the_language) + "   [" +
                (LanguageHelper.getLanguage(getActivity()).equalsIgnoreCase("ar") ?
                        getString(R.string.arabic) : getString(R.string.english)) + "]");

        binding.swNotificationSounds.setChecked(mViewModel.getDataManager().getNotificationsSound() == 1);

        setUpOnViewClicked();

    }

    private void setUpOnViewClicked() {

        binding.swNotificationSounds.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (buttonView.isPressed()) {
                mViewModel.getDataManager().setNotificationsSound(isChecked ? 1 : 0);
            }
        });

        binding.tvPrivacyPolicy.setOnClickListener(v -> startActivity(PrivacyPolicyActivity.newIntent(getActivity())));

        binding.tvTerms.setOnClickListener(v -> startActivity(TermsActivity.newIntent(getActivity())));

        binding.tvAbout.setOnClickListener(v -> startActivity(AboutActivity.newIntent(getActivity())));

        binding.tvFaq.setOnClickListener(v -> startActivity(FAQsActivity.newIntent(getActivity())));

        binding.tvContactUs.setOnClickListener(v -> startActivity(ContactUsActivity.newIntent(getActivity())));

        binding.tvLang.setOnClickListener(v -> showLanguageDialog());

        binding.tvRateUs.setOnClickListener(v -> AppUtils.openPlayStoreForApp(getActivity()));

        binding.tvUsingApp.setOnClickListener(v -> startActivity(IntroActivity.newIntent(getActivity())));

        binding.tvShareApp.setOnClickListener(v -> shareApp());

    }

    private void showLanguageDialog() {
        DialogSelectLanguageBinding dialogSelectLanguageBinding = DialogSelectLanguageBinding.inflate(getLayoutInflater());
        MaterialDialog dialog = new MaterialDialog.Builder(getActivity())
                .customView(dialogSelectLanguageBinding.getRoot(), true)
                .cancelable(true).build();

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        if (LanguageHelper.getLanguage(getActivity()).equalsIgnoreCase("ar")) {
            dialogSelectLanguageBinding.rbArabic.setChecked(true);
        } else {
            dialogSelectLanguageBinding.rbEnglish.setChecked(true);
        }


        dialogSelectLanguageBinding.rbArabic.setOnClickListener(v -> {

            if (LanguageHelper.getLanguage(getActivity()).equalsIgnoreCase("ar")) {
                dialog.dismiss();
                return;
            }

            dialog.dismiss();
            LanguageHelper.setLanguage(getActivity(), "ar");
//            if (mViewModel.getDataManager().isUserLogged()) {
//                showLoading();
//                mViewModel.updateServerLanguage(mViewModel.getDataManager().getCurrentUserId(), LanguageHelper.getLanguage(this));
//            } else {
            startActivity(((MainActivity) getActivity()).getIntentWithClearHistory(MainActivity.class));


        });

        dialogSelectLanguageBinding.rbEnglish.setOnClickListener(v -> {

            if (LanguageHelper.getLanguage(getActivity()).equalsIgnoreCase("en")) {
                dialog.dismiss();
                return;
            }

            dialog.dismiss();
            LanguageHelper.setLanguage(getActivity(), "en");
//            if (mViewModel.getDataManager().isUserLogged()) {
//                showLoading();
//                mViewModel.updateServerLanguage(mViewModel.getDataManager().getCurrentUserId(), LanguageHelper.getLanguage(this));
//            } else {
            startActivity(((MainActivity) getActivity()).getIntentWithClearHistory(MainActivity.class));

            dialog.dismiss();

        });

        dialog.show();
    }

    @Override
    public void performDependencyInjection(FragmentComponent buildComponent) {
        buildComponent.inject(this);
    }

    public void shareApp() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name));
        String appName = mViewModel.getDataManager().getSettingsObject().getAppTitle();
        String shareMessage = mViewModel.getDataManager().getSettingsObject().getAppShareNote();
        String androidLink = mViewModel.getDataManager().getSettingsObject().getAppAndroidLnk();
        String iosLink = mViewModel.getDataManager().getSettingsObject().getAppIosLink();
        String shareBody = appName + "\n" + shareMessage + "\n" + getString(R.string.for_android) + ":\n" + androidLink + "\n" + getString(R.string.for_ios) + ":\n" + iosLink;
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
        this.startActivity(Intent.createChooser(shareIntent, getString(R.string.share_via)));
    }

    public void refreshData() {

    }
}
