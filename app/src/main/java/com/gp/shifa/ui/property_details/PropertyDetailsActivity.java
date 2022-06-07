
package com.gp.shifa.ui.property_details;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.dynamiclinks.DynamicLink;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.gp.shifa.R;
import com.gp.shifa.data.models.AdAndOrderModel;
import com.gp.shifa.data.models.GoogleShopModel;
import com.gp.shifa.data.models.LikeModel;
import com.gp.shifa.data.models.SpecOptionModel;
import com.gp.shifa.databinding.ActivityPropertyDetailsBinding;
import com.gp.shifa.databinding.DialogReportBinding;
import com.gp.shifa.di.component.ActivityComponent;
import com.gp.shifa.ui.base.BaseActivity;
import com.gp.shifa.ui.chat.ChatActivity;
import com.gp.shifa.ui.common.SliderAdapter;
import com.gp.shifa.ui.member_profile.MemberProfileActivity;
import com.gp.shifa.ui.member_profile.member_ads.MemberAdsAdapter;
import com.gp.shifa.ui.profile.MyProfileActivity;
import com.gp.shifa.utils.CommonUtils;
import com.gp.shifa.utils.DateUtility;
import com.gp.shifa.utils.ErrorHandlingUtils;
import com.gp.shifa.utils.ImageUtils;
import com.gp.shifa.utils.LanguageHelper;
import com.gp.shifa.utils.LocationUtils;
import com.smarteist.autoimageslider.IndicatorAnimations;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import timber.log.Timber;

public class PropertyDetailsActivity extends BaseActivity<PropertyDetailsViewModel> implements PropertyDetailsNavigator, OnMapReadyCallback, MemberAdsAdapter.Callback {


    @Inject
    LinearLayoutManager similarAdsLayoutManager;
    @Inject
    MemberAdsAdapter similarAdsAdapter;
    @Inject
    LinearLayoutManager specsLayoutManager;
    @Inject
    PropertySpecsAdapter propertySpecsAdapter;
    @Inject
    SliderAdapter sliderAdapter;

    GoogleMap map;

    ActivityPropertyDetailsBinding binding;

    AdAndOrderModel itemDetails;
    private boolean isFetched = false;
    private List<GoogleShopModel> googlePlacesList = new ArrayList<>();

