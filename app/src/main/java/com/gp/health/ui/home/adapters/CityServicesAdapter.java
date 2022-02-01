package com.gp.health.ui.home.adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gp.health.R;
import com.gp.health.data.models.CityServiceModel;
import com.gp.health.databinding.ItemEmptyViewBinding;
import com.gp.health.ui.base.BaseViewHolder;
import com.gp.health.utils.LanguageHelper;
import com.tobiasschuerg.progresscircle.ProgressCircleView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CityServicesAdapter extends RecyclerView.Adapter<BaseViewHolder> {

    public static final int VIEW_TYPE_EMPTY = 0;
    public static final int VIEW_TYPE_CITY_SERVICE = 1;
    public List<CityServiceModel> mCityServicesList;
    private Callback mCallback;

    public CityServicesAdapter(List<CityServiceModel> list) {
        mCityServicesList = list;
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
            case VIEW_TYPE_CITY_SERVICE:
                return new CityServiceViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_city_service, parent, false));
            case VIEW_TYPE_EMPTY:
            default:
                return new EmptyViewHolder(
                        ItemEmptyViewBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false).getRoot());
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (mCityServicesList != null && mCityServicesList.size() > 0) {
            return VIEW_TYPE_CITY_SERVICE;
        } else {
            return VIEW_TYPE_EMPTY;
        }
    }

    @Override
    public int getItemCount() {
        if (mCityServicesList != null && mCityServicesList.size() > 0) {
            return mCityServicesList.size();
        } else {
            return 0;
        }
    }

    public void addItems(List<CityServiceModel> list) {
        mCityServicesList.clear();
        mCityServicesList.addAll(list);
        notifyDataSetChanged();
    }

    public void clearItems() {
        mCityServicesList.clear();
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
    public class CityServiceViewHolder extends BaseViewHolder {

        @BindView(R.id.tvTitle)
        TextView tvTitle;
        @BindView(R.id.pbPercent)
        ProgressCircleView pbPercent;

        public CityServiceViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        protected void clear() {

        }

        @SuppressLint("SetTextI18n")
        public void onBind(int position) {

            CityServiceModel model = mCityServicesList.get(position);

            if (LanguageHelper.getLanguage(itemView.getContext()).equals("ar"))
                tvTitle.setText(model.getCountryService().getTitle().getAr());
            else
                tvTitle.setText(model.getCountryService().getTitle().getEn());


            pbPercent.setProgress(model.getRate());

        }
    }
}
