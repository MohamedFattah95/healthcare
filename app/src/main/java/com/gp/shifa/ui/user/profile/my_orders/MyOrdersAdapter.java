package com.gp.shifa.ui.user.profile.my_orders;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gp.shifa.R;
import com.gp.shifa.data.models.AdAndOrderModel;
import com.gp.shifa.databinding.ItemEmptyViewBinding;
import com.gp.shifa.ui.base.BaseViewHolder;
import com.gp.shifa.utils.CommonUtils;
import com.gp.shifa.utils.DateUtility;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MyOrdersAdapter extends RecyclerView.Adapter<BaseViewHolder> {

    public static final int VIEW_TYPE_EMPTY = 0;
    public static final int VIEW_TYPE_MY_ORDER = 1;

    public List<AdAndOrderModel> mMyOrdersList;
    private Callback mCallback;

    public MyOrdersAdapter(List<AdAndOrderModel> list) {
        mMyOrdersList = list;
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
            case VIEW_TYPE_MY_ORDER:
                return new MyOrderViewHolder(
                        LayoutInflater.from(parent.getContext()).inflate(R.layout.item_my_order, parent, false));
            case VIEW_TYPE_EMPTY:
            default:
                return new EmptyViewHolder(
                        ItemEmptyViewBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false).getRoot());
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (mMyOrdersList != null && mMyOrdersList.size() > 0) {
            return VIEW_TYPE_MY_ORDER;
        } else {
            return VIEW_TYPE_EMPTY;
        }
    }

    @Override
    public int getItemCount() {
        if (mMyOrdersList != null && mMyOrdersList.size() > 0) {
            return mMyOrdersList.size();
        } else {
            return 1;
        }
    }

    public void addItems(List<AdAndOrderModel> list) {
        mMyOrdersList.addAll(list);
        notifyDataSetChanged();
    }

    public void removeItem(int position) {
        mMyOrdersList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, mMyOrdersList.size());
    }

    public void clearItems() {
        mMyOrdersList.clear();
        notifyDataSetChanged();
    }

    public interface Callback {

        void deleteOrder(int orderId, int position);

        void editOrder(int orderId);

        void getPropertyDetails(int id);
    }

    @SuppressLint("NonConstantResourceId")
    public class MyOrderViewHolder extends BaseViewHolder {

        @BindView(R.id.iv_delete)
        ImageView ivDelete;
        @BindView(R.id.iv_edit)
        ImageView ivEdit;
        @BindView(R.id.tvOrderDate)
        TextView tvOrderDate;
        @BindView(R.id.tvOrderLocation)
        TextView tvOrderLocation;
        @BindView(R.id.tvOrderDesc)
        TextView tvOrderDesc;
        @BindView(R.id.tvOrderPrice)
        TextView tvOrderPrice;
        @BindView(R.id.tvOrderTitle)
        TextView tvOrderTitle;

        public MyOrderViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        protected void clear() {

        }

        @SuppressLint("SetTextI18n")
        public void onBind(int position) {

            AdAndOrderModel model = mMyOrdersList.get(position);

            if (!model.getCategories().isEmpty())
                tvOrderTitle.setText(model.getCategories().get(0).getTitle());
            else
                tvOrderDate.setText(itemView.getResources().getString(R.string.n_a));

            if (model.getUpdatedAt() != null)
                tvOrderDate.setText(DateUtility.getDateOnlyTFormat(model.getUpdatedAt()));
            else
                tvOrderDate.setText(itemView.getResources().getString(R.string.n_a));

            if (model.getCountry() != null)
                tvOrderLocation.setText(model.getCountry());
            else
                tvOrderLocation.setText(itemView.getResources().getString(R.string.n_a));

            if (model.getDescription() != null)
                tvOrderDesc.setText(model.getDescription());
            else if (!model.getCategories().isEmpty() && model.getCountry() != null)
                tvOrderDesc.setText(model.getCategories().get(0).getTitle() + ", " + model.getCountry());
            else
                tvOrderDesc.setText(itemView.getResources().getString(R.string.n_a));

            if (model.getPriceMin() != null && model.getPriceMax() != null && !model.getPriceMin().isEmpty() && !model.getPriceMax().isEmpty())
                tvOrderPrice.setText(CommonUtils.setPriceCurrency(itemView.getContext(), Double.parseDouble(model.getPriceMin())) + " : " +
                        CommonUtils.setPriceCurrency(itemView.getContext(), Double.parseDouble(model.getPriceMax())));
            else
                tvOrderPrice.setText(itemView.getResources().getString(R.string.n_a));

            ivDelete.setOnClickListener(v -> mCallback.deleteOrder(model.getId(), position));

            ivEdit.setOnClickListener(v -> mCallback.editOrder(model.getId()));

            itemView.setOnClickListener(v -> mCallback.getPropertyDetails(model.getId()));

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
