package com.gp.shifa.ui.common;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.facebook.imagepipeline.decoder.SimpleProgressiveJpegConfig;
import com.gp.shifa.R;
import com.gp.shifa.data.models.MediaModel;
import com.smarteist.autoimageslider.SliderViewAdapter;

import java.util.ArrayList;
import java.util.List;

public class SliderAdapter extends SliderViewAdapter<SliderAdapter.SliderAdapterVH> {

    private final ArrayList<MediaModel> mImgsList;

    public SliderAdapter(ArrayList<MediaModel> mImgsList) {
        this.mImgsList = mImgsList;
    }

    @Override
    public SliderAdapterVH onCreateViewHolder(ViewGroup parent) {
        @SuppressLint("InflateParams")
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_slider_img, null);
        return new SliderAdapterVH(inflate);
    }

    @Override
    public void onBindViewHolder(SliderAdapterVH viewHolder, int position) {
        viewHolder.onBind(position);
    }

    public void addItems(List<MediaModel> list) {
        mImgsList.clear();
        mImgsList.addAll(list);
        notifyDataSetChanged();
    }

    public void addItem(List<MediaModel> list) {
        mImgsList.addAll(list);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mImgsList.size();
    }

    class SliderAdapterVH extends SliderViewAdapter.ViewHolder {
        View itemView;
        ImageView imageView;

        public SliderAdapterVH(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.sliderImageView);
            this.itemView = itemView;

            ImagePipelineConfig config = ImagePipelineConfig.newBuilder(itemView.getContext())
                    .setProgressiveJpegConfig(new SimpleProgressiveJpegConfig())
                    .setResizeAndRotateEnabledForNetwork(true)
                    .setDownsampleEnabled(true)
                    .build();
            Fresco.initialize(itemView.getContext(), config);
        }

        public void onBind(int position) {
            MediaModel model = mImgsList.get(position);

            Glide.with(itemView)
                    .load(model.getFileName())
                    .error(R.drawable.img_back)
                    .placeholder(R.drawable.img_back)
                    .into(imageView);


        }
    }

}
