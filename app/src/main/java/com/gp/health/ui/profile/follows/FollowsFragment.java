package com.gp.health.ui.profile.follows;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gp.health.databinding.FragmentFollowsBinding;
import com.gp.health.di.component.FragmentComponent;
import com.gp.health.ui.base.BaseFragment;
import com.gp.health.ui.member_profile.MemberProfileActivity;
import com.gp.health.utils.ErrorHandlingUtils;

import javax.inject.Inject;

public class FollowsFragment extends BaseFragment<FollowsViewModel> implements FollowsNavigator, FollowsAdapter.Callback {

    @Inject
    LinearLayoutManager followsLinearLayoutManager;
    @Inject
    FollowsAdapter followsAdapter;

    FragmentFollowsBinding binding;

    private int page = 1;
    private int lastPage;

    public static FollowsFragment newInstance(int instance) {
        Bundle args = new Bundle();
        args.putInt(BaseFragment.ARGS_INSTANCE, instance);
        FollowsFragment fragment = new FollowsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public void refreshData() {

    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel.setNavigator(this);
        followsAdapter.setCallback(this);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentFollowsBinding.inflate(inflater, container, false);
        setUp();
        return binding.getRoot();
    }

    private void setUp() {

        subscribeViewModel();
        setUpFollowsAdapter();

        binding.rvFollows.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (followsLinearLayoutManager != null && followsLinearLayoutManager.findLastCompletelyVisibleItemPosition() == followsAdapter.getItemCount() - 1) {
                    if (page < lastPage) {
                        showLoading();
                        mViewModel.getUserFollowings(mViewModel.getDataManager().getCurrentUserId(), ++page);
                    }
                }
            }
        });

        binding.swipeRefreshView.setOnRefreshListener(() -> {
            if (mViewModel.getDataManager().isUserLogged()) {
                showLoading();
                binding.swipeRefreshView.setRefreshing(true);
                page = 1;
                mViewModel.getUserFollowings(mViewModel.getDataManager().getCurrentUserId(), page);
            } else {
                binding.swipeRefreshView.setRefreshing(false);
            }
        });

        if (mViewModel.getDataManager().isUserLogged()) {
            binding.swipeRefreshView.setRefreshing(true);
            page = 1;
            mViewModel.getUserFollowings(mViewModel.getDataManager().getCurrentUserId(), page);
        } else {
            binding.swipeRefreshView.setRefreshing(false);
        }

    }

    private void setUpFollowsAdapter() {
        binding.rvFollows.setLayoutManager(followsLinearLayoutManager);
        binding.rvFollows.setAdapter(followsAdapter);
    }

    private void subscribeViewModel() {

        mViewModel.getUserFollowingsLiveData().observe(requireActivity(), response -> {
            hideLoading();
            binding.swipeRefreshView.setRefreshing(false);
            if (page == 1) {
                followsAdapter.clearItems();
            }
            int startIndex = followsAdapter.getItemCount();
            followsAdapter.addItems(response.getData());
            followsAdapter.notifyItemRangeInserted(startIndex, response.getData().size());
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

    @Override
    public void unFollowed(int position) {
        hideLoading();
        followsAdapter.removeItem(position);
    }

    @Override
    public void unFollowUser(int followingId, int position) {
        showLoading();
        mViewModel.unFollowUser(followingId, position);
    }

    @Override
    public void openMemberProfile(int followingId) {
        startActivity(MemberProfileActivity.newIntent(requireActivity()).putExtra("userId", followingId));
    }
}
