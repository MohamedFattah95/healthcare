package com.gp.shifa.ui.member_profile.member_ratings;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gp.shifa.databinding.FragmentMemberRatingsBinding;
import com.gp.shifa.di.component.FragmentComponent;
import com.gp.shifa.ui.base.BaseFragment;
import com.gp.shifa.ui.member_profile.MemberProfileActivity;
import com.gp.shifa.ui.profile.my_ratings.MyRatingsAdapter;
import com.gp.shifa.utils.ErrorHandlingUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import javax.inject.Inject;

public class MemberRatingsFragment extends BaseFragment<MemberRatingsViewModel> implements MemberRatingsNavigator, MyRatingsAdapter.Callback {

    @Inject
    LinearLayoutManager ratingsLinearLayoutManager;
    @Inject
    MyRatingsAdapter ratingsAdapter;

    FragmentMemberRatingsBinding binding;

    private int page = 1;
    private int lastPage;

    public static MemberRatingsFragment newInstance(int instance) {
        Bundle args = new Bundle();
        args.putInt(BaseFragment.ARGS_INSTANCE, instance);
        MemberRatingsFragment fragment = new MemberRatingsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public void refreshData() {

    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel.setNavigator(this);
        ratingsAdapter.setCallback(this);
        EventBus.getDefault().register(this);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentMemberRatingsBinding.inflate(inflater, container, false);
        setUp();
        return binding.getRoot();
    }

    private void setUp() {

        subscribeViewModel();
        setUpMyRatingsAdapter();

        binding.rvRatings.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (ratingsLinearLayoutManager != null && ratingsLinearLayoutManager.findLastCompletelyVisibleItemPosition() == ratingsAdapter.getItemCount() - 1) {
                    if (page < lastPage) {
                        showLoading();
                        mViewModel.getUserRatings(requireActivity().getIntent().getIntExtra("userId", -1), ++page);
                    }
                }

                if (dy < 0 && !((MemberProfileActivity) requireActivity()).binding.fabAddRate.isShown())
                    ((MemberProfileActivity) requireActivity()).binding.fabAddRate.show();
                else if (dy > 0 && ((MemberProfileActivity) requireActivity()).binding.fabAddRate.isShown())
                    ((MemberProfileActivity) requireActivity()).binding.fabAddRate.hide();
            }
        });

        binding.swipeRefreshView.setOnRefreshListener(() -> {
            showLoading();
            binding.swipeRefreshView.setRefreshing(true);
            page = 1;
            mViewModel.getUserRatings(requireActivity().getIntent().getIntExtra("userId", -1), page);

        });

        binding.swipeRefreshView.setRefreshing(true);
        page = 1;
        mViewModel.getUserRatings(requireActivity().getIntent().getIntExtra("userId", -1), page);


    }

    private void setUpMyRatingsAdapter() {
        binding.rvRatings.setLayoutManager(ratingsLinearLayoutManager);
        binding.rvRatings.setAdapter(ratingsAdapter);
    }

    private void subscribeViewModel() {

        mViewModel.getUserRatingsLiveData().observe(requireActivity(), response -> {
            hideLoading();
            binding.swipeRefreshView.setRefreshing(false);
            if (page == 1) {
                ratingsAdapter.clearItems();
            }
            int startIndex = ratingsAdapter.getItemCount();
            ratingsAdapter.addItems(response.getData());
            ratingsAdapter.notifyItemRangeInserted(startIndex, response.getData().size());
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

    @Subscribe
    public void refreshRatings(String keyword) {
        if (keyword.equalsIgnoreCase("refresh_member_ratings")) {
            showLoading();
            binding.swipeRefreshView.setRefreshing(true);
            page = 1;
            mViewModel.getUserRatings(requireActivity().getIntent().getIntExtra("userId", -1), page);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
