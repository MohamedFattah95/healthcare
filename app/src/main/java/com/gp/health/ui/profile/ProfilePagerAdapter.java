package com.gp.health.ui.profile;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.gp.health.ui.profile.blocked.BlockedFragment;
import com.gp.health.ui.profile.follows.FollowsFragment;
import com.gp.health.ui.profile.my_ads.MyAdsFragment;
import com.gp.health.ui.profile.my_orders.MyOrdersFragment;
import com.gp.health.ui.profile.my_ratings.MyRatingsFragment;


public class ProfilePagerAdapter extends FragmentStateAdapter {

    public ProfilePagerAdapter(FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return MyAdsFragment.newInstance(0);
            case 1:
                return MyOrdersFragment.newInstance(0);
            case 2:
                return MyRatingsFragment.newInstance(0);
            case 3:
                return FollowsFragment.newInstance(0);
            case 4:
                return BlockedFragment.newInstance(0);

            default:
                return null;
        }
    }

    @Override
    public int getItemCount() {
        return 5;
    }
}
