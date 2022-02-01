
package com.gp.health.ui.adding_ad.add_ad_info_desc;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;

import com.afollestad.materialdialogs.MaterialDialog;
import com.developers.imagezipper.ImageZipper;
import com.google.android.gms.maps.model.LatLng;
import com.gp.health.R;
import com.gp.health.data.firebase.ItemsFirebase;
import com.gp.health.data.models.AdAndOrderModel;
import com.gp.health.data.models.MediaModel;
import com.gp.health.databinding.ActivityAddAdInfoDescBinding;
import com.gp.health.databinding.DialogSuccessAddingAdBinding;
import com.gp.health.di.component.ActivityComponent;
import com.gp.health.ui.base.BaseActivity;
import com.gp.health.ui.profile.MyProfileActivity;
import com.gp.health.utils.ErrorHandlingUtils;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class AddAdInfoDescActivity extends BaseActivity<AddAdInfoDescViewModel> implements AddAdInfoDescNavigator {

    ActivityAddAdInfoDescBinding binding;

    int selectedCategoryId = -1;
    ArrayList<MediaModel> imgList = new ArrayList<>();
    ArrayList<MediaModel> vidList = new ArrayList<>();
    LatLng mLatLng;
    HashMap<String, String> optionsMap = new HashMap<>();

    AdAndOrderModel adDetails = null;

    int selectedOwnerId = 1;
    String selectedCity = null;

    public static Intent newIntent(Context context) {
        return new Intent(context, AddAdInfoDescActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddAdInfoDescBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mViewModel.setNavigator(this);

        binding.toolbar.toolbarTitle.setText(R.string.ad_info_decs);
        setUp();

    }

    private void setUp() {

        selectedCategoryId = getIntent().getIntExtra("categoryId", -1);
        imgList = (ArrayList<MediaModel>) getIntent().getSerializableExtra("imgList");
        vidList = (ArrayList<MediaModel>) getIntent().getSerializableExtra("vidList");
        mLatLng = getIntent().getParcelableExtra("latLng");
        optionsMap = (HashMap<String, String>) getIntent().getSerializableExtra("options");

        if (getIntent().hasExtra("adDetails"))
            adDetails = (AdAndOrderModel) getIntent().getSerializableExtra("adDetails");

        setUpOnViewClicked();
        subscribeViewModel();

        if (adDetails != null) {

            binding.etAdName.setText(adDetails.getTitle());
            selectedCity = String.valueOf(adDetails.getCountry());
            selectedOwnerId = adDetails.getWonerRelationId();
            binding.etArea.setText(adDetails.getSpace().replace(",", ""));
            binding.etPrice.setText(adDetails.getPrice().replace(",", ""));
            binding.etDesc.setText(adDetails.getDescription());

            if (adDetails.getWonerRelationId() == 1) {
                binding.rbOwner.setChecked(true);
                binding.llPercent.setVisibility(View.VISIBLE);
                binding.tvOwnerPercent.setVisibility(View.VISIBLE);
                binding.etPercentage.setText(adDetails.getWonerPerc());
            } else if (adDetails.getWonerRelationId() == 2) {
                binding.rbAgent.setChecked(true);
                binding.llPercent.setVisibility(View.GONE);
                binding.tvOwnerPercent.setVisibility(View.GONE);
            } else if (adDetails.getWonerRelationId() == 3) {
                binding.rbMarketer.setChecked(true);
                binding.llPercent.setVisibility(View.GONE);
                binding.tvOwnerPercent.setVisibility(View.GONE);
            }


            binding.cbAcceptTerms.setChecked(true);

            binding.btnPublish.setText(R.string.save);

        }


        Geocoder arGeoCoder = new Geocoder(this, new Locale("ar"));
        String arCityName = null;
        String arSubCityName = null;

        try {

            arCityName = arGeoCoder.getFromLocation(mLatLng.latitude, mLatLng.longitude, 1).get(0).getLocality();
            arSubCityName = arGeoCoder.getFromLocation(mLatLng.latitude, mLatLng.longitude, 1).get(0).getSubLocality();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {

            if (arCityName != null && arSubCityName != null)
                selectedCity = arCityName + "," + arSubCityName;
            else
                selectedCity = arCityName;

        }


    }

    private void subscribeViewModel() {

        mViewModel.getSubmitAdLiveData().observe(this, response -> {
            hideLoading();
            showSuccessMessage(response.getMessage());

            ItemsFirebase.addItemLocation(response.getData().getId(), mLatLng.latitude, mLatLng.longitude);

            EventBus.getDefault().post("finish_ad");

            showAddingAdSuccessfullyDialog();
        });

        mViewModel.getUpdateAdLiveData().observe(this, response -> {
            ItemsFirebase.removeItemLocation(adDetails.getId());
            hideLoading();
            showSuccessMessage(response.getMessage());

            EventBus.getDefault().post("finish_ad");

            showAddingAdSuccessfullyDialog();
            ItemsFirebase.addItemLocation(adDetails.getId(), mLatLng.latitude, mLatLng.longitude);
        });

    }

    public void setUpOnViewClicked() {

        binding.toolbar.backButton.setOnClickListener(v -> finish());

        binding.rbOwner.setOnClickListener(v -> {
            selectedOwnerId = 1;
            binding.llPercent.setVisibility(View.VISIBLE);
            binding.tvOwnerPercent.setVisibility(View.VISIBLE);
        });
        binding.rbAgent.setOnClickListener(v -> {
            selectedOwnerId = 2;
            binding.llPercent.setVisibility(View.GONE);
            binding.tvOwnerPercent.setVisibility(View.GONE);
        });
        binding.rbMarketer.setOnClickListener(v -> {
            selectedOwnerId = 3;
            binding.llPercent.setVisibility(View.GONE);
            binding.tvOwnerPercent.setVisibility(View.GONE);
        });

        binding.btnPublish.setOnClickListener(v -> {

            if (binding.etAdName.getText().toString().trim().isEmpty()) {
                binding.etAdName.setError(getText(R.string.empty_ad_name));
                binding.etAdName.requestFocus();
                return;
            }
            binding.etAdName.setError(null);

            if (selectedCity == null) {
                selectedCity = "غير_محدد";
            }

            if (binding.etArea.getText().toString().trim().isEmpty()) {
                binding.etArea.setError(getText(R.string.empty_area));
                binding.etArea.requestFocus();
                return;
            }

            try {
                if (Double.parseDouble(binding.etArea.getText().toString().trim()) == 0) {
                    binding.etArea.setError(getText(R.string.area_greater_0));
                    binding.etArea.requestFocus();
                    return;
                }
                binding.etArea.setError(null);
            } catch (NumberFormatException e) {
                e.printStackTrace();
                binding.etArea.setError(getText(R.string.invalid_area));
                binding.etArea.requestFocus();
                return;
            }

            if (binding.etPrice.getText().toString().trim().isEmpty()) {
                binding.etPrice.setError(getText(R.string.empty_price));
                binding.etPrice.requestFocus();
                return;
            }

            try {
                if (Double.parseDouble(binding.etPrice.getText().toString().trim()) == 0) {
                    binding.etPrice.setError(getText(R.string.price_greater_0));
                    binding.etPrice.requestFocus();
                    return;
                }
                binding.etPrice.setError(null);
            } catch (NumberFormatException e) {
                e.printStackTrace();
                binding.etPrice.setError(getText(R.string.invalid_price));
                binding.etPrice.requestFocus();
                return;
            }

            if (binding.etDesc.getText().toString().trim().isEmpty()) {
                binding.etDesc.setError(getText(R.string.empty_desc));
                binding.etDesc.requestFocus();
                return;
            }

            if (selectedOwnerId == 1) {
                if (binding.etPercentage.getText().toString().trim().isEmpty()) {
                    binding.etPercentage.setError(getText(R.string.empty_percentage));
                    binding.etPercentage.requestFocus();
                    return;
                }

                if (Integer.parseInt(binding.etPercentage.getText().toString().trim()) > 100) {
                    binding.etPercentage.setError(getText(R.string.invalid_percentage));
                    binding.etPercentage.requestFocus();
                    return;
                }
            }

            if (!binding.cbAcceptTerms.isChecked()) {
                showMessage(R.string.accept_terms_to_continue);
                return;
            }

            MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);

            // putting dynamic options in builder
            for (Map.Entry<String, String> entry : optionsMap.entrySet()) {
                builder.addFormDataPart(entry.getKey(), entry.getValue());
            }

            builder.addFormDataPart("item_type_id", 1 + "");
            builder.addFormDataPart("title", binding.etAdName.getText().toString().trim());
            builder.addFormDataPart("country", selectedCity);
            builder.addFormDataPart("space", binding.etArea.getText().toString().trim());
            builder.addFormDataPart("price", binding.etPrice.getText().toString().trim());
            builder.addFormDataPart("description", binding.etDesc.getText().toString().trim());

            if (selectedOwnerId == 1) {
                builder.addFormDataPart("woner_perc", binding.etPercentage.getText().toString().trim());
            }

            builder.addFormDataPart("woner_relation_id", selectedOwnerId + "");
            builder.addFormDataPart("category_id", selectedCategoryId + "");
            builder.addFormDataPart("lat", mLatLng.latitude + "");
            builder.addFormDataPart("lng", mLatLng.longitude + "");
            builder.addFormDataPart("has_images", !imgList.isEmpty() ? "1" : "0");


            for (int i = 0; i < imgList.size(); i++) {

                if (imgList.get(i) != null && imgList.get(i).getFileName() != null) {

                    if (imgList.get(i).getFileName().contains("storage/emulated")) {
                        /*image from user device*/
                        RequestBody requestBody;
                        try {
                            requestBody = RequestBody.create(MediaType.parse("image/*"), new ImageZipper(this).compressToFile(new File(imgList.get(i).getFileName())));
                        } catch (IOException e) {
                            e.printStackTrace();
                            requestBody = RequestBody.create(MediaType.parse("image/*"), new File(imgList.get(i).getFileName()));
                        }

                        builder.addFormDataPart("images[" + i + "]", new File(imgList.get(i).getFileName()).getName(), requestBody);

                    }

//                    else {
//                        /*url image*/
//                        builder.addFormDataPart("images[" + i + "]", imgList.get(i).getFileName());
//
//                    }

                }
            }


            if (!vidList.isEmpty() && vidList.get(0) != null && vidList.get(0).getFileName() != null) {

                if (vidList.get(0).getFileName().contains("storage/emulated")) {
                    /*video from user device*/
                    RequestBody requestBody = RequestBody.create(MediaType.parse("video/*"), new File(vidList.get(0).getFileName()));
                    builder.addFormDataPart("video", new File(vidList.get(0).getFileName()).getName(), requestBody);

                }


//                else {
//                    /*url video*/
//                    builder.addFormDataPart("video", vidList.get(0).getFileName());
//
//                }

            }


            showLoading();

            if (adDetails == null)
                mViewModel.submitAd(builder);
            else {

                builder.addFormDataPart("_method", "put");
                mViewModel.updateAd(adDetails.getId(), builder);

            }


        });

    }

    private void showAddingAdSuccessfullyDialog() {
        DialogSuccessAddingAdBinding addingAdBinding = DialogSuccessAddingAdBinding.inflate(getLayoutInflater());
        MaterialDialog dialog = new MaterialDialog.Builder(this)
                .customView(addingAdBinding.getRoot(), true)
                .cancelable(false).build();

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        if (adDetails != null)
            addingAdBinding.tvMsg.setText(R.string.ad_edited_successfully);

        addingAdBinding.btnOk.setOnClickListener(v -> {

            dialog.dismiss();
            finish();

            startActivity(MyProfileActivity.newIntent(this).putExtra("from_adding_ad", true));

        });

        dialog.show();
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
}
