
package com.gp.health.ui.media;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;

import com.gp.health.R;
import com.gp.health.data.models.MediaModel;
import com.gp.health.databinding.ActivityMediaBinding;
import com.gp.health.di.component.ActivityComponent;
import com.gp.health.ui.base.BaseActivity;
import com.gp.health.utils.ErrorHandlingUtils;

import java.util.ArrayList;

import javax.inject.Inject;

public class MediaActivity extends BaseActivity<MediaViewModel> implements MediaNavigator {

    ActivityMediaBinding binding;

    @Inject
    LinearLayoutManager linearLayoutManager;
    @Inject
    MediaAdapter mediaAdapter;

    public static Intent newIntent(Context context) {
        return new Intent(context, MediaActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMediaBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mViewModel.setNavigator(this);

        binding.toolbar.toolbarTitle.setText(R.string.images_and_videos);
        binding.toolbar.backButton.setOnClickListener(v -> finish());

        binding.rvMedia.setLayoutManager(linearLayoutManager);
        binding.rvMedia.setAdapter(mediaAdapter);

        RecyclerView.SmoothScroller smoothScroller = new LinearSmoothScroller(this) {
            @Override
            protected int getVerticalSnapPreference() {
                return LinearSmoothScroller.SNAP_TO_ANY;
            }
        };

        mediaAdapter.addItems((ArrayList<MediaModel>) getIntent().getSerializableExtra("media"));

        smoothScroller.setTargetPosition(getIntent().getIntExtra("position", 0));
        linearLayoutManager.startSmoothScroll(smoothScroller);


        subscribeViewModel();

    }

    private void subscribeViewModel() {

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
