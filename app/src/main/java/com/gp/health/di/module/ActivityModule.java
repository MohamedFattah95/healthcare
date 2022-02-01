package com.gp.health.di.module;

import androidx.core.util.Supplier;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.gp.health.ViewModelProviderFactory;
import com.gp.health.data.DataManager;
import com.gp.health.ui.about.AboutViewModel;
import com.gp.health.ui.adding_ad.ad_fees_info.AdFeesInfoViewModel;
import com.gp.health.ui.adding_ad.ad_location.AdLocationViewModel;
import com.gp.health.ui.adding_ad.add_ad_details.AddAdDetailsViewModel;
import com.gp.health.ui.adding_ad.add_ad_details.DynamicSpecsAdapter;
import com.gp.health.ui.adding_ad.add_ad_images.AdImagesAdapter;
import com.gp.health.ui.adding_ad.add_ad_images.AdVideosAdapter;
import com.gp.health.ui.adding_ad.add_ad_images.AddAdImagesViewModel;
import com.gp.health.ui.adding_ad.add_ad_info_desc.AddAdInfoDescViewModel;
import com.gp.health.ui.adding_ad.add_ad_terms.AddAdTermsViewModel;
import com.gp.health.ui.adding_ad.select_ad_category.AdCategoriesAdapter;
import com.gp.health.ui.adding_ad.select_ad_category.SelectAdCategoryViewModel;
import com.gp.health.ui.adding_order.AddingOrderViewModel;
import com.gp.health.ui.base.BaseActivity;
import com.gp.health.ui.chat.ChatViewModel;
import com.gp.health.ui.common.SliderAdapter;
import com.gp.health.ui.contact_us.ContactUsViewModel;
import com.gp.health.ui.edit_profile.EditProfileViewModel;
import com.gp.health.ui.error_handler.ErrorHandlerViewModel;
import com.gp.health.ui.faqs.FAQsAdapter;
import com.gp.health.ui.faqs.FAQsViewModel;
import com.gp.health.ui.intro.IntroViewModel;
import com.gp.health.ui.main.MainViewModel;
import com.gp.health.ui.media.MediaAdapter;
import com.gp.health.ui.media.MediaViewModel;
import com.gp.health.ui.member_profile.MemberProfilePagerAdapter;
import com.gp.health.ui.member_profile.MemberProfileViewModel;
import com.gp.health.ui.member_profile.member_ads.MemberAdsAdapter;
import com.gp.health.ui.mobile_search.MembersAdapter;
import com.gp.health.ui.mobile_search.MobileSearchViewModel;
import com.gp.health.ui.notifications.NotificationsAdapter;
import com.gp.health.ui.notifications.NotificationsViewModel;
import com.gp.health.ui.packages.PackagesAdapter;
import com.gp.health.ui.packages.PackagesViewModel;
import com.gp.health.ui.privacy_policy.PrivacyPolicyViewModel;
import com.gp.health.ui.profile.ProfilePagerAdapter;
import com.gp.health.ui.profile.ProfileViewModel;
import com.gp.health.ui.property_details.PropertyDetailsViewModel;
import com.gp.health.ui.property_details.PropertySpecsAdapter;
import com.gp.health.ui.search.SearchViewModel;
import com.gp.health.ui.select_language.SelectLanguageViewModel;
import com.gp.health.ui.splash.SplashViewModel;
import com.gp.health.ui.terms.TermsViewModel;
import com.gp.health.ui.user.complete_profile.CompleteProfileViewModel;
import com.gp.health.ui.user.forgot_password.ForgotPasswordViewModel;
import com.gp.health.ui.user.login.LoginViewModel;
import com.gp.health.ui.user.register.RegisterViewModel;
import com.gp.health.ui.user.reset_password.ResetPasswordViewModel;
import com.gp.health.ui.user.verify_account.VerifyAccountViewModel;
import com.gp.health.ui.user.verify_code.VerifyCodeViewModel;
import com.gp.health.utils.rx.SchedulerProvider;

import java.util.ArrayList;

