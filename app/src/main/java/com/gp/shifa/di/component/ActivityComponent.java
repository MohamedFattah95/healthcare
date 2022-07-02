package com.gp.shifa.di.component;

import com.gp.shifa.di.module.ActivityModule;
import com.gp.shifa.di.scope.ActivityScope;
import com.gp.shifa.ui.category_doctors.CategoryDoctorsActivity;
import com.gp.shifa.ui.chat.ChatActivity;
import com.gp.shifa.ui.doctor_details.DoctorDetailsActivity;
import com.gp.shifa.ui.edit_profile.EditProfileActivity;
import com.gp.shifa.ui.error_handler.ErrorHandlerActivity;
import com.gp.shifa.ui.intro.IntroActivity;
import com.gp.shifa.ui.main.MainActivity;
import com.gp.shifa.ui.select_language.SelectLanguageActivity;
import com.gp.shifa.ui.splash.SplashActivity;
import com.gp.shifa.ui.user.change_password.ChangePasswordActivity;
import com.gp.shifa.ui.user.login.LoginActivity;
import com.gp.shifa.ui.user.register.RegisterActivity;
import com.gp.shifa.ui.user.reset_password.ResetPasswordActivity;

import dagger.Component;

@ActivityScope
@Component(modules = ActivityModule.class, dependencies = AppComponent.class)
public interface ActivityComponent {
    void inject(IntroActivity activity);

    void inject(LoginActivity activity);

    void inject(MainActivity activity);

    void inject(SplashActivity activity);

    void inject(RegisterActivity activity);

    void inject(ResetPasswordActivity activity);

    void inject(ErrorHandlerActivity activity);

    void inject(SelectLanguageActivity activity);

    void inject(ChatActivity activity);

    void inject(EditProfileActivity activity);

    void inject(DoctorDetailsActivity activity);

    void inject(CategoryDoctorsActivity activity);

    void inject(ChangePasswordActivity activity);
}
