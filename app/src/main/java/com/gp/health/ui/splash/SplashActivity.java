package com.gp.health.ui.splash;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.messaging.FirebaseMessaging;
import com.gp.health.databinding.ActivitySplashBinding;
import com.gp.health.di.component.ActivityComponent;
import com.gp.health.ui.base.BaseActivity;
import com.gp.health.ui.intro.IntroActivity;
import com.gp.health.ui.main.MainActivity;
import com.gp.health.ui.member_profile.MemberProfileActivity;
import com.gp.health.ui.profile.MyProfileActivity;
import com.gp.health.ui.property_details.PropertyDetailsActivity;
import com.gp.health.ui.select_language.SelectLanguageActivity;

import java.util.Timer;
import java.util.TimerTask;

public class SplashActivity extends BaseActivity<SplashViewModel> implements SplashNavigator {

    @Override
    public void openMainActivity() {
        startActivity(MainActivity.newIntent(SplashActivity.this));
        finish();
    }

    @Override
    public void openIntroActivity() {
        startActivity(IntroActivity.newIntent(SplashActivity.this));
        finish();
    }

    @Override
    public void openLanguageActivity() {
        startActivity(SelectLanguageActivity.newIntent(SplashActivity.this));
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivitySplashBinding binding = ActivitySplashBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        mViewModel.setNavigator(this);

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                handleDeepLinks();
            }
        }, 500);

        if (mViewModel != null && mViewModel.getDataManager().isUserLogged()) {

            if (mViewModel.getDataManager().getUserObject().getUserTypeId() == 2)
                FirebaseMessaging.getInstance().subscribeToTopic("seekers");
            else
                FirebaseMessaging.getInstance().unsubscribeFromTopic("seekers");

            if (mViewModel.getDataManager().getUserObject().getUserTypeId() == 3)
                FirebaseMessaging.getInstance().subscribeToTopic("woners");
            else
                FirebaseMessaging.getInstance().unsubscribeFromTopic("woners");

            if (mViewModel.getDataManager().getUserObject().getUserTypeId() == 4)
                FirebaseMessaging.getInstance().subscribeToTopic("marketings");
            else
                FirebaseMessaging.getInstance().unsubscribeFromTopic("marketings");

            if (mViewModel.getDataManager().getUserObject().getUserTypeId() == 5)
                FirebaseMessaging.getInstance().subscribeToTopic("companies");
            else
                FirebaseMessaging.getInstance().unsubscribeFromTopic("companies");

        }


    }

    @Override
    public void performDependencyInjection(ActivityComponent buildComponent) {
        buildComponent.inject(this);
    }

    @SuppressLint("LogNotTimber")
    private void handleDeepLinks() {
        FirebaseDynamicLinks.getInstance()
                .getDynamicLink(getIntent())
                .addOnSuccessListener(this, pendingDynamicLinkData -> {
                    // Get deep link from result (may be null if no link is found)
                    Uri deepLink;
                    if (pendingDynamicLinkData != null) {
                        deepLink = pendingDynamicLinkData.getLink();
                        Log.e("LINK", "onSuccess: " + deepLink);
                        if (deepLink != null) {
                            if (deepLink.getQueryParameter("item_id") != null) {

                                startActivity(PropertyDetailsActivity.newIntent(SplashActivity.this)
                                        .putExtra("itemId", Integer.valueOf(deepLink.getQueryParameter("item_id"))));
                                finish();

                            } else if (deepLink.getQueryParameter("member_id") != null) {

                                if (mViewModel.getDataManager().isUserLogged() &&
                                        mViewModel.getDataManager().getUserObject().getId() ==
                                                Integer.parseInt(deepLink.getQueryParameter("member_id"))) {

                                    startActivity(MyProfileActivity.newIntent(SplashActivity.this));

                                } else {
                                    startActivity(MemberProfileActivity.newIntent(SplashActivity.this)
                                            .putExtra("userId", Integer.parseInt(deepLink.getQueryParameter("member_id"))));
                                }
                                finish();

                            }
                        }


                    } else {
                        mViewModel.decideNextActivity();
                    }
                })
                .addOnFailureListener(this, e -> {
                    Log.e("TAG", "getDynamicLink:onFailure: " + e.getCause());
                    mViewModel.decideNextActivity();
                });
    }

}
