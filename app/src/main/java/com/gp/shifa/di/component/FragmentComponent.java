package com.gp.shifa.di.component;

import com.gp.shifa.di.module.FragmentModule;
import com.gp.shifa.di.scope.FragmentScope;
import com.gp.shifa.ui.add_commercial.AddCommercialFragment;
import com.gp.shifa.ui.categories.CategoriesFragment;
import com.gp.shifa.ui.chats.ChatsFragment;
import com.gp.shifa.ui.commission.CommissionFragment;
import com.gp.shifa.ui.doctors.DoctorsFragment;
import com.gp.shifa.ui.favorites.FavoritesFragment;
import com.gp.shifa.ui.home.HomeFragment;
import com.gp.shifa.ui.member_profile.member_ads.MemberAdsFragment;
import com.gp.shifa.ui.member_profile.member_ratings.MemberRatingsFragment;
import com.gp.shifa.ui.notifications.NotificationsFragment;
import com.gp.shifa.ui.settings.SettingsFragment;
import com.gp.shifa.ui.user.profile.ProfileFragment;
import com.gp.shifa.ui.user.profile.blocked.BlockedFragment;
import com.gp.shifa.ui.user.profile.follows.FollowsFragment;
import com.gp.shifa.ui.user.profile.my_ads.MyAdsFragment;
import com.gp.shifa.ui.user.profile.my_orders.MyOrdersFragment;
import com.gp.shifa.ui.user.profile.my_ratings.MyRatingsFragment;

import dagger.Component;

@FragmentScope
@Component(modules = FragmentModule.class, dependencies = AppComponent.class)
public interface FragmentComponent {
    void inject(HomeFragment fragment);

    void inject(CommissionFragment fragment);

    void inject(FavoritesFragment fragment);

    void inject(ProfileFragment fragment);

    void inject(NotificationsFragment fragment);

    void inject(AddCommercialFragment fragment);

    void inject(CategoriesFragment fragment);

    void inject(ChatsFragment fragment);

    void inject(MyAdsFragment fragment);

    void inject(MyOrdersFragment fragment);

    void inject(MyRatingsFragment fragment);

    void inject(FollowsFragment fragment);

    void inject(BlockedFragment fragment);

    void inject(MemberAdsFragment fragment);

    void inject(MemberRatingsFragment fragment);

    void inject(SettingsFragment fragment);

    void inject(DoctorsFragment fragment);
}
