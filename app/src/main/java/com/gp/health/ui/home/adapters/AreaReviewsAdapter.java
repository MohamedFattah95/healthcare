package com.gp.health.ui.home.adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gp.health.R;
import com.gp.health.data.models.CommentModel;
import com.gp.health.databinding.ItemEmptyViewBinding;
import com.gp.health.ui.base.BaseViewHolder;
import com.gp.health.utils.CommonUtils;
import com.gp.health.utils.DateUtility;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AreaReviewsAdapter extends RecyclerView.Adapter<BaseViewHolder> {

    public static final int VIEW_TYPE_EMPTY = 0;
    public static final int VIEW_TYPE_AREA_REVIEW = 1;
    public List<CommentModel> mReviewsList;
    private Callback mCallback;

    public AreaReviewsAdapter(List<CommentModel> list) {
        mReviewsList = list;
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
            case VIEW_TYPE_AREA_REVIEW:
                return new AreaReviewViewHolder(
                        LayoutInflater.from(parent.getContext()).inflate(R.layout.item_review, parent, false));
            case VIEW_TYPE_EMPTY:
            default:
                return new EmptyViewHolder(
                        ItemEmptyViewBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false).getRoot());
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (mReviewsList != null && mReviewsList.size() > 0) {
            return VIEW_TYPE_AREA_REVIEW;
        } else {
            return VIEW_TYPE_EMPTY;
        }
    }

    @Override
    public int getItemCount() {
        if (mReviewsList != null && mReviewsList.size() > 0) {
            return mReviewsList.size();
        } else {
            return 1;
        }
    }

    public void addItems(List<CommentModel> list) {
        mReviewsList.clear();
        mReviewsList.addAll(list);
        notifyDataSetChanged();
    }

    public void clearItems() {
        mReviewsList.clear();
        notifyDataSetChanged();
    }

    public interface Callback {

    }

    @SuppressLint("NonConstantResourceId")
    public class AreaReviewViewHolder extends BaseViewHolder {

        @BindView(R.id.rating)
        RatingBar rating;
        @BindView(R.id.tvComment)
        TextView tvComment;
        @BindView(R.id.tvDate)
        TextView tvDate;

        public AreaReviewViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }

        protected void clear() {

        }

        @SuppressLint("SetTextI18n")
        public void onBind(int position) {

            CommentModel model = mReviewsList.get(position);

            CommonUtils.setupRatingBar(rating);


            if (model.getComment() != null)
                rating.setRating(model.getRate());
            else
                rating.setRating(0f);

            if (model.getComment() != null && !model.getComment().isEmpty())
                tvComment.setText(model.getComment());
            else
                tvComment.setText(itemView.getResources().getString(R.string.n_a));

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
