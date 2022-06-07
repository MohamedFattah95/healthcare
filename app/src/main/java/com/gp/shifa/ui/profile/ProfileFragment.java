package com.gp.shifa.ui.profile;

import static android.content.Context.CLIPBOARD_SERVICE;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.github.sumimakito.awesomeqr.AwesomeQrRenderer;
import com.github.sumimakito.awesomeqr.RenderResult;
import com.github.sumimakito.awesomeqr.option.RenderOption;
import com.github.sumimakito.awesomeqr.option.color.Color;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.dynamiclinks.DynamicLink;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.gp.shifa.BuildConfig;
import com.gp.shifa.R;
import com.gp.shifa.databinding.FragmentProfileBinding;
import com.gp.shifa.di.component.FragmentComponent;
import com.gp.shifa.ui.base.BaseFragment;
import com.gp.shifa.ui.main.MainActivity;
import com.gp.shifa.ui.user.login.LoginActivity;
import com.gp.shifa.utils.ErrorHandlingUtils;
import com.gp.shifa.utils.PermissionsUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.inject.Inject;

public class ProfileFragment extends BaseFragment<ProfileViewModel> implements ProfileNavigator {

    public static final String TAG = "ProfileFragment";

    public static final int REQUEST_STORAGE_CODE = 111;

    @Inject
    ProfilePagerAdapter profilePagerAdapter;

    FragmentProfileBinding binding;

    public static ProfileFragment newInstance(int instance) {
        Bundle args = new Bundle();
        args.putInt(BaseFragment.ARGS_INSTANCE, instance);
        ProfileFragment fragment = new ProfileFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public void refreshData() {
        if (mViewModel != null && mViewModel.getDataManager().isUserLogged()) {
            mViewModel.getProfile();
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel.setNavigator(this);
        EventBus.getDefault().register(this);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        setUp();
        return binding.getRoot();
    }

    private void setUp() {
        setUpOnViewClicked();
        subscribeViewModel();

        if (mViewModel.getDataManager().isUserLogged()) {

            setUpProfilePager();
            mViewModel.getProfile();

        } else {
            binding.tvUsername.setText(getText(R.string.login_drawer));
            binding.llQR.setVisibility(View.GONE);
            binding.tvQuote.setVisibility(View.GONE);
        }

    }


    @SuppressLint("SetTextI18n")
    private void subscribeViewModel() {

        mViewModel.getProfileLiveData().observe(requireActivity(), response -> {

            binding.tvUsername.setText(response.getData().getUser().getName());
            Glide.with(this)
                    .load(response.getData().getUser().getImgSrc()
                            + "/" + response.getData().getUser().getImg())
                    .placeholder(R.drawable.ic_user_holder)
                    .error(R.drawable.ic_user_holder)
                    .into(binding.userImageView);


            binding.tvQuoteDetails.setText(response.getData().getUser().getGander());

            RenderOption renderOption = new RenderOption();
            renderOption.setContent(String.valueOf(response.getData().getUser().getId())); // content to encode
            renderOption.setSize(720); // size of the final QR code image
            renderOption.setBorderWidth(8); // width of the empty space around the QR code
            renderOption.setPatternScale(1f); // (optional) specify a scale for patterns
            renderOption.setRoundedPatterns(false); // (optional) if true, blocks will be drawn as dots instead
            renderOption.setClearBorder(true); // if set to true, the background will NOT be drawn on the border area
            renderOption.setColor(new Color(true, 0xFFFFFFFF, 0xFFFFFFFF, 0xFF000000));

            try {
                RenderResult result = AwesomeQrRenderer.render(renderOption);
                if (result.getBitmap() != null) {
                    // play with the bitmap
                    binding.ivQRCode.setImageBitmap(result.getBitmap());

                    binding.ivDownload.setOnClickListener(v -> storeImage(result.getBitmap()));

                } else {
                    binding.ivDownload.setVisibility(View.GONE);
                }
            } catch (Exception e) {
                e.printStackTrace();
                // Oops, something gone wrong.
            }


        });

    }

    private void setUpProfilePager() {
        String[] mTitles = new String[]{getString(R.string.my_ads), getString(R.string.my_orders), getString(R.string.my_ratings), getString(R.string.follows), getString(R.string.blocked)};

        binding.profileTabs.setTabData(mTitles);
        binding.vpProfile.setAdapter(profilePagerAdapter);
        binding.profileTabs.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                binding.vpProfile.setCurrentItem(position);
            }

            @Override
            public void onTabReselect(int position) {
            }
        });

