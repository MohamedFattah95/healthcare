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

public class AdVideosAdapter extends RecyclerView.Adapter<BaseViewHolder> {

    public static final int VIEW_TYPE_EMPTY = 0;
    public static final int VIEW_TYPE_AD_VIDEO = 1;
    private Callback mCallback;
    public ArrayList<MediaModel> mAdVideosList;

    public AdVideosAdapter(ArrayList<MediaModel> list) {
        mAdVideosList = list;
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
            case VIEW_TYPE_AD_VIDEO:
                return new AdVideoViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_video, parent, false));
            case VIEW_TYPE_EMPTY:
            default:
                return new EmptyViewHolder(
                        LayoutInflater.from(parent.getContext()).inflate(R.layout.item_empty_view, parent, false));
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (mAdVideosList != null && mAdVideosList.size() > 0) {
            return VIEW_TYPE_AD_VIDEO;
        } else {
            return VIEW_TYPE_EMPTY;
        }
    }

    @Override
    public int getItemCount() {
        if (mAdVideosList != null && mAdVideosList.size() > 0) {
            return mAdVideosList.size();
        } else {
            return 0;
        }
    }

    public void addItems(List<MediaModel> list) {
        mAdVideosList.clear();
        mAdVideosList.addAll(list);
        notifyDataSetChanged();
    }

    public void removeItem(int position) {
        mAdVideosList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, mAdVideosList.size());
    }

    public void clearItems() {
        mAdVideosList.clear();
        notifyDataSetChanged();
    }

    public interface Callback {

        void removeVideo(int videoId, int position);
    }

    @SuppressLint("NonConstantResourceId")
    public class AdVideoViewHolder extends BaseViewHolder {

        @BindView(R.id.ivThumb)
        ImageView ivThumb;
        @BindView(R.id.remove)
        ImageView remove;

        public AdVideoViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }

        protected void clear() {

        }

        @SuppressLint("SetTextI18n")
        public void onBind(int position) {

            MediaModel model = mAdVideosList.get(position);

            if (model.getFileName() != null && model.getFileName().contains("storage/emulated"))
                Glide.with(itemView)
                        .load(model.getFileName())
                        .error(R.drawable.img_back)
                        .placeholder(R.drawable.img_back)
                        .into(ivThumb);
            else
                Glide.with(itemView)
                        .load(model.getFileName())
                        .error(R.drawable.img_back)
                        .placeholder(R.drawable.img_back)
                        .into(ivThumb);

            remove.setOnClickListener(v -> {

                if (model.getId() != 0)
                    mCallback.removeVideo(model.getId(), position);
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
