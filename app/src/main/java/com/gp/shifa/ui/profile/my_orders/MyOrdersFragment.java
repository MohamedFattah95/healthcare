package com.gp.shifa.ui.profile.my_orders;

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
import com.gp.shifa.data.firebase.ItemsFirebase;
import com.gp.shifa.databinding.DialogDeleteOrderBinding;
import com.gp.shifa.databinding.FragmentMyOrdersBinding;
import com.gp.shifa.di.component.FragmentComponent;
import com.gp.shifa.ui.base.BaseFragment;
import com.gp.shifa.ui.property_details.PropertyDetailsActivity;
import com.gp.shifa.utils.ErrorHandlingUtils;

import java.util.HashMap;

import javax.inject.Inject;

public class MyOrdersFragment extends BaseFragment<MyOrdersViewModel> implements MyOrdersNavigator, MyOrdersAdapter.Callback {

    @Inject
    LinearLayoutManager myOrdersLinearLayoutManager;
    @Inject
    MyOrdersAdapter myOrdersAdapter;

    FragmentMyOrdersBinding binding;

    private int page = 1;
    private int lastPage;

    public static MyOrdersFragment newInstance(int instance) {
        Bundle args = new Bundle();
        args.putInt(BaseFragment.ARGS_INSTANCE, instance);
        MyOrdersFragment fragment = new MyOrdersFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public void refreshData() {

    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel.setNavigator(this);
        myOrdersAdapter.setCallback(this);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentMyOrdersBinding.inflate(inflater, container, false);
        setUp();
        return binding.getRoot();
    }

    private void setUp() {

        subscribeViewModel();
        setUpMyOrdersAdapter();
        setUpOnClick();

        binding.rvMyOrders.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (myOrdersLinearLayoutManager != null && myOrdersLinearLayoutManager.findLastCompletelyVisibleItemPosition() == myOrdersAdapter.getItemCount() - 1) {
                    if (page < lastPage) {
                        showLoading();
                        mViewModel.getUserOrders(mViewModel.getDataManager().getCurrentUserId(), 2, ++page);
                    }
                }

                if (dy < 0 && !binding.fabAddOrder.isShown())
                    binding.fabAddOrder.show();
                else if (dy > 0 && binding.fabAddOrder.isShown())
                    binding.fabAddOrder.hide();

            }
        });

        binding.swipeRefreshView.setOnRefreshListener(() -> {
            if (mViewModel.getDataManager().isUserLogged()) {
                showLoading();
                binding.swipeRefreshView.setRefreshing(true);
                page = 1;
                mViewModel.getUserOrders(mViewModel.getDataManager().getCurrentUserId(), 2, page);
            } else {
                binding.swipeRefreshView.setRefreshing(false);
            }
        });

        if (mViewModel.getDataManager().isUserLogged()) {
            binding.swipeRefreshView.setRefreshing(true);
            page = 1;
            mViewModel.getUserOrders(mViewModel.getDataManager().getCurrentUserId(), 2, page);
        } else {
            binding.swipeRefreshView.setRefreshing(false);
        }


    }

    private void setUpMyOrdersAdapter() {
        binding.rvMyOrders.setLayoutManager(myOrdersLinearLayoutManager);
        binding.rvMyOrders.setAdapter(myOrdersAdapter);
    }

    private void setUpOnClick() {


    }

    private void subscribeViewModel() {

        mViewModel.getUserOrdersLiveData().observe(requireActivity(), response -> {
            hideLoading();
            binding.swipeRefreshView.setRefreshing(false);
            if (page == 1) {
                myOrdersAdapter.clearItems();
            }
            int startIndex = myOrdersAdapter.getItemCount();
            myOrdersAdapter.addItems(response.getData());
            myOrdersAdapter.notifyItemRangeInserted(startIndex, response.getData().size());
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
    public void orderDeleted(String orderId, int position) {
        hideLoading();
        myOrdersAdapter.removeItem(position);
        ItemsFirebase.removeItemLocation(Integer.parseInt(orderId));
    }

    @Override
    public void deleteOrder(int orderId, int position) {
        showDeleteOrderDialog(orderId, position);
    }

    @Override
    public void editOrder(int orderId) {
        showLoading();
        mViewModel.getOrderDetails(orderId);
    }

    @Override
    public void getPropertyDetails(int id) {
        startActivity(PropertyDetailsActivity.newIntent(getActivity()).putExtra("itemId", id));
    }

    private void showDeleteOrderDialog(int orderId, int position) {

        DialogDeleteOrderBinding deleteOrderBinding = DialogDeleteOrderBinding.inflate(getLayoutInflater());
        MaterialDialog dialog = new MaterialDialog.Builder(requireActivity())
                .customView(deleteOrderBinding.getRoot(), true)
                .cancelable(false).build();

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        deleteOrderBinding.cancelBtn.setOnClickListener(v -> dialog.dismiss());

        deleteOrderBinding.yesBtn.setOnClickListener(v -> {

            dialog.dismiss();
            showLoading();

            HashMap<String, String> map = new HashMap<>();
            map.put("_method", "delete");
            map.put("ids", String.valueOf(orderId));

            mViewModel.deleteOrder(map, position);

        });

        dialog.show();

    }
}
