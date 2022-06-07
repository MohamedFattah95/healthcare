package com.gp.shifa.ui.profile.follows;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.gp.shifa.R;
import com.gp.shifa.data.models.FollowerModel;
import com.gp.shifa.databinding.ItemEmptyViewBinding;
import com.gp.shifa.ui.base.BaseViewHolder;
import com.gp.shifa.utils.CommonUtils;
import com.gp.shifa.utils.DateUtility;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class FollowsAdapter extends RecyclerView.Adapter<BaseViewHolder> {

    public static final int VIEW_TYPE_EMPTY = 0;
    public static final int VIEW_TYPE_FOLLOW = 1;

    public List<FollowerModel> mFollowsList;
    private Callback mCallback;

    public FollowsAdapter(List<FollowerModel> list) {
        mFollowsList = list;
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
            case VIEW_TYPE_FOLLOW:
                return new FollowViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_follows, parent, false));
            case VIEW_TYPE_EMPTY:
            default:
                return new EmptyViewHolder(
                        ItemEmptyViewBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false).getRoot());
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (mFollowsList != null && mFollowsList.size() > 0) {
            return VIEW_TYPE_FOLLOW;
        } else {
            return VIEW_TYPE_EMPTY;
        }
    }

    @Override
    public int getItemCount() {
        if (mFollowsList != null && mFollowsList.size() > 0) {
            return mFollowsList.size();
        } else {
            return 1;
        }
    }

    public void addItems(List<FollowerModel> list) {
        mFollowsList.addAll(list);
        notifyDataSetChanged();
    }

    public void clearItems() {
        mFollowsList.clear();
        notifyDataSetChanged();
    }

    public void removeItem(int position) {
        mFollowsList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, mFollowsList.size());
    }

    public interface Callback {

        void unFollowUser(int followingId, int position);

        void openMemberProfile(int followingId);
    }

    @SuppressLint("NonConstantResourceId")
    public class FollowViewHolder extends BaseViewHolder {

        @BindView(R.id.userImageView)
        CircleImageView userImageView;
        @BindView(R.id.ratingBarClient)
        RatingBar ratingBarClient;
        @BindView(R.id.ivUnFollow)
        ImageView ivUnFollow;
        @BindView(R.id.tvUsername)
        TextView tvUsername;
        @BindView(R.id.tvJoinDate)
        TextView tvJoinDate;

        public FollowViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }

        protected void clear() {

        }

        @SuppressLint("SetTextI18n")
        public void onBind(int position) {

            FollowerModel model = mFollowsList.get(position);

            CommonUtils.setupRatingBar(ratingBarClient);

            if (model.getFollowingImage() != null) {
                Glide.with(itemView).load(model.getFollowingImage())
                        .error(R.drawable.ic_user_holder)
                        .placeholder(R.drawable.ic_user_holder)
                        .into(userImageView);
            } else {
                Glide.with(itemView).load(R.drawable.ic_user_holder).into(userImageView);
            }

            ratingBarClient.setRating(model.getFollowingRate());


            if (model.getFollowingName() != null)
                tvUsername.setText(model.getFollowingName());
            else
                tvUsername.setText(itemView.getResources().getString(R.string.n_a));

            if (model.getCreatedAt() != null)
                tvJoinDate.setText(itemView.getResources().getString(R.string.join_at) + " " + DateUtility.getDateOnlyTFormat(model.getCreatedAt()));
            else
                tvJoinDate.setText(itemView.getResources().getString(R.string.join_at) + " " + itemView.getResources().getString(R.string.n_a));

            ivUnFollow.setOnClickListener(v -> mCallback.unFollowUser(model.getFollowingId(), position));

            itemView.setOnClickListener(v -> mCallback.openMemberProfile(model.getFollowingId()));

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
