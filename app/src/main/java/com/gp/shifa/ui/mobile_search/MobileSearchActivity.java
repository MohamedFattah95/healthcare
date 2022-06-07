package com.gp.shifa.ui.mobile_search;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.inputmethod.EditorInfo;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.gp.shifa.R;
import com.gp.shifa.databinding.ActivityMobileSearchBinding;
import com.gp.shifa.di.component.ActivityComponent;
import com.gp.shifa.ui.base.BaseActivity;
import com.gp.shifa.ui.member_profile.MemberProfileActivity;
import com.gp.shifa.ui.profile.MyProfileActivity;
import com.gp.shifa.utils.ErrorHandlingUtils;

import javax.inject.Inject;

public class MobileSearchActivity extends BaseActivity<MobileSearchViewModel> implements MobileSearchNavigator, MembersAdapter.Callback {

    @Inject
    LinearLayoutManager linearLayoutManager;
    @Inject
    MembersAdapter membersAdapter;

    ActivityMobileSearchBinding binding;

    public static Intent newIntent(Context context) {
        return new Intent(context, MobileSearchActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMobileSearchBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mViewModel.setNavigator(this);
        membersAdapter.setCallback(this);

        setUp();

    }

    private void setUp() {

        binding.backButton.setOnClickListener(v -> finish());

        binding.etMobile.requestFocus();

        subscribeViewModel();

        binding.rvMembers.setLayoutManager(linearLayoutManager);
        binding.rvMembers.setAdapter(membersAdapter);

        binding.swipeRefreshView.setOnRefreshListener(() -> {

            if (binding.etMobile.getText().toString().trim().isEmpty()) {
                binding.etMobile.setError(getText(R.string.empty_mobile));
                binding.etMobile.requestFocus();
                return;
            }

            if (!binding.etMobile.getText().toString().trim().startsWith("05")) {
                binding.etMobile.setError(getText(R.string.invalid_mobile_prefix));
                binding.etMobile.requestFocus();
                return;
            }

            if (binding.etMobile.getText().toString().trim().length() < 10) {
                binding.etMobile.setError(getText(R.string.invalid_mobile));
                binding.etMobile.setEnabled(true);
                binding.etMobile.requestFocus();
                return;
            }
            binding.etMobile.setError(null);

            showLoading();
            mViewModel.searchMembersByMobile(binding.etMobile.getText().toString().trim());

            showLoading();
            binding.swipeRefreshView.setRefreshing(true);


        });

        binding.etMobile.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {

                if (binding.etMobile.getText().toString().trim().isEmpty()) {
                    showMessage(R.string.empty_mobile);
                    binding.etMobile.requestFocus();
                    return true;
                }

                if (!binding.etMobile.getText().toString().trim().startsWith("05")) {
                    showMessage(R.string.invalid_mobile_prefix);
                    binding.etMobile.requestFocus();
                    return true;
                }

                if (binding.etMobile.getText().toString().trim().length() < 10) {
                    showMessage(R.string.invalid_mobile);
                    binding.etMobile.requestFocus();
                    return true;
                }

                showLoading();
                mViewModel.searchMembersByMobile(binding.etMobile.getText().toString().trim());

                return false;
            }
            return false;
        });

    }

    private void subscribeViewModel() {
        mViewModel.getMembersLiveData().observe(this, response -> {
            hideLoading();
            binding.swipeRefreshView.setRefreshing(false);
            membersAdapter.addItems(response.getData());
        });
    }

    @Override
    public void performDependencyInjection(ActivityComponent buildComponent) {
        buildComponent.inject(this);
    }

    @Override
    public void handleError(Throwable throwable) {
        hideLoading();
        binding.swipeRefreshView.setRefreshing(false);
        ErrorHandlingUtils.handleErrors(throwable);
    }

    @Override
    public void showMyApiMessage(String message) {
        hideLoading();
        binding.swipeRefreshView.setRefreshing(false);
        showErrorMessage(message);
    }

    @Override
    public void getMemberDetails(int userId) {

        if (mViewModel.getDataManager().isUserLogged()) {
            if (mViewModel.getDataManager().getUserObject().getUser().getId() == userId) {
                startActivity(MyProfileActivity.newIntent(this));
            } else {
                startActivity(MemberProfileActivity.newIntent(this).putExtra("userId", userId));
            }
        } else {
            startActivity(MemberProfileActivity.newIntent(this).putExtra("userId", userId));
        }

    }
}
