
package com.gp.health.ui.search;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.widget.TextViewCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.gms.maps.model.LatLng;
import com.gp.health.R;
import com.gp.health.databinding.ActivitySearchBinding;
import com.gp.health.di.component.ActivityComponent;
import com.gp.health.ui.adding_ad.add_ad_details.DynamicSpecsAdapter;
import com.gp.health.ui.base.BaseActivity;
import com.gp.health.ui.member_profile.MemberProfileActivity;
import com.gp.health.ui.mobile_search.MobileSearchActivity;
import com.gp.health.ui.profile.MyProfileActivity;
import com.gp.health.ui.scanner.ScannerActivity;
import com.gp.health.utils.ErrorHandlingUtils;
import com.gp.health.utils.LocationUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

public class SearchActivity extends BaseActivity<SearchViewModel> implements SearchNavigator, DynamicSpecsAdapter.Callback {

    private static final int REQUEST_CODE_QR_SCAN = 101;

    @Inject
    LinearLayoutManager specsLinearLayoutManager;
    @Inject
    DynamicSpecsAdapter dynamicSpecsAdapter;

    ActivitySearchBinding binding;

    int selectedCategoryPosition = 0;

    String selectedPropertyCategory = null;
    HashMap<String, String> options = new HashMap<>();
    String lastTime = null;
    LatLng mLatLng = null;

