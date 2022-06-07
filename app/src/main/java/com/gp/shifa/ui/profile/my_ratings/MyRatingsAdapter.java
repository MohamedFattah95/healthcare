package com.gp.shifa.ui.profile.my_ratings;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.gp.shifa.R;
import com.gp.shifa.data.models.CommentModel;
import com.gp.shifa.databinding.ItemEmptyViewBinding;
import com.gp.shifa.ui.base.BaseViewHolder;
import com.gp.shifa.utils.CommonUtils;
import com.gp.shifa.utils.DateUtility;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class MyRatingsAdapter extends RecyclerView.Adapter<BaseViewHolder> {

    public static final int VIEW_TYPE_EMPTY = 0;
    public static final int VIEW_TYPE_RATING = 1;

    public List<CommentModel> mMyRatingsList;
    private Callback mCallback;

    public MyRatingsAdapter(List<CommentModel> list) {
        mMyRatingsList = list;
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
            case VIEW_TYPE_RATING:
                return new RatingViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rating, parent, false));
            case VIEW_TYPE_EMPTY:
            default:
                return new EmptyViewHolder(
                        ItemEmptyViewBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false).getRoot());
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (mMyRatingsList != null && mMyRatingsList.size() > 0) {
            return VIEW_TYPE_RATING;
        } else {
            return VIEW_TYPE_EMPTY;
        }
    }

    @Override
    public int getItemCount() {
        if (mMyRatingsList != null && mMyRatingsList.size() > 0) {
            return mMyRatingsList.size();
        } else {
            return 1;
        }
    }

    public void addItems(List<CommentModel> list) {
        mMyRatingsList.addAll(list);
        notifyDataSetChanged();
    }

    public void clearItems() {
        mMyRatingsList.clear();
        notifyDataSetChanged();
    }

    public interface Callback {

    }

    @SuppressLint("NonConstantResourceId")
    public class RatingViewHolder extends BaseViewHolder {

        @BindView(R.id.userImageView)
        CircleImageView userImageView;
        @BindView(R.id.rating)
        RatingBar rating;
        @BindView(R.id.tvComment)
        TextView tvComment;
        @BindView(R.id.tvUsername)
        TextView tvUsername;
        @BindView(R.id.tvDate)
        TextView tvDate;

        public RatingViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        protected void clear() {

        }

        public void onBind(int position) {

            CommentModel model = mMyRatingsList.get(position);

            CommonUtils.setupRatingBar(rating);

            if (model.getUserImage() != null) {
                Glide.with(itemView).load(model.getUserImage())
                        .error(R.drawable.ic_user_holder)
                        .placeholder(R.drawable.ic_user_holder)
                        .into(userImageView);
            } else {
                Glide.with(itemView).load(R.drawable.ic_user_holder).into(userImageView);
            }

            if (model.getComment() != null)
                rating.setRating(model.getRate());
            else
                rating.setRating(0f);

            if (model.getComment() != null && !model.getComment().isEmpty())
                tvComment.setText(model.getComment());
            else
                tvComment.setText(itemView.getResources().getString(R.string.n_a));

            if (model.getUserTitle() != null)
                tvUsername.setText(model.getUserTitle());
            else
                tvUsername.setText(itemView.getResources().getString(R.string.n_a));

            if (model.getCreatedAt() != null && !model.getCreatedAt().isEmpty())
                tvDate.setText(DateUtility.getDateOnlyTFormat(model.getCreatedAt()));
            else
                tvDate.setText(itemView.getResources().getString(R.string.n_a));

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
