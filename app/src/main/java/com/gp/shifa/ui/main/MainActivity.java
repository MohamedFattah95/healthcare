package com.gp.shifa.ui.main;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.gp.shifa.R;
import com.gp.shifa.databinding.ActivityMainBinding;
import com.gp.shifa.di.component.ActivityComponent;
import com.gp.shifa.ui.add_commercial.AddCommercialFragment;
import com.gp.shifa.ui.base.BaseActivity;
import com.gp.shifa.ui.base.BaseFragment;
import com.gp.shifa.ui.categories.CategoriesFragment;
import com.gp.shifa.ui.chats.ChatsFragment;
import com.gp.shifa.ui.commission.CommissionFragment;
import com.gp.shifa.ui.doctors.DoctorsFragment;
import com.gp.shifa.ui.edit_profile.EditProfileActivity;
import com.gp.shifa.ui.favorites.FavoritesFragment;
import com.gp.shifa.ui.home.HomeFragment;
import com.gp.shifa.ui.notifications.NotificationsFragment;
import com.gp.shifa.ui.settings.SettingsFragment;
import com.gp.shifa.ui.user.login.LoginActivity;
import com.gp.shifa.ui.user.profile.ProfileFragment;
import com.gp.shifa.utils.CommonUtils;
import com.gp.shifa.utils.ErrorHandlingUtils;
import com.gp.shifa.utils.LanguageHelper;
import com.ncapdevi.fragnav.FragNavController;
import com.ncapdevi.fragnav.tabhistory.UniqueTabHistoryStrategy;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

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
            startActivity(getIntentWithClearHistory(MainActivity.class));

        });


        if (mViewModel.getDataManager().isUserLogged()) {
            tvUsername.setText(mViewModel.getDataManager().getUserObject().getUser().getName());
            Glide.with(this)
                    .load(mViewModel.getDataManager().getUserObject().getUser().getImgSrc() + "/" +
                            mViewModel.getDataManager().getUserObject().getUser().getImg())
                    .placeholder(R.drawable.ic_user_holder)
                    .error(R.drawable.ic_user_holder)
                    .into(cUserImage);

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
