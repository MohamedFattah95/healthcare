package com.gp.shifa.ui.doctors;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.gp.shifa.databinding.FragmentDoctorsBinding;
import com.gp.shifa.di.component.FragmentComponent;
import com.gp.shifa.ui.base.BaseFragment;
import com.gp.shifa.ui.doctor_details.DoctorDetailsActivity;
import com.gp.shifa.utils.ErrorHandlingUtils;

import javax.inject.Inject;

public class DoctorsFragment extends BaseFragment<DoctorsViewModel> implements DoctorsNavigator, DoctorsAdapter.Callback {

    @Inject
    LinearLayoutManager linearLayoutManager;
    @Inject
    DoctorsAdapter doctorsAdapter;

    FragmentDoctorsBinding binding;

    public static DoctorsFragment newInstance(int instance) {
        Bundle args = new Bundle();
        args.putInt(BaseFragment.ARGS_INSTANCE, instance);
        DoctorsFragment fragment = new DoctorsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public void refreshData() {
        if (mViewModel != null) {
            binding.swipe.setRefreshing(true);
            mViewModel.getDoctors(1);
            doctorsAdapter.clearItems();
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel.setNavigator(this);
        doctorsAdapter.setCallback(this);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentDoctorsBinding.inflate(inflater, container, false);

        setUp();
        return binding.getRoot();
    }

    private void setUp() {

        binding.rvDoctors.setLayoutManager(linearLayoutManager);
        binding.rvDoctors.setAdapter(doctorsAdapter);

        binding.swipe.setOnRefreshListener(() -> {
            binding.swipe.setRefreshing(true);
            mViewModel.getDoctors(1);
            doctorsAdapter.clearItems();
        });

        subscribeViewModel();
        mViewModel.getDoctors(1);

    }

    private void subscribeViewModel() {

        mViewModel.getDoctorsLiveData().observe(requireActivity(), response -> {
            hideLoading();
            binding.swipe.setRefreshing(false);
            doctorsAdapter.addItems(response.getData());

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

    @Override
    public void getDoctorDetails(int id) {
        startActivity(DoctorDetailsActivity.newIntent(requireActivity()).putExtra("doctor_id", id));

    }
}
