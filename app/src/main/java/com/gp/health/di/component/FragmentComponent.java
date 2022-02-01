package com.gp.health.di.component;

import com.gp.health.di.module.FragmentModule;
import com.gp.health.di.scope.FragmentScope;
import com.gp.health.ui.add_commercial.AddCommercialFragment;
import com.gp.health.ui.categories.CategoriesFragment;
import com.gp.health.ui.chats.ChatsFragment;
import com.gp.health.ui.commission.CommissionFragment;
import com.gp.health.ui.doctors.DoctorsFragment;
import com.gp.health.ui.favorites.FavoritesFragment;
import com.gp.health.ui.home.HomeFragment;
import com.gp.health.ui.member_profile.member_ads.MemberAdsFragment;
import com.gp.health.ui.member_profile.member_ratings.MemberRatingsFragment;
import com.gp.health.ui.notifications.NotificationsFragment;
import com.gp.health.ui.profile.ProfileFragment;
import com.gp.health.ui.profile.blocked.BlockedFragment;
import com.gp.health.ui.profile.follows.FollowsFragment;
import com.gp.health.ui.profile.my_ads.MyAdsFragment;
import com.gp.health.ui.profile.my_orders.MyOrdersFragment;
import com.gp.health.ui.profile.my_ratings.MyRatingsFragment;
import com.gp.health.ui.settings.SettingsFragment;

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
