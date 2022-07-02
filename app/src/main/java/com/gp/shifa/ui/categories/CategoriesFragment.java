package com.gp.shifa.ui.categories;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.gp.shifa.data.models.CategoriesModel;
import com.gp.shifa.databinding.FragmentCategoriesBinding;
import com.gp.shifa.di.component.FragmentComponent;
import com.gp.shifa.ui.base.BaseFragment;
import com.gp.shifa.ui.category_doctors.CategoryDoctorsActivity;
import com.gp.shifa.utils.ErrorHandlingUtils;

import javax.inject.Inject;

public class CategoriesFragment extends BaseFragment<CategoriesViewModel> implements CategoriesNavigator, CategoriesAdapter.Callback {

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
        binding.swipe.setRefreshing(true);
        mViewModel.getCategories();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel.setNavigator(this);
        categoriesAdapter.setCallback(this);
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
            binding.swipe.setRefreshing(true);
            mViewModel.getCategories();
        });


        binding.swipe.setRefreshing(true);
        mViewModel.getCategories();

        subscribeViewModel();

    }

    private void subscribeViewModel() {

        mViewModel.getCategoriesLiveData().observe(requireActivity(), response -> {
            hideLoading();
            binding.swipe.setRefreshing(false);
            categoriesAdapter.addItems(response.getData());

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
        binding.swipe.setRefreshing(false);

    }

    @Override
    public void showMyApiMessage(String message) {
        hideLoading();
        binding.swipe.setRefreshing(false);
        showErrorMessage(message);
    }

    @Override
    public void onCategorySelected(CategoriesModel category) {

        startActivity(CategoryDoctorsActivity.newIntent(requireActivity()).putExtra("category", category));

    }
}
