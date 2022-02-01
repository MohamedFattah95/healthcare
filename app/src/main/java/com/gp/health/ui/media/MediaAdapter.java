package com.gp.health.ui.media;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.facebook.imagepipeline.decoder.SimpleProgressiveJpegConfig;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.gp.health.R;
import com.gp.health.data.models.MediaModel;
import com.gp.health.ui.base.BaseViewHolder;
import com.gp.health.utils.ImageUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MediaAdapter extends RecyclerView.Adapter<BaseViewHolder> {

    public static final int VIEW_TYPE_EMPTY = 0;
    public static final int VIEW_TYPE_IMG = 1;
    public static final int VIEW_TYPE_VID = 2;
    private final List<MediaModel> mMediaList;
    private Callback mCallback;

    public MediaAdapter(List<MediaModel> list) {
        mMediaList = list;
    }

    public void setCallback(Callback callback) {
        mCallback = callback;
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        holder.onBind(position);
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        switch (viewType) {
            case VIEW_TYPE_IMG:
                return new ImageViewHolder(
                        LayoutInflater.from(parent.getContext()).inflate(R.layout.item_media_img, parent, false));
            case VIEW_TYPE_VID:
                return new VideoViewHolder(
                        LayoutInflater.from(parent.getContext()).inflate(R.layout.item_media_vid, parent, false));
            case VIEW_TYPE_EMPTY:
            default:
                return new EmptyViewHolder(
                        LayoutInflater.from(parent.getContext()).inflate(R.layout.item_empty_view, parent, false));
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (mMediaList != null && mMediaList.size() > 0) {

            if (mMediaList.get(position).getFileName().contains(".mp4"))
                return VIEW_TYPE_VID;
            else
                return VIEW_TYPE_IMG;

        } else {
            return VIEW_TYPE_EMPTY;
        }
    }

    @Override
    public int getItemCount() {
        if (mMediaList != null && mMediaList.size() > 0) {
            return mMediaList.size();
        } else {
            return 0;
        }
    }

    public void addItems(ArrayList<MediaModel> list) {
        mMediaList.clear();
        mMediaList.addAll(list);
        notifyDataSetChanged();
    }

    public void addItem(ArrayList<MediaModel> list) {
        mMediaList.addAll(list);
        notifyDataSetChanged();
    }

    public void clearItems() {
        mMediaList.clear();
        notifyDataSetChanged();
    }

    public interface Callback {

    }

    public static class EmptyViewHolder extends BaseViewHolder {


        EmptyViewHolder(View itemView) {
            super(itemView);

        }

        @Override
        public void onBind(int position) {

        }


    }

    @SuppressLint("NonConstantResourceId")
    public class ImageViewHolder extends BaseViewHolder {

        @BindView(R.id.sliderImageView)
        ImageView sliderImageView;

        public ImageViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            ImagePipelineConfig config = ImagePipelineConfig.newBuilder(itemView.getContext())
                    .setProgressiveJpegConfig(new SimpleProgressiveJpegConfig())
                    .setResizeAndRotateEnabledForNetwork(true)
                    .setDownsampleEnabled(true)
                    .build();
            Fresco.initialize(itemView.getContext(), config);
        }

        protected void clear() {

        }

        public void onBind(int position) {

            MediaModel model = mMediaList.get(position);

            Glide.with(itemView)
                    .load(model.getFileName())
                    .error(R.drawable.img_back)
                    .placeholder(R.drawable.img_back)
                    .into(sliderImageView);


            itemView.setOnClickListener(v -> {

                List<String> image = new ArrayList<>();

                for (int i = 0; i < mMediaList.size(); i++) {
                    if (!mMediaList.get(i).getFileName().contains(".mp4"))
                        image.add(mMediaList.get(i).getFileName());
                }

                if (image.isEmpty())
                    return;

                ImageUtils.showFrescoImage(itemView.getContext(), image, position);

            });


        }
    }

    @SuppressLint("NonConstantResourceId")
    public class VideoViewHolder extends BaseViewHolder {

        @BindView(R.id.exoplayerView)
        PlayerView exoplayerView;
        @BindView(R.id.progressBar)
        ProgressBar progressBar;

        private SimpleExoPlayer simpleExoplayer;
        private long playbackPosition = 0;

        public VideoViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        protected void clear() {

        }

        public void onBind(int position) {

            MediaModel model = mMediaList.get(position);

            initializePlayer(model.getFileName());

            itemView.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
                @Override
                public void onViewAttachedToWindow(View view) {
                    simpleExoplayer = new SimpleExoPlayer.Builder(itemView.getContext()).build();
                }

                @Override
                public void onViewDetachedFromWindow(View view) {
                    releasePlayer();
                }
            });

        }

        private void initializePlayer(String url) {
            simpleExoplayer = new SimpleExoPlayer.Builder(itemView.getContext()).build();
            preparePlayer(url);
            exoplayerView.setPlayer(simpleExoplayer);
            simpleExoplayer.seekTo(playbackPosition);
            simpleExoplayer.setPlayWhenReady(false);
            simpleExoplayer.addListener(new Player.EventListener() {
                @Override
                public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                    if (playbackState == Player.STATE_BUFFERING)
                        progressBar.setVisibility(View.VISIBLE);
                    else if (playbackState == Player.STATE_READY || playbackState == Player.STATE_ENDED)
                        progressBar.setVisibility(View.INVISIBLE);
                }

                @Override
                public void onPlayerError(ExoPlaybackException error) {

                }
            });
        }

        private MediaSource buildMediaSource(String path) {
            String playerInfo = Util.getUserAgent(itemView.getContext(), "ExoPlayerInfo");
            DefaultDataSourceFactory dataSourceFactory = new DefaultDataSourceFactory(itemView.getContext(), playerInfo);
            return new ProgressiveMediaSource.Factory(dataSourceFactory)
                    .setExtractorsFactory(new DefaultExtractorsFactory())
                    .createMediaSource(Uri.parse(path));
        }

        private void preparePlayer(String videoUrl) {
            MediaSource mediaSource = buildMediaSource(videoUrl);
            simpleExoplayer.prepare(mediaSource);
        }

        private void releasePlayer() {
            playbackPosition = simpleExoplayer.getCurrentPosition();
            simpleExoplayer.release();
        }
    }
}
