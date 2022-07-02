
package com.gp.shifa.ui.category_doctors;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.gp.shifa.data.models.CategoriesModel;
import com.gp.shifa.databinding.ActivityCategoryDoctorsBinding;
import com.gp.shifa.di.component.ActivityComponent;
import com.gp.shifa.ui.base.BaseActivity;
import com.gp.shifa.ui.doctor_details.DoctorDetailsActivity;
import com.gp.shifa.utils.ErrorHandlingUtils;

import javax.inject.Inject;

public class CategoryDoctorsActivity extends BaseActivity<CategoryDoctorsViewModel> implements CategoryDoctorsNavigator, CategoriesDoctorsAdapter.Callback {

    @Inject
    LinearLayoutManager linearLayoutManager;
    @Inject
    CategoriesDoctorsAdapter doctorsAdapter;

    ActivityCategoryDoctorsBinding binding;


    public static Intent newIntent(Context context) {
        return new Intent(context, CategoryDoctorsActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCategoryDoctorsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mViewModel.setNavigator(this);
        doctorsAdapter.setCallback(this);

        binding.toolbar.toolbarTitle.setText(((CategoriesModel) getIntent().getSerializableExtra("category")).getName());
        setUp();

    }

    private void setUp() {

        subscribeViewModel();
        setUpOnViewClicked();
        setupDoctorsAdapter();

        showLoading();
        mViewModel.getCategoryDoctors(((CategoriesModel) getIntent().getSerializableExtra("category")).getId());

        binding.swipe.setOnRefreshListener(() -> {
            binding.swipe.setRefreshing(true);
            mViewModel.getCategoryDoctors(((CategoriesModel) getIntent().getSerializableExtra("category")).getId());

        });


    }

    private void subscribeViewModel() {

        mViewModel.getCategoryDoctorsLiveData().observe(this, response -> {
            hideLoading();
            binding.swipe.setRefreshing(false);
            doctorsAdapter.addItems(response.getData().getMedicals());
        });

    }

    private void setupDoctorsAdapter() {
        binding.rvDoctors.setLayoutManager(linearLayoutManager);
        binding.rvDoctors.setAdapter(doctorsAdapter);
    }

    @Override
    public void onBackPressed() {
        setResult(Activity.RESULT_CANCELED, new Intent());
        super.onBackPressed();
    }

    @SuppressLint("NewApi")
    public void setUpOnViewClicked() {

        binding.toolbar.backButton.setOnClickListener(v -> {
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
    public void onDoctorClick(int id) {
        startActivity(DoctorDetailsActivity.newIntent(this).putExtra("doctor_id", id));
    }
}
