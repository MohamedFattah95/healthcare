package com.gp.shifa.ui.favorites;

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
import com.gp.shifa.ui.base.BaseViewHolder;
import com.gp.shifa.utils.CommonUtils;
import com.gp.shifa.utils.DateUtility;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FavoritesAdapter extends RecyclerView.Adapter<BaseViewHolder> {

    public static final int VIEW_TYPE_EMPTY = 0;
    public static final int VIEW_TYPE_FAVORITE = 1;

    private Callback mCallback;
    public List<AdAndOrderModel> mFavoritesList;

    public FavoritesAdapter(List<AdAndOrderModel> list) {
        mFavoritesList = list;
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
            case VIEW_TYPE_FAVORITE:
                return new FavoriteViewHolder(
                        LayoutInflater.from(parent.getContext()).inflate(R.layout.item_favorite, parent, false));
            case VIEW_TYPE_EMPTY:
            default:
                return new EmptyViewHolder(
                        LayoutInflater.from(parent.getContext()).inflate(R.layout.item_empty_view, parent, false));
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (mFavoritesList != null && mFavoritesList.size() > 0) {
            return VIEW_TYPE_FAVORITE;
        } else {
            return VIEW_TYPE_EMPTY;
        }
    }

    @Override
    public int getItemCount() {
        if (mFavoritesList != null && mFavoritesList.size() > 0) {
            return mFavoritesList.size();
        } else {
            return 1;
        }
    }

    public void addItems(List<AdAndOrderModel> list) {
        mFavoritesList.addAll(list);
        notifyDataSetChanged();
    }

    public void clearItems() {
        mFavoritesList.clear();
        notifyDataSetChanged();
    }

    public void removeItem(int position) {
        mFavoritesList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, mFavoritesList.size());
    }

    public interface Callback {
        void getPropertyDetails(int id);

        void unFavorite(int itemId, int position);
    }

    @SuppressLint("NonConstantResourceId")
    public class FavoriteViewHolder extends BaseViewHolder {

        @BindView(R.id.adCover)
        ImageView adCover;
        @BindView(R.id.favoriteImage)
        ImageView favoriteImage;
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

        public FavoriteViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }

        protected void clear() {

        }

        @SuppressLint("SetTextI18n")
        public void onBind(int position) {

            AdAndOrderModel model = mFavoritesList.get(position);

            if (model.getImages() != null && !model.getImages().isEmpty()) {
                Glide.with(itemView).load(model.getImages().get(0).getFileName())
                        .error(R.drawable.img_back)
                        .placeholder(R.drawable.img_back)
                        .into(adCover);
            } else {
                Glide.with(itemView).load(R.drawable.img_back).into(adCover);
            }


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

            if (model.getIsLikedByMe() != null) {
                favoriteImage.setImageResource(R.drawable.ic_favorite);
            } else {
                favoriteImage.setImageResource(R.drawable.ic_un_favorite);
            }

            itemView.setOnClickListener(v -> mCallback.getPropertyDetails(model.getId()));

            favoriteImage.setOnClickListener(v -> mCallback.unFavorite(model.getId(), position));

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
