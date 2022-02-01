package com.gp.health.ui.adding_ad.add_ad_images;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.gp.health.R;
import com.gp.health.data.models.MediaModel;
import com.gp.health.ui.base.BaseViewHolder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AdImagesAdapter extends RecyclerView.Adapter<BaseViewHolder> {

    public static final int VIEW_TYPE_EMPTY = 0;
    public static final int VIEW_TYPE_AD_IMAGE = 1;
    private Callback mCallback;
    public ArrayList<MediaModel> mAdImagesList;

    public AdImagesAdapter(ArrayList<MediaModel> list) {
        mAdImagesList = list;
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
            case VIEW_TYPE_AD_IMAGE:
                return new AdImageViewHolder(
                        LayoutInflater.from(parent.getContext()).inflate(R.layout.item_image, parent, false));
            case VIEW_TYPE_EMPTY:
            default:
                return new EmptyViewHolder(
                        LayoutInflater.from(parent.getContext()).inflate(R.layout.item_empty_view, parent, false));
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (mAdImagesList != null && mAdImagesList.size() > 0) {
            return VIEW_TYPE_AD_IMAGE;
        } else {
            return VIEW_TYPE_EMPTY;
        }
    }

    @Override
    public int getItemCount() {
        if (mAdImagesList != null && mAdImagesList.size() > 0) {
            return mAdImagesList.size();
        } else {
            return 0;
        }
    }

    public void addItems(List<MediaModel> list) {
        mAdImagesList.addAll(list);
        notifyDataSetChanged();
    }

    public void removeItem(int position) {
        mAdImagesList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, mAdImagesList.size());
    }

    public void clearItems() {
        mAdImagesList.clear();
        notifyDataSetChanged();
    }

    public interface Callback {

        void removeImage(int mediaId, int position);
    }

    @SuppressLint("NonConstantResourceId")
    public class AdImageViewHolder extends BaseViewHolder {

        @BindView(R.id.chatImage)
        ImageView image;
        @BindView(R.id.remove)
        ImageView remove;

        public AdImageViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }

        protected void clear() {

        }

        @SuppressLint("SetTextI18n")
        public void onBind(int position) {

            MediaModel model = mAdImagesList.get(position);

            if (model.getFileName() != null && model.getFileName().contains("storage/emulated"))
                Glide.with(itemView)
                        .load(model.getFileName())
                        .error(R.drawable.img_back)
                        .placeholder(R.drawable.img_back)
                        .into(image);
            else
                Glide.with(itemView)
                        .load(model.getFileName())
                        .error(R.drawable.img_back)
                        .placeholder(R.drawable.img_back)
                        .into(image);

            remove.setOnClickListener(v -> {

                if (model.getId() != 0)
                    mCallback.removeImage(model.getId(), position);
                else
                    removeItem(position);

            });

        }
    }

    public class EmptyViewHolder extends BaseViewHolder {


        EmptyViewHolder(View itemView) {
            super(itemView);

        }

        @Override
        public void onBind(int position) {

        }


    }
}