import dagger.Module;
import dagger.Provides;

@Module
public class ActivityModule {
    private final BaseActivity<?> activity;

    public ActivityModule(BaseActivity<?> activity) {
        this.activity = activity;
    }

    @Provides
    MemberProfilePagerAdapter provideMemberProfilePagerAdapter() {
        return new MemberProfilePagerAdapter(activity);
    }

    @Provides
    ProfilePagerAdapter provideProfilePagerAdapter() {
        return new ProfilePagerAdapter(activity);
    }

    @Provides
    NotificationsAdapter provideNotificationsAdapter() {
        return new NotificationsAdapter(new ArrayList<>());
    }

    @Provides
    PackagesAdapter providePackagesAdapter() {
        return new PackagesAdapter(new ArrayList<>());
    }

    @Provides
    AdImagesAdapter provideAdImagesAdapter() {
        return new AdImagesAdapter(new ArrayList<>());
    }

    @Provides
    AdVideosAdapter provideAdVideosAdapter() {
        return new AdVideosAdapter(new ArrayList<>());
    }

    @Provides
    AdCategoriesAdapter provideAdCategoriesAdapter() {
        return new AdCategoriesAdapter(new ArrayList<>());
    }

    @Provides
    PropertySpecsAdapter providePropertySpecsAdapter() {
        return new PropertySpecsAdapter(new ArrayList<>());
    }

    @Provides
    FAQsAdapter provideFAQsAdapter() {
        return new FAQsAdapter(new ArrayList<>());
    }

    @Provides
    MembersAdapter provideMembersAdapter() {
        return new MembersAdapter(new ArrayList<>());
    }

    @Provides
    MemberAdsAdapter provideMemberAdsAdapter() {
        return new MemberAdsAdapter(new ArrayList<>());
    }

    @Provides
    DynamicSpecsAdapter provideDynamicSpecsAdapter() {
        return new DynamicSpecsAdapter(new ArrayList<>());
    }

    @Provides
    SliderAdapter provideSliderAdapter() {
        return new SliderAdapter(new ArrayList<>());
    }

    @Provides
    MediaAdapter provideMySliderAdapter() {
        return new MediaAdapter(new ArrayList<>());
    }

    @Provides
    LinearLayoutManager provideLinearLayoutManager() {
        return new LinearLayoutManager(activity);
    }

    @Provides
    IntroViewModel provideFeedViewModel(DataManager dataManager, SchedulerProvider schedulerProvider) {
        Supplier<IntroViewModel> supplier = () -> new IntroViewModel(dataManager, schedulerProvider);
        ViewModelProviderFactory<IntroViewModel> factory = new ViewModelProviderFactory<>(IntroViewModel.class, supplier);
        return new ViewModelProvider(activity, factory).get(IntroViewModel.class);
    }

    @Provides
    MainViewModel provideMainViewModel(DataManager dataManager, SchedulerProvider schedulerProvider) {
        Supplier<MainViewModel> supplier = () -> new MainViewModel(dataManager, schedulerProvider);
        ViewModelProviderFactory<MainViewModel> factory = new ViewModelProviderFactory<>(MainViewModel.class, supplier);
        return new ViewModelProvider(activity, factory).get(MainViewModel.class);
    }

    @Provides
    LoginViewModel provideLoginViewModel(DataManager dataManager, SchedulerProvider schedulerProvider) {
        Supplier<LoginViewModel> supplier = () -> new LoginViewModel(dataManager, schedulerProvider);
        ViewModelProviderFactory<LoginViewModel> factory = new ViewModelProviderFactory<>(LoginViewModel.class, supplier);
        return new ViewModelProvider(activity, factory).get(LoginViewModel.class);
    }

    @Provides
    SplashViewModel provideSplashViewModel(DataManager dataManager, SchedulerProvider schedulerProvider) {
        Supplier<SplashViewModel> supplier = () -> new SplashViewModel(dataManager, schedulerProvider);
        ViewModelProviderFactory<SplashViewModel> factory = new ViewModelProviderFactory<>(SplashViewModel.class, supplier);
        return new ViewModelProvider(activity, factory).get(SplashViewModel.class);
    }

