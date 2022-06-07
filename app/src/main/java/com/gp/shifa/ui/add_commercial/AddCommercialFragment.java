package com.gp.shifa.ui.add_commercial;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.developers.imagezipper.ImageZipper;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.gp.shifa.R;
import com.gp.shifa.data.firebase.ItemsFirebase;
import com.gp.shifa.databinding.FragmentAddCommercialBinding;
import com.gp.shifa.di.component.FragmentComponent;
import com.gp.shifa.ui.base.BaseFragment;
import com.gp.shifa.ui.main.MainActivity;
import com.gp.shifa.utils.ErrorHandlingUtils;
import com.gp.shifa.utils.LanguageHelper;
import com.gp.shifa.utils.LocationUtils;
import com.gp.shifa.utils.location.GpsUtils;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import io.nlopez.smartlocation.SmartLocation;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

public class AddCommercialFragment extends BaseFragment<AddCommercialViewModel> implements AddCommercialNavigator,
        OnMapReadyCallback, EasyPermissions.PermissionCallbacks, GoogleMap.OnCameraIdleListener {

    public static final int REQUEST_LOCATION_CODE = 444;
    String[] perms = {Manifest.permission.ACCESS_FINE_LOCATION};

    FragmentAddCommercialBinding binding;

    GoogleMap map;
    private static final int PICK_LOGO_IMG = 1;
    LatLng mLatLng = null;
    File logoImg = null;
    String selectedDuration = null;

    public static AddCommercialFragment newInstance(int instance) {
        Bundle args = new Bundle();
        args.putInt(BaseFragment.ARGS_INSTANCE, instance);
        AddCommercialFragment fragment = new AddCommercialFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public void refreshData() {
        EasyPermissions.requestPermissions(this, getString(R.string.location_rationale), REQUEST_LOCATION_CODE, perms);
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel.setNavigator(this);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentAddCommercialBinding.inflate(inflater, container, false);

        binding.mMapView.onCreate(savedInstanceState);
        binding.mMapView.getMapAsync(this);

        setUp();
        return binding.getRoot();
    }

    private void setUp() {

        subscribeViewModel();

        mViewModel.getSubscriptionPackages();

        if (mViewModel.getDataManager().getSettingsObject() != null) {

            if (mViewModel.getDataManager().getSettingsObject().getAddCommerce() != null && mViewModel.getDataManager().getSettingsObject().getAddCommerce().contains("</")) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    binding.tvCommercialDesc.setText(Html.fromHtml(mViewModel.getDataManager().getSettingsObject().getAddCommerce(), Html.FROM_HTML_MODE_COMPACT));
                } else {
                    binding.tvCommercialDesc.setText(Html.fromHtml(mViewModel.getDataManager().getSettingsObject().getAddCommerce()));
                }
            } else if (mViewModel.getDataManager().getSettingsObject().getAddCommerce() != null) {
                binding.tvCommercialDesc.setText(mViewModel.getDataManager().getSettingsObject().getAddCommerce());
            }

        }

//        if (mViewModel.getDataManager().isUserLogged() && mViewModel.getDataManager().getUserObject() != null){
//            binding.etEmail.setText(mViewModel.getDataManager().getUserObject().getEmail());
//            binding.etMobile.setText(mViewModel.getDataManager().getUserObject().getMobile());
//        }

        binding.logoImageView.setOnClickListener(v -> ImagePicker.Companion.with(this)
                .crop()
                .compress(1024)
                .maxResultSize(720, 720)
                .start(PICK_LOGO_IMG));

        binding.payBtn.setOnClickListener(v -> {

            if (binding.placeNameET.getText().toString().trim().isEmpty()) {
                binding.placeNameET.setError(getText(R.string.empty_place_name));
                binding.placeNameET.requestFocus();
                return;
            }
            binding.placeNameET.setError(null);

//            if (!binding.etEmail.getText().toString().trim().isEmpty() && !CommonUtils.isEmailValid(binding.etEmail.getText().toString().trim())) {
//                binding.etEmail.setError(getText(R.string.invalid_email));
//                binding.etEmail.requestFocus();
//                return;
//            }
//            binding.etEmail.setError(null);
//
//            if (binding.etMobile.getText().toString().trim().isEmpty()) {
//                binding.etMobile.setError(getText(R.string.empty_mobile));
//                binding.etMobile.requestFocus();
//                return;
//            }
//
//            if (binding.etMobile.getText().toString().trim().length() < 10) {
//                binding.etMobile.setError(getText(R.string.invalid_mobile));
//                binding.etMobile.setEnabled(true);
//                binding.etMobile.requestFocus();
//                return;
//            }
//
//            if (!binding.etMobile.getText().toString().trim().startsWith("05")) {
//                binding.etMobile.setError(getText(R.string.invalid_mobile_prefix));
//                binding.etMobile.setEnabled(true);
//                binding.etMobile.requestFocus();
//                return;
//            }
//            binding.etMobile.setError(null);

            if (selectedDuration == null) {
                showMessage(R.string.choose_duration);
                return;
            }

            if (mLatLng == null) {
                showMessage(R.string.select_location);
                return;
            }

            if (logoImg == null) {
                showMessage(R.string.choose_logo);
                return;
            }

            MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);

            builder.addFormDataPart("title[" + LanguageHelper.getLanguage(getActivity()) + "]", binding.placeNameET.getText().toString().trim());
            builder.addFormDataPart("mobile", mViewModel.getDataManager().getUserObject().getUser().getPhone());
            builder.addFormDataPart("lat", mLatLng.latitude + "");
            builder.addFormDataPart("lng", mLatLng.longitude + "");

            if (mViewModel.getDataManager().getUserObject().getUser().getEmail() != null)
                builder.addFormDataPart("contacts", mViewModel.getDataManager().getUserObject().getUser().getEmail());

            builder.addFormDataPart("subscription_package_id", selectedDuration);

            if (logoImg != null) {
                RequestBody requestBody;
                try {
                    requestBody = RequestBody.create(MediaType.parse("image/*"), new ImageZipper(requireActivity()).compressToFile(logoImg));
                } catch (IOException e) {
                    e.printStackTrace();
                    requestBody = RequestBody.create(MediaType.parse("image/*"), logoImg);
                }

                builder.addFormDataPart("logo", logoImg.getName(), requestBody);
            }

            showLoading();
            mViewModel.submitCommercialActivity(builder);

        });


    }

    private void subscribeViewModel() {

        mViewModel.getSubmitCommercialActivityLiveData().observe(requireActivity(), response -> {

            hideLoading();

            ItemsFirebase.addCommercialLocation(response.getData(), mLatLng.latitude, mLatLng.longitude);

            showSuccessMessage(response.getMessage());

            binding.placeNameET.setText("");
            selectedDuration = null;
            mLatLng = null;
            logoImg = null;
            binding.logoImageView.setImageResource(R.drawable.ic_camera01);

            requireActivity().onBackPressed();

        });

        mViewModel.getSubscriptionPackagesLiveData().observe(requireActivity(), response -> {

            List<String> durations = new ArrayList<>();
            durations.add(getString(R.string.choose_duration));

            for (int i = 0; i < response.getData().size(); i++) {
                durations.add(response.getData().get(i).getTitle());
            }

            ArrayAdapter<String> adapter =
                    new ArrayAdapter<>(
                            getActivity(),
                            R.layout.spinner_item,
                            durations);

            binding.spDuration.setAdapter(adapter);

            binding.spDuration.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                    if (position != 0) {
                        selectedDuration = String.valueOf(response.getData().get(position - 1).getId());
                    } else {
                        selectedDuration = null;
                    }

                }

                @Override
                public void onNothingSelected(AdapterView<?> parentView) {
                }

            });


        });

    }

    @Override
    public void performDependencyInjection(FragmentComponent buildComponent) {
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
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == AppSettingsDialog.DEFAULT_SETTINGS_REQ_CODE) {
            // Do something after user returned from app settings screen
            if (EasyPermissions.hasPermissions(requireActivity(), perms))
                onCurrentLocationClicked();
        }

        if (resultCode == Activity.RESULT_OK) {

            if (requestCode == PICK_LOGO_IMG) {
                logoImg = new File(data.getData().getPath());
                Glide.with(this)
                        .load(logoImg.getPath())
                        .placeholder(R.drawable.ic_camera01)
                        .error(R.drawable.ic_camera01)
                        .into(binding.logoImageView);
            }

        }
    }

    private void onCurrentLocationClicked() {
        if (!EasyPermissions.hasPermissions(requireActivity(), perms)) {
            if (EasyPermissions.somePermissionPermanentlyDenied(this, Arrays.asList(perms))) {
                new AppSettingsDialog.Builder(this).build().show();
            } else {
                EasyPermissions.requestPermissions(this, getString(R.string.location_rationale), REQUEST_LOCATION_CODE, perms);
            }
        } else {
            if (LocationUtils.isGPSEnabled(requireActivity())) {
                SmartLocation.with(getActivity()).location().oneFix().start(location -> {
                    mLatLng = new LatLng(location.getLatitude(), location.getLongitude());
                    this.map.animateCamera(CameraUpdateFactory.newLatLngZoom(mLatLng, 15));
//                    updateAddressText(location.getLatitude(), location.getLongitude());
                });
            } else {
                gpsCheck();
                mLatLng = this.map.getCameraPosition().target;
                showMessage(R.string.open_gps);
            }
        }
    }

    @SuppressLint({"LogNotTimber", "MissingPermission"})
    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;

        if (LocationUtils.isGPSEnabled(requireActivity()) &&
                ((MainActivity) requireActivity()).hasPermission(Manifest.permission.ACCESS_FINE_LOCATION)) {
            SmartLocation.with(getActivity()).location().oneFix().start(location -> {
                mLatLng = new LatLng(location.getLatitude(), location.getLongitude());
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(mLatLng, 15));
                googleMap.setOnCameraIdleListener(this);
            });

        } else {

            // move camera to Riyadh
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(24.7135517, 46.6752957), 5f));
            googleMap.setOnCameraIdleListener(this);
        }

        LatLngBounds ksaBounds = new LatLngBounds(
                new LatLng(16.217169, 34.740374), // SW bounds
                new LatLng(32.177219, 55.196917)  // NE bounds
        );
        // Constrain the camera target to the KSA bounds.
        map.setLatLngBoundsForCameraTarget(ksaBounds);

        map.getUiSettings().setZoomControlsEnabled(true);
        map.getUiSettings().setMyLocationButtonEnabled(true);

        map.setOnMyLocationButtonClickListener(() -> {
            onCurrentLocationClicked();
            return false;
        });

        if (EasyPermissions.hasPermissions(requireActivity(), perms))
            map.setMyLocationEnabled(true);
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
        if (!LocationUtils.isGPSEnabled(requireActivity())) {
            new GpsUtils(requireActivity()).turnGPSOn(isGPSEnable -> {
            });
        }
    }

    @Override
    public void onCameraIdle() {
        mLatLng = this.map.getCameraPosition().target;

        Geocoder geocoder = new Geocoder(requireActivity(), Locale.getDefault());
        List<Address> addresses;
        String countryCode = null;

        try {
            addresses = geocoder.getFromLocation(mLatLng.latitude, mLatLng.longitude, 1);
            countryCode = addresses.get(0).getCountryCode();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {

            if (countryCode != null) {
                if (!countryCode.equals("SA")) {
                    this.map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(24.7135517, 46.6752957), 5f));
                    showMessage(R.string.out_of_ksa);
                }
            } else {
                this.map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(24.7135517, 46.6752957), 5f));
                showMessage(R.string.out_of_ksa);
            }

        }
    }
}
