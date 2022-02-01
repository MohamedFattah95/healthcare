
package com.gp.health.ui.adding_ad.add_ad_details;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.gms.maps.model.LatLng;
import com.gp.health.R;
import com.gp.health.data.models.AdAndOrderModel;
import com.gp.health.data.models.MediaModel;
import com.gp.health.databinding.ActivityAddAdDetailsBinding;
import com.gp.health.di.component.ActivityComponent;
import com.gp.health.ui.adding_ad.add_ad_info_desc.AddAdInfoDescActivity;
import com.gp.health.ui.base.BaseActivity;
import com.gp.health.utils.ErrorHandlingUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.HashMap;

import javax.inject.Inject;

public class AddAdDetailsActivity extends BaseActivity<AddAdDetailsViewModel> implements AddAdDetailsNavigator, DynamicSpecsAdapter.Callback {

    @Inject
    LinearLayoutManager specsLinearLayoutManager;
    @Inject
    DynamicSpecsAdapter dynamicSpecsAdapter;

    ActivityAddAdDetailsBinding binding;

    int selectedCategoryId = -1;
    ArrayList<MediaModel> imgList = new ArrayList<>();
    ArrayList<MediaModel> vidList = new ArrayList<>();
    LatLng mLatLng;

    HashMap<String, String> optionsMap = new HashMap<>();

    AdAndOrderModel adDetails = null;