        binding.vpProfile.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);

            }

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                binding.profileTabs.setCurrentTab(position);

            }

            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
            }
        });
        binding.vpProfile.setCurrentItem(0);
    }


    @Override
    public void performDependencyInjection(FragmentComponent buildComponent) {
        buildComponent.inject(this);
    }

    public void setUpOnViewClicked() {

        binding.tvUsername.setOnClickListener(v -> {
            if (!mViewModel.getDataManager().isUserLogged())
                startActivity(((MainActivity) requireActivity()).getIntentWithClearHistory(LoginActivity.class));
        });

        binding.ivCopy.setOnClickListener(v -> copyProfileLink());


    }

    @SuppressLint("LogNotTimber")
    public void copyProfileLink() {

        FirebaseDynamicLinks.getInstance().createDynamicLink()
                .setLink(Uri.parse("https://www.aqar.7jazi.com/dashboard/api/v1/shared?member_id=" + mViewModel.getDataManager().getUserObject().getUser().getId()))
                .setDomainUriPrefix("https://alaqaria.page.link?member_id=" + mViewModel.getDataManager().getUserObject().getUser().getId())
                .setAndroidParameters(new DynamicLink.AndroidParameters.Builder().build())
                .setIosParameters(new DynamicLink.IosParameters.Builder("com.qrc.aqar").build())
                .buildShortDynamicLink()
                .addOnCompleteListener(requireActivity(), task -> {
                    if (task.isSuccessful()) {
                        // Short link created
                        Uri shortLink = task.getResult().getShortLink();
                        if (shortLink != null) {
                            String itemLinkShare = shortLink.toString();
                            Log.e("TAG", "setupDeepLinkShort: " + itemLinkShare);

                            ClipboardManager clipboard = (ClipboardManager) requireActivity().getSystemService(CLIPBOARD_SERVICE);
                            ClipData clip = ClipData.newPlainText("ProfileLink", shortLink.toString());
                            clipboard.setPrimaryClip(clip);
                            showSuccessMessage(getString(R.string.copied));

                        }
                    } else {
                        Log.e("TAG", "setupDeepLinkShort: " + task.getException());
                        showErrorMessage(getString(R.string.some_error));
                    }
                });


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
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @SuppressLint("SetTextI18n")
    @Subscribe
    public void updateProfile(String keyword) {
        if (keyword.equalsIgnoreCase("update_profile")) {
            if (mViewModel.getDataManager().isUserLogged()) {
                binding.tvUsername.setText(mViewModel.getDataManager().getUserObject().getUser().getName());
                Glide.with(this)
                        .load(mViewModel.getDataManager().getUserObject().getUser().getImgSrc()
                                + "/" + mViewModel.getDataManager().getUserObject().getUser().getImg())
                        .placeholder(R.drawable.ic_user_holder)
                        .error(R.drawable.ic_user_holder)
                        .into(binding.userImageView);

                binding.tvQuoteDetails.setText(mViewModel.getDataManager().getUserObject().getUser().getGander());

            } else binding.tvUsername.setText(getText(R.string.login_drawer));
        }
    }

    private void storeImage(Bitmap image) {
        File pictureFile = getOutputMediaFile();
        if (pictureFile == null) {
            PermissionsUtils.requestStoragePermission(requireActivity(), REQUEST_STORAGE_CODE);
            return;
        }
        try {
            FileOutputStream fos = new FileOutputStream(pictureFile);
            image.compress(Bitmap.CompressFormat.PNG, 90, fos);
            fos.close();

            showSuccessMessage(getString(R.string.saved) + " " + getString(R.string.in_folder) + " Al Aqaria");
        } catch (Exception e) {
            PermissionsUtils.requestStoragePermission(requireActivity(), REQUEST_STORAGE_CODE);
            e.printStackTrace();
        }
    }

    private File getOutputMediaFile() {
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.
        File mediaStorageDir = new File(Environment.getExternalStorageDirectory() + "/Al Aqaria");

        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }
        // Create a media file name
        return new File(mediaStorageDir.getPath() + File.separator +
                "QR_" + new SimpleDateFormat("ddMMyyyy_HHmmss", Locale.US).format(new Date()) + ".jpg");
    }

    public void MyOnRequestPermissionResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == REQUEST_STORAGE_CODE && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            try {
                binding.ivDownload.performClick();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            try {
                if (ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(),
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    PermissionsUtils.requestStoragePermission(requireActivity(), REQUEST_STORAGE_CODE);
                } else {

                    // now, user has denied permission permanently!

                    Snackbar snackbar = Snackbar.make(requireActivity().findViewById(android.R.id.content), "You have previously declined this permission.\n" +
                            "You must approve this permission in \"Permissions\" in the app settings on your device.", Snackbar.LENGTH_LONG)
                            .setAction(R.string.settings_menu,
                                    view -> startActivity(new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + BuildConfig.APPLICATION_ID))));
                    snackbar.setText(getString(R.string.storage_required) + "\n" + getString(R.string.go_to_app_settings));  //Or as much as you need
                    View view = snackbar.getView();
                    TextView tv = view.findViewById(R.id.snackbar_text);
                    tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12f);
                    snackbar.show();

                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

}