    public static Intent newIntent(Context context) {
        return new Intent(context, SearchActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySearchBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mViewModel.setNavigator(this);
        dynamicSpecsAdapter.setCallback(this);

        binding.toolbar.toolbarTitle.setText(R.string.search);
        setUp();

    }

    private void setUp() {

        selectedPropertyCategory = getIntent().getStringExtra("categoryId");
        lastTime = getIntent().getStringExtra("lastTime");
        mLatLng = getIntent().getParcelableExtra("latLng");

        subscribeViewModel();
        setUpOnViewClicked();
        setupSpecsAdapter();

        showLoading();
        mViewModel.getCategories();

        try {
            binding.tvLocation.setText(LocationUtils.getAddress(mLatLng.latitude, mLatLng.longitude, this));
        } catch (Exception e) {
            e.printStackTrace();
        }
        binding.cbInTwoWeeks.setChecked(lastTime != null);

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

                    selectedCategoryPosition = position;

                    if (position != 0) {

                        selectedPropertyCategory = String.valueOf(response.getData().get(position - 1).getId());
                        showLoading();
                        dynamicSpecsAdapter.clearItems();
                        options.clear();
                        mViewModel.getSpecOptions(selectedPropertyCategory);
                    } else {
                        selectedPropertyCategory = null;
                        dynamicSpecsAdapter.clearItems();
                        options.clear();
                    }

                }

                @Override
                public void onNothingSelected(AdapterView<?> parentView) {
                }

            });

            if (selectedPropertyCategory != null) {

                for (int i = 0; i < response.getData().size(); i++) {
                    if (response.getData().get(i).getId() == Integer.parseInt(selectedPropertyCategory)) {
                        binding.spPropertyCategory.setSelection(i + 1);
                        break;
                    } else if (i == response.getData().size() - 1) {
                        selectedCategoryPosition = 0;
                        selectedPropertyCategory = null;
                        dynamicSpecsAdapter.clearItems();
                    }
                }

            }

        });


        mViewModel.getSpecOptionLiveData().observe(this, response -> {
            hideLoading();
            dynamicSpecsAdapter.addItems(response.getData());
        });

    }

    private void setupSpecsAdapter() {
        binding.rvDynamicSpecs.setLayoutManager(specsLinearLayoutManager);
        binding.rvDynamicSpecs.setAdapter(dynamicSpecsAdapter);
    }

    @Override
    public void onBackPressed() {
        setResult(Activity.RESULT_CANCELED, new Intent());
        super.onBackPressed();
    }

    @SuppressLint("NewApi")
    public void setUpOnViewClicked() {

        binding.toolbar.backButton.setOnClickListener(v -> {
            setResult(Activity.RESULT_CANCELED, new Intent());
            finish();
        });

        binding.tvSearch.setOnClickListener(v -> {
            binding.tvSearch.setTextColor(getResources().getColor(R.color.white));
            TextViewCompat.setCompoundDrawableTintList(binding.tvSearch, AppCompatResources.getColorStateList(this, R.color.white));
            binding.tvSearch.setBackgroundTintList(AppCompatResources.getColorStateList(this, R.color.colorPrimary));

            binding.tvPhoneSearch.setTextColor(getResources().getColor(R.color.colorGrayDark));
            TextViewCompat.setCompoundDrawableTintList(binding.tvPhoneSearch, AppCompatResources.getColorStateList(this, R.color.colorGrayDark));
            binding.tvPhoneSearch.setBackgroundTintList(AppCompatResources.getColorStateList(this, R.color.white));

            binding.tvQRSearch.setTextColor(getResources().getColor(R.color.colorGrayDark));
            TextViewCompat.setCompoundDrawableTintList(binding.tvQRSearch, AppCompatResources.getColorStateList(this, R.color.colorGrayDark));
            binding.tvQRSearch.setBackgroundTintList(AppCompatResources.getColorStateList(this, R.color.white));

        });

        binding.tvPhoneSearch.setOnClickListener(v -> {

//            binding.tvPhoneSearch.setTextColor(getResources().getColor(R.color.white));
//            TextViewCompat.setCompoundDrawableTintList(binding.tvPhoneSearch, AppCompatResources.getColorStateList(this, R.color.white));
//            binding.tvPhoneSearch.setBackgroundTintList(AppCompatResources.getColorStateList(this, R.color.colorPrimary));
//
//            binding.tvSearch.setTextColor(getResources().getColor(R.color.colorGrayDark));
//            TextViewCompat.setCompoundDrawableTintList(binding.tvSearch, AppCompatResources.getColorStateList(this, R.color.colorGrayDark));
//            binding.tvSearch.setBackgroundTintList(AppCompatResources.getColorStateList(this, R.color.white));
//
//            binding.tvQRSearch.setTextColor(getResources().getColor(R.color.colorGrayDark));
//            TextViewCompat.setCompoundDrawableTintList(binding.tvQRSearch, AppCompatResources.getColorStateList(this, R.color.colorGrayDark));
//            binding.tvQRSearch.setBackgroundTintList(AppCompatResources.getColorStateList(this, R.color.white));

            startActivity(MobileSearchActivity.newIntent(this));


        });

        binding.tvQRSearch.setOnClickListener(v -> {

//            binding.tvQRSearch.setTextColor(getResources().getColor(R.color.white));
//            TextViewCompat.setCompoundDrawableTintList(binding.tvQRSearch, AppCompatResources.getColorStateList(this, R.color.white));
//            binding.tvQRSearch.setBackgroundTintList(AppCompatResources.getColorStateList(this, R.color.colorPrimary));
//
//            binding.tvPhoneSearch.setTextColor(getResources().getColor(R.color.colorGrayDark));
//            TextViewCompat.setCompoundDrawableTintList(binding.tvPhoneSearch, AppCompatResources.getColorStateList(this, R.color.colorGrayDark));
//            binding.tvPhoneSearch.setBackgroundTintList(AppCompatResources.getColorStateList(this, R.color.white));
//
//            binding.tvSearch.setTextColor(getResources().getColor(R.color.colorGrayDark));
//            TextViewCompat.setCompoundDrawableTintList(binding.tvSearch, AppCompatResources.getColorStateList(this, R.color.colorGrayDark));
//            binding.tvSearch.setBackgroundTintList(AppCompatResources.getColorStateList(this, R.color.white));

            startActivityForResult(new Intent(SearchActivity.this, ScannerActivity.class), REQUEST_CODE_QR_SCAN);

        });

        binding.tvLocation.setOnClickListener(v -> {
            setResult(Activity.RESULT_FIRST_USER, new Intent());
            finish();
        });

        binding.cbInTwoWeeks.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (buttonView.isPressed()) {
                if (isChecked) {
                    binding.cbInTwoWeeks.setChecked(true);
                    lastTime = "14";
                } else {
                    lastTime = null;
                }
            }
        });

        binding.btnSearch.setOnClickListener(v -> {

            Intent returnIntent = new Intent();
            returnIntent.putExtra("selectedCategoryPosition", selectedCategoryPosition);
            returnIntent.putExtra("categoryId", selectedPropertyCategory);
            returnIntent.putExtra("options", options);
            returnIntent.putExtra("lastTime", lastTime);
            setResult(Activity.RESULT_OK, returnIntent);
            finish();

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
//        showErrorMessage(message);
    }

    @Override
    public void setOption(String key, String value) {
        options.put(key, value);
    }

    @Override
    public void removeOption(String key) {
        options.remove(key);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {

            if (requestCode == REQUEST_CODE_QR_SCAN) {

                try {
                    if (data == null)
                        return;
                    //Getting the passed result
                    String result = data.getStringExtra("result");

                    if (result.matches("\\d+(?:\\.\\d+)?")) {
                        if (mViewModel.getDataManager().isUserLogged()) {
                            if (mViewModel.getDataManager().getUserObject().getId() == Integer.parseInt(result)) {
                                startActivity(MyProfileActivity.newIntent(this));
                            } else {
                                startActivity(MemberProfileActivity.newIntent(this).putExtra("userId", Integer.parseInt(result)));
                            }
                        } else {
                            startActivity(MemberProfileActivity.newIntent(this).putExtra("userId", Integer.parseInt(result)));
                        }
                    } else {
                        showMessage(R.string.no_result);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    showMessage(R.string.no_result);
                }


            }

        }

    }
}
