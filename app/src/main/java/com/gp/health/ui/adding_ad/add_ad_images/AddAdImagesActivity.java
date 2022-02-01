
package com.gp.health.ui.adding_ad.add_ad_images;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.otaliastudios.transcoder.Transcoder;
import com.otaliastudios.transcoder.TranscoderListener;
import com.gp.health.R;
import com.gp.health.data.models.AdAndOrderModel;
import com.gp.health.data.models.MediaModel;
import com.gp.health.databinding.ActivityAddAdImagesBinding;
import com.gp.health.di.component.ActivityComponent;
import com.gp.health.ui.adding_ad.ad_location.AdLocationActivity;
import com.gp.health.ui.base.BaseActivity;
import com.gp.health.utils.ErrorHandlingUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import io.github.memfis19.annca.Annca;
import io.github.memfis19.annca.internal.configuration.AnncaConfiguration;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

public class AddAdImagesActivity extends BaseActivity<AddAdImagesViewModel> implements AddAdImagesNavigator,
        AdImagesAdapter.Callback, AdVideosAdapter.Callback, EasyPermissions.PermissionCallbacks {

    private static final int PICK_IMG = 1;
    private static final int RECORD_VID = 2;
    private static final int MEDIA_PERMISSIONS = 5;

    String[] perms = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.CAMERA};

    private ProgressDialog progressDialog;

    private int clickMediaType; /*1 for pics, 2 for videos*/
    private AnncaConfiguration.Builder videoLimited;
    private String videoDesPath;

    @Inject
    LinearLayoutManager imagesLayoutManager;
    @Inject
    LinearLayoutManager videosLayoutManager;
    @Inject
    AdImagesAdapter adImagesAdapter;
    @Inject
    AdVideosAdapter adVideosAdapter;

    ActivityAddAdImagesBinding binding;

    int selectedCategoryId = -1;

    AdAndOrderModel adDetails = null;

    public static Intent newIntent(Context context) {
        return new Intent(context, AddAdImagesActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddAdImagesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mViewModel.setNavigator(this);
        adImagesAdapter.setCallback(this);
        adVideosAdapter.setCallback(this);
        EventBus.getDefault().register(this);

        EasyPermissions.requestPermissions(this, getString(R.string.media_rationale), MEDIA_PERMISSIONS, perms);

        binding.toolbar.toolbarTitle.setText(R.string.add_images);
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
        progressDialog = new ProgressDialog(this);

        setUpOnViewClicked();
        subscribeViewModel();
        setUpMediaAdapters();

        if (getIntent().hasExtra("adDetails")) {

            adDetails = (AdAndOrderModel) getIntent().getSerializableExtra("adDetails");
            selectedCategoryId = adDetails.getCategories().get(0).getId();

            if (adDetails.getImages() != null && !adDetails.getImages().isEmpty()) {
                adImagesAdapter.addItems(adDetails.getImages());
            }

            if (adDetails.getVideos() != null && !adDetails.getVideos().isEmpty()) {
                adVideosAdapter.addItems(adDetails.getVideos());
            }

        } else {
            selectedCategoryId = getIntent().getIntExtra("categoryId", -1);
        }

    }

    private void setUpMediaAdapters() {
        imagesLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        binding.rvImages.setLayoutManager(imagesLayoutManager);
        binding.rvImages.setAdapter(adImagesAdapter);

        videosLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        binding.rvVideos.setLayoutManager(videosLayoutManager);
        binding.rvVideos.setAdapter(adVideosAdapter);
    }

    private void subscribeViewModel() {

    }

    public void setUpOnViewClicked() {

        binding.toolbar.backButton.setOnClickListener(v -> finish());

        binding.llAdImages.setOnClickListener(v -> {
            onBtnAddPicClicked();
        });

        binding.llAddVideo.setOnClickListener(v -> {
//            new VideoPicker.Builder(this)
//                    .mode(VideoPicker.Mode.CAMERA_AND_GALLERY)
//                    .directory(VideoPicker.Directory.DEFAULT)
//                    .enableDebuggingMode(true)
//                    .build();
            onBtnAddVidClicked();
        });

        binding.btnAccept.setOnClickListener(v -> {

            if (adDetails == null) {
                startActivity(AdLocationActivity.newIntent(this)
                        .putExtra("categoryId", selectedCategoryId)
                        .putExtra("imgList", adImagesAdapter.mAdImagesList)
                        .putExtra("vidList", adVideosAdapter.mAdVideosList));
            } else {
                startActivity(AdLocationActivity.newIntent(this)
                        .putExtra("adDetails", adDetails)
                        .putExtra("categoryId", selectedCategoryId)
                        .putExtra("imgList", adImagesAdapter.mAdImagesList)
                        .putExtra("vidList", adVideosAdapter.mAdVideosList));
            }
        });

    }

    public void onBtnAddPicClicked() {
        clickMediaType = 1;
        handlePermissions();
        ImagePicker.Companion.with(this)
                .crop()
                .compress(1024)
                .maxResultSize(720, 720)
                .start(PICK_IMG);
    }

    @SuppressLint("MissingPermission")
    public void onBtnAddVidClicked() {
        clickMediaType = 2;
        handlePermissions();

        videoDesPath = getVideoFileDesPath();
        videoLimited = new AnncaConfiguration.Builder(this, RECORD_VID);
        videoLimited.setMediaAction(AnncaConfiguration.MEDIA_ACTION_VIDEO);
        videoLimited.setVideoDuration(90 * 1000);
        new Annca(videoLimited.build()).launchCamera();
    }

    @SuppressLint("LogNotTimber")
    public String getVideoFileDesPath() {
        //return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES).getAbsolutePath() + "/" + System.currentTimeMillis() + "_VID_comp.mp4";
        //return getApplicationContext().getExternalFilesDir(null).getAbsolutePath() + "/famous/" + System.currentTimeMillis() + "_VID_comp.mp4";
        File file = new File(getExternalFilesDir(null), System.currentTimeMillis() + "_VID_comp.mp4");
        Log.e("TAG", "getDesPath: " + file.getPath());
        return file.getPath();
    }

    @SuppressLint("LogNotTimber")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (resultCode == RESULT_OK) {
            if (requestCode == PICK_IMG) {

                if (data != null && data.getData().getPath() != null) {
                    List<MediaModel> mediaModel = new ArrayList<>();
                    mediaModel.add(new MediaModel(1, data.getData().getPath()));
                    adImagesAdapter.addItems(mediaModel);
                } else {
                    showErrorMessage(getString(R.string.error));
                }

            } else if (requestCode == RECORD_VID) {

                String filePath;
                if (data != null) {
                    filePath = data.getStringExtra(AnncaConfiguration.Arguments.FILE_PATH);
                } else {
                    showErrorMessage(getString(R.string.error_loading_video));
                    return;
                }
                Log.e("TAG", "Original Path: " + filePath);
                showProgressDialog(getString(R.string.preparing_video), getString(R.string.preparing_video_progress), false);
                compressVideo(filePath);

            } else if (requestCode == AppSettingsDialog.DEFAULT_SETTINGS_REQ_CODE) {
                // Do something after user returned from app settings screen
                if (EasyPermissions.hasPermissions(this, perms)) {
                    if (clickMediaType == 1) onBtnAddPicClicked();
                    else if (clickMediaType == 2) onBtnAddVidClicked();
                }
            }
        }
    }

    public void showProgressDialog(String title, String message, boolean canceledOnTouchOutside) {
        progressDialog.setTitle(title);
        progressDialog.setMessage(message);
        progressDialog.setCanceledOnTouchOutside(canceledOnTouchOutside);
        progressDialog.show();
    }

    @SuppressLint("LogNotTimber")
    private void compressVideo(String originalFile) {
        String outputPath = videoDesPath;
        Transcoder.into(outputPath)
                .addDataSource(originalFile)
                .setListener(new TranscoderListener() {
                    public void onTranscodeProgress(double progress) {
                        int currentProgress = (int) Math.floor(progress * 100);
                        Log.e("TAG", "onProgress = " + currentProgress);
                        progressDialog.setMessage(getString(R.string.preparing_file) + " " + currentProgress + "%");
                    }

                    public void onTranscodeCompleted(int successCode) {
                        progressDialog.dismiss();
                        List<MediaModel> mediaModel = new ArrayList<>();
                        mediaModel.add(new MediaModel(2, outputPath));
                        adVideosAdapter.addItems(mediaModel);
                        Log.e("TAG", "onTranscodeCompleted: " + outputPath);
                    }

                    public void onTranscodeCanceled() {
                    }

                    public void onTranscodeFailed(@NonNull Throwable exception) {
                        showErrorMessage(getString(R.string.some_error));
                        Log.e("TAG", "onTranscodeFailed: " + exception.getMessage());
                    }
                }).transcode();
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
    public void imageDeleted(int position) {
        hideLoading();
        adImagesAdapter.removeItem(position);
    }

    @Override
    public void videoDeleted(int position) {
        hideLoading();
        adVideosAdapter.removeItem(position);
    }

    @Override
    public void removeImage(int imgId, int position) {
        showLoading();
        mViewModel.removeImage(imgId, position);
    }

    @Override
    public void removeVideo(int videoId, int position) {
        showLoading();
        mViewModel.removeVideo(videoId, position);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NotNull String @NotNull [] permissions, @NotNull int @NotNull [] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        if (clickMediaType == 1) onBtnAddPicClicked();
        else if (clickMediaType == 2) onBtnAddVidClicked();
    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        //showMessage("");
    }

    private void handlePermissions() {
        if (!EasyPermissions.hasPermissions(this, perms)) {
            if (EasyPermissions.somePermissionPermanentlyDenied(this, Arrays.asList(perms))) {
                new AppSettingsDialog.Builder(this).build().show();
            } else {
                EasyPermissions.requestPermissions(this, getString(R.string.media_rationale), MEDIA_PERMISSIONS, perms);
            }
        }
    }
}
