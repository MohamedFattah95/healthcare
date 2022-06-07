package com.gp.shifa.ui.categories;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gp.shifa.R;
import com.gp.shifa.data.models.NotificationsModel;
import com.gp.shifa.databinding.ItemEmptyViewBinding;
import com.gp.shifa.ui.base.BaseViewHolder;

import java.util.List;

import butterknife.ButterKnife;

public class CategoriesAdapter extends RecyclerView.Adapter<BaseViewHolder> {

    public static final int VIEW_TYPE_EMPTY = 0;
    public static final int VIEW_TYPE_CATS = 1;

    private Callback mCallback;
    private List<NotificationsModel> mCategoriesList;

    public CategoriesAdapter(List<NotificationsModel> list) {
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
            case VIEW_TYPE_CATS:
                return new CategoriesViewHolder(
                        LayoutInflater.from(parent.getContext()).inflate(R.layout.item_care_category, parent, false));
            case VIEW_TYPE_EMPTY:
            default:
                return new EmptyViewHolder(
                        ItemEmptyViewBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false).getRoot());
        }
    }

    @Override
    public int getItemViewType(int position) {
//        if (mCategoriesList != null && mCategoriesList.size() > 0) {
            return VIEW_TYPE_CATS;
//        } else {
//            return VIEW_TYPE_EMPTY;
//        }
    }

    @Override
    public int getItemCount() {
//        if (mCategoriesList != null && mCategoriesList.size() > 0) {
//            return mCategoriesList.size();
//        } else {
            return 19;
//        }
    }

    public void addItems(List<NotificationsModel> list) {
        mCategoriesList.addAll(list);
        notifyDataSetChanged();
    }

    public void clearItems() {
        mCategoriesList.clear();
        notifyDataSetChanged();
    }

    public interface Callback {
    }

    @SuppressLint("NonConstantResourceId")
    public class CategoriesViewHolder extends BaseViewHolder {

        public CategoriesViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        protected void clear() {

        }

        @SuppressLint("SetTextI18n")
        public void onBind(int position) {

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