    @Provides
    SelectLanguageViewModel provideSelectLanguageViewModel(DataManager dataManager, SchedulerProvider schedulerProvider) {
        Supplier<SelectLanguageViewModel> supplier = () -> new SelectLanguageViewModel(dataManager, schedulerProvider);
        ViewModelProviderFactory<SelectLanguageViewModel> factory = new ViewModelProviderFactory<>(SelectLanguageViewModel.class, supplier);
        return new ViewModelProvider(activity, factory).get(SelectLanguageViewModel.class);
    }

    @Provides
    TermsViewModel provideTermsViewModel(DataManager dataManager, SchedulerProvider schedulerProvider) {
        Supplier<TermsViewModel> supplier = () -> new TermsViewModel(dataManager, schedulerProvider);
        ViewModelProviderFactory<TermsViewModel> factory = new ViewModelProviderFactory<>(TermsViewModel.class, supplier);
        return new ViewModelProvider(activity, factory).get(TermsViewModel.class);
    }

    @Provides
    AdLocationViewModel currentLocationViewModel(DataManager dataManager, SchedulerProvider schedulerProvider) {
        Supplier<AdLocationViewModel> supplier = () -> new AdLocationViewModel(dataManager, schedulerProvider);
        ViewModelProviderFactory<AdLocationViewModel> factory = new ViewModelProviderFactory<>(AdLocationViewModel.class, supplier);
        return new ViewModelProvider(activity, factory).get(AdLocationViewModel.class);
    }

    @Provides
    RegisterViewModel provideRegisterViewModel(DataManager dataManager, SchedulerProvider schedulerProvider) {
        Supplier<RegisterViewModel> supplier = () -> new RegisterViewModel(dataManager, schedulerProvider);
        ViewModelProviderFactory<RegisterViewModel> factory = new ViewModelProviderFactory<>(RegisterViewModel.class, supplier);
        return new ViewModelProvider(activity, factory).get(RegisterViewModel.class);
    }

    @Provides
    VerifyAccountViewModel provideVerifyAccountViewModel(DataManager dataManager, SchedulerProvider schedulerProvider) {
        Supplier<VerifyAccountViewModel> supplier = () -> new VerifyAccountViewModel(dataManager, schedulerProvider);
        ViewModelProviderFactory<VerifyAccountViewModel> factory = new ViewModelProviderFactory<>(VerifyAccountViewModel.class, supplier);
        return new ViewModelProvider(activity, factory).get(VerifyAccountViewModel.class);
    }

    @Provides
    CompleteProfileViewModel provideCompleteProfileViewModel(DataManager dataManager, SchedulerProvider schedulerProvider) {
        Supplier<CompleteProfileViewModel> supplier = () -> new CompleteProfileViewModel(dataManager, schedulerProvider);
        ViewModelProviderFactory<CompleteProfileViewModel> factory = new ViewModelProviderFactory<>(CompleteProfileViewModel.class, supplier);
        return new ViewModelProvider(activity, factory).get(CompleteProfileViewModel.class);
    }

    @Provides
    EditProfileViewModel provideEditProfileViewModel(DataManager dataManager, SchedulerProvider schedulerProvider) {
        Supplier<EditProfileViewModel> supplier = () -> new EditProfileViewModel(dataManager, schedulerProvider);
        ViewModelProviderFactory<EditProfileViewModel> factory = new ViewModelProviderFactory<>(EditProfileViewModel.class, supplier);
        return new ViewModelProvider(activity, factory).get(EditProfileViewModel.class);
    }

    @Provides
    ContactUsViewModel provideComplaintsAndSuggestionsViewModel(DataManager dataManager, SchedulerProvider schedulerProvider) {
        Supplier<ContactUsViewModel> supplier = () -> new ContactUsViewModel(dataManager, schedulerProvider);
        ViewModelProviderFactory<ContactUsViewModel> factory = new ViewModelProviderFactory<>(ContactUsViewModel.class, supplier);
        return new ViewModelProvider(activity, factory).get(ContactUsViewModel.class);
    }

