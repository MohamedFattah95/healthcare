package com.gp.health.di.module;

import androidx.core.util.Supplier;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.gp.health.ViewModelProviderFactory;
import com.gp.health.data.DataManager;
import com.gp.health.ui.add_commercial.AddCommercialViewModel;
import com.gp.health.ui.categories.CategoriesAdapter;
import com.gp.health.ui.categories.CategoriesViewModel;
import com.gp.health.ui.base.BaseFragment;
import com.gp.health.ui.chats.ChatsAdapter;
import com.gp.health.ui.chats.ChatsViewModel;
import com.gp.health.ui.commission.BanksAdapter;
import com.gp.health.ui.commission.CommissionViewModel;
import com.gp.health.ui.common.SliderAdapter;
import com.gp.health.ui.contact_us.ContactUsViewModel;
import com.gp.health.ui.doctors.DoctorsAdapter;
import com.gp.health.ui.doctors.DoctorsViewModel;
import com.gp.health.ui.favorites.FavoritesAdapter;
import com.gp.health.ui.favorites.FavoritesViewModel;
import com.gp.health.ui.home.HomeViewModel;
import com.gp.health.ui.home.adapters.AreaReviewsAdapter;
import com.gp.health.ui.home.adapters.CategoriesHomeAdapter;
import com.gp.health.ui.home.adapters.CityServiceRateAdapter;
import com.gp.health.ui.home.adapters.CityServicesAdapter;
import com.gp.health.ui.home.adapters.DoctorsHomeAdapter;
import com.gp.health.ui.home.adapters.ListAdsAdapter;
import com.gp.health.ui.member_profile.member_ads.MemberAdsAdapter;
import com.gp.health.ui.member_profile.member_ads.MemberAdsViewModel;
import com.gp.health.ui.member_profile.member_ratings.MemberRatingsViewModel;
import com.gp.health.ui.notifications.NotificationsAdapter;
import com.gp.health.ui.notifications.NotificationsViewModel;
import com.gp.health.ui.privacy_policy.PrivacyPolicyViewModel;
import com.gp.health.ui.profile.ProfilePagerAdapter;
import com.gp.health.ui.profile.ProfileViewModel;
import com.gp.health.ui.profile.blocked.BlockedAdapter;
import com.gp.health.ui.profile.blocked.BlockedViewModel;
import com.gp.health.ui.profile.follows.FollowsAdapter;
import com.gp.health.ui.profile.follows.FollowsViewModel;
import com.gp.health.ui.profile.my_ads.MyAdsAdapter;
import com.gp.health.ui.profile.my_ads.MyAdsViewModel;
import com.gp.health.ui.profile.my_orders.MyOrdersAdapter;
import com.gp.health.ui.profile.my_orders.MyOrdersViewModel;
import com.gp.health.ui.profile.my_ratings.MyRatingsAdapter;
import com.gp.health.ui.profile.my_ratings.MyRatingsViewModel;
import com.gp.health.ui.settings.SettingsViewModel;
import com.gp.health.ui.terms.TermsViewModel;
import com.gp.health.utils.rx.SchedulerProvider;

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
    ProfilePagerAdapter provideProfilePagerAdapter() {
        return new ProfilePagerAdapter(fragment.getActivity());
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
    AreaReviewsAdapter provideAreaReviewsAdapter() {
        return new AreaReviewsAdapter(new ArrayList<>());
    }

    @Provides
    ChatsAdapter provideChatsAdapter() {
        return new ChatsAdapter(new ArrayList<>());
    }

    @Provides
    MyAdsAdapter provideMyAdsAdapter() {
        return new MyAdsAdapter(new ArrayList<>());
    }

    @Provides
    MemberAdsAdapter provideMemberAdsAdapter() {
        return new MemberAdsAdapter(new ArrayList<>());
    }

    @Provides
    MyOrdersAdapter provideMyOrdersAdapter() {
        return new MyOrdersAdapter(new ArrayList<>());
    }

    @Provides
    MyRatingsAdapter provideMyRatingsAdapter() {
        return new MyRatingsAdapter(new ArrayList<>());
    }

    @Provides
    FollowsAdapter provideFollowsAdapter() {
        return new FollowsAdapter(new ArrayList<>());
    }

    @Provides
    BlockedAdapter provideBlockedAdapter() {
        return new BlockedAdapter(new ArrayList<>());
    }

    @Provides
    BanksAdapter provideBanksAdapter() {
        return new BanksAdapter(new ArrayList<>());
    }

    @Provides
    FavoritesAdapter provideFavoritesAdapter() {
        return new FavoritesAdapter(new ArrayList<>());
    }

    @Provides
    DoctorsHomeAdapter provideMapAdsAdapter() {
        return new DoctorsHomeAdapter(new ArrayList<>());
    }

    @Provides
    ListAdsAdapter provideListAdsAdapter() {
        return new ListAdsAdapter(new ArrayList<>());
    }

    @Provides
    NotificationsAdapter provideNotificationsAdapter() {
        return new NotificationsAdapter(new ArrayList<>());
    }

    @Provides
    CityServicesAdapter provideCityServicesAdapter() {
        return new CityServicesAdapter(new ArrayList<>());
    }

    @Provides
    CityServiceRateAdapter provideCityServiceRateAdapter() {
        return new CityServiceRateAdapter(new ArrayList<>());
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
    CommissionViewModel providCommissionViewModel(DataManager dataManager, SchedulerProvider schedulerProvider) {
        Supplier<CommissionViewModel> supplier = () -> new CommissionViewModel(dataManager, schedulerProvider);
        ViewModelProviderFactory<CommissionViewModel> factory = new ViewModelProviderFactory<>(CommissionViewModel.class, supplier);
        return new ViewModelProvider(fragment, factory).get(CommissionViewModel.class);
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
    TermsViewModel provideTermsViewModel(DataManager dataManager, SchedulerProvider schedulerProvider) {
        Supplier<TermsViewModel> supplier = () -> new TermsViewModel(dataManager, schedulerProvider);
        ViewModelProviderFactory<TermsViewModel> factory = new ViewModelProviderFactory<>(TermsViewModel.class, supplier);
        return new ViewModelProvider(fragment, factory).get(TermsViewModel.class);
    }

    @Provides
    SettingsViewModel provideAboutViewModel(DataManager dataManager, SchedulerProvider schedulerProvider) {
        Supplier<SettingsViewModel> supplier = () -> new SettingsViewModel(dataManager, schedulerProvider);
        ViewModelProviderFactory<SettingsViewModel> factory = new ViewModelProviderFactory<>(SettingsViewModel.class, supplier);
        return new ViewModelProvider(fragment, factory).get(SettingsViewModel.class);
    }

    @Provides
    ContactUsViewModel provideContactUsViewModel(DataManager dataManager, SchedulerProvider schedulerProvider) {
        Supplier<ContactUsViewModel> supplier = () -> new ContactUsViewModel(dataManager, schedulerProvider);
        ViewModelProviderFactory<ContactUsViewModel> factory = new ViewModelProviderFactory<>(ContactUsViewModel.class, supplier);
        return new ViewModelProvider(fragment, factory).get(ContactUsViewModel.class);
    }

    @Provides
    PrivacyPolicyViewModel providePrivacyPolicyViewModel(DataManager dataManager, SchedulerProvider schedulerProvider) {
        Supplier<PrivacyPolicyViewModel> supplier = () -> new PrivacyPolicyViewModel(dataManager, schedulerProvider);
        ViewModelProviderFactory<PrivacyPolicyViewModel> factory = new ViewModelProviderFactory<>(PrivacyPolicyViewModel.class, supplier);
        return new ViewModelProvider(fragment, factory).get(PrivacyPolicyViewModel.class);
    }

    @Provides
    AddCommercialViewModel provideAddCommercialViewModel(DataManager dataManager, SchedulerProvider schedulerProvider) {
        Supplier<AddCommercialViewModel> supplier = () -> new AddCommercialViewModel(dataManager, schedulerProvider);
        ViewModelProviderFactory<AddCommercialViewModel> factory = new ViewModelProviderFactory<>(AddCommercialViewModel.class, supplier);
        return new ViewModelProvider(fragment, factory).get(AddCommercialViewModel.class);
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

    @Provides
    NotificationsViewModel provideNotificationsViewModel(DataManager dataManager, SchedulerProvider schedulerProvider) {
        Supplier<NotificationsViewModel> supplier = () -> new NotificationsViewModel(dataManager, schedulerProvider);
        ViewModelProviderFactory<NotificationsViewModel> factory = new ViewModelProviderFactory<>(NotificationsViewModel.class, supplier);
        return new ViewModelProvider(fragment, factory).get(NotificationsViewModel.class);
    }

    @Provides
    MyAdsViewModel provideMyAdsViewModel(DataManager dataManager, SchedulerProvider schedulerProvider) {
        Supplier<MyAdsViewModel> supplier = () -> new MyAdsViewModel(dataManager, schedulerProvider);
        ViewModelProviderFactory<MyAdsViewModel> factory = new ViewModelProviderFactory<>(MyAdsViewModel.class, supplier);
        return new ViewModelProvider(fragment, factory).get(MyAdsViewModel.class);
    }

    @Provides
    MyOrdersViewModel provideMyOrdersViewModel(DataManager dataManager, SchedulerProvider schedulerProvider) {
        Supplier<MyOrdersViewModel> supplier = () -> new MyOrdersViewModel(dataManager, schedulerProvider);
        ViewModelProviderFactory<MyOrdersViewModel> factory = new ViewModelProviderFactory<>(MyOrdersViewModel.class, supplier);
        return new ViewModelProvider(fragment, factory).get(MyOrdersViewModel.class);
    }

    @Provides
    MyRatingsViewModel provideMyRatingsViewModel(DataManager dataManager, SchedulerProvider schedulerProvider) {
        Supplier<MyRatingsViewModel> supplier = () -> new MyRatingsViewModel(dataManager, schedulerProvider);
        ViewModelProviderFactory<MyRatingsViewModel> factory = new ViewModelProviderFactory<>(MyRatingsViewModel.class, supplier);
        return new ViewModelProvider(fragment, factory).get(MyRatingsViewModel.class);
    }

    @Provides
    FollowsViewModel provideFollowsViewModel(DataManager dataManager, SchedulerProvider schedulerProvider) {
        Supplier<FollowsViewModel> supplier = () -> new FollowsViewModel(dataManager, schedulerProvider);
        ViewModelProviderFactory<FollowsViewModel> factory = new ViewModelProviderFactory<>(FollowsViewModel.class, supplier);
        return new ViewModelProvider(fragment, factory).get(FollowsViewModel.class);
    }

    @Provides
    BlockedViewModel provideBlockedViewModel(DataManager dataManager, SchedulerProvider schedulerProvider) {
        Supplier<BlockedViewModel> supplier = () -> new BlockedViewModel(dataManager, schedulerProvider);
        ViewModelProviderFactory<BlockedViewModel> factory = new ViewModelProviderFactory<>(BlockedViewModel.class, supplier);
        return new ViewModelProvider(fragment, factory).get(BlockedViewModel.class);
    }

    @Provides
    MemberAdsViewModel provideMemberAdsViewModel(DataManager dataManager, SchedulerProvider schedulerProvider) {
        Supplier<MemberAdsViewModel> supplier = () -> new MemberAdsViewModel(dataManager, schedulerProvider);
        ViewModelProviderFactory<MemberAdsViewModel> factory = new ViewModelProviderFactory<>(MemberAdsViewModel.class, supplier);
        return new ViewModelProvider(fragment, factory).get(MemberAdsViewModel.class);
    }

    @Provides
    MemberRatingsViewModel provideMemberRatingsViewModel(DataManager dataManager, SchedulerProvider schedulerProvider) {
        Supplier<MemberRatingsViewModel> supplier = () -> new MemberRatingsViewModel(dataManager, schedulerProvider);
        ViewModelProviderFactory<MemberRatingsViewModel> factory = new ViewModelProviderFactory<>(MemberRatingsViewModel.class, supplier);
        return new ViewModelProvider(fragment, factory).get(MemberRatingsViewModel.class);
    }
}
