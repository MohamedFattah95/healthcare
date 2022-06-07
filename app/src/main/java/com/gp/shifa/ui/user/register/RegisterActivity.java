package com.gp.shifa.ui.user.register;

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
import com.gp.shifa.databinding.ActivityRegisterBinding;
import com.gp.shifa.di.component.ActivityComponent;
import com.gp.shifa.ui.base.BaseActivity;
import com.gp.shifa.utils.CommonUtils;
import com.gp.shifa.utils.ErrorHandlingUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class RegisterActivity extends BaseActivity<RegisterViewModel> implements RegisterNavigator {

    private ActivityRegisterBinding binding;

    private static final int PICK_PROFILE_IMG = 123;

    File profileImg = null;

    int countryId;
    int areaId;
    String date = "1986-6-6";
    String gender = "male";

    public static Intent newIntent(Context context) {
        return new Intent(context, RegisterActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        mViewModel.setNavigator(this);

        setUp();

    }

    private void setUp() {
        subscribeViewModel();
        setUpViewClicked();

        mViewModel.getCountriesAndAreas();
    }

    private void subscribeViewModel() {

        mViewModel.getCountriesAndAreasLiveData().observe(this, response -> {

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

            countryId = response.getData().get(0).getId();
            areaId = response.getData().get(0).getAreas().get(0).getId();

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
        });

        mViewModel.getRegistrationLiveData().observe(this, response -> {
            hideLoading();
            showMyApiMessage(response.getMessage());
            finish();
        });
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

    @Override
    public void performDependencyInjection(ActivityComponent buildComponent) {
        buildComponent.inject(this);
    }

    public void setUpViewClicked() {

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

        binding.rbFemale.setOnClickListener(v -> gender = "female");
        binding.rbMale.setOnClickListener(v -> gender = "male");

        binding.btnSignUp.setOnClickListener(v -> {

            if (profileImg == null) {
                showMessage(R.string.image_required);
                return;
            }

            if (binding.etName.getText().toString().trim().isEmpty()) {
                binding.etName.setError(getText(R.string.empty_full_name));
                binding.etName.requestFocus();
                return;
            }
            binding.etName.setError(null);

            if (binding.etEmail.getText().toString().trim().isEmpty()
                    || !CommonUtils.isEmailValid(binding.etEmail.getText().toString().trim())) {
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

            if (binding.etPassword.getText().toString().trim().isEmpty()) {
                binding.etPassword.setError(getText(R.string.empty_password));
                binding.etPassword.requestFocus();
                return;
            }

            if (binding.etPassword.getText().toString().trim().length() < 8) {
                binding.etPassword.setError(getText(R.string.invalid_password));
                binding.etPassword.requestFocus();
                return;
            }

            binding.etPassword.setError(null);

            showLoading();

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
            builder.addFormDataPart("name", binding.etName.getText().toString().trim());
            builder.addFormDataPart("email", binding.etEmail.getText().toString().trim());
            builder.addFormDataPart("date", date);
            builder.addFormDataPart("gander", gender);
            builder.addFormDataPart("governorate_id", String.valueOf(countryId));
            builder.addFormDataPart("area_id", String.valueOf(areaId));
            builder.addFormDataPart("password", binding.etPassword.getText().toString().trim());

            mViewModel.doRegistration(builder);


        });

        binding.tvLogin.setOnClickListener(v -> finish());

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
