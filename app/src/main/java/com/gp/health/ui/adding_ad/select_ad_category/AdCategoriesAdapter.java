package com.gp.health.ui.adding_ad.select_ad_category;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.gp.health.R;
import com.gp.health.data.models.CategoriesModel;
import com.gp.health.ui.base.BaseViewHolder;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AdCategoriesAdapter extends RecyclerView.Adapter<BaseViewHolder> {

    public static final int VIEW_TYPE_EMPTY = 0;
    public static final int VIEW_TYPE_AD_CATEGORY = 1;

    private Callback mCallback;
    private List<CategoriesModel> mAdCategoriesList;

    public AdCategoriesAdapter(List<CategoriesModel> list) {
        mAdCategoriesList = list;
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
            case VIEW_TYPE_AD_CATEGORY:
                return new AdCategoryViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_care_category, parent, false));
            case VIEW_TYPE_EMPTY:
            default:
                return new EmptyViewHolder(
                        LayoutInflater.from(parent.getContext()).inflate(R.layout.item_empty_view, parent, false));
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (mAdCategoriesList != null && mAdCategoriesList.size() > 0) {
            return VIEW_TYPE_AD_CATEGORY;
        } else {
            return VIEW_TYPE_EMPTY;
        }
    }

    @Override
    public int getItemCount() {
        if (mAdCategoriesList != null && mAdCategoriesList.size() > 0) {
            return mAdCategoriesList.size();
        } else {
            return 1;
        }
    }

    public void addItems(List<CategoriesModel> list) {
        mAdCategoriesList.clear();
        mAdCategoriesList.addAll(list);
        notifyDataSetChanged();
    }

    public void clearItems() {
        mAdCategoriesList.clear();
        notifyDataSetChanged();
    }

    public interface Callback {

        void selectCategory(CategoriesModel category);
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
    public class AdCategoryViewHolder extends BaseViewHolder {

        @BindView(R.id.tv_category)
        AppCompatTextView tvCategory;

        public AdCategoryViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        protected void clear() {

        }

        @SuppressLint("SetTextI18n")
        public void onBind(int position) {

            CategoriesModel model = mAdCategoriesList.get(position);

            tvCategory.setText(model.getTitle());

            itemView.setOnClickListener(v -> mCallback.selectCategory(model));

        }
    }
}
