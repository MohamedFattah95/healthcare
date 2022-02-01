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

public class DoctorsHomeAdapter extends RecyclerView.Adapter<BaseViewHolder> {

    public static final int VIEW_TYPE_EMPTY = 0;
    public static final int VIEW_TYPE_MAP_AD = 1;

    private Callback mCallback;
    public List<AdAndOrderModel> mMapAdsList;

    public DoctorsHomeAdapter(List<AdAndOrderModel> list) {
        mMapAdsList = list;
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
            case VIEW_TYPE_MAP_AD:
                return new MapAdViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_doctor_home, parent, false));
            case VIEW_TYPE_EMPTY:
            default:
                return new EmptyViewHolder(
                        ItemEmptyViewBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false).getRoot());
        }
    }

    @Override
    public int getItemViewType(int position) {
//        if (mMapAdsList != null && mMapAdsList.size() > 0) {
            return VIEW_TYPE_MAP_AD;
//        } else {
//            return VIEW_TYPE_EMPTY;
//        }
    }

    @Override
    public int getItemCount() {
//        if (mMapAdsList != null && mMapAdsList.size() > 0) {
//            return mMapAdsList.size();
//        } else {
            return 10;
//        }
    }

    public void addItems(List<AdAndOrderModel> list) {
        mMapAdsList.clear();
        mMapAdsList.addAll(list);
        notifyDataSetChanged();
    }

    public void clearItems() {
        mMapAdsList.clear();
        notifyDataSetChanged();
    }

    public interface Callback {

        void getPropertyDetails(int id);

        void favoriteOrUnFavorite(int itemId, int position);
    }

    @SuppressLint("NonConstantResourceId")
    public class MapAdViewHolder extends BaseViewHolder {

//        @BindView(R.id.adCover)
//        ImageView adCover;
//        @BindView(R.id.favoriteImage)
//        ImageView favoriteImage;
//        @BindView(R.id.tvAdDate)
//        TextView tvAdDate;
//        @BindView(R.id.tvAdLocation)
//        TextView tvAdLocation;
//        @BindView(R.id.tvAdDesc)
//        TextView tvAdDesc;
//        @BindView(R.id.tvAdPrice)
//        TextView tvAdPrice;
//        @BindView(R.id.tvAdTitle)
//        TextView tvAdTitle;

        public MapAdViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        protected void clear() {

        }

        @SuppressLint("SetTextI18n")
        public void onBind(int position) {

//            AdAndOrderModel model = mMapAdsList.get(position);
//
//            if (model.getImages() != null && !model.getImages().isEmpty()) {
//                Glide.with(itemView).load(model.getImages().get(0).getFileName())
//                        .error(R.drawable.img_back)
//                        .placeholder(R.drawable.img_back)
//                        .into(adCover);
//            } else {
//                Glide.with(itemView).load(R.drawable.img_back).into(adCover);
//            }
//
//
//            if (model.getUpdatedAt() != null)
//                tvAdDate.setText(DateUtility.getDateOnlyTFormat(model.getUpdatedAt()));
//            else
//                tvAdDate.setText(itemView.getResources().getString(R.string.n_a));
//
//            if (model.getCountry() != null)
//                tvAdLocation.setText(model.getCountry());
//            else
//                tvAdLocation.setText(itemView.getResources().getString(R.string.n_a));
//
//            if (model.getDescription() != null)
//                tvAdDesc.setText(model.getDescription());
//            else
//                tvAdDesc.setText(itemView.getResources().getString(R.string.n_a));
//
//            if (model.getType() == 1) {
//
//                if (model.getTitle() != null)
//                    tvAdTitle.setText(model.getTitle());
//                else
//                    tvAdTitle.setText(itemView.getResources().getString(R.string.n_a));
//
//                if (model.getPrice() != null && !model.getPrice().isEmpty())
//                    tvAdPrice.setText(model.getPrice() + " " + itemView.getResources().getString(R.string.sr));
//                else
//                    tvAdPrice.setText(itemView.getResources().getString(R.string.n_a));
//
//            } else {
//
//                if (model.getCategories() != null && !model.getCategories().isEmpty())
//                    tvAdTitle.setText("(" + itemView.getResources().getString(R.string.order) + ") " + model.getCategories().get(0).getTitle());
//                else
//                    tvAdTitle.setText("(" + itemView.getResources().getString(R.string.order) + ")");
//
//                if (model.getPriceMin() != null && model.getPriceMax() != null && !model.getPriceMin().isEmpty() && !model.getPriceMax().isEmpty())
//                    tvAdPrice.setText(CommonUtils.setPriceCurrency(itemView.getContext(), Double.parseDouble(model.getPriceMin())) + " : " +
//                            CommonUtils.setPriceCurrency(itemView.getContext(), Double.parseDouble(model.getPriceMax())));
//                else
//                    tvAdPrice.setText(itemView.getResources().getString(R.string.n_a));
//
//            }
//
//            if (model.getIsLikedByMe() != null) {
//                favoriteImage.setImageResource(R.drawable.ic_favorite);
//            } else {
//                favoriteImage.setImageResource(R.drawable.ic_un_favorite);
//            }
//
//            itemView.setOnClickListener(v -> mCallback.getPropertyDetails(model.getId()));
//
//            favoriteImage.setOnClickListener(v -> mCallback.favoriteOrUnFavorite(model.getId(), position));

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
