package com.gp.shifa.ui.user.complete_profile;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.developers.imagezipper.ImageZipper;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.gp.shifa.R;
import com.gp.shifa.data.models.UserModel;
import com.gp.shifa.databinding.ActivityCompleteProfileBinding;
import com.gp.shifa.di.component.ActivityComponent;
import com.gp.shifa.ui.base.BaseActivity;
import com.gp.shifa.ui.main.MainActivity;
import com.gp.shifa.utils.CommonUtils;
import com.gp.shifa.utils.ErrorHandlingUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class CompleteProfileActivity extends BaseActivity<CompleteProfileViewModel> implements CompleteProfileNavigator {

    private static final int PICK_PROFILE_IMG = 123;

    File profileImg = null;
    String selectedMemberType = null;

    private ActivityCompleteProfileBinding binding;

    public static Intent newIntent(Context context) {
        return new Intent(context, CompleteProfileActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCompleteProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mViewModel.setNavigator(this);

        setUp();

    }

    private void setUp() {

        setUpViewClicked();
        subscribeViewModel();

        showLoading();
        mViewModel.getUserDate();
        mViewModel.getMemberTypes();

    }

    private void subscribeViewModel() {

        mViewModel.getUpdateProfileLiveData().observe(this, response -> {
            hideLoading();
            binding.etMobile.setEnabled(false);
            binding.etFullName.setEnabled(false);
            startActivity(getIntentWithClearHistory(MainActivity.class));
        });

        mViewModel.getMemberTypesLiveData().observe(this, response -> {

            List<String> memberTypes = new ArrayList<>();
            memberTypes.add(getString(R.string.choose_membership_type));

            for (int i = 0; i < response.getData().size(); i++) {
                memberTypes.add(response.getData().get(i).getName());
            }

            ArrayAdapter<String> adapter =
                    new ArrayAdapter<>(
                            this,
                            R.layout.spinner_item,
                            memberTypes);

            binding.spMemberType.setAdapter(adapter);

            binding.spMemberType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                    if (position != 0) {
                        selectedMemberType = String.valueOf(response.getData().get(position - 1).getId());
                    } else {
                        selectedMemberType = null;
                    }

                }

                @Override
                public void onNothingSelected(AdapterView<?> parentView) {
                    selectedMemberType = null;
                }

            });

        });
    }

    @Override
    public void performDependencyInjection(ActivityComponent buildComponent) {
        buildComponent.inject(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
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

    public void setUpViewClicked() {

        binding.ivBack.setOnClickListener(v -> finish());
        binding.tvSkip.setOnClickListener(v -> startActivity(getIntentWithClearHistory(MainActivity.class)));

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

        binding.tilName.setEndIconOnClickListener(v -> {
            binding.etFullName.setEnabled(!binding.etFullName.isEnabled());
            binding.etFullName.requestFocus();
            showKeyboard();
        });

        binding.tilMobile.setEndIconOnClickListener(v -> {
            binding.etMobile.setEnabled(!binding.etMobile.isEnabled());
            binding.etMobile.requestFocus();
            showKeyboard();
        });

        binding.btnSubmit.setOnClickListener(v -> {

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

            if (binding.etMobile.getText().toString().trim().length() < 10) {
                binding.etMobile.setError(getText(R.string.invalid_mobile));
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

            if (!binding.etPassword.getText().toString().trim().isEmpty() && binding.etPassword.getText().toString().trim().length() < 6) {
                binding.etPassword.setError(getText(R.string.invalid_password));
                binding.etPassword.requestFocus();
                return;
            }

            if (!binding.etPassword.getText().toString().trim().equals(binding.etConfirmPassword.getText().toString().trim())) {
                binding.etPassword.setError(getText(R.string.passwords_not_matched));
                binding.etConfirmPassword.setError(getText(R.string.passwords_not_matched));
                binding.etConfirmPassword.requestFocus();
                return;
            }
            binding.etPassword.setError(null);
            binding.etConfirmPassword.setError(null);

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

            if (!binding.etPassword.getText().toString().trim().isEmpty())
                builder.addFormDataPart("password", binding.etPassword.getText().toString().trim());


            showLoading();
            mViewModel.updateProfile(builder);

        });

    }

    @Override
    public void setUserData(UserModel userModel) {

        hideLoading();
        Glide.with(this)
                .load(R.drawable.ic_user_holder)
                .placeholder(R.drawable.ic_user_holder)
                .error(R.drawable.ic_user_holder)
                .into(binding.civProfilePic);

        binding.etMobile.setText(userModel.getUser().getPhone());
        binding.etFullName.setText(userModel.getUser().getName());

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
}