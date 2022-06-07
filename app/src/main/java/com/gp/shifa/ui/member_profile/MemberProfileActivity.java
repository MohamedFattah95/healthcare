package com.gp.shifa.ui.member_profile;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.viewpager2.widget.ViewPager2;

import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.gp.shifa.R;
import com.gp.shifa.data.models.UserModel;
import com.gp.shifa.databinding.ActivityMemberProfileBinding;
import com.gp.shifa.databinding.DialogRatingBinding;
import com.gp.shifa.di.component.ActivityComponent;
import com.gp.shifa.ui.base.BaseActivity;
import com.gp.shifa.ui.chat.ChatActivity;
import com.gp.shifa.utils.CommonUtils;
import com.gp.shifa.utils.ErrorHandlingUtils;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Inject;

public class MemberProfileActivity extends BaseActivity<MemberProfileViewModel> implements MemberProfileNavigator {

    @Inject
    MemberProfilePagerAdapter memberProfilePagerAdapter;

    public ActivityMemberProfileBinding binding;

    UserModel memberModel;

    public static Intent newIntent(Context context) {
        return new Intent(context, MemberProfileActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMemberProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mViewModel.setNavigator(this);

        setUp();

    }

    private void setUp() {

        setUpOnViewClicked();
        subscribeViewModel();
        setUpMemberProfilePager();

        showLoading();
        mViewModel.getMemberDetails(getIntent().getIntExtra("userId", -1));

    }

    private void setUpMemberProfilePager() {
        String[] mTitles = new String[]{getString(R.string.member_ads), getString(R.string.member_ratings)};

        binding.memberProfileTabs.setTabData(mTitles);
        binding.vpMemberProfile.setAdapter(memberProfilePagerAdapter);
        binding.memberProfileTabs.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                binding.vpMemberProfile.setCurrentItem(position);
            }

            @Override
            public void onTabReselect(int position) {
            }
        });

        binding.vpMemberProfile.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);

            }

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                binding.memberProfileTabs.setCurrentTab(position);
                if (position == 1)
                    binding.fabAddRate.show();
                else
                    binding.fabAddRate.hide();

            }

            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
            }
        });
        binding.vpMemberProfile.setCurrentItem(0);
    }

    private void setUpOnViewClicked() {

        binding.toolbar.backButton.setOnClickListener(v -> finish());

        binding.tvBlock.setOnClickListener(v -> {
            if (mViewModel.getDataManager().isUserLogged()) {
                showLoading();
                mViewModel.blockUser(memberModel.getUser().getId());
            } else {
                CommonUtils.handleNotAuthenticated(this);
            }

        });

        binding.tvFollow.setOnClickListener(v -> {
            if (mViewModel.getDataManager().isUserLogged()) {
                showLoading();
                mViewModel.followOrUnFollowUser(memberModel.getUser().getId());
            } else {
                CommonUtils.handleNotAuthenticated(this);
            }

        });

        binding.tvChat.setOnClickListener(v -> {
            if (mViewModel.getDataManager().isUserLogged()) {

                startActivity(ChatActivity.newIntent(this)
                        .putExtra("receiver_id", memberModel.getUser().getId()));

            } else {
                CommonUtils.handleNotAuthenticated(this);
            }

        });

        binding.tvCall.setOnClickListener(v -> {
            if (mViewModel.getDataManager().isUserLogged()) {
                startActivity(new Intent(Intent.ACTION_DIAL).setData(Uri.parse("tel:" + memberModel.getUser().getPhone())));
            } else {
                CommonUtils.handleNotAuthenticated(this);
            }

        });

        binding.fabAddRate.setOnClickListener(v -> {
            if (mViewModel.getDataManager().isUserLogged())
                showRateMemberDialog();
            else
                CommonUtils.handleNotAuthenticated(this);
        });

    }

    @SuppressLint("SetTextI18n")
    private void subscribeViewModel() {

        mViewModel.getProfileLiveData().observe(this, response -> {
            hideLoading();

            memberModel = response.getData();

            binding.toolbar.toolbarTitle.setText(response.getData().getUser().getName());

            binding.tvUsername.setText(response.getData().getUser().getName());
            Glide.with(this)
                    .load(response.getData().getUser().getImgSrc()
                            + "/" + response.getData().getUser().getImg())
                    .placeholder(R.drawable.ic_user_holder)
                    .error(R.drawable.ic_user_holder)
                    .into(binding.userImageView);


            binding.tvQuoteDetails.setText(response.getData().getUser().getGander());

        });

        mViewModel.getBlockUserLiveData().observe(this, response -> {
            hideLoading();
            finish();
        });

        mViewModel.getFollowUserLiveData().observe(this, response -> {
            hideLoading();
            showSuccessMessage(response.getMessage());
        });

        mViewModel.getRateUserLiveData().observe(this, response -> {
            hideLoading();
            showSuccessMessage(response.getMessage());

            EventBus.getDefault().post("refresh_member_ratings");
        });

    }

    private void showRateMemberDialog() {
        DialogRatingBinding dialogRatingBinding = DialogRatingBinding.inflate(getLayoutInflater());
        MaterialDialog dialog = new MaterialDialog.Builder(this)
                .customView(dialogRatingBinding.getRoot(), true)
                .cancelable(true).build();

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        CommonUtils.setupRatingBar(dialogRatingBinding.ratingBar);

        dialogRatingBinding.shareBtn.setOnClickListener(v -> {

            if (dialogRatingBinding.msgBoxDialog.getText().toString().trim().isEmpty()) {
                dialogRatingBinding.msgBoxDialog.setError(getString(R.string.write_comment));
                dialogRatingBinding.msgBoxDialog.requestFocus();
                return;
            }
            dialogRatingBinding.msgBoxDialog.setError(null);

            showLoading();
            mViewModel.submitMemberRate(getIntent().getIntExtra("userId", -1),
                    dialogRatingBinding.msgBoxDialog.getText().toString().trim(), (int) dialogRatingBinding.ratingBar.getRating());

            dialog.dismiss();

        });

        dialog.show();
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

    @Override
    public void userIsBlocked(String message) {
        hideLoading();
        showErrorMessage(message);
        finish();
    }
}
