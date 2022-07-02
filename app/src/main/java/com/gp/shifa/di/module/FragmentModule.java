package com.gp.shifa.di.module;

import androidx.core.util.Supplier;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.gp.shifa.ViewModelProviderFactory;
import com.gp.shifa.data.DataManager;
import com.gp.shifa.ui.base.BaseFragment;
import com.gp.shifa.ui.categories.CategoriesAdapter;
import com.gp.shifa.ui.categories.CategoriesViewModel;
import com.gp.shifa.ui.chats.ChatsAdapter;
import com.gp.shifa.ui.chats.ChatsViewModel;
import com.gp.shifa.ui.common.SliderAdapter;
import com.gp.shifa.ui.doctors.DoctorsAdapter;
import com.gp.shifa.ui.doctors.DoctorsViewModel;
import com.gp.shifa.ui.favorites.FavoritesViewModel;
import com.gp.shifa.ui.home.HomeViewModel;
import com.gp.shifa.ui.home.adapters.CategoriesHomeAdapter;
import com.gp.shifa.ui.home.adapters.DoctorsHomeAdapter;
import com.gp.shifa.ui.settings.SettingsViewModel;
import com.gp.shifa.ui.user.profile.ProfileViewModel;
import com.gp.shifa.utils.rx.SchedulerProvider;

import java.util.ArrayList;

import dagger.Module;
import dagger.Provides;

@Module
public class FragmentModule {

    private final BaseFragment<?> fragment;

    public FragmentModule(BaseFragment<?> fragment) {
        this.fragment = fragment;
    }

    @Provides
    LinearLayoutManager provideLinearLayoutManager() {
        return new LinearLayoutManager(fragment.getActivity());
    }

    @Provides
    CategoriesHomeAdapter provideCategoriesHomeAdapter() {
        return new CategoriesHomeAdapter(new ArrayList<>());
    }

    @Provides
    CategoriesAdapter provideCategoriesAdapter() {
        return new CategoriesAdapter(new ArrayList<>());
    }

    @Provides
    DoctorsAdapter provideDoctorsAdapter() {
        return new DoctorsAdapter(new ArrayList<>());
    }

    @Provides
    SliderAdapter provideSliderAdapter() {
        return new SliderAdapter(new ArrayList<>());
    }

    @Provides
    ChatsAdapter provideChatsAdapter() {
        return new ChatsAdapter(new ArrayList<>());
    }

    @Provides
    DoctorsHomeAdapter provideMapAdsAdapter() {
        return new DoctorsHomeAdapter(new ArrayList<>());
    }

    @Provides
    HomeViewModel provideHomeViewModel(DataManager dataManager, SchedulerProvider schedulerProvider) {
        Supplier<HomeViewModel> supplier = () -> new HomeViewModel(dataManager, schedulerProvider);
        ViewModelProviderFactory<HomeViewModel> factory = new ViewModelProviderFactory<>(HomeViewModel.class, supplier);
        return new ViewModelProvider(fragment, factory).get(HomeViewModel.class);
    }

    @Provides
    ProfileViewModel provideProfileViewModel(DataManager dataManager, SchedulerProvider schedulerProvider) {
        Supplier<ProfileViewModel> supplier = () -> new ProfileViewModel(dataManager, schedulerProvider);
        ViewModelProviderFactory<ProfileViewModel> factory = new ViewModelProviderFactory<>(ProfileViewModel.class, supplier);
        return new ViewModelProvider(fragment, factory).get(ProfileViewModel.class);
    }

    @Provides
    FavoritesViewModel provideFavoriteLocationsViewModel(DataManager dataManager, SchedulerProvider schedulerProvider) {
        Supplier<FavoritesViewModel> supplier = () -> new FavoritesViewModel(dataManager, schedulerProvider);
        ViewModelProviderFactory<FavoritesViewModel> factory = new ViewModelProviderFactory<>(FavoritesViewModel.class, supplier);
        return new ViewModelProvider(fragment, factory).get(FavoritesViewModel.class);
    }

    @Provides
    DoctorsViewModel provideDoctorsViewModel(DataManager dataManager, SchedulerProvider schedulerProvider) {
        Supplier<DoctorsViewModel> supplier = () -> new DoctorsViewModel(dataManager, schedulerProvider);
        ViewModelProviderFactory<DoctorsViewModel> factory = new ViewModelProviderFactory<>(DoctorsViewModel.class, supplier);
        return new ViewModelProvider(fragment, factory).get(DoctorsViewModel.class);
    }

    @Provides
    SettingsViewModel provideAboutViewModel(DataManager dataManager, SchedulerProvider schedulerProvider) {
        Supplier<SettingsViewModel> supplier = () -> new SettingsViewModel(dataManager, schedulerProvider);
        ViewModelProviderFactory<SettingsViewModel> factory = new ViewModelProviderFactory<>(SettingsViewModel.class, supplier);
        return new ViewModelProvider(fragment, factory).get(SettingsViewModel.class);
    }

    @Provides
    CategoriesViewModel provideAreasViewModel(DataManager dataManager, SchedulerProvider schedulerProvider) {
        Supplier<CategoriesViewModel> supplier = () -> new CategoriesViewModel(dataManager, schedulerProvider);
        ViewModelProviderFactory<CategoriesViewModel> factory = new ViewModelProviderFactory<>(CategoriesViewModel.class, supplier);
        return new ViewModelProvider(fragment, factory).get(CategoriesViewModel.class);
    }

    @Provides
    ChatsViewModel provideChatsViewModel(DataManager dataManager, SchedulerProvider schedulerProvider) {
        Supplier<ChatsViewModel> supplier = () -> new ChatsViewModel(dataManager, schedulerProvider);
        ViewModelProviderFactory<ChatsViewModel> factory = new ViewModelProviderFactory<>(ChatsViewModel.class, supplier);
        return new ViewModelProvider(fragment, factory).get(ChatsViewModel.class);
    }

}
