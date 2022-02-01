package com.gp.health.ui.home.adapters;

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
import com.gp.health.utils.CommonUtils;
import com.gp.health.utils.DateUtility;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ListAdsAdapter extends RecyclerView.Adapter<BaseViewHolder> {

    public static final int VIEW_TYPE_EMPTY = 0;
    public static final int VIEW_TYPE_LIST_AD = 1;

    private Callback mCallback;
    public List<AdAndOrderModel> mListAdsList;

    public ListAdsAdapter(List<AdAndOrderModel> list) {
        mListAdsList = list;
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
            case VIEW_TYPE_LIST_AD:
                return new ListAdViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_ad, parent, false));
            case VIEW_TYPE_EMPTY:
            default:
                return new EmptyViewHolder(
                        ItemEmptyViewBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false).getRoot());
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (mListAdsList != null && mListAdsList.size() > 0) {
            return VIEW_TYPE_LIST_AD;
        } else {
            return VIEW_TYPE_EMPTY;
        }
    }

    @Override
    public int getItemCount() {
        if (mListAdsList != null && mListAdsList.size() > 0) {
            return mListAdsList.size();
        } else {
            return 1;
        }
    }

    public void addItems(List<AdAndOrderModel> list) {
        mListAdsList.addAll(list);
        notifyDataSetChanged();
    }

    public void clearItems() {
        mListAdsList.clear();
        notifyDataSetChanged();
    }

    public interface Callback {

        void getPropertyDetails(int id);

        void favoriteOrUnFavorite(int itemId, int position);
    }

    @SuppressLint("NonConstantResourceId")
    public class ListAdViewHolder extends BaseViewHolder {

        @BindView(R.id.adCover)
        ImageView adCover;
        @BindView(R.id.tvAdDate)
        TextView tvAdDate;
        @BindView(R.id.tvAdLocation)
        TextView tvAdLocation;
        @BindView(R.id.tvAdPrice)
        TextView tvAdPrice;
        @BindView(R.id.tvAdTitle)
        TextView tvAdTitle;
        @BindView(R.id.tvAdArea)
        TextView tvAdArea;
        @BindView(R.id.tvAdCategory)
        TextView tvAdCategory;
        @BindView(R.id.favoriteImage)
        ImageView favoriteImage;
        @BindView(R.id.medalImage)
        ImageView medalImage;

        public ListAdViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }

        protected void clear() {

        }

        @SuppressLint("SetTextI18n")
        public void onBind(int position) {

            AdAndOrderModel model = mListAdsList.get(position);

            if (model.getImages() != null && !model.getImages().isEmpty()) {
                Glide.with(itemView).load(model.getImages().get(0).getFileName())
                        .error(R.drawable.img_back)
                        .placeholder(R.drawable.img_back)
                        .into(adCover);
            } else {
                Glide.with(itemView).load(R.drawable.img_back).into(adCover);
            }



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


            if (model.getType() == 1) {

                if (model.getTitle() != null)
                    tvAdTitle.setText(model.getTitle());
                else
                    tvAdTitle.setText(itemView.getResources().getString(R.string.n_a));

                if (model.getPrice() != null && !model.getPrice().isEmpty())
                    tvAdPrice.setText(model.getPrice() + " " + itemView.getResources().getString(R.string.sr));
                else
                    tvAdPrice.setText(itemView.getResources().getString(R.string.n_a));

            } else {

                if (model.getCategories() != null && !model.getCategories().isEmpty())
                    tvAdTitle.setText("(" + itemView.getResources().getString(R.string.order) + ") " + model.getCategories().get(0).getTitle());
                else
                    tvAdTitle.setText("(" + itemView.getResources().getString(R.string.order) + ")");

                if (model.getPriceMin() != null && model.getPriceMax() != null && !model.getPriceMin().isEmpty() && !model.getPriceMax().isEmpty())
                    tvAdPrice.setText(CommonUtils.setPriceCurrency(itemView.getContext(), Double.parseDouble(model.getPriceMin())) + " : " +
                            CommonUtils.setPriceCurrency(itemView.getContext(), Double.parseDouble(model.getPriceMax())));
                else
                    tvAdPrice.setText(itemView.getResources().getString(R.string.n_a));

            }

            if (model.getSpace() != null && !model.getSpace().isEmpty())
                tvAdArea.setText(model.getSpace() + "\n" + itemView.getResources().getString(R.string.m2));
            else
                tvAdArea.setText(itemView.getResources().getString(R.string.n_a));

            if (model.getIsLikedByMe() != null) {
                favoriteImage.setImageResource(R.drawable.ic_favorite_round);
            } else {
                favoriteImage.setImageResource(R.drawable.ic_un_favorite_round);
            }

            if (model.getFeatured() == 1) {
                medalImage.setVisibility(View.VISIBLE);
            } else {
                medalImage.setVisibility(View.GONE);
            }

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