    @Provides
    PrivacyPolicyViewModel providePrivacyPolicyViewModel(DataManager dataManager, SchedulerProvider schedulerProvider) {
        Supplier<PrivacyPolicyViewModel> supplier = () -> new PrivacyPolicyViewModel(dataManager, schedulerProvider);
        ViewModelProviderFactory<PrivacyPolicyViewModel> factory = new ViewModelProviderFactory<>(PrivacyPolicyViewModel.class, supplier);
        return new ViewModelProvider(activity, factory).get(PrivacyPolicyViewModel.class);
    }

    @Provides
    NotificationsViewModel provideNotificationsViewModel(DataManager dataManager, SchedulerProvider schedulerProvider) {
        Supplier<NotificationsViewModel> supplier = () -> new NotificationsViewModel(dataManager, schedulerProvider);
        ViewModelProviderFactory<NotificationsViewModel> factory = new ViewModelProviderFactory<>(NotificationsViewModel.class, supplier);
        return new ViewModelProvider(activity, factory).get(NotificationsViewModel.class);
    }

    @Provides
    ForgotPasswordViewModel provideForgotPasswordViewModel(DataManager dataManager, SchedulerProvider schedulerProvider) {
        Supplier<ForgotPasswordViewModel> supplier = () -> new ForgotPasswordViewModel(dataManager, schedulerProvider);
        ViewModelProviderFactory<ForgotPasswordViewModel> factory = new ViewModelProviderFactory<>(ForgotPasswordViewModel.class, supplier);
        return new ViewModelProvider(activity, factory).get(ForgotPasswordViewModel.class);
    }

    @Provides
    ResetPasswordViewModel provideResetPasswordViewModel(DataManager dataManager, SchedulerProvider schedulerProvider) {
        Supplier<ResetPasswordViewModel> supplier = () -> new ResetPasswordViewModel(dataManager, schedulerProvider);
        ViewModelProviderFactory<ResetPasswordViewModel> factory = new ViewModelProviderFactory<>(ResetPasswordViewModel.class, supplier);
        return new ViewModelProvider(activity, factory).get(ResetPasswordViewModel.class);
    }

    @Provides
    ErrorHandlerViewModel provideErrorHandlerViewModel(DataManager dataManager, SchedulerProvider schedulerProvider) {
        Supplier<ErrorHandlerViewModel> supplier = () -> new ErrorHandlerViewModel(dataManager, schedulerProvider);
        ViewModelProviderFactory<ErrorHandlerViewModel> factory = new ViewModelProviderFactory<>(ErrorHandlerViewModel.class, supplier);
        return new ViewModelProvider(activity, factory).get(ErrorHandlerViewModel.class);
    }

    @Provides
    VerifyCodeViewModel provideVerifyCodeViewModel(DataManager dataManager, SchedulerProvider schedulerProvider) {
        Supplier<VerifyCodeViewModel> supplier = () -> new VerifyCodeViewModel(dataManager, schedulerProvider);
        ViewModelProviderFactory<VerifyCodeViewModel> factory = new ViewModelProviderFactory<>(VerifyCodeViewModel.class, supplier);
        return new ViewModelProvider(activity, factory).get(VerifyCodeViewModel.class);
    }

    @Provides
    ChatViewModel provideChatViewModel(DataManager dataManager, SchedulerProvider schedulerProvider) {
        Supplier<ChatViewModel> supplier = () -> new ChatViewModel(dataManager, schedulerProvider);
        ViewModelProviderFactory<ChatViewModel> factory = new ViewModelProviderFactory<>(ChatViewModel.class, supplier);
        return new ViewModelProvider(activity, factory).get(ChatViewModel.class);
    }

