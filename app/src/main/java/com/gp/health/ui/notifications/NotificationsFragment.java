package com.gp.health.ui.notifications;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.gp.health.R;
import com.gp.health.data.models.NotificationsModel;
import com.gp.health.databinding.FragmentNotificationsBinding;
import com.gp.health.di.component.FragmentComponent;
import com.gp.health.ui.base.BaseFragment;
import com.gp.health.ui.chat.ChatActivity;
import com.gp.health.ui.property_details.PropertyDetailsActivity;
import com.gp.health.utils.CommonUtils;
import com.gp.health.utils.ErrorHandlingUtils;

import javax.inject.Inject;

public class NotificationsFragment extends BaseFragment<NotificationsViewModel> implements NotificationsNavigator, NotificationsAdapter.Callback {

    @Inject
    LinearLayoutManager linearLayoutManager;
    @Inject
    NotificationsAdapter notificationsAdapter;

    FragmentNotificationsBinding binding;

    private int page = 1;
    private int lastPage;

    public static NotificationsFragment newInstance(int instance) {
        Bundle args = new Bundle();
        args.putInt(BaseFragment.ARGS_INSTANCE, instance);
        NotificationsFragment fragment = new NotificationsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel.setNavigator(this);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentNotificationsBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        notificationsAdapter.setCallback(this);
        setUp();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mViewModel.getDataManager().isUserLogged()) {
            binding.swipeNotifications.setRefreshing(true);
            mViewModel.getNotifications(page);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        page = 1;
    }

    private void setUp() {
        subscribeViewModel();

        binding.rvNotifications.setLayoutManager(linearLayoutManager);
        binding.rvNotifications.setAdapter(notificationsAdapter);

        binding.rvNotifications.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (linearLayoutManager != null && linearLayoutManager.findLastCompletelyVisibleItemPosition() == notificationsAdapter.getItemCount() - 1) {
                    if (page < lastPage) {
                        showLoading();
                        mViewModel.getNotifications(++page);
                    }
                }
            }
        });

        binding.swipeNotifications.setOnRefreshListener(() -> {
            if (mViewModel.getDataManager().isUserLogged()) {
                showLoading();
                binding.swipeNotifications.setRefreshing(true);
                page = 1;
                mViewModel.getNotifications(page);
            } else {
                binding.swipeNotifications.setRefreshing(false);
            }
        });

        (requireActivity()).findViewById(R.id.imgBarReadAll).setOnClickListener(v -> {
            mViewModel.markAllAsRead(mViewModel.getDataManager().getCurrentUserId());
        });


    }

    private void subscribeViewModel() {
        mViewModel.getNotificationsLiveData().observe(requireActivity(), response -> {
            hideLoading();
            binding.swipeNotifications.setRefreshing(false);
            if (page == 1) {
                notificationsAdapter.clearItems();
            }
            int startIndex = notificationsAdapter.getItemCount();
            notificationsAdapter.addItems(response.getData());
            notificationsAdapter.notifyItemRangeInserted(startIndex, response.getData().size());
            lastPage = response.getPagination().getLastPage();
        });

        mViewModel.getNotificationSeenLiveData().observe(requireActivity(), voidDataWrapperModel -> {
            page = 1;
            binding.swipeNotifications.setRefreshing(true);
            mViewModel.getNotifications(page);
        });

        mViewModel.getMarkAllAsReadLiveData().observe(requireActivity(), voidDataWrapperModel -> {
            page = 1;
            binding.swipeNotifications.setRefreshing(true);
            mViewModel.getNotifications(page);

            ((BottomNavigationView) requireActivity().findViewById(R.id.bottom_navigation)).removeBadge(R.id.notifications);

        });

    }

    @Override
    public void performDependencyInjection(FragmentComponent buildComponent) {
        buildComponent.inject(this);
    }

    @Override
    public void handleError(Throwable throwable) {
        hideLoading();
        binding.swipeNotifications.setRefreshing(false);
        ErrorHandlingUtils.handleErrors(throwable);
    }

    @Override
    public void showMyApiMessage(String message) {
        hideLoading();
        binding.swipeNotifications.setRefreshing(false);
        showErrorMessage(message);
    }

    @Override
    public void onNotificationClicked(NotificationsModel model) {

        if (mViewModel.getDataManager().getUserObject().getName() == null) {
            CommonUtils.handleNotCompleted(getActivity());
            return;
        }

        int type = model.getItemType();
        if (type == 5) { /*type is 5 for chat * type is 7 for matched ads*/
            startActivity(ChatActivity.newIntent(requireActivity())
                    .putExtra("receiver_id", model.getUserSenderId()));
        } else if (type == 7) {
            startActivity(PropertyDetailsActivity.newIntent(requireActivity())
                    .putExtra("itemId", model.getItemId()));
        }

        mViewModel.markNotificationAsSeen(model.getId());

    }

    public void refreshData() {
        if (mViewModel.getDataManager().isUserLogged()) {
            binding.swipeNotifications.setRefreshing(true);
            page = 1;
            mViewModel.getNotifications(page);
        }
    }
}
