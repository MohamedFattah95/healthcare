package com.gp.shifa.di.module;

import androidx.core.util.Supplier;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.gp.shifa.ViewModelProviderFactory;
import com.gp.shifa.data.DataManager;
import com.gp.shifa.ui.base.BaseActivity;
import com.gp.shifa.ui.category_doctors.CategoriesDoctorsAdapter;
import com.gp.shifa.ui.category_doctors.CategoryDoctorsViewModel;
import com.gp.shifa.ui.chat.ChatViewModel;
import com.gp.shifa.ui.common.SliderAdapter;
import com.gp.shifa.ui.doctor_details.ClinicsAdapter;
import com.gp.shifa.ui.doctor_details.DoctorDetailsViewModel;
import com.gp.shifa.ui.edit_profile.EditProfileViewModel;
import com.gp.shifa.ui.error_handler.ErrorHandlerViewModel;
import com.gp.shifa.ui.intro.IntroViewModel;
import com.gp.shifa.ui.main.MainViewModel;
import com.gp.shifa.ui.select_language.SelectLanguageViewModel;
import com.gp.shifa.ui.splash.SplashViewModel;
import com.gp.shifa.ui.user.change_password.ChangePasswordViewModel;
import com.gp.shifa.ui.user.login.LoginViewModel;
import com.gp.shifa.ui.user.profile.ProfileViewModel;
import com.gp.shifa.ui.user.register.RegisterViewModel;
import com.gp.shifa.ui.user.reset_password.ResetPasswordViewModel;
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
    CategoriesDoctorsAdapter provideCategoriesDoctorsAdapter() {
        return new CategoriesDoctorsAdapter(new ArrayList<>());
    }

    @Provides
    ClinicsAdapter provideClinicsAdapter() {
        return new ClinicsAdapter(new ArrayList<>());
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
    RegisterViewModel provideRegisterViewModel(DataManager dataManager, SchedulerProvider schedulerProvider) {
        Supplier<RegisterViewModel> supplier = () -> new RegisterViewModel(dataManager, schedulerProvider);
        ViewModelProviderFactory<RegisterViewModel> factory = new ViewModelProviderFactory<>(RegisterViewModel.class, supplier);
        return new ViewModelProvider(activity, factory).get(RegisterViewModel.class);
    }

    @Provides
    ChangePasswordViewModel provideChangePasswordViewModel(DataManager dataManager, SchedulerProvider schedulerProvider) {
        Supplier<ChangePasswordViewModel> supplier = () -> new ChangePasswordViewModel(dataManager, schedulerProvider);
        ViewModelProviderFactory<ChangePasswordViewModel> factory = new ViewModelProviderFactory<>(ChangePasswordViewModel.class, supplier);
        return new ViewModelProvider(activity, factory).get(ChangePasswordViewModel.class);
    }

    @Provides
    EditProfileViewModel provideEditProfileViewModel(DataManager dataManager, SchedulerProvider schedulerProvider) {
        Supplier<EditProfileViewModel> supplier = () -> new EditProfileViewModel(dataManager, schedulerProvider);
        ViewModelProviderFactory<EditProfileViewModel> factory = new ViewModelProviderFactory<>(EditProfileViewModel.class, supplier);
        return new ViewModelProvider(activity, factory).get(EditProfileViewModel.class);
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
    ChatViewModel provideChatViewModel(DataManager dataManager, SchedulerProvider schedulerProvider) {
        Supplier<ChatViewModel> supplier = () -> new ChatViewModel(dataManager, schedulerProvider);
        ViewModelProviderFactory<ChatViewModel> factory = new ViewModelProviderFactory<>(ChatViewModel.class, supplier);
        return new ViewModelProvider(activity, factory).get(ChatViewModel.class);
    }

    @Provides
    DoctorDetailsViewModel providePropertyDetailsViewModel(DataManager dataManager, SchedulerProvider schedulerProvider) {
        Supplier<DoctorDetailsViewModel> supplier = () -> new DoctorDetailsViewModel(dataManager, schedulerProvider);
        ViewModelProviderFactory<DoctorDetailsViewModel> factory = new ViewModelProviderFactory<>(DoctorDetailsViewModel.class, supplier);
        return new ViewModelProvider(activity, factory).get(DoctorDetailsViewModel.class);
    }

    @Provides
    CategoryDoctorsViewModel provideSearchViewModel(DataManager dataManager, SchedulerProvider schedulerProvider) {
        Supplier<CategoryDoctorsViewModel> supplier = () -> new CategoryDoctorsViewModel(dataManager, schedulerProvider);
        ViewModelProviderFactory<CategoryDoctorsViewModel> factory = new ViewModelProviderFactory<>(CategoryDoctorsViewModel.class, supplier);
        return new ViewModelProvider(activity, factory).get(CategoryDoctorsViewModel.class);
    }

    @Provides
    ProfileViewModel provideProfileViewModel(DataManager dataManager, SchedulerProvider schedulerProvider) {
        Supplier<ProfileViewModel> supplier = () -> new ProfileViewModel(dataManager, schedulerProvider);
        ViewModelProviderFactory<ProfileViewModel> factory = new ViewModelProviderFactory<>(ProfileViewModel.class, supplier);
        return new ViewModelProvider(activity, factory).get(ProfileViewModel.class);
    }

}
