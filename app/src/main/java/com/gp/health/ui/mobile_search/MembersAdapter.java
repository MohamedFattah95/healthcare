package com.gp.health.ui.mobile_search;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.gp.health.R;
import com.gp.health.data.models.UserModel;
import com.gp.health.ui.base.BaseViewHolder;
import com.gp.health.utils.CommonUtils;
import com.gp.health.utils.DateUtility;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class MembersAdapter extends RecyclerView.Adapter<BaseViewHolder> {

    public static final int VIEW_TYPE_EMPTY = 0;
    public static final int VIEW_TYPE_MEMBER = 1;
    private Callback mCallback;
    private List<UserModel> mMembersList;

    public MembersAdapter(List<UserModel> list) {
        mMembersList = list;
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
            case VIEW_TYPE_MEMBER:
                return new MemberViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_member, parent, false));
            case VIEW_TYPE_EMPTY:
            default:
                return new EmptyViewHolder(
                        LayoutInflater.from(parent.getContext()).inflate(R.layout.item_empty_view, parent, false));
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (mMembersList != null && mMembersList.size() > 0) {
            return VIEW_TYPE_MEMBER;
        } else {
            return VIEW_TYPE_EMPTY;
        }
    }

    @Override
    public int getItemCount() {
        if (mMembersList != null && mMembersList.size() > 0) {
            return mMembersList.size();
        } else {
            return 1;
        }
    }

    public void addItems(List<UserModel> list) {
        mMembersList.clear();
        mMembersList.addAll(list);
        notifyDataSetChanged();
    }

    public void clearItems() {
        mMembersList.clear();
        notifyDataSetChanged();
    }

    public interface Callback {
        void getMemberDetails(int userId);
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
    public class MemberViewHolder extends BaseViewHolder {

        @BindView(R.id.ratingBarClient)
        RatingBar ratingBarClient;
        @BindView(R.id.userImageView)
        CircleImageView userImageView;
        @BindView(R.id.tvUsername)
        AppCompatTextView tvUsername;
        @BindView(R.id.tvJoinDate)
        AppCompatTextView tvJoinDate;

        public MemberViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }

        protected void clear() {

        }

        @SuppressLint("SetTextI18n")
        public void onBind(int position) {

            UserModel model = mMembersList.get(position);

            CommonUtils.setupRatingBar(ratingBarClient);

            if (model.getImage() != null) {
                Glide.with(itemView).load(model.getImage())
                        .error(R.drawable.ic_user_holder)
                        .placeholder(R.drawable.ic_user_holder)
                        .into(userImageView);
            } else {
                Glide.with(itemView).load(R.drawable.ic_user_holder).into(userImageView);
            }

            ratingBarClient.setRating(model.getRate());

            if (model.getName() != null)
                tvUsername.setText(model.getName());
            else
                tvUsername.setText(itemView.getResources().getString(R.string.n_a));

            if (model.getCreatedAt() != null)
                tvJoinDate.setText(itemView.getResources().getString(R.string.join_at) + " " + DateUtility.getDateOnlyTFormat(model.getCreatedAt()));
            else
                tvJoinDate.setText(itemView.getResources().getString(R.string.join_at) + " " + itemView.getResources().getString(R.string.n_a));

            itemView.setOnClickListener(v -> mCallback.getMemberDetails(model.getId()));

        }
    }
}
