package com.gp.health.ui.categories;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.gp.health.databinding.FragmentCategoriesBinding;
import com.gp.health.di.component.FragmentComponent;
import com.gp.health.ui.base.BaseFragment;
import com.gp.health.utils.ErrorHandlingUtils;

import javax.inject.Inject;

public class CategoriesFragment extends BaseFragment<CategoriesViewModel> implements CategoriesNavigator {

    @Inject
    LinearLayoutManager linearLayoutManager;
    @Inject
    CategoriesAdapter categoriesAdapter;

    FragmentCategoriesBinding binding;

    public static CategoriesFragment newInstance(int instance) {
        Bundle args = new Bundle();
        args.putInt(BaseFragment.ARGS_INSTANCE, instance);
        CategoriesFragment fragment = new CategoriesFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public void refreshData() {
        if (mViewModel.getDataManager().isUserLogged()) {
            binding.swipe.setRefreshing(true);
            mViewModel.getActiveAreas();
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel.setNavigator(this);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentCategoriesBinding.inflate(inflater, container, false);

        setUp();
        return binding.getRoot();
    }

    private void setUp() {

        binding.rvCategories.setLayoutManager(linearLayoutManager);
        binding.rvCategories.setAdapter(categoriesAdapter);

        binding.swipe.setOnRefreshListener(() -> {
            if (mViewModel.getDataManager().isUserLogged()) {
                showLoading();
                binding.swipe.setRefreshing(true);
                mViewModel.getActiveAreas();
            } else {
                binding.swipe.setRefreshing(false);
            }
        });

        subscribeViewModel();

    }

    private void subscribeViewModel() {

        mViewModel.getRootCitiesLiveData().observe(requireActivity(), response -> {

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
        binding.swipe.setRefreshing(false);
        showErrorMessage(message);
    }

}
