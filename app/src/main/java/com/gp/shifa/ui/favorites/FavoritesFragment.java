package com.gp.shifa.ui.favorites;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.gp.shifa.databinding.FragmentFavoritesBinding;
import com.gp.shifa.di.component.FragmentComponent;
import com.gp.shifa.ui.base.BaseFragment;
import com.gp.shifa.ui.doctor_details.DoctorDetailsActivity;
import com.gp.shifa.ui.doctors.DoctorsAdapter;
import com.gp.shifa.utils.ErrorHandlingUtils;

import javax.inject.Inject;

@SuppressLint("NonConstantResourceId")
public class FavoritesFragment extends BaseFragment<FavoritesViewModel> implements FavoritesNavigator, DoctorsAdapter.Callback {

    public static final String TAG = "FavoritesFragment";

    @Inject
    LinearLayoutManager favoritesLayoutManager;
    @Inject
    DoctorsAdapter favoritesAdapter;

    FragmentFavoritesBinding binding;


    public static FavoritesFragment newInstance(int instance) {
        Bundle args = new Bundle();
        args.putInt(BaseFragment.ARGS_INSTANCE, instance);
        FavoritesFragment fragment = new FavoritesFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public void refreshData() {

    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel.setNavigator(this);
        favoritesAdapter.setCallback(this);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentFavoritesBinding.inflate(inflater, container, false);
        setUp();
        return binding.getRoot();
    }

    private void setUp() {

        setUpFavoritesAdapter();
        subscribeViewModel();

        binding.swipeFavorites.setOnRefreshListener(() -> {

            if (mViewModel.getDataManager().isUserLogged()) {
                showLoading();
                binding.swipeFavorites.setRefreshing(true);
                mViewModel.getFavorites();
            }

        });

        if (mViewModel.getDataManager().isUserLogged()) {
            binding.swipeFavorites.setRefreshing(true);
            mViewModel.getFavorites();
        }
    }

    private void subscribeViewModel() {

        mViewModel.getFavoritesLiveData().observe(requireActivity(), response -> {
            hideLoading();
            favoritesAdapter.clearItems();
            binding.swipeFavorites.setRefreshing(false);
            favoritesAdapter.addItems(response.getData());
        });

    }

    private void setUpFavoritesAdapter() {
        binding.rvFavorites.setLayoutManager(favoritesLayoutManager);
        binding.rvFavorites.setAdapter(favoritesAdapter);
    }


    @Override
    public void performDependencyInjection(FragmentComponent buildComponent) {
        buildComponent.inject(this);
    }

    @Override
    public void handleError(Throwable throwable) {
        hideLoading();
        binding.swipeFavorites.setRefreshing(false);
        ErrorHandlingUtils.handleErrors(throwable);
    }

    @Override
    public void showMyApiMessage(String message) {
        hideLoading();
        binding.swipeFavorites.setRefreshing(false);
//        showErrorMessage(message);
    }


    @Override
    public void getDoctorDetails(int id) {
        startActivity(DoctorDetailsActivity.newIntent(requireActivity())
                .putExtra("fav", true)
                .putExtra("doctor_id", id));

    }
}
