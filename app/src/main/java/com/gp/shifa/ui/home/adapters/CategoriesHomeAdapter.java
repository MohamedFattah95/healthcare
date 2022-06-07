package com.gp.shifa.ui.home.adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gp.shifa.R;
import com.gp.shifa.data.models.CategoriesModel;
import com.gp.shifa.databinding.ItemEmptyViewBinding;
import com.gp.shifa.ui.base.BaseViewHolder;

import java.util.List;

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
//        if (mCategoriesList != null && mCategoriesList.size() > 0) {
            return VIEW_TYPE_CATEGORY;
//        } else {
//            return VIEW_TYPE_EMPTY;
//        }
    }

    @Override
    public int getItemCount() {
//        if (mCategoriesList != null && mCategoriesList.size() > 0) {
//            return mCategoriesList.size();
//        } else {
            return 8;
//        }
    }

    public void addItems(List<CategoriesModel> list) {
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

    public void selectCategory(int position) {
        for (int i = 0; i < mCategoriesList.size(); i++) {
            mCategoriesList.get(i).setSelected(false);
            notifyItemChanged(i, mCategoriesList.get(i));
        }
        mCategoriesList.get(position).setSelected(true);
        notifyItemChanged(position, mCategoriesList.get(position));

        mCallback.onCategorySelected(mCategoriesList.get(position), position);
    }

    public interface Callback {

        void onCategorySelected(CategoriesModel category, int position);
    }

    @SuppressLint("NonConstantResourceId")
    public class CategoryViewHolder extends BaseViewHolder {

//        @BindView(R.id.tv_category)
//        AppCompatTextView tvCategory;

        public CategoryViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }

        protected void clear() {

        }

        public void onBind(int position) {

//            CategoriesModel model = mCategoriesList.get(position);
//
//            tvCategory.setText(model.getTitle());
//
//            if (model.isSelected()) {
//                tvCategory.setBackgroundTintList(ColorStateList.valueOf(itemView.getResources().getColor(R.color.colorPrimary)));
//                tvCategory.setTextColor(itemView.getResources().getColor(R.color.white));
//            } else {
//                tvCategory.setBackgroundTintList(ColorStateList.valueOf(itemView.getResources().getColor(R.color.white)));
//                tvCategory.setTextColor(itemView.getResources().getColor(R.color.colorPrimary));
//            }
//
//            itemView.setOnClickListener(v -> {
//
//                for (int i = 0; i < mCategoriesList.size(); i++) {
//                    mCategoriesList.get(i).setSelected(false);
//                    notifyItemChanged(i, mCategoriesList.get(i));
//                }
//                mCategoriesList.get(position).setSelected(true);
//                notifyItemChanged(position, mCategoriesList.get(position));
//
//                mCallback.onCategorySelected(model, position);
//            });
//
//            setIsRecyclable(false);

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
