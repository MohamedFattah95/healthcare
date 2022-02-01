package com.gp.health.ui.profile.my_ads;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.gp.health.R;
import com.gp.health.data.models.AdAndOrderModel;
import com.gp.health.databinding.ItemEmptyViewBinding;
import com.gp.health.ui.base.BaseViewHolder;
import com.gp.health.utils.DateUtility;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MyAdsAdapter extends RecyclerView.Adapter<BaseViewHolder> {

    public static final int VIEW_TYPE_EMPTY = 0;
    public static final int VIEW_TYPE_MY_AD = 1;

    private Callback mCallback;
    public List<AdAndOrderModel> mMyAdsList;

    public MyAdsAdapter(List<AdAndOrderModel> list) {
        mMyAdsList = list;
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
            case VIEW_TYPE_MY_AD:
                return new MyAdViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_my_ad, parent, false));
            case VIEW_TYPE_EMPTY:
            default:
                return new EmptyViewHolder(
                        ItemEmptyViewBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false).getRoot());
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (mMyAdsList != null && mMyAdsList.size() > 0) {
            return VIEW_TYPE_MY_AD;
        } else {
            return VIEW_TYPE_EMPTY;
        }
    }

    @Override
    public int getItemCount() {
        if (mMyAdsList != null && mMyAdsList.size() > 0) {
            return mMyAdsList.size();
        } else {
            return 1;
        }
    }

    public void addItems(List<AdAndOrderModel> list) {
        mMyAdsList.addAll(list);
        notifyDataSetChanged();
    }

    public void clearItems() {
        mMyAdsList.clear();
        notifyDataSetChanged();
    }

    public void removeItem(int position) {
        mMyAdsList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, mMyAdsList.size());
    }

    public interface Callback {

        void deleteAd(int adId, int position);

        void editAd(int adId);

        void getPropertyDetails(int id);
    }

    @SuppressLint("NonConstantResourceId")
    public class MyAdViewHolder extends BaseViewHolder {

        @BindView(R.id.adCover)
        ImageView adCover;
        @BindView(R.id.iv_delete)
        ImageView ivDelete;
        @BindView(R.id.iv_edit)
        ImageView ivEdit;
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

        public MyAdViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }

        protected void clear() {

        }

        @SuppressLint("SetTextI18n")
        public void onBind(int position) {

            AdAndOrderModel model = mMyAdsList.get(position);

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

            ivDelete.setOnClickListener(v -> mCallback.deleteAd(model.getId(), position));

            ivEdit.setOnClickListener(v -> mCallback.editAd(model.getId()));

            itemView.setOnClickListener(v -> mCallback.getPropertyDetails(model.getId()));

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
