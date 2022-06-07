package com.gp.shifa.ui.member_profile.member_ads;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.gp.shifa.R;
import com.gp.shifa.data.models.AdAndOrderModel;
import com.gp.shifa.databinding.ItemEmptyViewBinding;
import com.gp.shifa.ui.base.BaseViewHolder;
import com.gp.shifa.utils.DateUtility;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MemberAdsAdapter extends RecyclerView.Adapter<BaseViewHolder> {

    public static final int VIEW_TYPE_EMPTY = 0;
    public static final int VIEW_TYPE_AD = 1;

    private Callback mCallback;
    public List<AdAndOrderModel> mAdsList;

    public MemberAdsAdapter(List<AdAndOrderModel> list) {
        mAdsList = list;
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
            case VIEW_TYPE_AD:
                return new AdViewHolder(
                        LayoutInflater.from(parent.getContext()).inflate(R.layout.item_member_ad, parent, false));
            case VIEW_TYPE_EMPTY:
            default:
                return new EmptyViewHolder(
                        ItemEmptyViewBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false).getRoot());
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (mAdsList != null && mAdsList.size() > 0) {
            return VIEW_TYPE_AD;
        } else {
            return VIEW_TYPE_EMPTY;
        }
    }

    @Override
    public int getItemCount() {
        if (mAdsList != null && mAdsList.size() > 0) {
            return mAdsList.size();
        } else {
            return 1;
        }
    }

    public void addItems(List<AdAndOrderModel> list) {
        mAdsList.addAll(list);
        notifyDataSetChanged();
    }

    public void clearItems() {
        mAdsList.clear();
        notifyDataSetChanged();
    }

    public interface Callback {

        void getPropertyDetails(int id);

        void favoriteOrUnFavorite(int itemId, int position);
    }

    @SuppressLint("NonConstantResourceId")
    public class AdViewHolder extends BaseViewHolder {

        @BindView(R.id.adCover)
        ImageView adCover;
        @BindView(R.id.favoriteImage)
        ImageView favoriteImage;
        @BindView(R.id.tvDateAgo)
        TextView tvDateAgo;
        @BindView(R.id.tvAdDate)
        TextView tvAdDate;
        @BindView(R.id.tvAdLocation)
        TextView tvAdLocation;
        @BindView(R.id.tvAdDesc)
        TextView tvAdDesc;
        @BindView(R.id.tvAdPrice)
        TextView tvAdPrice;
        @BindView(R.id.tvAdTitle)
        TextView tvAdTitle;
        @BindView(R.id.tvAdCategory)
        TextView tvAdCategory;

        public AdViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }

        protected void clear() {

        }

        @SuppressLint("SetTextI18n")
        public void onBind(int position) {

            AdAndOrderModel model = mAdsList.get(position);

            if (model.getImages() != null && !model.getImages().isEmpty()) {
                Glide.with(itemView).load(model.getImages().get(0).getFileName())
                        .error(R.drawable.img_back)
                        .placeholder(R.drawable.img_back)
                        .into(adCover);
            } else {
                Glide.with(itemView).load(R.drawable.img_back).into(adCover);
            }

            if (model.getTitle() != null)
                tvAdTitle.setText(model.getTitle());
            else
                tvAdTitle.setText(itemView.getResources().getString(R.string.n_a));

            if (model.getCategories() != null && !model.getCategories().isEmpty())
                tvAdCategory.setText(model.getCategories().get(0).getTitle());
            else
                tvAdCategory.setText(itemView.getResources().getString(R.string.n_a));

            if (model.getCreatedAt() != null)
                tvDateAgo.setText(model.getCreatedAt());
            else
                tvDateAgo.setText(itemView.getResources().getString(R.string.n_a));

            if (model.getUpdatedAt() != null)
                tvAdDate.setText(DateUtility.getDateOnlyTFormat(model.getUpdatedAt()));
            else
                tvAdDate.setText(itemView.getResources().getString(R.string.n_a));

            if (model.getCountry() != null)
                tvAdLocation.setText(model.getCountry());
            else
                tvAdLocation.setText(itemView.getResources().getString(R.string.n_a));

            if (model.getDescription() != null)
                tvAdDesc.setText(model.getDescription());
            else
                tvAdDesc.setText(itemView.getResources().getString(R.string.n_a));

            if (model.getPrice() != null && !model.getPrice().isEmpty())
                tvAdPrice.setText(model.getPrice() + " " + itemView.getResources().getString(R.string.sr));
            else
                tvAdPrice.setText(itemView.getResources().getString(R.string.n_a));

            if (model.getIsLikedByMe() != null)
                favoriteImage.setImageResource(R.drawable.ic_favorite);
            else
                favoriteImage.setImageResource(R.drawable.ic_un_favorite);

            itemView.setOnClickListener(v -> mCallback.getPropertyDetails(model.getId()));

            favoriteImage.setOnClickListener(v -> mCallback.favoriteOrUnFavorite(model.getId(), position));


        }
    }

    public static class EmptyViewHolder extends BaseViewHolder {

        EmptyViewHolder(View itemView) {
            super(itemView);

        }

        @Override
        public void onBind(int position) {

        }

    }
}
