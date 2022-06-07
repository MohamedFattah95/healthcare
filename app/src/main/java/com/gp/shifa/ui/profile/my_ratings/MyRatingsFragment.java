package com.gp.shifa.ui.profile.my_ratings;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gp.shifa.databinding.FragmentMyRatingsBinding;
import com.gp.shifa.di.component.FragmentComponent;
import com.gp.shifa.ui.base.BaseFragment;
import com.gp.shifa.utils.ErrorHandlingUtils;

import javax.inject.Inject;

public class MyRatingsFragment extends BaseFragment<MyRatingsViewModel> implements MyRatingsNavigator, MyRatingsAdapter.Callback {

    @Inject
    LinearLayoutManager myRatingsLinearLayoutManager;
    @Inject
    MyRatingsAdapter myRatingsAdapter;

    FragmentMyRatingsBinding binding;

    private int page = 1;
    private int lastPage;

    public static MyRatingsFragment newInstance(int instance) {
        Bundle args = new Bundle();
        args.putInt(BaseFragment.ARGS_INSTANCE, instance);
        MyRatingsFragment fragment = new MyRatingsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public void refreshData() {

    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel.setNavigator(this);
        myRatingsAdapter.setCallback(this);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentMyRatingsBinding.inflate(inflater, container, false);
        setUp();
        return binding.getRoot();
    }

    private void setUp() {

        subscribeViewModel();
        setUpMyRatingsAdapter();

        binding.rvMyRatings.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (myRatingsLinearLayoutManager != null && myRatingsLinearLayoutManager.findLastCompletelyVisibleItemPosition() == myRatingsAdapter.getItemCount() - 1) {
                    if (page < lastPage) {
                        showLoading();
                        mViewModel.getUserRatings(mViewModel.getDataManager().getCurrentUserId(), ++page);
                    }
                }
            }
        });

        binding.swipeRefreshView.setOnRefreshListener(() -> {
            if (mViewModel.getDataManager().isUserLogged()) {
                showLoading();
                binding.swipeRefreshView.setRefreshing(true);
                page = 1;
                mViewModel.getUserRatings(mViewModel.getDataManager().getCurrentUserId(), page);
            } else {
                binding.swipeRefreshView.setRefreshing(false);
            }
        });

        if (mViewModel.getDataManager().isUserLogged()) {
            binding.swipeRefreshView.setRefreshing(true);
            page = 1;
            mViewModel.getUserRatings(mViewModel.getDataManager().getCurrentUserId(), page);
        } else {
            binding.swipeRefreshView.setRefreshing(false);
        }


    }

    private void setUpMyRatingsAdapter() {
        binding.rvMyRatings.setLayoutManager(myRatingsLinearLayoutManager);
        binding.rvMyRatings.setAdapter(myRatingsAdapter);
    }

    private void subscribeViewModel() {

        mViewModel.getUserRatingsLiveData().observe(getActivity(), response -> {
            hideLoading();
            binding.swipeRefreshView.setRefreshing(false);
            if (page == 1) {
                myRatingsAdapter.clearItems();
            }
            int startIndex = myRatingsAdapter.getItemCount();
            myRatingsAdapter.addItems(response.getData());
            myRatingsAdapter.notifyItemRangeInserted(startIndex, response.getData().size());
            lastPage = response.getPagination().getLastPage();
        });

    }

    @Override
    public void performDependencyInjection(FragmentComponent buildComponent) {
        buildComponent.inject(this);
    }

    @Override
    public void handleError(Throwable throwable) {
        binding.swipeRefreshView.setRefreshing(false);
        hideLoading();
        ErrorHandlingUtils.handleErrors(throwable);
    }

    @Override
    public void showMyApiMessage(String message) {
        binding.swipeRefreshView.setRefreshing(false);
        hideLoading();
//        showErrorMessage(message);
    }

}
