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
import com.gp.shifa.data.models.LikeModel;
import com.gp.shifa.data.models.MediaModel;
import com.gp.shifa.databinding.FragmentHomeBinding;
import com.gp.shifa.di.component.FragmentComponent;
import com.gp.shifa.ui.base.BaseFragment;
import com.gp.shifa.ui.common.SliderAdapter;
import com.gp.shifa.ui.home.adapters.CategoriesHomeAdapter;
import com.gp.shifa.ui.home.adapters.DoctorsHomeAdapter;
import com.gp.shifa.ui.home.adapters.ListAdsAdapter;
import com.gp.shifa.ui.main.MainActivity;
import com.gp.shifa.ui.property_details.PropertyDetailsActivity;
import com.gp.shifa.utils.CommonUtils;
import com.gp.shifa.utils.ErrorHandlingUtils;
import com.smarteist.autoimageslider.IndicatorAnimations;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;

import javax.inject.Inject;

public class HomeFragment extends BaseFragment<HomeViewModel> implements HomeNavigator, DoctorsHomeAdapter.Callback,
        CategoriesHomeAdapter.Callback, ListAdsAdapter.Callback {

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

        MediaModel m1 = new MediaModel(0,"https://cloudinary.hbs.edu/hbsit/image/upload/s--sFv3MZbN--/f_auto,c_fill,h_375,w_750,/v20200101/D730ED9CC0AF1A0C18B3499EF75E86D7.jpg");
        MediaModel m2 = new MediaModel(0,"https://m.economictimes.com/thumb/msid-77562369,width-1200,height-900,resizemode-4,imgsize-51396/healthcare.jpg");
        MediaModel m3 = new MediaModel(0,"https://business.aucegypt.edu/sites/business.aucegypt.edu/files/styles/small_widget/public/2021-04/Infection%20Prevention%20and%20Control%20Professional%20Certificate.jpg?itok=Jaxm4sX1");
        MediaModel m4 = new MediaModel(0,"https://thumbs.dreamstime.com/b/health-medical-insurance-life-whole-family-concept-practitioner-doctor-protective-gesture-icon-103341022.jpg");

        ArrayList<MediaModel> mediaModels = new ArrayList<>();

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
        });

        mViewModel.getItemsLiveData().observe(requireActivity(), response -> {
            hideLoading();
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
    public void favoriteDone(LikeModel data, int position) {

    }

    @Override
    public void performDependencyInjection(FragmentComponent buildComponent) {
        buildComponent.inject(this);
    }


    @Override
    public void getPropertyDetails(int id) {
        startActivity(PropertyDetailsActivity.newIntent(getActivity()).putExtra("itemId", id));
    }

    @Override
    public void favoriteOrUnFavorite(int itemId, int position) {
        if (mViewModel.getDataManager().isUserLogged()) {
            showLoading();
            mViewModel.favoriteOrUnFavorite(itemId, position);

        } else {
            CommonUtils.handleNotAuthenticated(requireActivity());
        }
    }

    @Override
    public void onCategorySelected(CategoriesModel category, int position) {

    }

}
