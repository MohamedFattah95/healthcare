package com.gp.health.ui.main;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.gp.health.R;
import com.gp.health.databinding.ActivityMainBinding;
import com.gp.health.di.component.ActivityComponent;
import com.gp.health.ui.add_commercial.AddCommercialFragment;
import com.gp.health.ui.adding_ad.add_ad_terms.AddAdTermsActivity;
import com.gp.health.ui.adding_order.AddingOrderActivity;
import com.gp.health.ui.base.BaseActivity;
import com.gp.health.ui.base.BaseFragment;
import com.gp.health.ui.categories.CategoriesFragment;
import com.gp.health.ui.chats.ChatsFragment;
import com.gp.health.ui.commission.CommissionFragment;
import com.gp.health.ui.doctors.DoctorsFragment;
import com.gp.health.ui.edit_profile.EditProfileActivity;
import com.gp.health.ui.favorites.FavoritesFragment;
import com.gp.health.ui.home.HomeFragment;
import com.gp.health.ui.notifications.NotificationsFragment;
import com.gp.health.ui.profile.ProfileFragment;
import com.gp.health.ui.settings.SettingsFragment;
import com.gp.health.ui.user.login.LoginActivity;
import com.gp.health.utils.CommonUtils;
import com.gp.health.utils.DateUtility;
import com.gp.health.utils.ErrorHandlingUtils;
import com.gp.health.utils.LanguageHelper;
import com.ncapdevi.fragnav.FragNavController;
import com.ncapdevi.fragnav.tabhistory.UniqueTabHistoryStrategy;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

@SuppressLint("NonConstantResourceId")
public class MainActivity extends BaseActivity<MainViewModel> implements MainNavigator, BaseFragment.FragmentNavigation, BottomNavigationView.OnNavigationItemSelectedListener {

    public static final int REQUEST_STORAGE_CODE = 111;

    static FragNavController fragNavController;
    static List<Fragment> navigation_fragments;

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mViewModel.setNavigator(this);
        EventBus.getDefault().register(this);