    @Provides
    MemberProfileViewModel provideMemberProfileViewModel(DataManager dataManager, SchedulerProvider schedulerProvider) {
        Supplier<MemberProfileViewModel> supplier = () -> new MemberProfileViewModel(dataManager, schedulerProvider);
        ViewModelProviderFactory<MemberProfileViewModel> factory = new ViewModelProviderFactory<>(MemberProfileViewModel.class, supplier);
        return new ViewModelProvider(activity, factory).get(MemberProfileViewModel.class);
    }

    @Provides
    AdFeesInfoViewModel provideAdFeesInfoViewModel(DataManager dataManager, SchedulerProvider schedulerProvider) {
        Supplier<AdFeesInfoViewModel> supplier = () -> new AdFeesInfoViewModel(dataManager, schedulerProvider);
        ViewModelProviderFactory<AdFeesInfoViewModel> factory = new ViewModelProviderFactory<>(AdFeesInfoViewModel.class, supplier);
        return new ViewModelProvider(activity, factory).get(AdFeesInfoViewModel.class);
    }

    @Provides
    AddAdImagesViewModel provideAddAdImagesViewModel(DataManager dataManager, SchedulerProvider schedulerProvider) {
        Supplier<AddAdImagesViewModel> supplier = () -> new AddAdImagesViewModel(dataManager, schedulerProvider);
        ViewModelProviderFactory<AddAdImagesViewModel> factory = new ViewModelProviderFactory<>(AddAdImagesViewModel.class, supplier);
        return new ViewModelProvider(activity, factory).get(AddAdImagesViewModel.class);
    }

    @Provides
    AddAdTermsViewModel provideAddAdTermsViewModel(DataManager dataManager, SchedulerProvider schedulerProvider) {
        Supplier<AddAdTermsViewModel> supplier = () -> new AddAdTermsViewModel(dataManager, schedulerProvider);
        ViewModelProviderFactory<AddAdTermsViewModel> factory = new ViewModelProviderFactory<>(AddAdTermsViewModel.class, supplier);
        return new ViewModelProvider(activity, factory).get(AddAdTermsViewModel.class);
    }

    @Provides
    SelectAdCategoryViewModel provideSelectAdCategoryViewModel(DataManager dataManager, SchedulerProvider schedulerProvider) {
        Supplier<SelectAdCategoryViewModel> supplier = () -> new SelectAdCategoryViewModel(dataManager, schedulerProvider);
        ViewModelProviderFactory<SelectAdCategoryViewModel> factory = new ViewModelProviderFactory<>(SelectAdCategoryViewModel.class, supplier);
        return new ViewModelProvider(activity, factory).get(SelectAdCategoryViewModel.class);
    }

    @Provides
    PropertyDetailsViewModel providePropertyDetailsViewModel(DataManager dataManager, SchedulerProvider schedulerProvider) {
        Supplier<PropertyDetailsViewModel> supplier = () -> new PropertyDetailsViewModel(dataManager, schedulerProvider);
        ViewModelProviderFactory<PropertyDetailsViewModel> factory = new ViewModelProviderFactory<>(PropertyDetailsViewModel.class, supplier);
        return new ViewModelProvider(activity, factory).get(PropertyDetailsViewModel.class);
    }

    @Provides
    SearchViewModel provideSearchViewModel(DataManager dataManager, SchedulerProvider schedulerProvider) {
        Supplier<SearchViewModel> supplier = () -> new SearchViewModel(dataManager, schedulerProvider);
        ViewModelProviderFactory<SearchViewModel> factory = new ViewModelProviderFactory<>(SearchViewModel.class, supplier);
        return new ViewModelProvider(activity, factory).get(SearchViewModel.class);
    }

    @Provides
    AddAdDetailsViewModel provideAddAdDetailsViewModel(DataManager dataManager, SchedulerProvider schedulerProvider) {
        Supplier<AddAdDetailsViewModel> supplier = () -> new AddAdDetailsViewModel(dataManager, schedulerProvider);
        ViewModelProviderFactory<AddAdDetailsViewModel> factory = new ViewModelProviderFactory<>(AddAdDetailsViewModel.class, supplier);
        return new ViewModelProvider(activity, factory).get(AddAdDetailsViewModel.class);
    }

