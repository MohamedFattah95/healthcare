package com.gp.health.ui.profile.my_ads;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.gp.health.R;
import com.gp.health.data.firebase.ItemsFirebase;
import com.gp.health.databinding.DialogDeleteAdBinding;
import com.gp.health.databinding.FragmentMyAdsBinding;
import com.gp.health.di.component.FragmentComponent;
import com.gp.health.ui.adding_ad.add_ad_images.AddAdImagesActivity;
import com.gp.health.ui.adding_ad.add_ad_terms.AddAdTermsActivity;
import com.gp.health.ui.base.BaseFragment;
import com.gp.health.ui.property_details.PropertyDetailsActivity;
import com.gp.health.utils.ErrorHandlingUtils;

import java.util.HashMap;

import javax.inject.Inject;

public class MyAdsFragment extends BaseFragment<MyAdsViewModel> implements MyAdsNavigator, MyAdsAdapter.Callback {

    @Inject
    LinearLayoutManager myAdsLinearLayoutManager;
    @Inject
    MyAdsAdapter myAdsAdapter;

    FragmentMyAdsBinding binding;

    private int page = 1;
    private int lastPage;

    public static MyAdsFragment newInstance(int instance) {
        Bundle args = new Bundle();
        args.putInt(BaseFragment.ARGS_INSTANCE, instance);
        MyAdsFragment fragment = new MyAdsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public void refreshData() {

    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel.setNavigator(this);
        myAdsAdapter.setCallback(this);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentMyAdsBinding.inflate(inflater, container, false);
        setUp();
        return binding.getRoot();
    }

    private void setUp() {

        subscribeViewModel();
        setUpMyAdsAdapter();
        setUpOnClick();

        binding.rvMyAds.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (myAdsLinearLayoutManager != null && myAdsLinearLayoutManager.findLastCompletelyVisibleItemPosition() == myAdsAdapter.getItemCount() - 1) {
                    if (page < lastPage) {
                        showLoading();
                        mViewModel.getUserAds(mViewModel.getDataManager().getCurrentUserId(), 1, ++page);
                    }
                }

                if (dy < 0 && !binding.fabAddAd.isShown())
                    binding.fabAddAd.show();
                else if (dy > 0 && binding.fabAddAd.isShown())
                    binding.fabAddAd.hide();

            }
        });


        binding.swipeRefreshView.setOnRefreshListener(() -> {
            if (mViewModel.getDataManager().isUserLogged()) {
                showLoading();
                binding.swipeRefreshView.setRefreshing(true);
                page = 1;
                mViewModel.getUserAds(mViewModel.getDataManager().getCurrentUserId(), 1, page);
            } else {
                binding.swipeRefreshView.setRefreshing(false);
            }
        });

        if (mViewModel.getDataManager().isUserLogged()) {
            binding.swipeRefreshView.setRefreshing(true);
            page = 1;
            mViewModel.getUserAds(mViewModel.getDataManager().getCurrentUserId(), 1, page);
        } else {
            binding.swipeRefreshView.setRefreshing(false);
        }


    }

    private void setUpOnClick() {

        binding.fabAddAd.setOnClickListener(v -> startActivity(AddAdTermsActivity.newIntent(getActivity())));

    }

    private void setUpMyAdsAdapter() {
        binding.rvMyAds.setLayoutManager(myAdsLinearLayoutManager);
        binding.rvMyAds.setAdapter(myAdsAdapter);
    }

    private void subscribeViewModel() {

        mViewModel.getUserAdsLiveData().observe(requireActivity(), response -> {
            hideLoading();
            binding.swipeRefreshView.setRefreshing(false);
            if (page == 1) {
                myAdsAdapter.clearItems();
            }
            int startIndex = myAdsAdapter.getItemCount();
            myAdsAdapter.addItems(response.getData());
            myAdsAdapter.notifyItemRangeInserted(startIndex, response.getData().size());
            lastPage = response.getPagination().getLastPage();
        });

        mViewModel.getAdDetailsLiveData().observe(requireActivity(), response -> {
            hideLoading();
            startActivity(AddAdImagesActivity.newIntent(requireActivity())
                    .putExtra("adDetails", response.getData()));
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
    public void adDeleted(String adId, int position) {
        hideLoading();
        myAdsAdapter.removeItem(position);
        ItemsFirebase.removeItemLocation(Integer.parseInt(adId));
    }

    @Override
    public void deleteAd(int adId, int position) {
        showDeleteAdDialog(adId, position);
    }

    @Override
    public void editAd(int adId) {
        showLoading();
        mViewModel.getAdDetails(adId);
    }

    @Override
    public void getPropertyDetails(int id) {
        startActivity(PropertyDetailsActivity.newIntent(getActivity()).putExtra("itemId", id));
    }

    private void showDeleteAdDialog(int adId, int position) {
        DialogDeleteAdBinding dialogDeleteAdBinding = DialogDeleteAdBinding.inflate(getLayoutInflater());
        MaterialDialog dialog = new MaterialDialog.Builder(requireActivity())
                .customView(dialogDeleteAdBinding.getRoot(), true)
                .cancelable(true).build();


        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        dialogDeleteAdBinding.rbOutApp.setOnClickListener(v -> {
            dialogDeleteAdBinding.tvTransNumTitle.setVisibility(View.GONE);
            dialogDeleteAdBinding.etTransNum.setVisibility(View.GONE);
        });

        dialogDeleteAdBinding.rbByApp.setOnClickListener(v -> {
            dialogDeleteAdBinding.tvTransNumTitle.setVisibility(View.VISIBLE);
            dialogDeleteAdBinding.etTransNum.setVisibility(View.VISIBLE);
        });

        dialogDeleteAdBinding.okBtn.setOnClickListener(v -> {

            if (!dialogDeleteAdBinding.rbByApp.isChecked() && !dialogDeleteAdBinding.rbOutApp.isChecked()) {
                showMessage(R.string.how_sold);
                return;
            }

            if (dialogDeleteAdBinding.rbByApp.isChecked()) {

                dialogDeleteAdBinding.tvTransNumTitle.setVisibility(View.VISIBLE);
                dialogDeleteAdBinding.etTransNum.setVisibility(View.VISIBLE);

                if (dialogDeleteAdBinding.etTransNum.getText().toString().trim().isEmpty()) {
                    dialogDeleteAdBinding.etTransNum.setError(getString(R.string.trans_number));
                    dialogDeleteAdBinding.etTransNum.requestFocus();
                    return;
                }
                dialogDeleteAdBinding.etTransNum.setError(null);

                dialog.dismiss();
                showLoading();

                HashMap<String, String> map = new HashMap<>();
                map.put("_method", "delete");
                map.put("ids", String.valueOf(adId));
                map.put("soled_in_out", "2");
                map.put("transfere_no", dialogDeleteAdBinding.etTransNum.getText().toString().trim());

                mViewModel.deleteAd(map, position);

            } else {

                dialog.dismiss();
                showLoading();

                HashMap<String, String> map = new HashMap<>();
                map.put("_method", "delete");
                map.put("ids", String.valueOf(adId));
                map.put("soled_in_out", "1");

                mViewModel.deleteAd(map, position);

            }


        });

        dialog.show();
    }
}