        setUpNavigationController(savedInstanceState);
        setUp();

    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        fragNavController.onSaveInstanceState(outState);
    }

    private void setUpNavigationController(Bundle savedInstanceState) {

        fragNavController = new FragNavController(getSupportFragmentManager(), R.id.fragment_container);

        navigation_fragments = new ArrayList<>();
        navigation_fragments.add(HomeFragment.newInstance(0));
        navigation_fragments.add(FavoritesFragment.newInstance(0));
        navigation_fragments.add(CommissionFragment.newInstance(0));
        navigation_fragments.add(AddCommercialFragment.newInstance(0));
        navigation_fragments.add(ProfileFragment.newInstance(0));
        navigation_fragments.add(NotificationsFragment.newInstance(0));
        navigation_fragments.add(CategoriesFragment.newInstance(0));
        navigation_fragments.add(ChatsFragment.newInstance(0));
        navigation_fragments.add(SettingsFragment.newInstance(0));
        navigation_fragments.add(DoctorsFragment.newInstance(0));
        fragNavController.setRootFragments(navigation_fragments);

        fragNavController.setCreateEager(true);
        fragNavController.setFragmentHideStrategy(FragNavController.HIDE);
        UniqueTabHistoryStrategy NavSwitchController = new UniqueTabHistoryStrategy((index, fragNavTransactionOptions) ->
                binding.navigationView.setCheckedItem(binding.navigationView.getMenu().getItem(index)));

        fragNavController.setNavigationStrategy(NavSwitchController);

        fragNavController.initialize(FragNavController.TAB1, savedInstanceState);

        if (savedInstanceState == null) {
            binding.navigationView.setCheckedItem(binding.navigationView.getMenu().getItem(FragNavController.TAB1));
        }


    }

    public static Intent newIntent(Context context) {
        return new Intent(context, MainActivity.class);
    }

    @Override
    public void handleError(Throwable throwable) {
        hideLoading();
        ErrorHandlingUtils.handleErrors(throwable);
    }

    @Override
    public void showMyApiMessage(String message) {
        hideLoading();
        showErrorMessage(message);
    }

    @Override
    public void onBackPressed() {
        if (binding.drawerView.isOpen()) {
            binding.drawerView.closeDrawers();
        } else if (!(fragNavController.getCurrentFrag() instanceof HomeFragment)) {
            fragNavController.switchTab(0);
            binding.homeContainer.bottomNavigation.setSelectedItemId(R.id.home);
            fragNavController.clearStack();
            handleToolBar(navigation_fragments.get(0));

        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void openLoginActivity() {
        startActivity(getIntentWithClearHistory(LoginActivity.class));
        finish();
    }

    @Override
    public void performDependencyInjection(ActivityComponent buildComponent) {
        buildComponent.inject(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        binding.drawerView.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);

        if (mViewModel.getDataManager().isUserLogged()) {
            if (mViewModel.getDataManager().getUserObject() != null && mViewModel.getDataManager().getUserObject().getUnseenNotificationsCount() > 0) {
                binding.homeContainer.bottomNavigation.getOrCreateBadge(R.id.notifications).setNumber(mViewModel.getDataManager().getUserObject().getUnseenNotificationsCount());
                Objects.requireNonNull(binding.homeContainer.bottomNavigation.getBadge(R.id.notifications)).setBadgeGravity(BadgeDrawable.BOTTOM_END);
            } else {
                binding.homeContainer.bottomNavigation.removeBadge(R.id.notifications);
            }
        } else {
            binding.homeContainer.bottomNavigation.removeBadge(R.id.notifications);
        }


    }


    private void handleNavItems() {

        if (mViewModel.getDataManager().isUserLogged()) {
            binding.navigationView.getMenu().findItem(R.id.navFavs).setVisible(true);
            binding.navigationView.getMenu().findItem(R.id.navCommission).setVisible(true);
            binding.navigationView.getMenu().findItem(R.id.navAddCommercial).setVisible(true);
            binding.navigationView.getMenu().findItem(R.id.navLogout).setVisible(true);
        } else {
            binding.navigationView.getMenu().findItem(R.id.navFavs).setVisible(false);
            binding.navigationView.getMenu().findItem(R.id.navCommission).setVisible(false);
            binding.navigationView.getMenu().findItem(R.id.navAddCommercial).setVisible(false);
            binding.navigationView.getMenu().findItem(R.id.navLogout).setVisible(false);
        }

    }

    private void setUp() {

        binding.toolbar.toolbarTitle.setText(getString(R.string.home_menu));
        binding.homeContainer.bottomNavigation.setSelectedItemId(R.id.home);

        binding.homeContainer.bottomNavigation.setOnNavigationItemSelectedListener(this);
        binding.toolbar.drawerButton.setOnClickListener(view -> binding.drawerView.openDrawer(GravityCompat.START));
        binding.toolbar.imgBarEditProfile.setOnClickListener(v -> startActivity(EditProfileActivity.newIntent(this)));

        setupNavMenu();
        subscribeToLiveData();
        handleNavItems();

        mViewModel.getAppSettings();

    }

    private void showAddAqarDialog() {
        MaterialDialog dialog = new MaterialDialog.Builder(this)
                .customView(R.layout.dialog_add_aqar, true)
                .cancelable(true).build();

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        dialog.findViewById(R.id.closeImage).setOnClickListener(v -> dialog.dismiss());

        dialog.findViewById(R.id.cvAddAd).setOnClickListener(v -> {

            startActivity(AddAdTermsActivity.newIntent(this));
            dialog.dismiss();

        });

        dialog.findViewById(R.id.cvAddOrder).setOnClickListener(v -> {

            startActivity(AddingOrderActivity.newIntent(this));
            dialog.dismiss();

        });

        dialog.show();
    }

    private void setupNavMenu() {
        setupNavDrawerHeader();
        binding.navigationView.setItemIconTintList(null);
        binding.navigationView.setNavigationItemSelectedListener(
                item -> {
                    binding.drawerView.closeDrawer(GravityCompat.START);
                    switch (item.getItemId()) {
                        case R.id.navHome:
                            fragNavController.switchTab(0);
                            handleToolBar(navigation_fragments.get(0));
                            ((HomeFragment) navigation_fragments.get(0)).refreshData();

                            binding.homeContainer.bottomNavigation.setSelectedItemId(R.id.home);


                            return true;
                        case R.id.navFavs:
                            fragNavController.switchTab(1);
                            handleToolBar(navigation_fragments.get(1));
                            ((FavoritesFragment) navigation_fragments.get(1)).refreshData();

                            binding.homeContainer.bottomNavigation.setSelected(false);

                            return true;
                        case R.id.navCommission:
                            fragNavController.switchTab(2);
                            handleToolBar(navigation_fragments.get(2));
                            ((CommissionFragment) navigation_fragments.get(2)).refreshData();

                            binding.homeContainer.bottomNavigation.setSelected(false);
                            return true;
                        case R.id.navAddCommercial:
                            fragNavController.switchTab(3);
                            handleToolBar(navigation_fragments.get(3));
                            ((AddCommercialFragment) navigation_fragments.get(3)).refreshData();

                            binding.homeContainer.bottomNavigation.setSelected(false);
                            return true;
                        case R.id.navSettings:

                            fragNavController.switchTab(8);
                            handleToolBar(navigation_fragments.get(8));
                            ((SettingsFragment) navigation_fragments.get(8)).refreshData();

                            binding.homeContainer.bottomNavigation.setSelected(false);

                            return true;

                        case R.id.navDoctors:

                            fragNavController.switchTab(9);
                            handleToolBar(navigation_fragments.get(9));
                            ((DoctorsFragment) navigation_fragments.get(9)).refreshData();

                            binding.homeContainer.bottomNavigation.setSelected(false);

                            return true;

                        case R.id.navLogout:
//                            finish();
                            showLoading();
                            mViewModel.doLogout();
                            return true;

                        default:
                            return false;
                    }
                });
    }


    public void navigateToProfile() {
        fragNavController.switchTab(4);
        handleToolBar(navigation_fragments.get(4));
        ((ProfileFragment) navigation_fragments.get(4)).refreshData();

        binding.homeContainer.bottomNavigation.setSelectedItemId(R.id.profile);
    }

    private void handleToolBar(Fragment fragment) {
        if (fragment instanceof HomeFragment) {
            binding.toolbar.imgBarReadAll.setVisibility(View.GONE);
            binding.toolbar.imgBarEditProfile.setVisibility(View.GONE);
            binding.toolbar.imgBarSearch.setVisibility(View.VISIBLE);
            binding.toolbar.searchBar.setVisibility(View.GONE);
            binding.toolbar.toolbarTitle.setText(getText(R.string.home_menu));
        } else if (fragment instanceof FavoritesFragment) {
            binding.toolbar.imgBarReadAll.setVisibility(View.GONE);
            binding.toolbar.imgBarEditProfile.setVisibility(View.GONE);
            binding.toolbar.imgBarSearch.setVisibility(View.GONE);
            binding.toolbar.searchBar.setVisibility(View.GONE);
            binding.toolbar.toolbarTitle.setText(getText(R.string.my_favorites));
        } else if (fragment instanceof CommissionFragment) {
            binding.toolbar.imgBarReadAll.setVisibility(View.GONE);
            binding.toolbar.imgBarEditProfile.setVisibility(View.GONE);
            binding.toolbar.imgBarSearch.setVisibility(View.GONE);
            binding.toolbar.searchBar.setVisibility(View.GONE);
            binding.toolbar.toolbarTitle.setText(getText(R.string.commission));
        } else if (fragment instanceof AddCommercialFragment) {
            binding.toolbar.imgBarReadAll.setVisibility(View.GONE);
            binding.toolbar.imgBarEditProfile.setVisibility(View.GONE);
            binding.toolbar.imgBarSearch.setVisibility(View.GONE);
            binding.toolbar.searchBar.setVisibility(View.GONE);
            binding.toolbar.toolbarTitle.setText(getText(R.string.add_commercial));
        } else if (fragment instanceof SettingsFragment) {
            binding.toolbar.imgBarReadAll.setVisibility(View.GONE);
            binding.toolbar.imgBarEditProfile.setVisibility(View.GONE);
            binding.toolbar.imgBarSearch.setVisibility(View.GONE);
            binding.toolbar.searchBar.setVisibility(View.GONE);
            binding.toolbar.toolbarTitle.setText(getText(R.string.settings_menu));
        } else if (fragment instanceof ProfileFragment) {
            if (mViewModel.getDataManager().isUserLogged())
                binding.toolbar.imgBarEditProfile.setVisibility(View.VISIBLE);
            else
                binding.toolbar.imgBarEditProfile.setVisibility(View.GONE);
            binding.toolbar.imgBarReadAll.setVisibility(View.GONE);
            binding.toolbar.imgBarSearch.setVisibility(View.GONE);
            binding.toolbar.searchBar.setVisibility(View.GONE);
            binding.toolbar.toolbarTitle.setText(getText(R.string.my_account));
        } else if (fragment instanceof NotificationsFragment) {
            if (mViewModel.getDataManager().isUserLogged())
                binding.toolbar.imgBarReadAll.setVisibility(View.VISIBLE);
            else
                binding.toolbar.imgBarReadAll.setVisibility(View.GONE);
            binding.toolbar.imgBarEditProfile.setVisibility(View.GONE);
            binding.toolbar.imgBarSearch.setVisibility(View.GONE);
            binding.toolbar.searchBar.setVisibility(View.GONE);
            binding.toolbar.toolbarTitle.setText(getText(R.string.notifications));
        } else if (fragment instanceof CategoriesFragment) {
            binding.toolbar.imgBarReadAll.setVisibility(View.GONE);
            binding.toolbar.imgBarEditProfile.setVisibility(View.GONE);
            binding.toolbar.imgBarSearch.setVisibility(View.GONE);
            binding.toolbar.searchBar.setVisibility(View.GONE);
            binding.toolbar.toolbarTitle.setText(getText(R.string.categories));
        } else if (fragment instanceof ChatsFragment) {
            binding.toolbar.imgBarReadAll.setVisibility(View.GONE);
            binding.toolbar.imgBarEditProfile.setVisibility(View.GONE);
            binding.toolbar.imgBarSearch.setVisibility(View.GONE);
            binding.toolbar.searchBar.setVisibility(View.GONE);
            binding.toolbar.toolbarTitle.setText(getText(R.string.chat_text));
        } else if (fragment instanceof DoctorsFragment) {
            binding.toolbar.imgBarReadAll.setVisibility(View.GONE);
            binding.toolbar.imgBarEditProfile.setVisibility(View.GONE);
            binding.toolbar.imgBarSearch.setVisibility(View.GONE);
            binding.toolbar.searchBar.setVisibility(View.GONE);
            binding.toolbar.toolbarTitle.setText(getText(R.string.doctors));
        }
    }

    @SuppressLint("SetTextI18n")
    public void setupNavDrawerHeader() {
        View parent = binding.navigationView.getHeaderView(0);
        TextView tvUsername = parent.findViewById(R.id.tvUsername);
        CircleImageView cUserImage = parent.findViewById(R.id.userImageView);
        ImageView ivLevel = parent.findViewById(R.id.ivLevel);
        RatingBar ratingBar = parent.findViewById(R.id.ratingBarClient);
        TextView tvLanguage = parent.findViewById(R.id.tvLang);
        TextView tvRateCount = parent.findViewById(R.id.tvRateCount);
        TextView tvJoinDate = parent.findViewById(R.id.tvJoinDate);
        CommonUtils.setupRatingBar(ratingBar);


        tvLanguage.setOnClickListener(v -> {
            LanguageHelper.setLanguage(this, LanguageHelper.getLanguage(this).equalsIgnoreCase("ar") ? "en" : "ar");
//            if (mViewModel.getDataManager().isUserLogged()) {
//                binding.drawerView.closeDrawer(GravityCompat.START);
//                showLoading();
//                mViewModel.updateServerLanguage(mViewModel.getDataManager().getCurrentUserId(), LanguageHelper.getLanguage(this));
//            } else {
//                startActivity(getIntentWithClearHistory(MainActivity.class));
//            }

            startActivity(getIntentWithClearHistory(MainActivity.class));

        });


        if (mViewModel.getDataManager().isUserLogged()) {
            tvUsername.setText(mViewModel.getDataManager().getUserObject().getName());
            Glide.with(this)
                    .load(mViewModel.getDataManager().getUserObject().getImage())
                    .placeholder(R.drawable.ic_user_holder)
                    .error(R.drawable.ic_user_holder)
                    .into(cUserImage);

            if (mViewModel.getDataManager().getUserObject().getGrade() == 0)
                Glide.with(this)
                        .load("")
                        .into(ivLevel);
            else if (mViewModel.getDataManager().getUserObject().getGrade() == 1)
                Glide.with(this)
                        .load(R.drawable.ic_bronze)
                        .into(ivLevel);
            else if (mViewModel.getDataManager().getUserObject().getGrade() == 2)
                Glide.with(this)
                        .load(R.drawable.ic_silver)
                        .into(ivLevel);
            else if (mViewModel.getDataManager().getUserObject().getGrade() == 3)
                Glide.with(this)
                        .load(R.drawable.ic_gold)
                        .into(ivLevel);
            else if (mViewModel.getDataManager().getUserObject().getGrade() == 4)
                Glide.with(this)
                        .load(R.drawable.ic_platinum)
                        .into(ivLevel);

            ratingBar.setRating(mViewModel.getDataManager().getUserObject().getRate());
            tvRateCount.setText("(" + mViewModel.getDataManager().getUserObject().getRateCount() + ")");

            tvJoinDate.setText(getString(R.string.join_at) + " " + DateUtility.getDateOnlyTFormat(mViewModel.getDataManager().getUserObject().getCreatedAt()));

        } else tvUsername.setText(getText(R.string.login_drawer));

        cUserImage.setOnClickListener(view -> {
            if (mViewModel.getDataManager().isUserLogged()) {
                navigateToProfile();
                binding.drawerView.closeDrawer(GravityCompat.START);
            } else mViewModel.getNavigator().openLoginActivity();
        });

        tvUsername.setOnClickListener(view -> {
            if (mViewModel.getDataManager().isUserLogged()) {
                navigateToProfile();
                binding.drawerView.closeDrawer(GravityCompat.START);
            } else mViewModel.getNavigator().openLoginActivity();
        });

    }

    private void subscribeToLiveData() {

        mViewModel.getLogoutLiveData().observe(this, response -> {
            hideLoading();
            binding.drawerView.closeDrawer(GravityCompat.START);
            mViewModel.getDataManager().setUserAsLoggedOut();
            openLoginActivity();
        });

    }

    @Override
    public void pushFragment(Fragment fragment) {
        fragNavController.pushFragment(fragment);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_STORAGE_CODE) {
            ((ProfileFragment) navigation_fragments.get(4)).MyOnRequestPermissionResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.profile:
                if (fragNavController.getCurrentFrag() instanceof ProfileFragment) {
                    ((ProfileFragment) navigation_fragments.get(4)).refreshData();
                    return true;
                }

                fragNavController.switchTab(4);
                handleToolBar(navigation_fragments.get(4));

                return true;
            case R.id.notifications:

                if (fragNavController.getCurrentFrag() instanceof NotificationsFragment) {
                    ((NotificationsFragment) navigation_fragments.get(5)).refreshData();
                    return true;
                }

                fragNavController.switchTab(5);
                handleToolBar(navigation_fragments.get(5));

                return true;
            case R.id.home:

                if (fragNavController.getCurrentFrag() instanceof HomeFragment) {
                    ((HomeFragment) navigation_fragments.get(0)).refreshData();
                    return true;
                }

                fragNavController.switchTab(0);
                handleToolBar(navigation_fragments.get(0));

                return true;
            case R.id.categories:

                if (fragNavController.getCurrentFrag() instanceof CategoriesFragment) {
                    ((CategoriesFragment) navigation_fragments.get(6)).refreshData();
                    return true;
                }

                fragNavController.switchTab(6);
                handleToolBar(navigation_fragments.get(6));

                return true;
            case R.id.chats:

                if (fragNavController.getCurrentFrag() instanceof ChatsFragment) {
                    ((ChatsFragment) navigation_fragments.get(7)).refreshData();
                    return true;
                }

                fragNavController.switchTab(7);
                handleToolBar(navigation_fragments.get(7));
                ((ChatsFragment) navigation_fragments.get(7)).refreshData();

                return true;
        }
        return false;

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void updateProfile(String keyword) {
        if (keyword.equalsIgnoreCase("update_profile")) {
            setupNavDrawerHeader();
        }
    }


    public void completeProfile() {
        startActivity(EditProfileActivity.newIntent(this));
    }

    public void navigateToCategories() {

        ((CategoriesFragment) navigation_fragments.get(6)).refreshData();
        binding.homeContainer.bottomNavigation.setSelectedItemId(R.id.categories);
        fragNavController.switchTab(6);
        handleToolBar(navigation_fragments.get(6));

    }

    public void navigateToDoctors() {

        ((DoctorsFragment) navigation_fragments.get(9)).refreshData();
        binding.homeContainer.bottomNavigation.setSelected(false);
        fragNavController.switchTab(9);
        handleToolBar(navigation_fragments.get(9));

    }
}
