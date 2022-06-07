package com.gp.shifa.di.component;

import com.gp.shifa.di.module.ActivityModule;
import com.gp.shifa.di.scope.ActivityScope;
import com.gp.shifa.ui.about.AboutActivity;
import com.gp.shifa.ui.chat.ChatActivity;
import com.gp.shifa.ui.contact_us.ContactUsActivity;
import com.gp.shifa.ui.edit_profile.EditProfileActivity;
import com.gp.shifa.ui.error_handler.ErrorHandlerActivity;
import com.gp.shifa.ui.faqs.FAQsActivity;
import com.gp.shifa.ui.intro.IntroActivity;
import com.gp.shifa.ui.main.MainActivity;
import com.gp.shifa.ui.member_profile.MemberProfileActivity;
import com.gp.shifa.ui.mobile_search.MobileSearchActivity;
import com.gp.shifa.ui.packages.PackagesActivity;
import com.gp.shifa.ui.privacy_policy.PrivacyPolicyActivity;
import com.gp.shifa.ui.profile.MyProfileActivity;
import com.gp.shifa.ui.property_details.PropertyDetailsActivity;
import com.gp.shifa.ui.search.SearchActivity;
import com.gp.shifa.ui.select_language.SelectLanguageActivity;
import com.gp.shifa.ui.splash.SplashActivity;
import com.gp.shifa.ui.terms.TermsActivity;
import com.gp.shifa.ui.user.complete_profile.CompleteProfileActivity;
import com.gp.shifa.ui.user.forgot_password.ForgotPasswordActivity;
import com.gp.shifa.ui.user.login.LoginActivity;
import com.gp.shifa.ui.user.register.RegisterActivity;
import com.gp.shifa.ui.user.reset_password.ResetPasswordActivity;
import com.gp.shifa.ui.user.verify_account.VerifyAccountActivity;
import com.gp.shifa.ui.user.verify_code.VerifyCodeActivity;

import dagger.Component;

@ActivityScope
@Component(modules = ActivityModule.class, dependencies = AppComponent.class)
public interface ActivityComponent {
    void inject(IntroActivity activity);

    void inject(LoginActivity activity);

    void inject(MainActivity activity);

    void inject(SplashActivity activity);

    void inject(RegisterActivity activity);

    void inject(VerifyAccountActivity activity);

    void inject(CompleteProfileActivity activity);

    void inject(ForgotPasswordActivity activity);

    void inject(ResetPasswordActivity activity);

    void inject(ErrorHandlerActivity activity);

    void inject(VerifyCodeActivity activity);

    void inject(TermsActivity activity);

    void inject(SelectLanguageActivity activity);

    void inject(PrivacyPolicyActivity activity);

    void inject(ChatActivity activity);

    void inject(EditProfileActivity activity);

    void inject(MemberProfileActivity activity);

    void inject(PropertyDetailsActivity activity);

    void inject(SearchActivity activity);

    void inject(AboutActivity activity);

    void inject(FAQsActivity activity);

    void inject(ContactUsActivity activity);

    void inject(MobileSearchActivity activity);

    void inject(MyProfileActivity activity);

    void inject(PackagesActivity activity);
}
