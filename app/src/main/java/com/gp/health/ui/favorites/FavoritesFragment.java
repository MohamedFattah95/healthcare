package com.gp.health.ui.favorites;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gp.health.databinding.FragmentFavoritesBinding;
import com.gp.health.di.component.FragmentComponent;
import com.gp.health.ui.base.BaseFragment;
import com.gp.health.ui.property_details.PropertyDetailsActivity;
import com.gp.health.utils.CommonUtils;
import com.gp.health.utils.ErrorHandlingUtils;

import javax.inject.Inject;

@SuppressLint("NonConstantResourceId")
public class FavoritesFragment extends BaseFragment<FavoritesViewModel> implements FavoritesNavigator, FavoritesAdapter.Callback {

    public static final String TAG = "FavoritesFragment";

    @Inject
    LinearLayoutManager favoritesLayoutManager;
    @Inject
    FavoritesAdapter favoritesAdapter;

    FragmentFavoritesBinding binding;

    private int page = 1;
    private int lastPage;

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

        binding.rvFavorites.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (favoritesLayoutManager != null && favoritesLayoutManager.findLastCompletelyVisibleItemPosition() == favoritesAdapter.getItemCount() - 1) {
                    if (page < lastPage) {
                        showLoading();
                        mViewModel.getFavorites(mViewModel.getDataManager().getCurrentUserId(), ++page);
                    }
                }

            }
        });


        binding.swipeFavorites.setOnRefreshListener(() -> {

            if (mViewModel.getDataManager().isUserLogged()) {
                showLoading();
                binding.swipeFavorites.setRefreshing(true);
                page = 1;
                mViewModel.getFavorites(mViewModel.getDataManager().getCurrentUserId(), page);
            }

        });

        if (mViewModel.getDataManager().isUserLogged()) {
            binding.swipeFavorites.setRefreshing(true);
            page = 1;
            mViewModel.getFavorites(mViewModel.getDataManager().getCurrentUserId(), page);
        }
    }

    private void subscribeViewModel() {

        mViewModel.getFavoritesLiveData().observe(requireActivity(), response -> {
            hideLoading();
            binding.swipeFavorites.setRefreshing(false);
            if (page == 1) {
                favoritesAdapter.clearItems();
            }
            int startIndex = favoritesAdapter.getItemCount();
            favoritesAdapter.addItems(response.getData());
            favoritesAdapter.notifyItemRangeInserted(startIndex, response.getData().size());
            lastPage = response.getPagination().getLastPage();
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
    public void getPropertyDetails(int id) {
        startActivity(PropertyDetailsActivity.newIntent(getActivity()).putExtra("itemId", id));
    }

    @Override
    public void unFavorite(int itemId, int position) {
        if (mViewModel.getDataManager().isUserLogged()) {
            showLoading();
            mViewModel.unFavorite(itemId, position);

        } else {
            CommonUtils.handleNotAuthenticated(requireActivity());
        }
    }

    @Override
    public void unFavoriteDone(int position) {
        hideLoading();
        favoritesAdapter.removeItem(position);
    }
}
