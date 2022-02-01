package com.gp.health.di.component;

import com.gp.health.di.module.ActivityModule;
import com.gp.health.di.scope.ActivityScope;
import com.gp.health.ui.about.AboutActivity;
import com.gp.health.ui.adding_ad.ad_fees_info.AdFeesInfoActivity;
import com.gp.health.ui.adding_ad.ad_location.AdLocationActivity;
import com.gp.health.ui.adding_ad.add_ad_details.AddAdDetailsActivity;
import com.gp.health.ui.adding_ad.add_ad_images.AddAdImagesActivity;
import com.gp.health.ui.adding_ad.add_ad_info_desc.AddAdInfoDescActivity;
import com.gp.health.ui.adding_ad.add_ad_terms.AddAdTermsActivity;
import com.gp.health.ui.adding_ad.select_ad_category.SelectAdCategoryActivity;
import com.gp.health.ui.adding_order.AddingOrderActivity;
import com.gp.health.ui.chat.ChatActivity;
import com.gp.health.ui.contact_us.ContactUsActivity;
import com.gp.health.ui.edit_profile.EditProfileActivity;
import com.gp.health.ui.error_handler.ErrorHandlerActivity;
import com.gp.health.ui.faqs.FAQsActivity;
import com.gp.health.ui.intro.IntroActivity;
import com.gp.health.ui.main.MainActivity;
import com.gp.health.ui.media.MediaActivity;
import com.gp.health.ui.member_profile.MemberProfileActivity;
import com.gp.health.ui.mobile_search.MobileSearchActivity;
import com.gp.health.ui.packages.PackagesActivity;
import com.gp.health.ui.privacy_policy.PrivacyPolicyActivity;
import com.gp.health.ui.profile.MyProfileActivity;
import com.gp.health.ui.property_details.PropertyDetailsActivity;
import com.gp.health.ui.search.SearchActivity;
import com.gp.health.ui.select_language.SelectLanguageActivity;
import com.gp.health.ui.splash.SplashActivity;
import com.gp.health.ui.terms.TermsActivity;
import com.gp.health.ui.user.complete_profile.CompleteProfileActivity;
import com.gp.health.ui.user.forgot_password.ForgotPasswordActivity;
import com.gp.health.ui.user.login.LoginActivity;
import com.gp.health.ui.user.register.RegisterActivity;
import com.gp.health.ui.user.reset_password.ResetPasswordActivity;
import com.gp.health.ui.user.verify_account.VerifyAccountActivity;
import com.gp.health.ui.user.verify_code.VerifyCodeActivity;

import dagger.Component;

@ActivityScope
@Component(modules = ActivityModule.class, dependencies = AppComponent.class)
public interface ActivityComponent {
    void inject(IntroActivity activity);

    void inject(LoginActivity activity);

    void inject(MainActivity activity);

    void inject(SplashActivity activity);

    void inject(AdLocationActivity activity);

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

    void inject(AddAdTermsActivity activity);

    void inject(SelectAdCategoryActivity activity);

    void inject(AdFeesInfoActivity activity);

    void inject(AddAdImagesActivity activity);

    void inject(PropertyDetailsActivity activity);

    void inject(SearchActivity activity);

    void inject(AddAdDetailsActivity activity);

    void inject(AddAdInfoDescActivity activity);

    void inject(AddingOrderActivity activity);

    void inject(AboutActivity activity);

    void inject(FAQsActivity activity);

    void inject(ContactUsActivity activity);

    void inject(MobileSearchActivity activity);

    void inject(MyProfileActivity activity);

    void inject(PackagesActivity activity);

    void inject(MediaActivity activity);
}
