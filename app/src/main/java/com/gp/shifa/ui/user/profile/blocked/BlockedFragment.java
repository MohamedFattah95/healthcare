package com.gp.shifa.ui.user.profile.blocked;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gp.shifa.databinding.FragmentBlockedBinding;
import com.gp.shifa.di.component.FragmentComponent;
import com.gp.shifa.ui.base.BaseFragment;
import com.gp.shifa.utils.ErrorHandlingUtils;

import javax.inject.Inject;

public class BlockedFragment extends BaseFragment<BlockedViewModel> implements BlockedNavigator, BlockedAdapter.Callback {

    @Inject
    LinearLayoutManager blockedLinearLayoutManager;
    @Inject
    BlockedAdapter blockedAdapter;

    FragmentBlockedBinding binding;

    private int page = 1;
    private int lastPage;

    public static BlockedFragment newInstance(int instance) {
        Bundle args = new Bundle();
        args.putInt(BaseFragment.ARGS_INSTANCE, instance);
        BlockedFragment fragment = new BlockedFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public void refreshData() {

    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel.setNavigator(this);
        blockedAdapter.setCallback(this);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentBlockedBinding.inflate(inflater, container, false);
        setUp();
        return binding.getRoot();
    }

    private void setUp() {

        subscribeViewModel();
        setUpBlockedAdapter();

        binding.rvBlocked.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (blockedLinearLayoutManager != null && blockedLinearLayoutManager.findLastCompletelyVisibleItemPosition() == blockedAdapter.getItemCount() - 1) {
                    if (page < lastPage) {
                        showLoading();
                        mViewModel.getUserBlocked(mViewModel.getDataManager().getCurrentUserId(), ++page);
                    }
                }
            }
        });

        binding.swipeRefreshView.setOnRefreshListener(() -> {
            if (mViewModel.getDataManager().isUserLogged()) {
                showLoading();
                binding.swipeRefreshView.setRefreshing(true);
                page = 1;
                mViewModel.getUserBlocked(mViewModel.getDataManager().getCurrentUserId(), page);
            } else {
                binding.swipeRefreshView.setRefreshing(false);
            }
        });

        if (mViewModel.getDataManager().isUserLogged()) {
            binding.swipeRefreshView.setRefreshing(true);
            page = 1;
            mViewModel.getUserBlocked(mViewModel.getDataManager().getCurrentUserId(), page);
        } else {
            binding.swipeRefreshView.setRefreshing(false);
        }


    }

    private void setUpBlockedAdapter() {
        binding.rvBlocked.setLayoutManager(blockedLinearLayoutManager);
        binding.rvBlocked.setAdapter(blockedAdapter);
    }

    private void subscribeViewModel() {

        mViewModel.getUserBlockedLiveData().observe(requireActivity(), response -> {
            hideLoading();
            binding.swipeRefreshView.setRefreshing(false);
            if (page == 1) {
                blockedAdapter.clearItems();
            }
            int startIndex = blockedAdapter.getItemCount();
            blockedAdapter.addItems(response.getData());
            blockedAdapter.notifyItemRangeInserted(startIndex, response.getData().size());
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
    public void unBlocked(int position) {
        hideLoading();
        blockedAdapter.removeItem(position);
    }

    @Override
    public void unBlockUser(int userId, int position) {
        showLoading();
        mViewModel.unBlockUser(userId, position);
    }
}