    public static Intent newIntent(Context context) {
        return new Intent(context, PropertyDetailsActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPropertyDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mViewModel.setNavigator(this);
        similarAdsAdapter.setCallback(this);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        binding.mMapView.onCreate(savedInstanceState);
        binding.mMapView.getMapAsync(this);

        setUp();

    }

    private void setUp() {

        subscribeViewModel();
        setUpSpecsAdapter();
        setUpSimilarAdsAdapter();
        setUpSlider();
        setUpOnViewClicked();


    }

    private void setUpSimilarAdsAdapter() {
        binding.rvSimilarAds.setLayoutManager(similarAdsLayoutManager);
        binding.rvSimilarAds.setAdapter(similarAdsAdapter);
    }

    private void setUpSpecsAdapter() {
        binding.rvSpecs.setLayoutManager(specsLayoutManager);
        binding.rvSpecs.setAdapter(propertySpecsAdapter);
    }

    private void setUpSlider() {
        binding.slider.setSliderAdapter(sliderAdapter);
        binding.slider.setIndicatorAnimation(IndicatorAnimations.WORM);
        binding.slider.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        binding.slider.setAutoCycleDirection(SliderView.LAYOUT_DIRECTION_LOCALE);
        binding.slider.setScrollTimeInSec(5);
        binding.slider.startAutoCycle();
    }

    @SuppressLint("SetTextI18n")
    private void subscribeViewModel() {

        mViewModel.getItemDetailsLiveData().observe(this, response -> {
            hideLoading();
            binding.swipeRefreshView.setRefreshing(false);

            itemDetails = response.getData();

            if (mViewModel.getDataManager().isUserLogged() && itemDetails.getUser() != null &&
                    itemDetails.getUser().getUser().getId() == mViewModel.getDataManager().getUserObject().getUser().getId()) {

                binding.tvReport.setVisibility(View.GONE);
                binding.tvChat.setVisibility(View.GONE);
                binding.tvCall.setVisibility(View.GONE);

            }

            if (response.getData().getType() == 1)
                binding.toolbar.toolbarTitle.setText(getString(R.string.ad_number) + " " + response.getData().getId());
            else
                binding.toolbar.toolbarTitle.setText(getString(R.string.order_number) + " " + response.getData().getId());

            if (response.getData().getImages() != null && response.getData().getImages().isEmpty()
                    && response.getData().getVideos() != null && response.getData().getVideos().isEmpty()) {

                binding.slider.setVisibility(View.GONE);

            } else if (response.getData().getImages() != null && !response.getData().getImages().isEmpty()) {
                sliderAdapter.addItems(response.getData().getImages());
                if (response.getData().getVideos() != null && !response.getData().getVideos().isEmpty())
                    sliderAdapter.addItem(response.getData().getVideos());
                binding.slider.setVisibility(View.VISIBLE);
            } else {

                if (response.getData().getVideos() != null && !response.getData().getVideos().isEmpty()) {
                    sliderAdapter.addItems(response.getData().getVideos());
                    binding.slider.setVisibility(View.VISIBLE);
                } else
                    binding.slider.setVisibility(View.GONE);
            }

            if (response.getData().getType() == 1) {

                if (response.getData().getTitle() != null)
                    binding.tvAdTitle.setText(response.getData().getTitle());
                else
                    binding.tvAdTitle.setText(getString(R.string.n_a));

                if (response.getData().getPrice() != null && !response.getData().getPrice().isEmpty())
                    binding.tvAdPrice.setText(response.getData().getPrice() + " " + getString(R.string.sr));
                else
                    binding.tvAdPrice.setText(getString(R.string.n_a));

            } else {

                if (response.getData().getCategories() != null && !response.getData().getCategories().isEmpty())
                    binding.tvAdTitle.setText("(" + getString(R.string.order) + ") " + response.getData().getCategories().get(0).getTitle());
                else
                    binding.tvAdTitle.setText("(" + getString(R.string.order) + ")");

                if (response.getData().getPriceMin() != null && response.getData().getPriceMax() != null && !response.getData().getPriceMin().isEmpty() && !response.getData().getPriceMax().isEmpty())
                    binding.tvAdPrice.setText(CommonUtils.setPriceCurrency(this, Double.parseDouble(response.getData().getPriceMin())) + " : " +
                            CommonUtils.setPriceCurrency(this, Double.parseDouble(response.getData().getPriceMax())));
                else
                    binding.tvAdPrice.setText(getString(R.string.n_a));

            }

            if (response.getData().getCountry() != null)
                binding.tvAdLocation.setText(response.getData().getCountry());
            else
                binding.tvAdLocation.setText(getString(R.string.n_a));

            if (response.getData().getCreatedAt() != null)
                binding.tvAdDate.setText(response.getData().getCreatedAt());
            else
                binding.tvAdDate.setText(getString(R.string.n_a));

            if (response.getData().getUpdatedAt() != null)
                binding.tvAdUpdateDate.setText(getString(R.string.last_update) + " " + DateUtility.getDateOnlyTFormat(response.getData().getUpdatedAt()));
            else
                binding.tvAdUpdateDate.setText(getString(R.string.last_update) + " " + getString(R.string.n_a));

            binding.tvAdViews.setText(response.getData().getViewsCount() + "");

            propertySpecsAdapter.addItems(response.getData().getOptions());

            SpecOptionModel memberRelation = new SpecOptionModel();
            memberRelation.setOptionType("text");
            memberRelation.setOptionTitle(getString(R.string.advertiser_relation));

            if (response.getData().getWonerRelationId() == 1) {
                memberRelation.setOptionValue(getString(R.string.owner));

                SpecOptionModel ownerPercent = new SpecOptionModel();
                ownerPercent.setOptionType("text");
                ownerPercent.setOptionTitle(getString(R.string.own_percentage));
                ownerPercent.setOptionValue(response.getData().getWonerPerc() + " %");

                propertySpecsAdapter.addItem(memberRelation);
                propertySpecsAdapter.addItem(ownerPercent);

            } else if (response.getData().getWonerRelationId() == 2) {
                memberRelation.setOptionValue(getString(R.string.agent));
                propertySpecsAdapter.addItem(memberRelation);
            } else if (response.getData().getWonerRelationId() == 3) {
                memberRelation.setOptionValue(getString(R.string.marketer));
                propertySpecsAdapter.addItem(memberRelation);

            }

            if (response.getData().getDescription() != null)
                binding.tvPropertyDesc.setText(response.getData().getDescription());
            else
                binding.tvPropertyDesc.setText(getString(R.string.n_a));

            if (response.getData().getLng() != null && response.getData().getLat() != null) {

                map.addMarker(new MarkerOptions()
                        .position(new LatLng(
                                Double.parseDouble(response.getData().getLat()),
                                Double.parseDouble(response.getData().getLng())))
                        .title(itemDetails.getTitle())
                        .icon(ImageUtils.bitmapDescriptorFromVector(this, R.drawable.ic_pin)));

                map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(
                        Double.parseDouble(response.getData().getLat()),
                        Double.parseDouble(response.getData().getLng())), 12f));
            }


            if (response.getData().getCategories() != null && !response.getData().getCategories().isEmpty())
                mViewModel.getSimilarItems(response.getData().getCategories().get(0).getId(), response.getData().getId());
            else {
                binding.tvSimilarAds.setVisibility(View.GONE);
                binding.rvSimilarAds.setVisibility(View.GONE);
            }

            CommonUtils.setupRatingBar(binding.ratingBarClient);

            if (response.getData().getUser() != null) {

                binding.tvUsername.setText(response.getData().getUser().getUser().getName());
                Glide.with(this)
                        .load(response.getData().getUser().getUser().getImgSrc()
                                + "/" + response.getData().getUser().getUser().getImg())
                        .placeholder(R.drawable.ic_user_holder)
                        .error(R.drawable.ic_user_holder)
                        .into(binding.userImageView);

            }

            if (response.getData().getIsLikedByMe() != null)
                binding.tvFavorite.setCompoundDrawablesRelativeWithIntrinsicBounds(0, R.drawable.ic_favorite_round, 0, 0);


        });

