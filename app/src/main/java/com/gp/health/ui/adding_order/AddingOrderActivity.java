
package com.gp.health.ui.adding_order;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.gp.health.R;
import com.gp.health.data.firebase.ItemsFirebase;
import com.gp.health.data.models.AdAndOrderModel;
import com.gp.health.databinding.ActivityAddingOrderBinding;
import com.gp.health.databinding.DialogSuccessOrderCreationBinding;
import com.gp.health.di.component.ActivityComponent;
import com.gp.health.ui.adding_ad.add_ad_details.DynamicSpecsAdapter;
import com.gp.health.ui.base.BaseActivity;
import com.gp.health.ui.profile.MyProfileActivity;
import com.gp.health.utils.ErrorHandlingUtils;
import com.gp.health.utils.LocationUtils;
import com.gp.health.utils.location.GpsUtils;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.inject.Inject;

import io.nlopez.smartlocation.SmartLocation;
import okhttp3.MultipartBody;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

public class AddingOrderActivity extends BaseActivity<AddingOrderViewModel> implements AddingOrderNavigator, OnMapReadyCallback,
        DynamicSpecsAdapter.Callback, GoogleMap.OnCameraIdleListener, EasyPermissions.PermissionCallbacks {

    public static final int REQUEST_LOCATION_CODE = 444;
    String[] perms = {Manifest.permission.ACCESS_FINE_LOCATION};

    @Inject
    LinearLayoutManager specsLinearLayoutManager;
    @Inject
    DynamicSpecsAdapter dynamicSpecsAdapter;

    GoogleMap map;

    ActivityAddingOrderBinding binding;

    String selectedPropertyCategory = null;
    String selectedCity = null;
    LatLng mLatLng = null;

    HashMap<String, String> optionsMap = new HashMap<>();

    AdAndOrderModel orderDetails = null;

    public static Intent newIntent(Context context) {
        return new Intent(context, AddingOrderActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddingOrderBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mViewModel.setNavigator(this);
        dynamicSpecsAdapter.setCallback(this);

        EasyPermissions.requestPermissions(this, getString(R.string.location_rationale), REQUEST_LOCATION_CODE, perms);

        binding.mMapView.onCreate(savedInstanceState);
        binding.mMapView.getMapAsync(this);

        binding.toolbar.toolbarTitle.setText(R.string.order_details);
        setUp();

    }

    private void setUp() {

        setupSpecsAdapter();
        setUpOnViewClicked();
        subscribeViewModel();

        if (getIntent().hasExtra("orderDetails"))
            orderDetails = (AdAndOrderModel) getIntent().getSerializableExtra("orderDetails");

        showLoading();
        mViewModel.getCategories();

        if (orderDetails != null) {

            binding.etLowPrice.setText(orderDetails.getPriceMin());
            binding.etHighPrice.setText(orderDetails.getPriceMax());
            binding.cbHasImages.setChecked(orderDetails.getHasImages() == 1);

            binding.sendBtn.setText(R.string.save);
        }
    }

    private void setupSpecsAdapter() {
        binding.rvDynamicSpecs.setLayoutManager(specsLinearLayoutManager);
        binding.rvDynamicSpecs.setAdapter(dynamicSpecsAdapter);
    }

    private void subscribeViewModel() {

        mViewModel.getCategoriesLiveData().observe(this, response -> {
            hideLoading();
            List<String> categories = new ArrayList<>();
            categories.add(getString(R.string.all));

            for (int i = 0; i < response.getData().size(); i++) {
                categories.add(response.getData().get(i).getTitle());
            }

            ArrayAdapter<String> adapter =
                    new ArrayAdapter<>(
                            this,
                            R.layout.spinner_item,
                            categories);

            binding.spPropertyCategory.setAdapter(adapter);

            binding.spPropertyCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                    if (position != 0) {
                        selectedPropertyCategory = String.valueOf(response.getData().get(position - 1).getId());
                        showLoading();
                        dynamicSpecsAdapter.clearItems();
                        mViewModel.getSpecOptions(selectedPropertyCategory);
                    } else {
                        selectedPropertyCategory = null;
                        dynamicSpecsAdapter.clearItems();
                    }

                    optionsMap.clear();

                }

                @Override
                public void onNothingSelected(AdapterView<?> parentView) {
                }

            });

            if (orderDetails != null) {

                for (int i = 0; i < response.getData().size(); i++) {
                    if (orderDetails.getCategories() != null && !orderDetails.getCategories().isEmpty() && orderDetails.getCategories().get(0) != null &&
                            response.getData().get(i).getId() == orderDetails.getCategories().get(0).getId()) {
                        binding.spPropertyCategory.setSelection(i + 1);
                        selectedPropertyCategory = String.valueOf(orderDetails.getCategories().get(0).getId());
                        break;
                    } else if (i == response.getData().size() - 1) {
                        selectedPropertyCategory = null;
                        dynamicSpecsAdapter.clearItems();
                    }
                }

            }

        });


        mViewModel.getSpecOptionLiveData().observe(this, response -> {
            hideLoading();

            if (orderDetails == null)
                dynamicSpecsAdapter.addItems(response.getData());
            else {

                for (int i = 0; i < response.getData().size(); i++) {
                    for (int k = 0; k < orderDetails.getOptions().size(); k++) {

                        if (response.getData().get(i).getOptionId() == orderDetails.getOptions().get(k).getOptionId()) {
                            if (response.getData().get(i).getOptionType().equalsIgnoreCase("text") ||
                                    response.getData().get(i).getOptionType().equalsIgnoreCase("number")) {
                                response.getData().get(i).setOptionValue(orderDetails.getOptions().get(k).getOptionValue());
                            } else if (response.getData().get(i).getOptionType().equalsIgnoreCase("checkbox")) {

                                for (int z = 0; z < response.getData().get(i).getOptionValues().size(); z++) {

                                    if (orderDetails.getOptions().get(k).getOptionType().equalsIgnoreCase("checkbox")) {

                                        for (int x = 0; x < orderDetails.getOptions().get(k).getItemOptionValues().size(); x++) {

                                            if (orderDetails.getOptions().get(k).getItemOptionValues().get(x).getOptionValueId() ==
                                                    response.getData().get(i).getOptionValues().get(z).getOptionValueId())
                                                response.getData().get(i).getOptionValues().get(z).setSelected(true);
                                        }
                                    }

                                }


                            } else if (response.getData().get(i).getOptionType().equalsIgnoreCase("select")) {
                                if (orderDetails.getOptions().get(k).getItemOptionValues() != null && !orderDetails.getOptions().get(k).getItemOptionValues().isEmpty())
                                    response.getData().get(i).setSelectedOptionValue(orderDetails.getOptions().get(k).getItemOptionValues().get(0));
                            } else {
                                response.getData().get(i).setSelectedOptionValue(null);
                            }
                            break;
                        }

                    }

                    if (i == response.getData().size() - 1) {
                        dynamicSpecsAdapter.addItems(response.getData());
                    }
                }


            }

        });

        mViewModel.getSubmitOrderLiveData().observe(this, response -> {
            hideLoading();

            ItemsFirebase.addItemLocation(response.getData().getId(), mLatLng.latitude, mLatLng.longitude);

            showSuccessMessage(response.getMessage());
            showSuccessOrderCreationDialog();
        });

        mViewModel.getUpdateOrderLiveData().observe(this, response -> {
            ItemsFirebase.removeItemLocation(orderDetails.getId());
            hideLoading();

            showSuccessMessage(response.getMessage());
            showSuccessOrderCreationDialog();

            ItemsFirebase.addItemLocation(orderDetails.getId(), mLatLng.latitude, mLatLng.longitude);
        });

    }

    public void setUpOnViewClicked() {

        binding.toolbar.backButton.setOnClickListener(v -> finish());

        binding.sendBtn.setOnClickListener(v -> {

            if (selectedCity == null) {
                selectedCity = "غير_محدد";
            }

            if (selectedPropertyCategory == null) {
                showMessage(R.string.choose_desired_property);
                return;
            }

            if (binding.etLowPrice.getText().toString().isEmpty()) {
                binding.etLowPrice.setError(getText(R.string.empty_low_price));
                binding.etLowPrice.requestFocus();
                return;
            }
            binding.etLowPrice.setError(null);

            if (binding.etHighPrice.getText().toString().isEmpty()) {
                binding.etHighPrice.setError(getText(R.string.empty_high_price));
                binding.etHighPrice.requestFocus();
                return;
            }
            binding.etHighPrice.setError(null);

            if (Double.parseDouble(binding.etHighPrice.getText().toString()) <= Double.parseDouble(binding.etLowPrice.getText().toString())) {
                binding.etLowPrice.setError(getText(R.string.invalid_low_high_price));
                binding.etHighPrice.setError(getText(R.string.invalid_low_high_price));
                binding.etLowPrice.requestFocus();
                return;
            }
            binding.etLowPrice.setError(null);
            binding.etHighPrice.setError(null);

            if (mLatLng == null) {
                showMessage(R.string.select_location);
                return;
            }

            MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);

            // putting dynamic options in builder
            for (Map.Entry<String, String> entry : optionsMap.entrySet()) {
                builder.addFormDataPart(entry.getKey(), entry.getValue());
            }

            builder.addFormDataPart("item_type_id", 2 + "");
            builder.addFormDataPart("country", selectedCity);
            builder.addFormDataPart("category_id", selectedPropertyCategory);
            builder.addFormDataPart("lat", mLatLng.latitude + "");
            builder.addFormDataPart("lng", mLatLng.longitude + "");
            builder.addFormDataPart("price_min", binding.etLowPrice.getText().toString().trim());
            builder.addFormDataPart("price_max", binding.etHighPrice.getText().toString().trim());
            builder.addFormDataPart("has_images", binding.cbHasImages.isChecked() ? "1" : "0");


            showLoading();

            if (orderDetails == null)
                mViewModel.submitOrder(builder);
            else {

                builder.addFormDataPart("_method", "put");
                mViewModel.updateOrder(orderDetails.getId(), builder);
            }

        });

    }

    private void showSuccessOrderCreationDialog() {
        DialogSuccessOrderCreationBinding orderCreationBinding = DialogSuccessOrderCreationBinding.inflate(getLayoutInflater());
        MaterialDialog dialog = new MaterialDialog.Builder(this)
                .customView(orderCreationBinding.getRoot(), true)
                .cancelable(false).build();

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        if (orderDetails != null)
            orderCreationBinding.tvMsg.setText(R.string.order_edited_successfully);

        orderCreationBinding.btnOk.setOnClickListener(v -> {

            dialog.dismiss();
            finish();

            startActivity(MyProfileActivity.newIntent(this).putExtra("from_adding_order", true));

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

    @Override
    public void noOptions() {
        hideLoading();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NotNull String @NotNull [] permissions, @NotNull int @NotNull [] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        if (map != null)
            map.setMyLocationEnabled(true);
    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        showMessage(R.string.location_rationale);
    }

    public void onCurrentLocationBtnClicked() {
        map.clear();

        if (!EasyPermissions.hasPermissions(this, perms)) {
            if (EasyPermissions.somePermissionPermanentlyDenied(this, Arrays.asList(perms))) {
                new AppSettingsDialog.Builder(this).build().show();
            } else {
                EasyPermissions.requestPermissions(this, getString(R.string.location_rationale), REQUEST_LOCATION_CODE, perms);
            }
        } else {
            if (LocationUtils.isGPSEnabled(this)) {
                SmartLocation.with(this).location().oneFix().start(location -> {
                    mLatLng = new LatLng(location.getLatitude(), location.getLongitude());
                    this.map.animateCamera(CameraUpdateFactory.newLatLngZoom(mLatLng, 15));
                });
            } else {
                gpsCheck();
                mLatLng = this.map.getCameraPosition().target;
                showMessage(R.string.open_gps);
            }
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;

        LatLngBounds ksaBounds = new LatLngBounds(
                new LatLng(16.217169, 34.740374), // SW bounds
                new LatLng(32.177219, 55.196917)  // NE bounds
        );
        // Constrain the camera target to the KSA bounds.
        map.setLatLngBoundsForCameraTarget(ksaBounds);

        map.getUiSettings().setZoomControlsEnabled(true);
        map.getUiSettings().setMyLocationButtonEnabled(true);

        map.setOnMyLocationButtonClickListener(() -> {
            onCurrentLocationBtnClicked();
            return false;
        });

        if (orderDetails != null && orderDetails.getLat() != null && orderDetails.getLng() != null) {

            this.map.animateCamera(CameraUpdateFactory.newLatLngZoom(
                    new LatLng(Double.parseDouble(orderDetails.getLat()), Double.parseDouble(orderDetails.getLng())), 15));

            this.map.setOnCameraIdleListener(this);

        } else {

            if (LocationUtils.isGPSEnabled(this) && hasPermission(Manifest.permission.ACCESS_FINE_LOCATION)) {
                SmartLocation.with(this).location().oneFix().start(location -> {
                    mLatLng = new LatLng(location.getLatitude(), location.getLongitude());
                    this.map.animateCamera(CameraUpdateFactory.newLatLngZoom(mLatLng, 15));
                    this.map.setOnCameraIdleListener(this);
                });

            } else {
                this.map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(24.7135517, 46.6752957), 5f));
                this.map.setOnCameraIdleListener(this);
            }


        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        map.setMyLocationEnabled(true);
        map.setOnMyLocationClickListener(location -> gpsCheck());


    }

    @Override
    public void onResume() {
        binding.mMapView.onResume();
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding.mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        binding.mMapView.onLowMemory();
    }

    private void gpsCheck() {
        if (!LocationUtils.isGPSEnabled(this)) {
            new GpsUtils(this).turnGPSOn(isGPSEnable -> {
            });
        }
    }

    @Override
    public void setOption(String key, String value) {
        optionsMap.put(key, value);
    }

    @Override
    public void removeOption(String key) {
        optionsMap.remove(key);
    }

    @Override
    public void onCameraIdle() {
        mLatLng = this.map.getCameraPosition().target;

        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        List<Address> addresses;
        String countryCode = null;

        Geocoder arGeoCoder = new Geocoder(this, new Locale("ar"));
        String arCityName = null;
        String arSubCityName = null;

        try {
            addresses = geocoder.getFromLocation(mLatLng.latitude, mLatLng.longitude, 1);
            countryCode = addresses.get(0).getCountryCode();

            arCityName = arGeoCoder.getFromLocation(mLatLng.latitude, mLatLng.longitude, 1).get(0).getLocality();
            arSubCityName = arGeoCoder.getFromLocation(mLatLng.latitude, mLatLng.longitude, 1).get(0).getSubLocality();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {

            if (arCityName != null && arSubCityName != null)
                selectedCity = arCityName + "," + arSubCityName;
            else
                selectedCity = arCityName;

            if (countryCode != null) {
                if (!countryCode.equals("SA")) {
                    this.map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(24.774265, 46.738586), 5f));
                    showMessage(R.string.out_of_ksa);
                }
            } else {
                this.map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(24.774265, 46.738586), 5f));
                showMessage(R.string.out_of_ksa);
            }

        }
    }
}
