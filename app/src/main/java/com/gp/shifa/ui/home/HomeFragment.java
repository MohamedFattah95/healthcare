package com.gp.shifa.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gp.shifa.data.models.CategoriesModel;
import com.gp.shifa.data.models.DoctorDetailsModel;
import com.gp.shifa.databinding.FragmentHomeBinding;
import com.gp.shifa.di.component.FragmentComponent;
import com.gp.shifa.ui.base.BaseFragment;
import com.gp.shifa.ui.category_doctors.CategoryDoctorsActivity;
import com.gp.shifa.ui.common.SliderAdapter;
import com.gp.shifa.ui.doctor_details.DoctorDetailsActivity;
import com.gp.shifa.ui.home.adapters.CategoriesHomeAdapter;
import com.gp.shifa.ui.home.adapters.DoctorsHomeAdapter;
import com.gp.shifa.ui.main.MainActivity;
import com.gp.shifa.utils.ErrorHandlingUtils;
import com.smarteist.autoimageslider.IndicatorAnimations;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;

import javax.inject.Inject;

public class HomeFragment extends BaseFragment<HomeViewModel> implements HomeNavigator, DoctorsHomeAdapter.Callback,
        CategoriesHomeAdapter.Callback {

    public static final String TAG = "HomeFragment";
    private static final int SEARCH_CODE = 123;

    @Inject
    LinearLayoutManager doctorsLinearLayoutManager;
    @Inject
    LinearLayoutManager categoryLinearLayoutManager;

    @Inject
    SliderAdapter sliderAdapter;
    @Inject
    DoctorsHomeAdapter doctorsAdapter;
    @Inject
    CategoriesHomeAdapter categoriesHomeAdapter;

    private FragmentHomeBinding binding;

    public static HomeFragment newInstance(int instance) {
        Bundle args = new Bundle();
        args.putInt(BaseFragment.ARGS_INSTANCE, instance);
        HomeFragment fragment = new HomeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public void refreshData() {
        mViewModel.getCategories();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel.setNavigator(this);
        categoriesHomeAdapter.setCallback(this);
        doctorsAdapter.setCallback(this);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);

        setUp();

        return binding.getRoot();
    }


    private void setUp() {

        subscribeViewModel();

        setUpOnViewClicked();
        setUpSlider();
        setUpCategoriesAdapter();
        setUpDoctorsAdapter();

        mViewModel.getCategories();
        mViewModel.getDoctors();

    }

    private void setUpOnViewClicked() {

        binding.tvMoreCategories.setOnClickListener(v ->
                ((MainActivity) requireActivity()).navigateToCategories());

        binding.tvMoreDoctors.setOnClickListener(v ->
                ((MainActivity) requireActivity()).navigateToDoctors());

    }

    private void setUpSlider() {
        binding.slider.setSliderAdapter(sliderAdapter);
        binding.slider.setIndicatorAnimation(IndicatorAnimations.WORM);
        binding.slider.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        binding.slider.setAutoCycleDirection(SliderView.LAYOUT_DIRECTION_LOCALE);
        binding.slider.setScrollTimeInSec(5);
        binding.slider.startAutoCycle();

        DoctorDetailsModel.MedicalsBean.ImagesBean m1 = new DoctorDetailsModel.MedicalsBean.ImagesBean("https://cloudinary.hbs.edu/hbsit/image/upload/s--sFv3MZbN--/f_auto,c_fill,h_375,w_750,/v20200101/D730ED9CC0AF1A0C18B3499EF75E86D7.jpg");
        DoctorDetailsModel.MedicalsBean.ImagesBean m2 = new DoctorDetailsModel.MedicalsBean.ImagesBean("https://m.economictimes.com/thumb/msid-77562369,width-1200,height-900,resizemode-4,imgsize-51396/healthcare.jpg");
        DoctorDetailsModel.MedicalsBean.ImagesBean m3 = new DoctorDetailsModel.MedicalsBean.ImagesBean("https://business.aucegypt.edu/sites/business.aucegypt.edu/files/styles/small_widget/public/2021-04/Infection%20Prevention%20and%20Control%20Professional%20Certificate.jpg?itok=Jaxm4sX1");
        DoctorDetailsModel.MedicalsBean.ImagesBean m4 = new DoctorDetailsModel.MedicalsBean.ImagesBean("https://thumbs.dreamstime.com/b/health-medical-insurance-life-whole-family-concept-practitioner-doctor-protective-gesture-icon-103341022.jpg");

        ArrayList<DoctorDetailsModel.MedicalsBean.ImagesBean> mediaModels = new ArrayList<>();

        mediaModels.add(m1);
        mediaModels.add(m2);
        mediaModels.add(m3);
        mediaModels.add(m4);

        sliderAdapter.addItems(mediaModels);
    }

    private void setUpDoctorsAdapter() {
        doctorsLinearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        binding.rvDoctors.setLayoutManager(doctorsLinearLayoutManager);
        binding.rvDoctors.setAdapter(doctorsAdapter);
    }

    private void setUpCategoriesAdapter() {
        categoryLinearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        binding.rvCategories.setLayoutManager(categoryLinearLayoutManager);
        binding.rvCategories.setAdapter(categoriesHomeAdapter);
    }

    private void subscribeViewModel() {

        mViewModel.getCategoriesLiveData().observe(requireActivity(), response -> {
            hideLoading();

            categoriesHomeAdapter.addItems(response.getData());

        });


        mViewModel.getDoctorsLiveData().observe(requireActivity(), response -> {
            hideLoading();

            doctorsAdapter.addItems(response.getData());

        });


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
    public void performDependencyInjection(FragmentComponent buildComponent) {
        buildComponent.inject(this);
    }

    @Override
    public void onCategorySelected(CategoriesModel category) {
        startActivity(CategoryDoctorsActivity.newIntent(requireActivity()).putExtra("category", category));
    }

    @Override
    public void onDoctorClick(int id) {
        startActivity(DoctorDetailsActivity.newIntent(requireActivity()).putExtra("doctor_id", id));
    }
}