    @Provides
    AddAdInfoDescViewModel provideAddAdInfoDescViewModel(DataManager dataManager, SchedulerProvider schedulerProvider) {
        Supplier<AddAdInfoDescViewModel> supplier = () -> new AddAdInfoDescViewModel(dataManager, schedulerProvider);
        ViewModelProviderFactory<AddAdInfoDescViewModel> factory = new ViewModelProviderFactory<>(AddAdInfoDescViewModel.class, supplier);
        return new ViewModelProvider(activity, factory).get(AddAdInfoDescViewModel.class);
    }

    @Provides
    AddingOrderViewModel provideAddingOrderViewModel(DataManager dataManager, SchedulerProvider schedulerProvider) {
        Supplier<AddingOrderViewModel> supplier = () -> new AddingOrderViewModel(dataManager, schedulerProvider);
        ViewModelProviderFactory<AddingOrderViewModel> factory = new ViewModelProviderFactory<>(AddingOrderViewModel.class, supplier);
        return new ViewModelProvider(activity, factory).get(AddingOrderViewModel.class);
    }

    @Provides
    FAQsViewModel provideFAQsViewModel(DataManager dataManager, SchedulerProvider schedulerProvider) {
        Supplier<FAQsViewModel> supplier = () -> new FAQsViewModel(dataManager, schedulerProvider);
        ViewModelProviderFactory<FAQsViewModel> factory = new ViewModelProviderFactory<>(FAQsViewModel.class, supplier);
        return new ViewModelProvider(activity, factory).get(FAQsViewModel.class);
    }

    @Provides
    AboutViewModel provideAboutViewModel(DataManager dataManager, SchedulerProvider schedulerProvider) {
        Supplier<AboutViewModel> supplier = () -> new AboutViewModel(dataManager, schedulerProvider);
        ViewModelProviderFactory<AboutViewModel> factory = new ViewModelProviderFactory<>(AboutViewModel.class, supplier);
        return new ViewModelProvider(activity, factory).get(AboutViewModel.class);
    }

    @Provides
    MobileSearchViewModel provideMobileSearchViewModel(DataManager dataManager, SchedulerProvider schedulerProvider) {
        Supplier<MobileSearchViewModel> supplier = () -> new MobileSearchViewModel(dataManager, schedulerProvider);
        ViewModelProviderFactory<MobileSearchViewModel> factory = new ViewModelProviderFactory<>(MobileSearchViewModel.class, supplier);
        return new ViewModelProvider(activity, factory).get(MobileSearchViewModel.class);
    }

    @Provides
    ProfileViewModel provideProfileViewModel(DataManager dataManager, SchedulerProvider schedulerProvider) {
        Supplier<ProfileViewModel> supplier = () -> new ProfileViewModel(dataManager, schedulerProvider);
        ViewModelProviderFactory<ProfileViewModel> factory = new ViewModelProviderFactory<>(ProfileViewModel.class, supplier);
        return new ViewModelProvider(activity, factory).get(ProfileViewModel.class);
    }

    @Provides
    PackagesViewModel providePackagesViewModel(DataManager dataManager, SchedulerProvider schedulerProvider) {
        Supplier<PackagesViewModel> supplier = () -> new PackagesViewModel(dataManager, schedulerProvider);
        ViewModelProviderFactory<PackagesViewModel> factory = new ViewModelProviderFactory<>(PackagesViewModel.class, supplier);
        return new ViewModelProvider(activity, factory).get(PackagesViewModel.class);
    }

    @Provides
    MediaViewModel provideMediaViewModel(DataManager dataManager, SchedulerProvider schedulerProvider) {
        Supplier<MediaViewModel> supplier = () -> new MediaViewModel(dataManager, schedulerProvider);
        ViewModelProviderFactory<MediaViewModel> factory = new ViewModelProviderFactory<>(MediaViewModel.class, supplier);
        return new ViewModelProvider(activity, factory).get(MediaViewModel.class);
    }

}
