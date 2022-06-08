package com.gp.shifa.ui.user.profile.blocked;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.gp.shifa.R;
import com.gp.shifa.data.models.BlockedModel;
import com.gp.shifa.databinding.ItemEmptyViewBinding;
import com.gp.shifa.ui.base.BaseViewHolder;
import com.gp.shifa.utils.CommonUtils;
import com.gp.shifa.utils.DateUtility;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class BlockedAdapter extends RecyclerView.Adapter<BaseViewHolder> {

    public static final int VIEW_TYPE_EMPTY = 0;
    public static final int VIEW_TYPE_BLOCKED = 1;

    public List<BlockedModel> mBlockedList;
    private Callback mCallback;

    public BlockedAdapter(List<BlockedModel> list) {
        mBlockedList = list;
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
            case VIEW_TYPE_BLOCKED:
                return new BlockedViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_blocked, parent, false));
            case VIEW_TYPE_EMPTY:
            default:
                return new EmptyViewHolder(
                        ItemEmptyViewBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false).getRoot());
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (mBlockedList != null && mBlockedList.size() > 0) {
            return VIEW_TYPE_BLOCKED;
        } else {
            return VIEW_TYPE_EMPTY;
        }
    }

    @Override
    public int getItemCount() {
        if (mBlockedList != null && mBlockedList.size() > 0) {
            return mBlockedList.size();
        } else {
            return 1;
        }
    }

    public void addItems(List<BlockedModel> list) {
        mBlockedList.addAll(list);
        notifyDataSetChanged();
    }

    public void clearItems() {
        mBlockedList.clear();
        notifyDataSetChanged();
    }

    public void removeItem(int position) {
        mBlockedList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, mBlockedList.size());
    }

    public interface Callback {

        void unBlockUser(int userId, int position);
    }

    @SuppressLint("NonConstantResourceId")
    public class BlockedViewHolder extends BaseViewHolder {

        @BindView(R.id.ratingBarClient)
        RatingBar ratingBarClient;
        @BindView(R.id.userImageView)
        CircleImageView userImageView;
        @BindView(R.id.ivBlocked)
        ImageView ivBlocked;
        @BindView(R.id.tvUsername)
        AppCompatTextView tvUsername;
        @BindView(R.id.tvJoinDate)
        AppCompatTextView tvJoinDate;

        public BlockedViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }

        protected void clear() {

        }

        @SuppressLint("SetTextI18n")
        public void onBind(int position) {

            BlockedModel model = mBlockedList.get(position);

            CommonUtils.setupRatingBar(ratingBarClient);

            if (model.getBannedImage() != null) {
                Glide.with(itemView).load(model.getBannedImage())
                        .error(R.drawable.ic_user_holder)
                        .placeholder(R.drawable.ic_user_holder)
                        .into(userImageView);
            } else {
                Glide.with(itemView).load(R.drawable.ic_user_holder).into(userImageView);
            }

            if (model.getBannedRate() != null && !model.getBannedRate().isEmpty())
                ratingBarClient.setRating(Float.parseFloat(model.getBannedRate()));
            else
                ratingBarClient.setRating(0f);

            if (model.getBannedName() != null)
                tvUsername.setText(model.getBannedName());
            else
                tvUsername.setText(itemView.getResources().getString(R.string.n_a));

            if (model.getCreatedAt() != null)
                tvJoinDate.setText(itemView.getResources().getString(R.string.join_at) + " " + DateUtility.getDateOnlyTFormat(model.getCreatedAt()));
            else
                tvJoinDate.setText(itemView.getResources().getString(R.string.join_at) + " " + itemView.getResources().getString(R.string.n_a));

            ivBlocked.setOnClickListener(v -> mCallback.unBlockUser(model.getBannedId(), position));

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
