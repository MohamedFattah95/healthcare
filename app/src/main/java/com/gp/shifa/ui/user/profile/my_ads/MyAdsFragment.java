package com.gp.shifa.ui.user.profile.my_ads;

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
import com.gp.shifa.R;
import com.gp.shifa.data.firebase.ItemsFirebase;
import com.gp.shifa.databinding.DialogDeleteAdBinding;
import com.gp.shifa.databinding.FragmentMyAdsBinding;
import com.gp.shifa.di.component.FragmentComponent;
import com.gp.shifa.ui.base.BaseFragment;
import com.gp.shifa.ui.property_details.PropertyDetailsActivity;
import com.gp.shifa.utils.ErrorHandlingUtils;

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
