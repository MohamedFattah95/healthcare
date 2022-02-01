package com.gp.health.ui.adding_ad.ad_location;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.gp.health.R;
import com.gp.health.data.models.GoogleShopModel;
import com.gp.health.ui.base.BaseViewHolder;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GooglePlacesAdapter extends RecyclerView.Adapter<BaseViewHolder> {

    private static final String TAG = "ShopsAdapter";
    public static final int VIEW_TYPE_SHOP = 1;

    private List<GoogleShopModel> mPlacesList;

    private PlacesCallBack callback;

    public interface PlacesCallBack {
        void onPlaceClick(GoogleShopModel model);
    }


    public void setCallback(PlacesCallBack callback) {
        this.callback = callback;
    }

    public GooglePlacesAdapter(List<GoogleShopModel> mPlacesList) {
        this.mPlacesList = mPlacesList;
    }


    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        holder.onBind(position);
    }


    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        return new ShopsViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_place, parent, false));
    }


    @Override
    public int getItemViewType(int position) {
        return VIEW_TYPE_SHOP;

    }

    @Override
    public int getItemCount() {
        if (mPlacesList != null) {
            return Math.min(mPlacesList.size(), 4);
        } else {
            return 0;
        }
    }

    public void addItems(List<GoogleShopModel> list) {
        mPlacesList.clear();
        mPlacesList.addAll(list);
        notifyDataSetChanged();
    }

    public void addItem(GoogleShopModel shopModel) {
        mPlacesList.add(shopModel);
        notifyDataSetChanged();
    }

    public void clearItems() {
        mPlacesList.clear();
        notifyDataSetChanged();
    }

    public List<GoogleShopModel> getList() {
        return mPlacesList;
    }


    @SuppressLint("NonConstantResourceId")
    public class ShopsViewHolder extends BaseViewHolder {

        @BindView(R.id.tv_place_title)
        AppCompatTextView tvPlaceTitle;
        @BindView(R.id.tv_place_address)
        AppCompatTextView tvPlaceAddress;


        ShopsViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, itemView);
        }

        @SuppressLint("UseCompatLoadingForDrawables")
        @Override
        public void onBind(int position) {
            GoogleShopModel item = mPlacesList.get(position);

            try {
                tvPlaceTitle.setText(item.getName());
                tvPlaceAddress.setText(item.getFormatted_address());
            } catch (NullPointerException e) {
                e.printStackTrace();
            }

            itemView.setOnClickListener(view -> callback.onPlaceClick(item));

        }
    }
}
