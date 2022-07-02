package com.gp.shifa.ui.home.adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.gp.shifa.BuildConfig;
import com.gp.shifa.R;
import com.gp.shifa.data.models.CategoriesModel;
import com.gp.shifa.databinding.ItemEmptyViewBinding;
import com.gp.shifa.ui.base.BaseViewHolder;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CategoriesHomeAdapter extends RecyclerView.Adapter<BaseViewHolder> {

    public static final int VIEW_TYPE_EMPTY = 0;
    public static final int VIEW_TYPE_CATEGORY = 1;

    private Callback mCallback;
    public List<CategoriesModel> mCategoriesList;

    public CategoriesHomeAdapter(List<CategoriesModel> list) {
        mCategoriesList = list;
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
            case VIEW_TYPE_CATEGORY:
                return new CategoryViewHolder(
                        LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category_home, parent, false));
            case VIEW_TYPE_EMPTY:
            default:
                return new EmptyViewHolder(
                        ItemEmptyViewBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false).getRoot());
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (mCategoriesList != null && mCategoriesList.size() > 0) {
            return VIEW_TYPE_CATEGORY;
        } else {
            return VIEW_TYPE_EMPTY;
        }
    }

    @Override
    public int getItemCount() {
        if (mCategoriesList != null && mCategoriesList.size() > 0) {
            return Math.min(mCategoriesList.size(), 8);
        } else {
            return 0;
        }
    }

    public void addItems(List<CategoriesModel> list) {
        mCategoriesList.clear();
        mCategoriesList.addAll(list);
        notifyDataSetChanged();
    }

    public void addItem(CategoriesModel category) {
        mCategoriesList.add(category);
        notifyDataSetChanged();
    }

    public void clearItems() {
        mCategoriesList.clear();
        notifyDataSetChanged();
    }

    public interface Callback {
        void onCategorySelected(CategoriesModel category);
    }

    @SuppressLint("NonConstantResourceId")
    public class CategoryViewHolder extends BaseViewHolder {

        @BindView(R.id.catImage)
        ImageView catImage;
        @BindView(R.id.tvCatName)
        AppCompatTextView tvCatName;

        public CategoryViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }

        protected void clear() {

        }

        public void onBind(int position) {

            CategoriesModel model = mCategoriesList.get(position);

            tvCatName.setText(model.getName());

            Glide.with(itemView)
                    .load(BuildConfig.BASE_URL +
                            "assets/web/img/category/" + model.getImg())
                    .into(catImage);

            itemView.setOnClickListener(v -> mCallback.onCategorySelected(model));

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
