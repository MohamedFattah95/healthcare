package com.gp.shifa.di.component;

import com.gp.shifa.di.module.FragmentModule;
import com.gp.shifa.di.scope.FragmentScope;
import com.gp.shifa.ui.categories.CategoriesFragment;
import com.gp.shifa.ui.chats.ChatsFragment;
import com.gp.shifa.ui.doctors.DoctorsFragment;
import com.gp.shifa.ui.favorites.FavoritesFragment;
import com.gp.shifa.ui.home.HomeFragment;
import com.gp.shifa.ui.settings.SettingsFragment;
import com.gp.shifa.ui.user.profile.ProfileFragment;

import dagger.Component;

@FragmentScope
@Component(modules = FragmentModule.class, dependencies = AppComponent.class)
public interface FragmentComponent {
    void inject(HomeFragment fragment);

    void inject(FavoritesFragment fragment);

    void inject(ProfileFragment fragment);

    void inject(CategoriesFragment fragment);

    void inject(ChatsFragment fragment);

    void inject(SettingsFragment fragment);

    void inject(DoctorsFragment fragment);
}
