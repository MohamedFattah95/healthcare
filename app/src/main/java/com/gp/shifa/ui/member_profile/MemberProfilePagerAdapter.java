package com.gp.shifa.ui.member_profile;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.gp.shifa.ui.member_profile.member_ads.MemberAdsFragment;
import com.gp.shifa.ui.member_profile.member_ratings.MemberRatingsFragment;


public class MemberProfilePagerAdapter extends FragmentStateAdapter {

    public MemberProfilePagerAdapter(FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return MemberAdsFragment.newInstance(0);
            case 1:
                return MemberRatingsFragment.newInstance(0);

            default:
                return null;
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