        mViewModel.getSimilarItemsLiveData().observe(this, response -> {
            similarAdsAdapter.clearItems();
            similarAdsAdapter.addItems(response.getData());
        });

        mViewModel.getReportItemLiveData().observe(this, response -> {
            hideLoading();
            showSuccessMessage(response.getMessage());
        });

        mViewModel.getGoogleTypesLiveData().observe(this, response -> {

            if (response.getData().isEmpty()) {
                binding.cbShowServices.setChecked(false);
                hideLoading();
                return;
            }

            List<String> types = new ArrayList<>();
            for (int i = 0; i < response.getData().size(); i++) {
                types.add(response.getData().get(i).getSlug());

                if (i == response.getData().size() - 1) {
                    if (itemDetails != null && itemDetails.getLat() != null && itemDetails.getLng() != null &&
                            !itemDetails.getLat().isEmpty() && !itemDetails.getLng().isEmpty() &&
                            LocationUtils.isLatLngValid(itemDetails.getLat(), itemDetails.getLng()))
                        mViewModel.getNearByServices(getMapCategoryLocationBased(TextUtils.join("|", types)));
                    else {
                        binding.cbShowServices.setChecked(false);
                        hideLoading();
                    }
                }
            }


        });


        mViewModel.getShopsLiveData().observe(this, response -> {
            map.clear();
            map.addMarker(new MarkerOptions()
                    .position(new LatLng(
                            Double.parseDouble(itemDetails.getLat()),
                            Double.parseDouble(itemDetails.getLng())))
                    .title(itemDetails.getTitle())
                    .icon(ImageUtils.bitmapDescriptorFromVector(this, R.drawable.ic_pin)));

            for (GoogleShopModel shop : response.getShopsList()) {

                map.addMarker(new MarkerOptions()
                        .position(new LatLng(
                                shop.getGeometry().getLocation().getLat(),
                                shop.getGeometry().getLocation().getLng()))
                        .title(shop.getName())
                        .icon(BitmapDescriptorFactory.fromBitmap(ImageUtils.getBitmapFromLink(shop.getIcon()))));

            }

            isFetched = true;
            googlePlacesList = response.getShopsList();

            hideLoading();

        });

    }

    private Map<String, String> getMapCategoryLocationBased(String types) {
        Timber.e("getMapCategoryLocationBased >>> %s", types);
        Map<String, String> map = new HashMap<>();

        map.put("key", getString(R.string.google_map_api_key));
        map.put("language", LanguageHelper.getLanguage(this));
        map.put("type", types);
        map.put("location", itemDetails.getLat() + "," + itemDetails.getLng());
        map.put("rankby", "distance");
        return map;
    }

    public void setUpOnViewClicked() {

        binding.toolbar.backButton.setOnClickListener(v -> finish());

        binding.llMember.setOnClickListener(v -> {
            if (itemDetails != null && itemDetails.getUser() != null) {
                if (mViewModel.getDataManager().isUserLogged()) {
                    if (mViewModel.getDataManager().getUserObject().getUser().getId() == itemDetails.getUser().getUser().getId()) {
                        startActivity(MyProfileActivity.newIntent(this));
                    } else {
                        startActivity(MemberProfileActivity.newIntent(this).putExtra("userId", itemDetails.getUser().getUser().getId()));
                    }
                } else {
                    startActivity(MemberProfileActivity.newIntent(this).putExtra("userId", itemDetails.getUser().getUser().getId()));
                }
            }
        });

        binding.tvReport.setOnClickListener(v -> {

            if (mViewModel.getDataManager().isUserLogged()) {
                showReportDialog();
            } else {
                CommonUtils.handleNotAuthenticated(this);
            }

        });

        binding.tvShare.setOnClickListener(v -> shareItem());

        binding.tvFavorite.setOnClickListener(v -> {

            if (mViewModel.getDataManager().isUserLogged()) {
                showLoading();
                mViewModel.doFavorite(itemDetails.getId());
            } else {
                CommonUtils.handleNotAuthenticated(this);
            }

        });

        binding.tvChat.setOnClickListener(v -> {

            if (mViewModel.getDataManager().isUserLogged()) {
                if (itemDetails.getUser() != null) {
                    startActivity(ChatActivity.newIntent(this)
                            .putExtra("receiver_id", itemDetails.getUser().getUser().getId()));
                }
            } else {
                CommonUtils.handleNotAuthenticated(this);
            }

        });

        binding.tvCall.setOnClickListener(v -> {

            if (mViewModel.getDataManager().isUserLogged()) {
                if (itemDetails.getUser() != null)
                    startActivity(new Intent(Intent.ACTION_DIAL).setData(Uri.parse("tel:" + itemDetails.getUser().getUser().getPhone())));
            } else {
                CommonUtils.handleNotAuthenticated(this);
            }

        });

        binding.cbShowServices.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (buttonView.isPressed() && itemDetails != null &&
                    itemDetails.getLat() != null && itemDetails.getLng() != null &&
                    !itemDetails.getLat().isEmpty() && !itemDetails.getLng().isEmpty()) {

                if (isChecked) {
                    if (isFetched) {

                        map.clear();
                        map.addMarker(new MarkerOptions()
                                .position(new LatLng(
                                        Double.parseDouble(itemDetails.getLat()),
                                        Double.parseDouble(itemDetails.getLng())))
                                .title(itemDetails.getTitle())
                                .icon(ImageUtils.bitmapDescriptorFromVector(this, R.drawable.ic_pin)));

                        for (GoogleShopModel shop : googlePlacesList) {

                            map.addMarker(new MarkerOptions()
                                    .position(new LatLng(
                                            shop.getGeometry().getLocation().getLat(),
                                            shop.getGeometry().getLocation().getLng()))
                                    .title(shop.getName())
                                    .icon(BitmapDescriptorFactory.fromBitmap(ImageUtils.getBitmapFromLink(shop.getIcon()))));

                        }

                    } else {
                        showLoading();
                        mViewModel.getGoogleServicesTypes();
                    }
                } else {
                    map.clear();
                    map.addMarker(new MarkerOptions()
                            .position(new LatLng(
                                    Double.parseDouble(itemDetails.getLat()),
                                    Double.parseDouble(itemDetails.getLng())))
                            .title(itemDetails.getTitle())
                            .icon(ImageUtils.bitmapDescriptorFromVector(this, R.drawable.ic_pin)));

                }

            }
        });

    }

    private void showReportDialog() {
        DialogReportBinding reportBinding = DialogReportBinding.inflate(getLayoutInflater());
        MaterialDialog dialog = new MaterialDialog.Builder(this)
                .customView(reportBinding.getRoot(), true)
                .cancelable(true).build();


        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        reportBinding.okBtn.setOnClickListener(v -> {

            if (reportBinding.msgBoxDialog.getText().toString().trim().isEmpty()) {
                reportBinding.msgBoxDialog.setError(getText(R.string.write_comment));
                reportBinding.msgBoxDialog.requestFocus();
                return;
            }
            reportBinding.msgBoxDialog.setError(null);

            showLoading();
            mViewModel.reportItem(itemDetails.getId(), reportBinding.msgBoxDialog.getText().toString().trim());

            dialog.dismiss();

        });

        dialog.show();

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
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;

        this.map.clear();

        map.getUiSettings().setAllGesturesEnabled(false);
        map.getUiSettings().setZoomControlsEnabled(true);

        showLoading();
        mViewModel.getItemDetails(getIntent().getIntExtra("itemId", -1));

        binding.swipeRefreshView.setOnRefreshListener(() -> {
            showLoading();
            binding.swipeRefreshView.setRefreshing(true);
            mViewModel.getItemDetails(getIntent().getIntExtra("itemId", -1));
        });

    }

    @Override
    public void onResume() {
        binding.mMapView.onResume();
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding.mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        binding.mMapView.onLowMemory();
    }

    @Override
    public void getPropertyDetails(int id) {
        startActivity(PropertyDetailsActivity.newIntent(this).putExtra("itemId", id));
    }

    @Override
    public void favoriteOrUnFavorite(int itemId, int position) {
        if (mViewModel.getDataManager().isUserLogged()) {
            showLoading();
            mViewModel.favoriteOrUnFavorite(itemId, position);

        } else {
            CommonUtils.handleNotAuthenticated(this);
        }
    }

    @Override
    public void favoriteDone(LikeModel data, int position) {
        hideLoading();

        if (data.getLikes() == 1) {
            similarAdsAdapter.mAdsList.get(position).setIsLikedByMe("true");
        } else {
            similarAdsAdapter.mAdsList.get(position).setIsLikedByMe(null);
        }

        similarAdsAdapter.notifyItemChanged(position);
    }

    @Override
    public void favoriteDone(LikeModel data) {
        hideLoading();
        if (data.getLikes() == 1) {
            itemDetails.setIsLikedByMe("true");
            binding.tvFavorite.setCompoundDrawablesRelativeWithIntrinsicBounds(0, R.drawable.ic_favorite_round, 0, 0);
        } else {
            itemDetails.setIsLikedByMe(null);
            binding.tvFavorite.setCompoundDrawablesRelativeWithIntrinsicBounds(0, R.drawable.ic_un_favorite_round, 0, 0);
        }
    }

    @SuppressLint("LogNotTimber")
    public void shareItem() {

        FirebaseDynamicLinks.getInstance().createDynamicLink()
                .setLink(Uri.parse("https://www.aqar.7jazi.com/dashboard/api/v1/shared?item_id=" + itemDetails.getId()))
                .setDomainUriPrefix("https://alaqaria.page.link?item_id=" + itemDetails.getId())
                .setAndroidParameters(new DynamicLink.AndroidParameters.Builder().build())
                .setIosParameters(new DynamicLink.IosParameters.Builder("com.qrc.aqar").build())
                .buildShortDynamicLink()
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Short link created
                        Uri shortLink = task.getResult().getShortLink();
                        if (shortLink != null) {
                            String itemLinkShare = shortLink.toString();
                            Log.e("TAG", "setupDeepLinkShort: " + itemLinkShare);

                            Intent shareIntent = new Intent(Intent.ACTION_SEND);
                            shareIntent.setType("text/plain");
                            shareIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name));
                            String title = itemDetails.getTitle() != null ? itemDetails.getTitle() : "";
                            String description = itemDetails.getDescription() != null ? itemDetails.getDescription() : "";
                            String city = itemDetails.getCountry() != null ? itemDetails.getCountry() : "";

                            String shareBody = title + "\n" + description + "\n" + city + "\n" + itemLinkShare;
                            shareIntent.putExtra(Intent.EXTRA_TEXT, shareBody);

                            startActivity(Intent.createChooser(shareIntent, getString(R.string.share_via)));

                        }
                    } else {
                        Log.e("TAG", "setupDeepLinkShort: " + task.getException());
                    }
                });


    }
}
