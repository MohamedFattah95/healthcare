
package com.gp.health.ui.contact_us;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.gp.health.R;
import com.gp.health.databinding.ActivityContactUsBinding;
import com.gp.health.di.component.ActivityComponent;
import com.gp.health.ui.base.BaseActivity;
import com.gp.health.utils.CommonUtils;
import com.gp.health.utils.ErrorHandlingUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ContactUsActivity extends BaseActivity<ContactUsViewModel> implements ContactUsNavigator {

    ActivityContactUsBinding binding;

    private String selectedMsgTypeId;

    public static Intent newIntent(Context context) {
        return new Intent(context, ContactUsActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityContactUsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mViewModel.setNavigator(this);

        binding.toolbar.toolbarTitle.setText(R.string.contact_us);
        binding.toolbar.backButton.setOnClickListener(v -> finish());

        if (mViewModel.getDataManager().isUserLogged()) {
            binding.etName.setText(mViewModel.getDataManager().getUserObject().getName());
            binding.etEmail.setText(mViewModel.getDataManager().getUserObject().getEmail());
            binding.etPhone.setText(mViewModel.getDataManager().getUserObject().getMobile());

        }

        showLoading();
        mViewModel.getMessageTypes();


        subscribeViewModel();
        setupOnViewClicked();


        if (mViewModel.getDataManager().getSettingsObject() != null) {

            if (mViewModel.getDataManager().getSettingsObject().getFacebook() == null || mViewModel.getDataManager().getSettingsObject().getFacebook().isEmpty())
                binding.facebookIcon.setVisibility(View.GONE);


            if (mViewModel.getDataManager().getSettingsObject().getInstagram() == null || mViewModel.getDataManager().getSettingsObject().getInstagram().isEmpty())
                binding.instaIcon.setVisibility(View.GONE);


            if (mViewModel.getDataManager().getSettingsObject().getSnapchat() == null || mViewModel.getDataManager().getSettingsObject().getSnapchat().isEmpty())
                binding.snapchatIcon.setVisibility(View.GONE);


            if (mViewModel.getDataManager().getSettingsObject().getTweeter() == null || mViewModel.getDataManager().getSettingsObject().getTweeter().isEmpty())
                binding.twitterIcon.setVisibility(View.GONE);


            if (mViewModel.getDataManager().getSettingsObject().getYoutube() == null || mViewModel.getDataManager().getSettingsObject().getYoutube().isEmpty())
                binding.youtubeIcon.setVisibility(View.GONE);

        } else {

            binding.facebookIcon.setVisibility(View.GONE);
            binding.instaIcon.setVisibility(View.GONE);
            binding.snapchatIcon.setVisibility(View.GONE);
            binding.twitterIcon.setVisibility(View.GONE);
            binding.youtubeIcon.setVisibility(View.GONE);

        }

    }

    private void subscribeViewModel() {

        mViewModel.getMessageTypesLiveData().observe(this, response -> {
            hideLoading();

            List<String> msgTypes = new ArrayList<>();

            for (int i = 0; i < response.getData().size(); i++) {
                msgTypes.add(response.getData().get(i).getTitle());
            }

            ArrayAdapter<String> adapter =
                    new ArrayAdapter<>(
                            this,
                            R.layout.spinner_item,
                            msgTypes);

            binding.spMessageType.setAdapter(adapter);

            binding.spMessageType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                    selectedMsgTypeId = String.valueOf(response.getData().get(position).getId());

                }

                @Override
                public void onNothingSelected(AdapterView<?> parentView) {
                    // your code here
                }

            });

        });

        mViewModel.getContactUsMessageLiveData().observe(this, response -> {
            hideLoading();
            showSuccessMessage(response.getMessage());
            finish();
        });


    }

    public void setupOnViewClicked() {

        binding.btnSend.setOnClickListener(v -> {

            if (binding.etName.getText().toString().trim().isEmpty()) {
                binding.etName.setError(getText(R.string.empty_full_name));
                binding.etName.requestFocus();
                return;
            }
            binding.etName.setError(null);

            if (!binding.etEmail.getText().toString().trim().isEmpty() && !CommonUtils.isEmailValid(binding.etEmail.getText().toString().trim())) {
                binding.etEmail.setError(getText(R.string.invalid_email));
                binding.etEmail.requestFocus();
                return;
            }
            binding.etEmail.setError(null);

            if (binding.etPhone.getText().toString().trim().isEmpty()) {
                binding.etPhone.setError(getText(R.string.empty_mobile));
                binding.etPhone.requestFocus();
                return;
            }

            if (!binding.etPhone.getText().toString().trim().startsWith("05")) {
                binding.etPhone.setError(getText(R.string.invalid_mobile_prefix));
                binding.etPhone.requestFocus();
                return;
            }
            binding.etPhone.setError(null);

            if (binding.etMessageTitle.getText().toString().trim().isEmpty()) {
                binding.etMessageTitle.setError(getText(R.string.empty_msg_title));
                binding.etMessageTitle.requestFocus();
                return;
            }
            binding.etMessageTitle.setError(null);

            if (binding.etMessageText.getText().toString().trim().isEmpty()) {
                binding.etMessageText.setError(getText(R.string.empty_msg_text));
                binding.etMessageText.requestFocus();
                return;
            }
            binding.etMessageText.setError(null);

            HashMap<String, String> map = new HashMap<>();

            map.put("name", binding.etName.getText().toString().trim());
            map.put("mobile", binding.etPhone.getText().toString().trim());
            map.put("title", binding.etMessageTitle.getText().toString().trim());
            map.put("contact_us_type_id", selectedMsgTypeId);
            map.put("description", binding.etMessageText.getText().toString().trim());

            if (!binding.etEmail.getText().toString().trim().isEmpty())
                map.put("email", binding.etEmail.getText().toString().trim());
            if (mViewModel.getDataManager().isUserLogged())
                map.put("user_id", String.valueOf(mViewModel.getDataManager().getCurrentUserId()));

            showLoading();
            mViewModel.sendContactUsMessage(map);

        });


        binding.facebookIcon.setOnClickListener(v -> {
            if (mViewModel.getDataManager().getSettingsObject() != null) {
                String url = mViewModel.getDataManager().getSettingsObject().getFacebook();
                if (url != null && !url.startsWith("http://") && !url.startsWith("https://"))
                    url = "https://" + url;
                startActivity(new Intent(Intent.ACTION_VIEW).setData(Uri.parse(url)));
            }
        });

        binding.snapchatIcon.setOnClickListener(v -> {
            if (mViewModel.getDataManager().getSettingsObject() != null) {
                String url = mViewModel.getDataManager().getSettingsObject().getSnapchat();
                if (url != null && !url.startsWith("http://") && !url.startsWith("https://"))
                    url = "https://" + url;
                startActivity(new Intent(Intent.ACTION_VIEW).setData(Uri.parse(url)));
            }
        });

        binding.instaIcon.setOnClickListener(v -> {
            if (mViewModel.getDataManager().getSettingsObject() != null) {
                String url = mViewModel.getDataManager().getSettingsObject().getInstagram();
                if (url != null && !url.startsWith("http://") && !url.startsWith("https://"))
                    url = "https://" + url;
                startActivity(new Intent(Intent.ACTION_VIEW).setData(Uri.parse(url)));
            }
        });

        binding.twitterIcon.setOnClickListener(v -> {
            if (mViewModel.getDataManager().getSettingsObject() != null) {
                String url = mViewModel.getDataManager().getSettingsObject().getTweeter();
                if (url != null && !url.startsWith("http://") && !url.startsWith("https://"))
                    url = "https://" + url;
                startActivity(new Intent(Intent.ACTION_VIEW).setData(Uri.parse(url)));
            }
        });


        binding.youtubeIcon.setOnClickListener(v -> {
            if (mViewModel.getDataManager().getSettingsObject() != null) {
                String url = mViewModel.getDataManager().getSettingsObject().getYoutube();
                if (url != null && !url.startsWith("http://") && !url.startsWith("https://"))
                    url = "https://" + url;
                startActivity(new Intent(Intent.ACTION_VIEW).setData(Uri.parse(url)));
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

}