    public static Intent newIntent(Context context) {
        return new Intent(context, AddAdDetailsActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddAdDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mViewModel.setNavigator(this);
        dynamicSpecsAdapter.setCallback(this);
        EventBus.getDefault().register(this);

        binding.toolbar.toolbarTitle.setText(R.string.ad_details);
        setUp();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void finishAd(String keyword) {
        if (keyword.equals("finish_ad"))
            finish();
    }

    private void setUp() {

        selectedCategoryId = getIntent().getIntExtra("categoryId", -1);
        imgList = (ArrayList<MediaModel>) getIntent().getSerializableExtra("imgList");
        vidList = (ArrayList<MediaModel>) getIntent().getSerializableExtra("vidList");
        mLatLng = getIntent().getParcelableExtra("latLng");

        if (getIntent().hasExtra("adDetails")) {
            adDetails = (AdAndOrderModel) getIntent().getSerializableExtra("adDetails");
        }

        setupSpecsAdapter();
        setUpOnViewClicked();
        subscribeViewModel();

        showLoading();
        mViewModel.getSpecOptions(selectedCategoryId);
    }

    private void setupSpecsAdapter() {
        binding.rvDynamicSpecs.setLayoutManager(specsLinearLayoutManager);
        binding.rvDynamicSpecs.setAdapter(dynamicSpecsAdapter);
    }

    private void subscribeViewModel() {

        mViewModel.getSpecOptionLiveData().observe(this, response -> {
            hideLoading();

            if (adDetails == null)
                dynamicSpecsAdapter.addItems(response.getData());
            else {

                for (int i = 0; i < response.getData().size(); i++) {
                    for (int k = 0; k < adDetails.getOptions().size(); k++) {

                        if (response.getData().get(i).getOptionId() == adDetails.getOptions().get(k).getOptionId()) {
                            if (response.getData().get(i).getOptionType().equalsIgnoreCase("text") ||
                                    response.getData().get(i).getOptionType().equalsIgnoreCase("number")) {
                                response.getData().get(i).setOptionValue(adDetails.getOptions().get(k).getOptionValue());
                            } else if (response.getData().get(i).getOptionType().equalsIgnoreCase("checkbox")) {

                                for (int z = 0; z < response.getData().get(i).getOptionValues().size(); z++) {

                                    if (adDetails.getOptions().get(k).getOptionType().equalsIgnoreCase("checkbox")) {

                                        for (int x = 0; x < adDetails.getOptions().get(k).getItemOptionValues().size(); x++) {

                                            if (adDetails.getOptions().get(k).getItemOptionValues().get(x).getOptionValueId() ==
                                                    response.getData().get(i).getOptionValues().get(z).getOptionValueId())
                                                response.getData().get(i).getOptionValues().get(z).setSelected(true);
                                        }
                                    }

                                }


                            } else if (response.getData().get(i).getOptionType().equalsIgnoreCase("select")) {
                                if (adDetails.getOptions().get(k).getItemOptionValues() != null && !adDetails.getOptions().get(k).getItemOptionValues().isEmpty())
                                    response.getData().get(i).setSelectedOptionValue(adDetails.getOptions().get(k).getItemOptionValues().get(0));
                            } else {
                                response.getData().get(i).setSelectedOptionValue(null);
                            }
                            break;
                        }

                    }

                    if (i == response.getData().size() - 1) {
                        dynamicSpecsAdapter.addItems(response.getData());
                    }
                }


            }

        });

    }

    public void setUpOnViewClicked() {

        binding.toolbar.backButton.setOnClickListener(v -> finish());

        binding.btnContinue.setOnClickListener(v -> {

            for (int i = 0; i < dynamicSpecsAdapter.mSpecsList.size(); i++) {

                if (dynamicSpecsAdapter.mSpecsList.get(i).getOptionIsRequired() == 1 &&
                        dynamicSpecsAdapter.mSpecsList.get(i).getOptionType().equals("text") &&
                        dynamicSpecsAdapter.mSpecsList.get(i).getOptionValue() == null) {

                    showMessage(dynamicSpecsAdapter.mSpecsList.get(i).getOptionTitle() + " " + getString(R.string.is_required));
                    binding.rvDynamicSpecs.smoothScrollToPosition(i);
                    break;

                } else if (dynamicSpecsAdapter.mSpecsList.get(i).getOptionIsRequired() == 1 &&
                        dynamicSpecsAdapter.mSpecsList.get(i).getOptionType().equals("number") &&
                        dynamicSpecsAdapter.mSpecsList.get(i).getOptionValue() == null) {

                    showMessage(dynamicSpecsAdapter.mSpecsList.get(i).getOptionTitle() + " " + getString(R.string.is_required));
                    binding.rvDynamicSpecs.smoothScrollToPosition(i);
                    break;

                } else if (dynamicSpecsAdapter.mSpecsList.get(i).getOptionIsRequired() == 1 &&
                        dynamicSpecsAdapter.mSpecsList.get(i).getOptionType().equals("select") &&
                        dynamicSpecsAdapter.mSpecsList.get(i).getSelectedOptionValue() == null) {

                    showMessage(dynamicSpecsAdapter.mSpecsList.get(i).getOptionTitle() + " " + getString(R.string.is_required));
                    binding.rvDynamicSpecs.smoothScrollToPosition(i);
                    break;

                }

                if (i == dynamicSpecsAdapter.mSpecsList.size() - 1) {

                    if (adDetails == null) {
                        startActivity(AddAdInfoDescActivity.newIntent(this)
                                .putExtra("categoryId", selectedCategoryId)
                                .putExtra("imgList", imgList)
                                .putExtra("vidList", vidList)
                                .putExtra("latLng", mLatLng)
                                .putExtra("options", optionsMap)
                        );
                    } else {
                        startActivity(AddAdInfoDescActivity.newIntent(this)
                                .putExtra("adDetails", adDetails)
                                .putExtra("categoryId", selectedCategoryId)
                                .putExtra("imgList", imgList)
                                .putExtra("vidList", vidList)
                                .putExtra("latLng", mLatLng)
                                .putExtra("options", optionsMap)
                        );
                    }

                }

            }


        });

    }

    @Override
    public void performDependencyInjection(ActivityComponent buildComponent) {
        buildComponent.inject(this);
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
    public void noOptions() {
        hideLoading();

        // TODO auto open next screen
        /*if (adDetails == null) {
            startActivity(AddAdInfoDescActivity.newIntent(this)
                    .putExtra("categoryId", selectedCategoryId)
                    .putExtra("imgList", imgList)
                    .putExtra("vidList", vidList)
                    .putExtra("latLng", mLatLng)
                    .putExtra("options", optionsMap)
            );
        } else {
            startActivity(AddAdInfoDescActivity.newIntent(this)
                    .putExtra("adDetails", adDetails)
                    .putExtra("categoryId", selectedCategoryId)
                    .putExtra("imgList", imgList)
                    .putExtra("vidList", vidList)
                    .putExtra("latLng", mLatLng)
                    .putExtra("options", optionsMap)
            );
        }*/
    }

    @Override
    public void setOption(String key, String value) {
        optionsMap.put(key, value);
    }

    @Override
    public void removeOption(String key) {
        optionsMap.remove(key);
    }
}
