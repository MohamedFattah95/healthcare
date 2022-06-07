package com.gp.shifa.di.module;

import androidx.core.util.Supplier;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.gp.shifa.ViewModelProviderFactory;
import com.gp.shifa.data.DataManager;
import com.gp.shifa.ui.about.AboutViewModel;
import com.gp.shifa.ui.base.BaseActivity;
import com.gp.shifa.ui.chat.ChatViewModel;
import com.gp.shifa.ui.common.SliderAdapter;
import com.gp.shifa.ui.contact_us.ContactUsViewModel;
import com.gp.shifa.ui.edit_profile.EditProfileViewModel;
import com.gp.shifa.ui.error_handler.ErrorHandlerViewModel;
import com.gp.shifa.ui.faqs.FAQsAdapter;
import com.gp.shifa.ui.faqs.FAQsViewModel;
import com.gp.shifa.ui.intro.IntroViewModel;
import com.gp.shifa.ui.main.MainViewModel;
import com.gp.shifa.ui.member_profile.MemberProfilePagerAdapter;
import com.gp.shifa.ui.member_profile.MemberProfileViewModel;
import com.gp.shifa.ui.member_profile.member_ads.MemberAdsAdapter;
import com.gp.shifa.ui.mobile_search.MembersAdapter;
import com.gp.shifa.ui.mobile_search.MobileSearchViewModel;
import com.gp.shifa.ui.notifications.NotificationsAdapter;
import com.gp.shifa.ui.notifications.NotificationsViewModel;
import com.gp.shifa.ui.packages.PackagesAdapter;
import com.gp.shifa.ui.packages.PackagesViewModel;
import com.gp.shifa.ui.privacy_policy.PrivacyPolicyViewModel;
import com.gp.shifa.ui.profile.ProfilePagerAdapter;
import com.gp.shifa.ui.profile.ProfileViewModel;
import com.gp.shifa.ui.property_details.PropertyDetailsViewModel;
import com.gp.shifa.ui.property_details.PropertySpecsAdapter;
import com.gp.shifa.ui.search.SearchViewModel;
import com.gp.shifa.ui.select_language.SelectLanguageViewModel;
import com.gp.shifa.ui.splash.SplashViewModel;
import com.gp.shifa.ui.terms.TermsViewModel;
import com.gp.shifa.ui.user.complete_profile.CompleteProfileViewModel;
import com.gp.shifa.ui.user.forgot_password.ForgotPasswordViewModel;
import com.gp.shifa.ui.user.login.LoginViewModel;
import com.gp.shifa.ui.user.register.RegisterViewModel;
import com.gp.shifa.ui.user.reset_password.ResetPasswordViewModel;
import com.gp.shifa.ui.user.verify_account.VerifyAccountViewModel;
import com.gp.shifa.ui.user.verify_code.VerifyCodeViewModel;
import com.gp.shifa.utils.rx.SchedulerProvider;

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
    SliderAdapter provideSliderAdapter() {
        return new SliderAdapter(new ArrayList<>());
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

}
