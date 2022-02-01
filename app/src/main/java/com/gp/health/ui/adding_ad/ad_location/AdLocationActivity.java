package com.gp.health.ui.adding_ad.ad_location;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.gp.health.R;
import com.gp.health.data.models.AdAndOrderModel;
import com.gp.health.data.models.GoogleShopModel;
import com.gp.health.data.models.MediaModel;
import com.gp.health.databinding.ActivityAdLocationBinding;
import com.gp.health.di.component.ActivityComponent;
import com.gp.health.ui.adding_ad.add_ad_details.AddAdDetailsActivity;
import com.gp.health.ui.base.BaseActivity;
import com.gp.health.utils.ErrorHandlingUtils;
import com.gp.health.utils.LanguageHelper;
import com.gp.health.utils.LocationUtils;
import com.gp.health.utils.location.GpsUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import io.nlopez.smartlocation.SmartLocation;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

@SuppressLint("NonConstantResourceId")
public class AdLocationActivity extends BaseActivity<AdLocationViewModel> implements AdLocationNavigator, OnMapReadyCallback,
        GooglePlacesAdapter.PlacesCallBack, GoogleMap.OnCameraIdleListener, EasyPermissions.PermissionCallbacks {

    private static final String TAG = "CurrentLocationActivity";
    public static final int REQUEST_LOCATION_CODE = 999;
    String[] perms = {Manifest.permission.ACCESS_FINE_LOCATION};

    GoogleMap map;
    LatLng mLatLng;

    private GooglePlacesAdapter googlePlacesAdapter;
    private LinearLayoutManager linearLayoutManager;

    ActivityAdLocationBinding binding;

    int selectedCategoryId = -1;
    ArrayList<MediaModel> imgList = new ArrayList<>();
    ArrayList<MediaModel> vidList = new ArrayList<>();

    AdAndOrderModel adDetails = null;

    public static Intent newIntent(Context context) {
        return new Intent(context, AdLocationActivity.class);
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
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        binding.mMapView.onLowMemory();
    }

    private void setUp() {

        selectedCategoryId = getIntent().getIntExtra("categoryId", -1);
        imgList = (ArrayList<MediaModel>) getIntent().getSerializableExtra("imgList");
        vidList = (ArrayList<MediaModel>) getIntent().getSerializableExtra("vidList");

        if (getIntent().hasExtra("adDetails"))
            adDetails = (AdAndOrderModel) getIntent().getSerializableExtra("adDetails");

        binding.toolbar.toolbarTitle.setText(R.string.aqar_location);

        subscribeViewModel();
        setupPLacesRec();
        setupOnClick();

        binding.placesSearchText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() == 0) {
                    binding.rvPlaces.setVisibility(View.GONE);
                    hideKeyboard();
                    return;
                }
                if (charSequence.length() > 2) {
                    mViewModel.getSearchPlaces(getMapSearch(charSequence.toString()));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAdLocationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mViewModel.setNavigator(this);
        EventBus.getDefault().register(this);

        EasyPermissions.requestPermissions(this, getString(R.string.location_rationale), REQUEST_LOCATION_CODE, perms);

        showLoading();
        binding.mMapView.onCreate(savedInstanceState);
        binding.mMapView.getMapAsync(this);

        setUp();

    }

    @Subscribe
    public void finishAd(String keyword) {
        if (keyword.equals("finish_ad"))
            finish();
    }

    private void setupPLacesRec() {
        linearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        binding.rvPlaces.setLayoutManager(linearLayoutManager);
        googlePlacesAdapter = new GooglePlacesAdapter(new ArrayList<>());
        googlePlacesAdapter.setCallback(this);
        binding.rvPlaces.setAdapter(googlePlacesAdapter);
    }


    @Override
    public void onPlaceClick(GoogleShopModel place) {
        binding.rvPlaces.setVisibility(View.GONE);
        hideKeyboard();

        mLatLng = new LatLng(place.getGeometry().getLocation().getLat(), place.getGeometry().getLocation().getLng());
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(mLatLng, 15));
    }

    private void subscribeViewModel() {

        mViewModel.getSearchPlacesLiveData().observe(this, response -> {
            binding.rvPlaces.setVisibility(View.VISIBLE);
            googlePlacesAdapter.addItems(response.getShopsList());
        });

    }

    private void setupOnClick() {
        binding.toolbar.backButton.setOnClickListener(v -> finish());

        binding.currentLocationBtn.setOnClickListener(v -> onCurrentLocationBtnClicked());

        binding.doneBtn.setOnClickListener(v -> {
            if (adDetails == null) {
                startActivity(AddAdDetailsActivity.newIntent(this)
                        .putExtra("categoryId", selectedCategoryId)
                        .putExtra("imgList", imgList)
                        .putExtra("vidList", vidList)
                        .putExtra("latLng", mLatLng));
            } else {
                startActivity(AddAdDetailsActivity.newIntent(this)
                        .putExtra("adDetails", adDetails)
                        .putExtra("categoryId", selectedCategoryId)
                        .putExtra("imgList", imgList)
                        .putExtra("vidList", vidList)
                        .putExtra("latLng", mLatLng));
            }
        });

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
                    this.map.animateCamera(CameraUpdateFactory.newLatLngZoom(mLatLng, 14f));
                });
            } else {
                gpsCheck();
                mLatLng = this.map.getCameraPosition().target;
                showMessage(R.string.open_gps);
            }
        }
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


    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;

        this.map.clear();

        LatLngBounds ksaBounds = new LatLngBounds(
                new LatLng(16.217169, 34.740374), // SW bounds
                new LatLng(32.177219, 55.196917)  // NE bounds
        );
        // Constrain the camera target to the KSA bounds.
        map.setLatLngBoundsForCameraTarget(ksaBounds);

        if (EasyPermissions.hasPermissions(this, perms))
            map.setMyLocationEnabled(true);

        if (adDetails != null && adDetails.getLat() != null && adDetails.getLng() != null) {

            this.map.animateCamera(CameraUpdateFactory.newLatLngZoom(
                    new LatLng(Double.parseDouble(adDetails.getLat()), Double.parseDouble(adDetails.getLng())), 15));

            mLatLng = new LatLng(Double.parseDouble(adDetails.getLat()), Double.parseDouble(adDetails.getLng()));

            this.map.setOnCameraIdleListener(this);

        } else {

            if (LocationUtils.isGPSEnabled(this) && hasPermission(Manifest.permission.ACCESS_FINE_LOCATION)) {
                SmartLocation.with(this).location().oneFix().start(location -> {
                    mLatLng = new LatLng(location.getLatitude(), location.getLongitude());
                    this.map.animateCamera(CameraUpdateFactory.newLatLngZoom(mLatLng, 15));

                    this.map.setOnCameraIdleListener(this);
                });

            } else {

                // move camera to Riyadh
                this.map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(24.7135517, 46.6752957), 5f));

                this.map.setOnCameraIdleListener(this);
            }
        }

        hideLoading();

    }

   /* @OnClick(R.id.placesSearchText)
    public void onGooglePlacesClicked() {
        // Set the fields to specify which types of place data to
        // return after the user has made a selection.
        List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS, Place.Field.LAT_LNG);
        // Start the autocomplete intent.
        Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fields).build(this);
        startActivityForResult(intent, 1);
    }*/

    @SuppressLint("LogNotTimber")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                try {
                    Place place;
                    if (data != null) {
                        this.map.clear();
                        place = Autocomplete.getPlaceFromIntent(data);
                        mLatLng = place.getLatLng();
                        this.map.animateCamera(CameraUpdateFactory.newLatLngZoom(mLatLng, 15));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                Status status = Autocomplete.getStatusFromIntent(data);
                if (status.getStatusMessage() != null) {
                    Log.i(TAG, status.getStatusMessage());
                }
            }
            return;
        }

        if (requestCode == AppSettingsDialog.DEFAULT_SETTINGS_REQ_CODE) {
            // Do something after user returned from app settings screen
            if (EasyPermissions.hasPermissions(this, perms))
                onCurrentLocationBtnClicked();
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private Map<String, String> getMapSearch(String query) {
        Map<String, String> map = new HashMap<>();

        map.put("key", getString(R.string.google_map_api_key));
        map.put("language", LanguageHelper.getLanguage(this));
        map.put("region", ".sa");
        map.put("query", query);
        return map;
    }

    private void gpsCheck() {
        if (!LocationUtils.isGPSEnabled(this)) {
            new GpsUtils(this).turnGPSOn(isGPSEnable -> {
            });
        }
    }

    @Override
    public void onCameraIdle() {
        //get latlng at the center by calling
        mLatLng = map.getCameraPosition().target;

        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
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
