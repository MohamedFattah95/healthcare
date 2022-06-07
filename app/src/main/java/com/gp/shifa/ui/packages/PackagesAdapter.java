package com.gp.shifa.ui.packages;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.gp.shifa.R;
import com.gp.shifa.data.models.SubscriptionPackagesModel;
import com.gp.shifa.ui.base.BaseViewHolder;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PackagesAdapter extends RecyclerView.Adapter<BaseViewHolder> {

    public static final int VIEW_TYPE_EMPTY = 0;
    public static final int VIEW_TYPE_FAQ = 1;
    private Callback mCallback;
    private List<SubscriptionPackagesModel> mPackagesList;

    public PackagesAdapter(List<SubscriptionPackagesModel> list) {
        mPackagesList = list;
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
            case VIEW_TYPE_FAQ:
                return new PackageViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_package, parent, false));
            case VIEW_TYPE_EMPTY:
            default:
                return new EmptyViewHolder(
                        LayoutInflater.from(parent.getContext()).inflate(R.layout.item_empty_view, parent, false));
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (mPackagesList != null && mPackagesList.size() > 0) {
            return VIEW_TYPE_FAQ;
        } else {
            return VIEW_TYPE_EMPTY;
        }
    }

    @Override
    public int getItemCount() {
        if (mPackagesList != null && mPackagesList.size() > 0) {
            return mPackagesList.size();
        } else {
            return 1;
        }
    }

    public void addItems(List<SubscriptionPackagesModel> list) {
        mPackagesList.clear();
        mPackagesList.addAll(list);
        notifyDataSetChanged();
    }

    public void clearItems() {
        mPackagesList.clear();
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
    public class PackageViewHolder extends BaseViewHolder {

        @BindView(R.id.tv_title)
        AppCompatTextView tvTitle;
        @BindView(R.id.tv_duration)
        AppCompatTextView tvDuration;


        public PackageViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }

        protected void clear() {

        }

        @SuppressLint("SetTextI18n")
        public void onBind(int position) {

            SubscriptionPackagesModel model = mPackagesList.get(position);

            tvTitle.setText(model.getTitle());
            tvDuration.setText(itemView.getResources().getString(R.string.duration) + ": " + model.getDuration());


        }
    }
}
