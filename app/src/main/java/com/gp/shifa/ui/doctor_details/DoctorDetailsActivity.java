
package com.gp.shifa.ui.doctor_details;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.gp.shifa.R;
import com.gp.shifa.data.models.DoctorDetailsModel;
import com.gp.shifa.databinding.ActivityDoctorDetailsBinding;
import com.gp.shifa.di.component.ActivityComponent;
import com.gp.shifa.ui.base.BaseActivity;
import com.gp.shifa.ui.chat.ChatActivity;
import com.gp.shifa.ui.common.SliderAdapter;
import com.gp.shifa.utils.CommonUtils;
import com.gp.shifa.utils.ErrorHandlingUtils;
import com.smarteist.autoimageslider.IndicatorAnimations;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class DoctorDetailsActivity extends BaseActivity<DoctorDetailsViewModel> implements DoctorDetailsNavigator {

    @Inject
    LinearLayoutManager similarAdsLayoutManager;
    @Inject
    ClinicsAdapter clinicsAdapter;

    @Inject
    SliderAdapter sliderAdapter;

    ActivityDoctorDetailsBinding binding;

    DoctorDetailsModel doctorDetailsModel;

    public static Intent newIntent(Context context) {
        return new Intent(context, DoctorDetailsActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDoctorDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mViewModel.setNavigator(this);

        setUp();

    }

    private void setUp() {

        subscribeViewModel();
        setUpSimilarAdsAdapter();
        setUpSlider();
        setUpOnViewClicked();

        showLoading();
        mViewModel.getDoctorDetails(getIntent().getIntExtra("doctor_id", 0));

        if (getIntent().hasExtra("fav")) {
            binding.tvFavorite.setCompoundDrawablesRelativeWithIntrinsicBounds(0, R.drawable.ic_favorite_round, 0, 0);
        }

        binding.swipeRefreshView.setOnRefreshListener(() -> {
            binding.swipeRefreshView.setRefreshing(true);
            showLoading();
            mViewModel.getDoctorDetails(getIntent().getIntExtra("doctor_id", 0));
        });

    }

    private void setUpSimilarAdsAdapter() {
        binding.rvClinics.setLayoutManager(similarAdsLayoutManager);
        binding.rvClinics.setAdapter(clinicsAdapter);
    }

    private void setUpSlider() {
        binding.slider.setSliderAdapter(sliderAdapter);
        binding.slider.setIndicatorAnimation(IndicatorAnimations.WORM);
        binding.slider.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        binding.slider.setAutoCycleDirection(SliderView.LAYOUT_DIRECTION_LOCALE);
        binding.slider.setScrollTimeInSec(5);
        binding.slider.startAutoCycle();
    }

    @SuppressLint("SetTextI18n")
    private void subscribeViewModel() {

        mViewModel.getFavLiveData().observe(this, response -> {

            hideLoading();

            if (response.getMessage().equals("save"))
                binding.tvFavorite.setCompoundDrawablesRelativeWithIntrinsicBounds(0, R.drawable.ic_favorite_round, 0, 0);
            else
                binding.tvFavorite.setCompoundDrawablesRelativeWithIntrinsicBounds(0, R.drawable.ic_un_favorite_round, 0, 0);


        });

        mViewModel.getDoctorDetailsLiveData().observe(this, response -> {
            hideLoading();
            binding.swipeRefreshView.setRefreshing(false);

            doctorDetailsModel = response.getData();

            if (!mViewModel.getDataManager().isUserLogged()) {

                binding.tvChat.setVisibility(View.GONE);
                binding.tvCall.setVisibility(View.GONE);
                binding.tvFavorite.setVisibility(View.GONE);

            }

            binding.toolbar.toolbarTitle.setText(doctorDetailsModel.getTitle() + " " + doctorDetailsModel.getName());

            List<DoctorDetailsModel.MedicalsBean.ImagesBean> imagesBeans = new ArrayList<>();

            for (int i = 1; i < doctorDetailsModel.getMedicals().size(); i++) {
                imagesBeans.addAll(doctorDetailsModel.getMedicals().get(i).getImages());
            }

            sliderAdapter.addItems(imagesBeans);
            binding.slider.setVisibility(View.VISIBLE);

            if (response.getData().getTitle() != null)
                binding.tvAdTitle.setText(response.getData().getTitle() + " " + response.getData().getName());

            binding.tvAdPrice.setText(getString(R.string.det_price) + " " + response.getData().getDetectionMin() + " " + getString(R.string.egp));

            if (!response.getData().getMedicals().isEmpty()
                    && response.getData().getMedicals().get(0).getGovernorate() != null)
                binding.tvAdLocation.setText(response.getData().getMedicals().get(0).getGovernorate().getName());
            else
                binding.tvAdLocation.setText(getString(R.string.n_a));


            if (response.getData().getSpecialty() != null)
                binding.tvDesc.setText(response.getData().getSpecialty());
            else
                binding.tvDesc.setText(getString(R.string.n_a));

            clinicsAdapter.clearItems();

            if (response.getData().getMedicals() != null && !response.getData().getMedicals().isEmpty())
                clinicsAdapter.addItems(response.getData().getMedicals());
            else {
                binding.tvClinics.setVisibility(View.GONE);
                binding.rvClinics.setVisibility(View.GONE);
            }

            CommonUtils.setupRatingBar(binding.ratingBarClient);

            binding.tvUsername.setText(response.getData().getTitle() + " " + response.getData().getName());
            Glide.with(this)
                    .load(response.getData().getImgSrc()
                            + "/" + response.getData().getImg())
                    .placeholder(R.drawable.ic_user_holder)
                    .error(R.drawable.ic_user_holder)
                    .into(binding.userImageView);


        });


    }

    public void setUpOnViewClicked() {

        binding.toolbar.backButton.setOnClickListener(v -> finish());


        binding.tvFavorite.setOnClickListener(v -> {

            if (mViewModel.getDataManager().isUserLogged()) {
                showLoading();
                mViewModel.doFavorite(doctorDetailsModel.getId());
            } else {
                CommonUtils.handleNotAuthenticated(this);
            }

        });

        binding.tvChat.setOnClickListener(v -> {

            if (mViewModel.getDataManager().isUserLogged()) {
                startActivity(ChatActivity.newIntent(this)
                        .putExtra("doctor", doctorDetailsModel)
                        .putExtra("receiver_id", doctorDetailsModel.getId())
                );
            } else {
                CommonUtils.handleNotAuthenticated(this);
            }

        });

        binding.tvCall.setOnClickListener(v -> {

            if (mViewModel.getDataManager().isUserLogged()) {
                if (doctorDetailsModel.getMedicals() != null && !doctorDetailsModel.getMedicals().isEmpty())
                    startActivity(new Intent(Intent.ACTION_DIAL).setData(Uri.parse("tel:" + doctorDetailsModel.getMedicals().get(0).getPhone())));
            } else {
                CommonUtils.handleNotAuthenticated(this);
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
        binding.swipeRefreshView.setRefreshing(false);
        ErrorHandlingUtils.handleErrors(throwable);
    }

    @Override
    public void showMyApiMessage(String message) {
        hideLoading();
        binding.swipeRefreshView.setRefreshing(false);
        showErrorMessage(message);
    }

}
