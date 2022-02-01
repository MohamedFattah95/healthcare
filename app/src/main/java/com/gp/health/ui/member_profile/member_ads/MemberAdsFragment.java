package com.gp.health.ui.member_profile.member_ads;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gp.health.data.models.LikeModel;
import com.gp.health.databinding.FragmentMemberAdsBinding;
import com.gp.health.di.component.FragmentComponent;
import com.gp.health.ui.base.BaseFragment;
import com.gp.health.ui.property_details.PropertyDetailsActivity;
import com.gp.health.utils.CommonUtils;
import com.gp.health.utils.ErrorHandlingUtils;

import javax.inject.Inject;

@SuppressLint("NonConstantResourceId")
public class MemberAdsFragment extends BaseFragment<MemberAdsViewModel> implements MemberAdsNavigator, MemberAdsAdapter.Callback {

    @Inject
    LinearLayoutManager adsLinearLayoutManager;
    @Inject
    MemberAdsAdapter memberAdsAdapter;

    FragmentMemberAdsBinding binding;

    private int page = 1;
    private int lastPage;

    public static MemberAdsFragment newInstance(int instance) {
        Bundle args = new Bundle();
        args.putInt(BaseFragment.ARGS_INSTANCE, instance);
        MemberAdsFragment fragment = new MemberAdsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public void refreshData() {

    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel.setNavigator(this);
        memberAdsAdapter.setCallback(this);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentMemberAdsBinding.inflate(inflater, container, false);
        setUp();
        return binding.getRoot();
    }

    private void setUp() {

        subscribeViewModel();
        setUpMyAdsAdapter();

        binding.rvMyAds.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (adsLinearLayoutManager != null && adsLinearLayoutManager.findLastCompletelyVisibleItemPosition() == memberAdsAdapter.getItemCount() - 1) {
                    if (page < lastPage) {
                        showLoading();
                        mViewModel.getUserAds(requireActivity().getIntent().getIntExtra("userId", -1), 1, ++page);
                    }
                }

            }
        });


        binding.swipeRefreshView.setOnRefreshListener(() -> {

            showLoading();
            binding.swipeRefreshView.setRefreshing(true);
            page = 1;
            mViewModel.getUserAds(requireActivity().getIntent().getIntExtra("userId", -1), 1, page);

        });

        binding.swipeRefreshView.setRefreshing(true);
        page = 1;
        mViewModel.getUserAds(requireActivity().getIntent().getIntExtra("userId", -1), 1, page);


    }

    private void setUpMyAdsAdapter() {
        binding.rvMyAds.setLayoutManager(adsLinearLayoutManager);
        binding.rvMyAds.setAdapter(memberAdsAdapter);
    }

    private void subscribeViewModel() {

        mViewModel.getUserAdsLiveData().observe(requireActivity(), response -> {
            hideLoading();
            binding.swipeRefreshView.setRefreshing(false);
            if (page == 1) {
                memberAdsAdapter.clearItems();
            }
            int startIndex = memberAdsAdapter.getItemCount();
            memberAdsAdapter.addItems(response.getData());
            memberAdsAdapter.notifyItemRangeInserted(startIndex, response.getData().size());
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
    public void favoriteDone(LikeModel data, int position) {
        hideLoading();

        if (data.getLikes() == 1) {
            memberAdsAdapter.mAdsList.get(position).setIsLikedByMe("true");
        } else {
            memberAdsAdapter.mAdsList.get(position).setIsLikedByMe(null);
        }

        memberAdsAdapter.notifyItemChanged(position);
    }
}
