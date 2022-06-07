
package com.gp.shifa.ui.edit_profile;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.developers.imagezipper.ImageZipper;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.gp.shifa.R;
import com.gp.shifa.data.models.UserModel;
import com.gp.shifa.databinding.ActivityEditProfileBinding;
import com.gp.shifa.di.component.ActivityComponent;
import com.gp.shifa.ui.base.BaseActivity;
import com.gp.shifa.utils.CommonUtils;
import com.gp.shifa.utils.ErrorHandlingUtils;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class EditProfileActivity extends BaseActivity<EditProfileViewModel> implements EditProfileNavigator {

    private static final int PICK_PROFILE_IMG = 123;

    UserModel userModel = null;

    File profileImg = null;
    String selectedMemberType = null;

    ActivityEditProfileBinding binding;

    public static Intent newIntent(Context context) {
        return new Intent(context, EditProfileActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mViewModel.setNavigator(this);

        binding.toolbar.toolbarTitle.setText(R.string.profile_menu);
        setUp();

    }

    private void setUp() {
        setUpOnViewClicked();
        subscribeViewModel();

        showLoading();
        mViewModel.getProfile();
    }


    private void subscribeViewModel() {


        mViewModel.getProfileLiveData().observe(this, response -> {

            mViewModel.getMemberTypes();

            userModel = response.getData();

            if (profileImg == null) {
                Glide.with(this)
                        .load(mViewModel.getDataManager().getUserObject().getUser().getImgSrc()
                                + "/" + mViewModel.getDataManager().getUserObject().getUser().getImg())
                        .placeholder(R.drawable.ic_user_holder)
                        .error(R.drawable.ic_user_holder)
                        .into(binding.civProfilePic);
                profileImg = null;
                binding.ivEditProfilePic.setImageResource(R.drawable.ic_camera);
            }

            if (response.getData().getUser().getName() != null)
                binding.etFullName.setText(response.getData().getUser().getName());

            if (response.getData().getUser().getEmail() != null)
                binding.etEmail.setText(response.getData().getUser().getEmail());

            if (response.getData().getUser().getPhone() != null)
                binding.etMobile.setText(response.getData().getUser().getPhone());

            binding.ivEditProfilePic.setImageResource(R.drawable.ic_camera);
            binding.etMobile.setEnabled(false);
            binding.etQuote.setText(userModel.getUser().getGander());

            binding.etOldPassword.setText("");
            binding.etNewPassword.setText("");
            binding.etConfirmNewPassword.setText("");

        });

        mViewModel.getUpdateProfileLiveData().observe(this, response -> {
            hideLoading();
            EventBus.getDefault().post("update_profile");
            showSuccessMessage(response.getMessage());
            finish();
        });

        mViewModel.getCheckOldPasswordLiveData().observe(this, response -> validateProfile());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {

            if (requestCode == PICK_PROFILE_IMG) {
                profileImg = new File(data.getData().getPath());
                Glide.with(this)
                        .load(profileImg.getPath())
                        .placeholder(R.drawable.ic_user_holder)
                        .error(R.drawable.ic_user_holder)
                        .into(binding.civProfilePic);

                binding.ivEditProfilePic.setImageResource(R.drawable.ic_close);

            }

        }
    }

    public void setUpOnViewClicked() {

        binding.toolbar.backButton.setOnClickListener(v -> finish());

        binding.civProfilePic.setOnClickListener(v ->
                ImagePicker.Companion.with(this)
                        .crop()
                        .compress(1024)
                        .maxResultSize(720, 720).start(PICK_PROFILE_IMG));

        binding.ivEditProfilePic.setOnClickListener(v -> {
            if (profileImg == null) {
                ImagePicker.Companion.with(this)
                        .crop()
                        .compress(1024)
                        .maxResultSize(720, 720)
                        .start(PICK_PROFILE_IMG);
            } else {
                Glide.with(this)
                        .load(R.drawable.ic_user_holder)
                        .placeholder(R.drawable.ic_user_holder)
                        .error(R.drawable.ic_user_holder)
                        .into(binding.civProfilePic);
                binding.ivEditProfilePic.setImageResource(R.drawable.ic_camera);
                profileImg = null;
            }
        });

        binding.tilMobile.setEndIconOnClickListener(v -> {
            binding.etMobile.setEnabled(!binding.etMobile.isEnabled());
            binding.etMobile.requestFocus();
            showKeyboard();
        });

        binding.tilName.setEndIconOnClickListener(v -> {
            binding.etFullName.setEnabled(!binding.etFullName.isEnabled());
            binding.etFullName.requestFocus();
            showKeyboard();
        });

        binding.tilEmail.setEndIconOnClickListener(v -> {
            binding.etEmail.setEnabled(!binding.etEmail.isEnabled());
            binding.etEmail.requestFocus();
            showKeyboard();
        });

        binding.btnSave.setOnClickListener(v -> {

            if (!binding.etOldPassword.getText().toString().trim().isEmpty() && binding.etOldPassword.getText().toString().trim().length() >= 6
                    && binding.etOldPassword.getText().toString().trim().length() <= 12) {
                showLoading();
                mViewModel.checkOldPassword(binding.etOldPassword.getText().toString().trim());
                return;
            }

            validateProfile();

        });

    }

    private void validateProfile() {

        if (binding.etFullName.getText().toString().trim().isEmpty()) {
            binding.etFullName.setError(getText(R.string.empty_full_name));
            binding.etFullName.requestFocus();
            return;
        }
        binding.etFullName.setError(null);

        if (!binding.etEmail.getText().toString().trim().isEmpty() && !CommonUtils.isEmailValid(binding.etEmail.getText().toString().trim())) {
            binding.etEmail.setError(getText(R.string.invalid_email));
            binding.etEmail.requestFocus();
            return;
        }
        binding.etEmail.setError(null);

        if (binding.etMobile.getText().toString().trim().isEmpty()) {
            binding.etMobile.setError(getText(R.string.empty_mobile));
            binding.etMobile.requestFocus();
            return;
        }

        if (binding.etMobile.getText().toString().trim().length() < 10) {
            binding.etMobile.setError(getText(R.string.invalid_mobile));
            binding.etMobile.setEnabled(true);
            binding.etMobile.requestFocus();
            return;
        }

        if (!binding.etMobile.getText().toString().trim().startsWith("05")) {
            binding.etMobile.setError(getText(R.string.invalid_mobile_prefix));
            binding.etMobile.setEnabled(true);
            binding.etMobile.requestFocus();
            return;
        }
        binding.etMobile.setError(null);

        if (selectedMemberType == null) {
            showMessage(R.string.choose_membership_type);
            return;
        }

        if (binding.etQuote.getText().toString().trim().isEmpty()) {
            binding.etQuote.setError(getText(R.string.empty_personal_quote));
            binding.etQuote.requestFocus();
            return;
        }
        binding.etQuote.setError(null);

        if (!binding.etNewPassword.getText().toString().trim().isEmpty() && binding.etNewPassword.getText().toString().trim().length() < 6) {
            binding.etNewPassword.setError(getText(R.string.invalid_password));
            binding.etNewPassword.requestFocus();
            return;
        }

        if (!binding.etNewPassword.getText().toString().trim().equals(binding.etConfirmNewPassword.getText().toString().trim())) {
            binding.etNewPassword.setError(getText(R.string.passwords_not_matched));
            binding.etConfirmNewPassword.setError(getText(R.string.passwords_not_matched));
            binding.etConfirmNewPassword.requestFocus();
            return;
        }

        binding.etNewPassword.setError(null);
        binding.etConfirmNewPassword.setError(null);

        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);

        if (profileImg != null) {
            RequestBody requestBody;

            try {
                requestBody = RequestBody.create(MediaType.parse("image/*"), new ImageZipper(this).compressToFile(profileImg));
            } catch (IOException e) {
                e.printStackTrace();
                requestBody = RequestBody.create(MediaType.parse("image/*"), profileImg);
            }

            builder.addFormDataPart("image", profileImg.getName(), requestBody);

        }

        builder.addFormDataPart("mobile", binding.etMobile.getText().toString().trim());
        builder.addFormDataPart("name", binding.etFullName.getText().toString().trim());
        builder.addFormDataPart("_method", "PUT");
        builder.addFormDataPart("description", binding.etQuote.getText().toString().trim());
        builder.addFormDataPart("user_type_id", selectedMemberType);

        if (!binding.etEmail.getText().toString().trim().isEmpty())
            builder.addFormDataPart("email", binding.etEmail.getText().toString().trim());

        if (!binding.etNewPassword.getText().toString().trim().isEmpty())
            builder.addFormDataPart("password", binding.etNewPassword.getText().toString().trim());


        showLoading();
        mViewModel.updateProfile(builder);
    }

    @Override
    public void performDependencyInjection(ActivityComponent buildComponent) {
        buildComponent.inject(this);
    }

    @Override
    public void handleError(Throwable throwable) {
        hideLoading();
        ErrorHandlingUtils.handleErrors(throwable);
    }

    @Override
    public void showMyApiMessage(String message) {
        hideLoading();
        showErrorMessage(message);
    }

    @Override
    public void wrongPassword() {
        hideLoading();
        showMessage(R.string.wrong_password);
    }

}
