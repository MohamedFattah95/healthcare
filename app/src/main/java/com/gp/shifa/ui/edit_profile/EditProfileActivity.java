
package com.gp.shifa.ui.edit_profile;

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
import com.gp.shifa.databinding.ActivityEditProfileBinding;
import com.gp.shifa.di.component.ActivityComponent;
import com.gp.shifa.ui.base.BaseActivity;
import com.gp.shifa.utils.CommonUtils;
import com.gp.shifa.utils.ErrorHandlingUtils;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class EditProfileActivity extends BaseActivity<EditProfileViewModel> implements EditProfileNavigator {

    private static final int PICK_PROFILE_IMG = 123;

    UserModel userModel = null;

    File profileImg = null;


    int countryId;
    int areaId;
    String date = "1986-6-6";
    String gender = "male";

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

        mViewModel.getCountriesAndAreasLiveData().observe(this, response -> {

            hideLoading();

            countryId = userModel.getUser().getGovernorateId();
            areaId = userModel.getUser().getAreaId();

            final int[] selectedCountryPosition = {0};

            List<String> countries = new ArrayList<>();
            List<String> areas = new ArrayList<>();

            for (int i = 0; i < response.getData().size(); i++) {
                countries.add(response.getData().get(i).getName());
            }

            ArrayAdapter<String> adapter =
                    new ArrayAdapter<>(
                            this,
                            R.layout.spinner_item,
                            countries);

            binding.spGovs.setAdapter(adapter);

            ArrayAdapter<String> areasAdapter =
                    new ArrayAdapter<>(
                            this,
                            R.layout.spinner_item,
                            areas);

            binding.spAreas.setAdapter(areasAdapter);

            binding.spGovs.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                    areas.clear();
                    selectedCountryPosition[0] = position;
                    countryId = response.getData().get(position).getId();
                    areaId = response.getData().get(position).getAreas().get(0).getId();

                    for (int i = 0; i < response.getData().get(position).getAreas().size(); i++) {
                        areas.add(response.getData().get(position).getAreas().get(i).getName());

                        if (i == response.getData().get(position).getAreas().size() - 1) {
                            areasAdapter.notifyDataSetChanged();

                            for (int k = 0; k < response.getData().get(position).getAreas().size(); k++) {
                                if (response.getData().get(position).getAreas().get(k).getId()
                                        == userModel.getUser().getAreaId()) {
                                    binding.spAreas.setSelection(k, true);
                                    break;
                                }
                            }
                        }

                    }

                }

                @Override
                public void onNothingSelected(AdapterView<?> parentView) {
                }

            });

            binding.spAreas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                    areaId = response.getData().get(selectedCountryPosition[0]).getAreas().get(position).getId();


                }

                @Override
                public void onNothingSelected(AdapterView<?> parentView) {
                }

            });

            for (int i = 0; i < response.getData().size(); i++) {
                if (response.getData().get(i).getId() == userModel.getUser().getGovernorateId()) {
                    binding.spGovs.setSelection(i, true);
                    break;
                }
            }

        });

        mViewModel.getProfileLiveData().observe(this, response -> {

            mViewModel.getCountriesAndAreas();

            userModel = response;

            if (profileImg == null) {
                Glide.with(this)
                        .load(userModel.getUser().getImgSrc()
                                + "/" + userModel.getUser().getImg())
                        .placeholder(R.drawable.ic_user_holder)
                        .error(R.drawable.ic_user_holder)
                        .into(binding.civProfilePic);
                profileImg = null;
                binding.ivEditProfilePic.setImageResource(R.drawable.ic_camera);
            }

            if (userModel.getUser().getName() != null)
                binding.etFullName.setText(userModel.getUser().getName());

            if (userModel.getUser().getEmail() != null)
                binding.etEmail.setText(userModel.getUser().getEmail());

            if (userModel.getUser().getPhone() != null)
                binding.etMobile.setText(userModel.getUser().getPhone());

            binding.ivEditProfilePic.setImageResource(R.drawable.ic_camera);
            binding.etMobile.setEnabled(false);

            if (userModel.getUser().getGander().equalsIgnoreCase("male")) {
                gender = "male";
                binding.rbMale.setChecked(true);
                binding.rbFemale.setChecked(false);
            } else {
                gender = "female";
                binding.rbMale.setChecked(false);
                binding.rbFemale.setChecked(true);
            }

        });

        mViewModel.getUpdateProfileLiveData().observe(this, response -> {
            hideLoading();
            EventBus.getDefault().post("update_profile");
            showSuccessMessage(response.getMessage());
            finish();
        });

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

        binding.rbFemale.setOnClickListener(v -> gender = "female");
        binding.rbMale.setOnClickListener(v -> gender = "male");

        binding.btnSave.setOnClickListener(v -> validateProfile());

    }

    private void validateProfile() {

        if (binding.etFullName.getText().toString().trim().isEmpty()) {
            binding.etFullName.setError(getText(R.string.empty_full_name));
            binding.etFullName.requestFocus();
            return;
        }
        binding.etFullName.setError(null);

        if (binding.etEmail.getText().toString().trim().isEmpty() || !CommonUtils.isEmailValid(binding.etEmail.getText().toString().trim())) {
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
        binding.etMobile.setError(null);

        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);

        if (profileImg != null) {
            RequestBody requestBody;

            try {
                requestBody = RequestBody.create(MediaType.parse("image/*"), new ImageZipper(this).compressToFile(profileImg));
            } catch (IOException e) {
                e.printStackTrace();
                requestBody = RequestBody.create(MediaType.parse("image/*"), profileImg);
            }

            builder.addFormDataPart("img", profileImg.getName(), requestBody);

        }

        builder.addFormDataPart("phone", binding.etMobile.getText().toString().trim());
        builder.addFormDataPart("name", binding.etFullName.getText().toString().trim());
        builder.addFormDataPart("email", binding.etEmail.getText().toString().trim());
        builder.addFormDataPart("date", date);
        builder.addFormDataPart("gander", gender);
        builder.addFormDataPart("governorate_id", String.valueOf(countryId));
        builder.addFormDataPart("area_id", String.valueOf(areaId));

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
